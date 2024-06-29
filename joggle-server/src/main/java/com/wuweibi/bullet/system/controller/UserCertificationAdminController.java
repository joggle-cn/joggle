package com.wuweibi.bullet.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.domain.dto.UserCertAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserCertificationAdminListVO;
import com.wuweibi.bullet.system.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户实名认证(UserCertification)表控制层
 *
 * @author marker
 * @since 2022-09-14 13:54:43
 */
@Slf4j
@RestController
@Api(value = "用户实名认证", tags = "用户实名认证")
@RequestMapping("/admin/user/certification")
public class UserCertificationAdminController {
    /**
     * 服务对象
     */
    @Resource
    private UserCertificationService userCertificationService;

    @Resource
    private RedisTemplate redisTemplate;


    @ApiOperation("用户实名认证分页查询")
    @GetMapping("/list")
    public R<Page<UserCertificationAdminListVO>> getPageList(PageParam page, UserCertAdminParam params) {
        return R.ok(this.userCertificationService.getAdminList(page.toMybatisPlusPage(), params));
    }



}
