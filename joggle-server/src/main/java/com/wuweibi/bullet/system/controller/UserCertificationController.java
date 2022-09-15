package com.wuweibi.bullet.system.controller;


import cn.hutool.core.util.IdcardUtil;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.system.domain.UserCertificationDTO;
import com.wuweibi.bullet.system.entity.UserCertification;
import com.wuweibi.bullet.system.service.ThirdMessageService;
import com.wuweibi.bullet.system.service.UserCertificationService;
import com.wuweibi.bullet.system.service.enums.SmsTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * 用户实名认证(UserCertification)表控制层
 *
 * @author marker
 * @since 2022-09-14 13:54:43
 */
@Slf4j
@RestController
@Api(value = "用户实名认证", tags = "用户实名认证")
@RequestMapping("/api/user/certification")
public class UserCertificationController  {
    /**
     * 服务对象
     */
    @Resource
    private UserCertificationService userCertificationService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 新增数据
     *
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping("/submit")
    public R<Boolean> save(@RequestBody @Valid UserCertificationDTO dto) {
        Long userId = SecurityUtils.getUserId();

        // 身份证格式校验
        if (!IdcardUtil.isValidCard(dto.getIdcard())) {
            return R.fail("身份证号码格式不正确");
        }

        // 验证码校验
        String codeKey = String.format(ThirdMessageService.SMS_CODE_FORMAT, SmsTypeEnum.AUTH.getType(),
                "86", dto.getPhone());
        Object cdv = redisTemplate.opsForValue().get(codeKey);
        if (cdv == null) {
            return R.fail("请获取短信验证码");
        }

        // 如果用户有认证成功和认证中的不允许提交
        // 返回：1通过 0等待审核  2 可申请
        int result = this.userCertificationService.checkCertRepeatOk(userId);
        switch (result){
            case 1: return R.fail("您的认证已经通过，无需重复提交！");
            case 0: return R.fail("请耐心等待认证审核！");
        }

        if(userCertificationService.checkIdcardAndPhone(dto.getPhone(), dto.getIdcard())){
            return R.fail("对不起，您提交的信息已存在");
        }

        UserCertification userCertification = new UserCertification();
        BeanUtils.copyProperties(dto, userCertification);
        userCertification.setCreateTime(new Date());
        userCertification.setUserId(userId);
        userCertification.setResult(0);
        userCertification.setType(1);
        this.userCertificationService.save(userCertification);

        return R.ok();
    }

}
