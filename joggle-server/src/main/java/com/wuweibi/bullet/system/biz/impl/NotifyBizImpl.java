package com.wuweibi.bullet.system.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.biz.NotifyBiz;
import com.wuweibi.bullet.system.domain.SendSmsDTO;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.system.service.ThirdMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;


/**
 * 通知中心
 *
 * @author marker
 */
@Slf4j
@Service
public class NotifyBizImpl implements NotifyBiz {

    @Resource
    private ThirdMessageService thirdMessageService;

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;
    @Resource
    private BulletConfig bulletConfig;


    @Override
    public boolean notification(@NotNull Long userId, @NotNull NotifyType notifyType, @NotNull Map<String, Object> param) {
        User user = userService.getByUserId(userId); // 可以考虑缓存
        if (null == user) {
            log.warn("设备用户[{}] 不存在...", userId);
            return false;
        }
        if (!Objects.equals(1, user.getSystemNotice())) {
            log.warn("user[{}] not open system notice", userId);
            return false;
        }
        JSONObject jsonParams = new JSONObject(); // 转换更佳优雅的取值参数
        jsonParams.putAll(param);

        jsonParams.put("url", bulletConfig.getServerUrl()); // 特殊参数，用于配置邮件跳转链接

        // TODO 这里需要改造为freemarker模板渲染
        String subject = String.format(notifyType.getEmailType().getSubject(), jsonParams.getString("deviceNo"));
        mailService.send(user.getEmail(), subject, param, notifyType.getEmailType().getTemplateCode());

        // 如果开通了短信通知，
        if (Objects.equals(1, user.getSmsNotice())) {
            SendSmsDTO smsDTO = new SendSmsDTO();
            smsDTO.setPhone(user.getPhone());
            smsDTO.setType(notifyType.getSmsType().toString());
            smsDTO.setParam(param);
            log.info("system notice send sms to userId[{}]", userId);
            thirdMessageService.sendSms(smsDTO);
        }
        return true;
    }
}
