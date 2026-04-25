package com.wuweibi.bullet.system.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.config.properties.JoggleProperties;
import com.wuweibi.bullet.enums.ServerModeEnum;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.biz.NotifyBiz;
import com.wuweibi.bullet.system.domain.SendSmsDTO;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.system.service.SysConfigService;
import com.wuweibi.bullet.system.service.ThirdMessageService;
import com.wuweibi.bullet.system.service.enums.SystemConfigEnum;
import com.wuweibi.bullet.utils.StringUtil;
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
    private JoggleProperties joggleProperties;

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public boolean notification(@NotNull Long userId, @NotNull NotifyType notifyType, @NotNull Map<String, Object> param) {
        // 如果是单用户运行模式，使用系统配置
        if (ServerModeEnum.standalone.equals(joggleProperties.getServerMode())) {
            return this.notification(notifyType, param);
        }
        // 多用户模式通知
        User user = userService.getByUserId(userId); // 可以考虑缓存
        if (null == user) {
            log.warn("设备用户[{}] 不存在...", userId);
            return false;
        }
        if (!Objects.equals(1, user.getSystemNotice())) {
            log.warn("user[{}] not open system notice", userId);
            return false;
        }
        JSONObject jsonParams = new JSONObject(param); // 转换更佳优雅的取值参数
        jsonParams.put("url", joggleProperties.getServerUrl()); // 特殊参数，用于配置邮件跳转链接

        // 构造subject邮件标题
        String subject = jsonParams.getString("subject");
        if(StringUtil.isBlank(subject)){
            subject =  notifyType.getEmailType().getDefaultSubject();
        }
//        String subject = String.format(notifyType.getEmailType().getSubject(), jsonParams.getString("deviceNo"));
        mailService.send(user.getEmail(), subject, param, notifyType.getEmailType().getTemplateCode());

        // 如果开通了短信通知，
        if (Objects.equals(1, user.getSmsNotice()) && null != notifyType.getSmsType()) {
            param.remove("url"); // 变量不支持传入URL ,过滤处理
            SendSmsDTO smsDTO = new SendSmsDTO();
            smsDTO.setPhone(user.getPhone());
            smsDTO.setType(notifyType.getSmsType().toString());
            smsDTO.setParam(param);
            log.info("system notice send sms to userId[{}]", userId);
            thirdMessageService.sendSms(smsDTO);
        }
        return true;
    }

    @Override
    public boolean notification(NotifyType notifyType, Map<String, Object> param) {
        String typeName = SystemConfigEnum.class.getSimpleName();
        Boolean noticeEnable = sysConfigService.getBooleanValue(typeName, SystemConfigEnum.NOTICE_ENABLE.getType());
        if (!noticeEnable) {
            log.warn("system notice is not open");
            return false;
        }
        // 获取通知手机号
        String phones = sysConfigService.getConfigValue(typeName, SystemConfigEnum.NOTICE_PHONES.getType());
        if (StringUtil.isBlank(phones)) {
            return false;
        }

        // 如果开通了短信通知，
        for (String phone : phones.split(",")) {
            SendSmsDTO smsDTO = new SendSmsDTO();
            smsDTO.setPhone(phone);
            smsDTO.setType(notifyType.getSmsType().toString());
            smsDTO.setParam(param);
            log.info("system notice send sms  [{}]");
            thirdMessageService.sendSms(smsDTO);
        }
        return true;
    }
}
