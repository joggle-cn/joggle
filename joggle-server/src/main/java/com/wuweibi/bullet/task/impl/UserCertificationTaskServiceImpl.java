package com.wuweibi.bullet.task.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.system.entity.UserCertification;
import com.wuweibi.bullet.system.mapper.UserCertificationMapper;
import com.wuweibi.bullet.system.service.util.HttpUtils;
import com.wuweibi.bullet.task.UserCertificationTaskService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 实名认证处理任务
 *
 * @author marker
 */
@Slf4j
@Service
public class UserCertificationTaskServiceImpl implements UserCertificationTaskService {


    @Resource
    private UserCertificationMapper userCertificationMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @Override
    @Transactional
    public void progress() {
        log.debug("[实名认证处理] 开始");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> params = new HashMap<>(2);
        params.put("limit", 10);
        Cursor<UserCertification> cursor = sqlSession.selectCursor(
                UserCertificationMapper.class.getName() + ".selectProgressList", params);
        Iterator iter = cursor.iterator();

        int count = 0;
        while (iter.hasNext()) {
            UserCertification uc = (UserCertification) iter.next();
            uc.setResult(2); // 默认拒绝
            uc.setResultMsg("身份证校验不通过，请重新提交真实信息。");
            Long userId = uc.getUserId();
            User user = userService.getById(userId);

            UserInfo userInfo = getIdCardInfo(uc);
            if (userInfo != null) {
                try {
                    uc.setBirthday(DateUtils.parseDate(userInfo.getBirthday(), "yyyy-M-d"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                uc.setProvince(userInfo.getProvince());
                uc.setCity(userInfo.getCity());
                uc.setArea(userInfo.getArea());
                uc.setDistrict(userInfo.getDistrict());
                uc.setSex(userInfo.getSex());
                uc.setResult(1);
                uc.setResultMsg("认证通过");
            }
            uc.setExamineTime(new Date());
            userCertificationMapper.updateById(uc);
            userService.updateUserCertification(userId, uc.getResult());

            // 发送通过邮件
            log.debug("send email[{}] notification 实名认证结果: {}", user.getEmail(), uc.getResultMsg());
            Map<String, Object> param = new HashMap<>(3);
            param.put("result", uc.getResult());
            param.put("resultMsg", uc.getResultMsg());
            param.put("url", bulletConfig.getServerUrl() );
            mailService.send(user.getEmail(), "Joggle实名认证结果", param, "certification_result.ftl");

            count++;
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("", e);
        } finally {
            sqlSession.close();
        }

        log.debug("[实名认证处理] 结束 处理数据量：{}", count);

    }
    @Resource
    private BulletConfig bulletConfig;




    @Data
    class UserInfo {
        private String province;
        private String city;
        private String district;
        private String area;
        private String sex;
        private String birthday; // ": "1965-3-10"
    }


    /**
     * 调用 三方接口
     *
     * @param uc
     * @return
     */
    private UserInfo getIdCardInfo(UserCertification uc) {
        String host = "https://zidv2.market.alicloudapi.com";
        String path = "/idcard/VerifyIdcardv2";
        String method = "GET";
        String appcode = bulletConfig.getAliAppcode();
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>(2);
        querys.put("cardNo", uc.getIdcard());
        querys.put("realName", uc.getRealName());

        try {
            log.debug("[调用认证接口] req:{}", querys);
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);

            if (response.getStatusLine().getStatusCode() == 200) {
                String respJson = IoUtil.read(response.getEntity().getContent(), "utf-8");
                log.debug("[调用认证接口] resp:{}", respJson);
                JSONObject jsonObject = JSON.parseObject(respJson);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject IdCardInfo = result.getJSONObject("IdCardInfor");
                if (!result.getBoolean("isok")) {
                    return null;
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setProvince(IdCardInfo.getString("province"));
                userInfo.setSex(IdCardInfo.getString("sex"));
                userInfo.setCity(IdCardInfo.getString("city"));
                userInfo.setBirthday(IdCardInfo.getString("birthday"));
                userInfo.setDistrict(IdCardInfo.getString("district"));
                userInfo.setArea(IdCardInfo.getString("area"));
                return userInfo;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
