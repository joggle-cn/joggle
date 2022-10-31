package com.wuweibi.bullet.res.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.IdDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.res.domain.UserPackageParam;
import com.wuweibi.bullet.res.domain.UserPackageVO;
import com.wuweibi.bullet.res.entity.UserPackage;
import com.wuweibi.bullet.res.service.UserPackageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户套餐(UserPackage)表控制层
 *
 * @author marker
 * @since 2022-10-31 09:15:53
 */
@Slf4j
@RestController
@Api(value = "用户套餐", tags = "用户套餐")
@RequestMapping("/admin/school/userPackage")
public class UserPackageController  {
    /**
     * 服务对象
     */
    @Resource
    private UserPackageService userPackageService;
    
    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<UserPackageVO>> getPageList(PageParam page, UserPackageParam params) {
        return R.ok(this.userPackageService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<UserPackage> detail(@RequestParam Serializable id) {
        return R.ok(this.userPackageService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param userPackage 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R<Boolean> save(@RequestBody UserPackage userPackage) {
        return R.ok(this.userPackageService.save(userPackage));
    }

    /**
     * 修改数据
     *
     * @param userPackage 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody UserPackage userPackage) {
        return R.ok(this.userPackageService.updateById(userPackage));
    }
 	
    /**
     * 批量删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("批量删除数据")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestParam("idList") List<Long> idList) {
        return R.ok(this.userPackageService.removeByIds(idList));
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
        return R.ok(this.userPackageService.removeById(idDTO.getId()));
    }

}
