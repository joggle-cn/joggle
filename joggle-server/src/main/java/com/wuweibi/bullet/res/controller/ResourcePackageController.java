package com.wuweibi.bullet.res.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.res.domain.ResourcePackageListVO;
import com.wuweibi.bullet.res.domain.ResourcePackageParam;
import com.wuweibi.bullet.res.entity.ResourcePackage;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * (ResourcePackage)表控制层
 *
 * @author marker
 * @since 2022-10-30 15:48:49
 */
@Slf4j
@RestController
@Api(value = "", tags = "用户套餐列表")
@RequestMapping("/api/resource/package")
public class ResourcePackageController {
    /**
     * 服务对象
     */
    @Resource
    private ResourcePackageService resourcePackageService;
    
    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<ResourcePackageListVO>> getPageList(PageParam page, ResourcePackageParam params) {
        return R.ok(this.resourcePackageService.getList(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<ResourcePackage> detail(@RequestParam Serializable id) {
        return R.ok(this.resourcePackageService.getById(id));
    }




}
