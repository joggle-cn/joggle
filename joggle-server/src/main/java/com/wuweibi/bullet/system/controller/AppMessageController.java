package com.wuweibi.bullet.system.controller;


import cn.hutool.core.util.PhoneUtil;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.domain.SmsDTO;
import com.wuweibi.bullet.system.service.ThirdMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * (消息服务)表控制层
 *
 * @author marker
 * @since 2021-05-19 15:31:21
 */
@RestController
@Api(value = "param", tags = "短信服务")
@RequestMapping("/api/open/message")
public class AppMessageController {
    /**
     * 服务对象
     */
    @Resource
    private ThirdMessageService thirdMessageService;

    /**
     * 获取短信验证码
     *
     * @return 所有数据
     */
    @ApiOperation(value = "获取短信验证码", notes = "短信类型  登录 LOGIN  实名认证AUTH")
    @PostMapping("/sms")
    public R sms(@RequestBody @Valid SmsDTO smsDTO) {
//        // 注册验证码
//        if (SmsTypeEnum.REGISTER.getType().equals(smsDTO.getType())) {
//            String captchaVerification = smsDTO.getCaptchaVerification();
//            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVerification);
//
//            boolean hasKey = stringRedisTemplate.hasKey(codeKey);
//            if (!hasKey) {
//                return R.failed("行为验证失败，请重试");
//            }
//            stringRedisTemplate.delete(codeKey);
//            // 验证账号是否注册
//            String phone = smsDTO.getPhone();
//            R<SysUser> r1 = remoteUserService.getByUserPhone(phone);
//            if (!r1.isFaild()) {
//                return R.failed("手机号已注册！");
//            }
//        }

        if (!PhoneUtil.isMobile(smsDTO.getCountryCode() + smsDTO.getPhone())) {
            return R.fail("手机号码不正确");
        }

        return thirdMessageService.sendSmsCode(smsDTO);
    }


}