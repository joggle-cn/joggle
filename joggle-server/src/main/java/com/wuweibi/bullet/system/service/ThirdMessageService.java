package com.wuweibi.bullet.system.service;

import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.domain.SendSmsDTO;
import com.wuweibi.bullet.system.domain.SmsDTO;

public interface ThirdMessageService {

     String SMS_CODE_FORMAT = "sms:%s:%s:%s";


    /**
     * 发送模板短信
     * @param sendSmsDTO 短信对象
     * @return
     */
    R sendSms(SendSmsDTO sendSmsDTO);

    /**
     * 发送手机验证码
     * @param mobile mobile
     * @return code
     */
    R   sendSmsCode(SmsDTO mobile);

}
