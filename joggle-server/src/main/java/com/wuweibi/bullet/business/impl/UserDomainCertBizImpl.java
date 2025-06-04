package com.wuweibi.bullet.business.impl;

import com.wuweibi.bullet.alias.CacheCode;
import com.wuweibi.bullet.business.UserDomainCertBiz;
import com.wuweibi.bullet.common.lock.annotation.DLock;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.mapper.UserDomainMapper;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.protocol.MsgDomainCert;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.entity.UserCertification;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.shredzone.acme4j.*;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.*;

@Slf4j
@Service
public class UserDomainCertBizImpl implements UserDomainCertBiz {

    @Resource
    private UserDomainService userDomainService;
    @Resource
    private UserService userService;

    public static final String acmeServerUrl = "https://acme-v02.api.letsencrypt.org/directory";

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    @DLock
    public void startCertReNewTask( )   {
        log.debug("[域名证书处理] 开始");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>(2);
        params.put("limit", 10);
        Cursor<UserCertification> cursor = sqlSession.selectCursor(
                UserDomainMapper.class.getName() + ".selectProgressList", params);
        Iterator iter = cursor.iterator();
        int count = 0;
        while (iter.hasNext()) {
            UserDomain userDomain = (UserDomain) iter.next();
            try {
                // todo 预校验域名是否指向joggled
                log.info("正在申请域名：{} 证书", userDomain.getDomain());
                reqDomainCert(userDomain); // 申请域名证书
                log.info("正在申请域名：{} 完成", userDomain.getDomain());

                count++;
            } catch (Exception e){
                log.error("域名证书申请失败", e);
            }
        }
        log.debug("[域名证书处理] 结束");
    }


    /**
     * 申请域名证书
     * @param userDomain
     * @throws IOException
     * @throws AcmeException
     */
    public boolean reqDomainCert( UserDomain userDomain) throws IOException, AcmeException {
        String domain = userDomain.getDomain();
        // 查询用户的信息
        String email = userService.getByUserId(userDomain.getUserId()).getEmail();

        KeyPair privateKey = loadOrCreatePrivateKeyPair(userDomain.getCertKey());
        String keyPairText = getKeyPairText(privateKey);
        userDomain.setCertKey(keyPairText);

        // 创建ACME客户端会话
        Session session = new Session(acmeServerUrl);
        Account account = new AccountBuilder()
                .agreeToTermsOfService()
                .useKeyPair(privateKey)
                .addContact("mailto:" + email)
                .create(session);

        // 加载或创建域名的私钥对
        KeyPair domainKeyPair = loadOrCreatePrivateKeyPair(userDomain.getDomainKey());
        String domainKeyPairText = getKeyPairText(domainKeyPair);
        userDomain.setDomainKey(domainKeyPairText);

        // 下单新证书
        Order order = account.newOrder()
                .domains(domain)
                .create();
        // 完成HTTP-01挑战
        authorize(order);

        // 生成CSR
        CSRBuilder csrb = new CSRBuilder();
        csrb.addDomain(domain);
        csrb.sign(domainKeyPair);
        // 执行证书签发
        order.execute(csrb.getEncoded());

        // 下载证书链
        Certificate certificate = order.getCertificate();
//        Optional<Instant>  optionalExpires = order.getExpires();
//        optionalExpires.ifPresent(instant -> userDomain.setDueTime(Date.from(instant)));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer certificateWriter = new OutputStreamWriter(byteArrayOutputStream);
        certificate.download();
        certificate.writeCertificate( certificateWriter);
        certificateWriter.flush();
        certificateWriter.close();
        String certificateStr = byteArrayOutputStream.toString();
        // 证书到期时间
        userDomain.setDueTime(certificate.getCertificate().getNotAfter());
        userDomain.setCertPem(certificateStr);
        userDomain.setApplyTime(new Date());
        userDomain.setIsCert(true);
        // 保存证书
        userDomainService.updateById(userDomain);

        // 将证书推送到joggled 通知所有节点更新证书。
        MsgDomainCert msgDomainCert = new MsgDomainCert(userDomain.getDomain(), userDomain.getCertKey(), userDomain.getCertPem());
        WebsocketPool pool = SpringUtils.getBean(WebsocketPool.class);
        pool.listStream().forEach(conn -> {
            conn.sendMessageToServer(msgDomainCert);
        });
        return true;
    }




    private static String getKeyPairText(KeyPair privateKey) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);
        try {
            KeyPairUtils.writeKeyPair(privateKey, writer);
            String keyPairText = byteArrayOutputStream.toString();
            return keyPairText;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            }   catch (IOException e) {
                throw e;
            }
        }
    }

    private KeyPair loadOrCreatePrivateKeyPair(String privateKeyText ) throws IOException {
        if (StringUtils.isNotBlank(privateKeyText)) { // 存在私钥
            ByteArrayInputStream bios = new ByteArrayInputStream(privateKeyText.getBytes(StandardCharsets.UTF_8));
            Reader reader = new InputStreamReader(bios, StandardCharsets.UTF_8);
            return KeyPairUtils.readKeyPair(reader);
        } else {
            KeyPair keyPair = KeyPairUtils.createKeyPair(2048);
            return keyPair;
        }
    }



    /**
     * 完成HTTP-01挑战验证
     */
    private void authorize(Order order) throws AcmeException {
        for (Authorization auth : order.getAuthorizations()) {
            Optional<Http01Challenge> challengeOptional = auth.findChallenge(Http01Challenge.class);
            if (!challengeOptional.isPresent()) {
                throw new AcmeException("找不到HTTP-01挑战");
            }
            Http01Challenge challenge = challengeOptional.get();

            // 将挑战令牌写入指定位置
            String token = challenge.getToken();
            String content = challenge.getAuthorization();

            // 这里需要实现将内容写入到你的web服务器
            // 例如: /.well-known/acme-challenge/<token>
            writeChallengeFile(token, content);

            // 触发挑战验证
            challenge.trigger();

            // 等待验证完成
            int attempts = 10;
            while (challenge.getStatus() != Status.VALID && attempts-- > 0) {
                if (challenge.getStatus() == Status.INVALID) {
                    throw new AcmeException("挑战验证失败");
                }
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                challenge.update();
            }
        }
    }

    @Resource
    private RedisTemplate redisTemplate;



    private void writeChallengeFile(String token, String content) {
        // 实现将挑战文件写入你的web服务器
        // 例如: src/main/resources/static/.well-known/acme-challenge/<token>
        // 注意: 生产环境需要写入到真实的web可访问目录
        String key = String.format(CacheCode.ACME_CHALLENGE_KEY,token);
        redisTemplate.opsForValue().set(key, content);
        redisTemplate.expire(key, 10, java.util.concurrent.TimeUnit.MINUTES);
    }
}
