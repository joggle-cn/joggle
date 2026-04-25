//package com.wuweibi.bullet.ertificate.service;
//
//import org.shredzone.acme4j.*;
//import org.shredzone.acme4j.challenge.Http01Challenge;
//import org.shredzone.acme4j.exception.AcmeException;
//import org.shredzone.acme4j.util.CSRBuilder;
//import org.shredzone.acme4j.util.KeyPairUtils;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.security.KeyPair;
//
//@Service
//public class AcmeService {
//
//    /**
//     * 获取或续期证书
//     */
//    public void fetchOrRenewCertificate() throws AcmeException, IOException {
//        // 加载或创建账户密钥对
//        KeyPair accountKeyPair = loadOrCreateKeyPair(accountKeyFile);
//
//        // 创建ACME客户端会话
//        Session session = new Session(acmeServerUrl);
//        Account account = new AccountBuilder()
//                .agreeToTermsOfService()
//                .useKeyPair(accountKeyPair)
//                .addContact("mailto:" + email)
//                .create(session);
//
//        // 加载或创建域名密钥对
//        KeyPair domainKeyPair = loadOrCreateKeyPair(domainKeyFile);
//
//        // 下单新证书
//        Order order = account.newOrder()
//                .domains(domain)
//                .create();
//
//        // 完成HTTP-01挑战
//        authorize(order);
//
//        // 生成CSR
//        CSRBuilder csrb = new CSRBuilder();
//        csrb.addDomain(domain);
//        csrb.setKeyPair(domainKeyPair);
//        csrb.sign();
//
//        // 执行证书签发
//        order.execute(csrb.getEncoded());
//
//        // 下载证书链
//        Certificate certificate = order.getCertificate();
//        try (FileWriter fw = new FileWriter(certificateFile)) {
//            certificate.writeCertificate(fw);
//        }
//    }
//    /**
//     * 完成HTTP-01挑战验证
//     */
//    private void authorize(Order order) throws AcmeException {
//        for (Authorization auth : order.getAuthorizations()) {
//            Http01Challenge challenge = auth.findChallenge(Http01Challenge.class);
//            if (challenge == null) {
//                throw new AcmeException("找不到HTTP-01挑战");
//            }
//
//            // 将挑战令牌写入指定位置
//            String token = challenge.getToken();
//            String content = challenge.getAuthorization();
//
//            // 这里需要实现将内容写入到你的web服务器
//            // 例如: /.well-known/acme-challenge/<token>
//            writeChallengeFile(token, content);
//
//            // 触发挑战验证
//            challenge.trigger();
//
//            // 等待验证完成
//            int attempts = 10;
//            while (challenge.getStatus() != Status.VALID && attempts-- > 0) {
//                if (challenge.getStatus() == Status.INVALID) {
//                    throw new AcmeException("挑战验证失败");
//                }
//                try {
//                    Thread.sleep(3000L);
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
//                challenge.update();
//            }
//        }
//    }
//
//    private KeyPair loadOrCreateKeyPair(String filename) throws IOException {
//        File file = new File(filename);
//        if (file.exists()) {
//            try (FileReader fr = new FileReader(file)) {
//                return KeyPairUtils.readKeyPair(fr);
//            }
//        } else {
//            KeyPair keyPair = KeyPairUtils.createKeyPair(2048);
//            try (FileWriter fw = new FileWriter(file)) {
//                KeyPairUtils.writeKeyPair(keyPair, fw);
//            }
//            return keyPair;
//        }
//    }
//
//    private void writeChallengeFile(String token, String content) {
//        // 实现将挑战文件写入你的web服务器
//        // 例如: src/main/resources/static/.well-known/acme-challenge/<token>
//        // 注意: 生产环境需要写入到真实的web可访问目录
//    }
//
//}
