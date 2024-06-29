package com.wuweibi.bullet.system.biz;

import com.wuweibi.bullet.system.service.enums.EmailTypeEnum;
import com.wuweibi.bullet.system.service.enums.SmsTypeEnum;
import lombok.Getter;

import java.util.Map;

public interface NotifyBiz {

    /**
     * 系统通知
     * @param userId 用户id
     * @param notifyType 通知类型
     * @param param 参数
     * @return
     */
    boolean notification(Long userId, NotifyType notifyType, Map<String, Object> param);


    @Getter
    enum NotifyType{

        DEVICE_DOWN("DEVICE_DOWN", EmailTypeEnum.DEVICE_DOWN, SmsTypeEnum.DEVICE_DOWN)



        ;

        // 类型
        private String type = "";

        // 邮件模板
        private EmailTypeEnum emailType;

        // 短信模板
        private SmsTypeEnum smsType;

        NotifyType(String type, EmailTypeEnum emailType, SmsTypeEnum smsType) {
            this.type = type;
            this.emailType = emailType;
            this.smsType = smsType;
        }


    }

}
