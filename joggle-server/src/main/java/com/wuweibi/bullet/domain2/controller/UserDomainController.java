package com.wuweibi.bullet.domain2.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.common.domain.IdDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain2.domain.UserDomainParam;
import com.wuweibi.bullet.domain2.domain.UserDomainVO;
import com.wuweibi.bullet.domain2.domain.dto.UserDomainAddDTO;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.service.UserDomainService;
import com.wuweibi.bullet.entity.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;

/**
 * 用户域名(UserDomain)表控制层
 *
 * @author marker
 * @since 2023-09-17 17:44:10
 */
@Slf4j
@WebApi
@RestController
@Api(value = "用户域名", tags = "用户域名")
@RequestMapping("/api/user/domain")
public class UserDomainController {
    /**
     * 服务对象
     */
    @Resource
    private UserDomainService userDomainService;

    /**
     * 分页查询所有数据2
     *
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("用户域名分页查询")
    @GetMapping("/list")
    public R<Page<UserDomainVO>> getPageList(@ApiIgnore @JwtUser Session session, PageParam page, UserDomainParam params) {
        params.setUserId(session.getUserId());
        return R.ok(this.userDomainService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<UserDomain> detail(@RequestParam Serializable id) {
        return R.ok(this.userDomainService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param addDTO 实体对象
     * @return 新增结果
     */
    @ApiOperation("用户域名新增")
    @PostMapping
    public R<Boolean> saveUserDomain(@ApiIgnore @JwtUser Session session, @RequestBody @Valid UserDomainAddDTO addDTO) {
        addDTO.setUserId(session.getUserId());
        if (!this.userDomainService.checkDomain(addDTO.getUserId(), addDTO.getDomain())) {
            return R.fail("域名TXT解析校验不通过");
        }
        return this.userDomainService.saveUserDomain(addDTO);
    }

    /**
     * 修改数据
     *
     * @param userDomain 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody UserDomain userDomain) {
        return R.ok(this.userDomainService.updateById(userDomain));
    }



    /**
     * 删除数据
     *
     * @param idDTO 主键
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping()
    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
        return R.ok(this.userDomainService.removeById(idDTO.getId()));
    }

}
