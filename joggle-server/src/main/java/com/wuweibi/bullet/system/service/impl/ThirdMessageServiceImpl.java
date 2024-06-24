package com.wuweibi.bullet.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.domain.SendSmsDTO;
import com.wuweibi.bullet.system.domain.SmsDTO;
import com.wuweibi.bullet.system.service.SysConfigService;
import com.wuweibi.bullet.system.service.ThirdMessageService;
import com.wuweibi.bullet.system.service.enums.SmsTypeEnum;
import com.wuweibi.bullet.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Message服务实现类
 * </p>
 *
 * @author marker
 * @since 2021-05-20
 */
@Slf4j
@Service
public class ThirdMessageServiceImpl implements ThirdMessageService {

    public static final int SMS_CODE_EXPIRE = 300;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BulletConfig yogaConfig;

    @Resource
    private SysConfigService sysConfigService;



    @Resource
    private com.aliyun.dysmsapi20170525.Client client;


    @Override
    public R sendSms(SendSmsDTO sendSmsDTO) {
        String phone = sendSmsDTO.getPhone();
        SmsTypeEnum smsTypeEnum;
        try {
            smsTypeEnum = SmsTypeEnum.valueOf(sendSmsDTO.getType());
        } catch (Exception e) {
            return R.fail("短信验证码类型错误");
        }

        String typeName = smsTypeEnum.getClass().getSimpleName();
        String templateCode = sysConfigService.getConfigValue(typeName, smsTypeEnum.toString());
        String smsSign = sysConfigService.getConfigValue(typeName, "SMS_SIGN");

        String templateParam = JSON.toJSONString(sendSmsDTO.getParam());
        log.info("templateParam: {}", templateParam);
        // 发送短信
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(smsSign)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam(templateParam);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if(!"OK".equals(sendSmsResponse.getBody().getCode())){
                log.error("短信发送异常：{}",sendSmsResponse.getBody().getMessage());
            }
        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.error(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            log.error(error.message);
        }
        return R.ok();
    }


    @Override
    public R sendSmsCode(SmsDTO smsDTO) {
        String phone = smsDTO.getPhone();
        SmsTypeEnum smsTypeEnum;
        try {
            smsTypeEnum = SmsTypeEnum.valueOf(smsDTO.getType());
        } catch (Exception e) {
            return R.fail("短信验证码类型错误");
        }

        String typeName = smsTypeEnum.getClass().getSimpleName();
        String templateCode = sysConfigService.getConfigValue(typeName, smsTypeEnum.toString());
        String smsSign = sysConfigService.getConfigValue(typeName, "SMS_SIGN");

        String codeKey = String.format(SMS_CODE_FORMAT, smsDTO.getType() , smsDTO.getCountryCode(), phone);
        Object cdv = redisTemplate.opsForValue().get(codeKey);
       if (cdv != null) {
           long keyExpire = redisTemplate.getExpire(codeKey);
           if (keyExpire > (SMS_CODE_EXPIRE - 90)) { // 90秒的等待期才能再次获取
               log.warn("手机号验证码获取频率高:{}，{}", phone, cdv);
               return R.fail("验证码发送过于频繁");
           }
        }

        String code = RandomUtil.randomNumbers(5);
        if (!SpringUtils.isProduction()) {
            code = "1234";
        }
        redisTemplate.opsForValue().set(codeKey, code, SMS_CODE_EXPIRE, TimeUnit.SECONDS);
        log.debug("手机号生成验证码:{},{}", phone, code);
        if (!SpringUtils.isProduction()) {
            return R.ok();
        }
        // 发送验证码
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(smsSign)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if(!"OK".equals(sendSmsResponse.getBody().getCode())){
                log.error("短信发送异常：{}",sendSmsResponse.getBody().getMessage());
            }
        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.error(error.message);
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            log.error(error.message);
        }
        return R.ok();
    }


}