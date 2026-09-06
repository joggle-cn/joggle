package com.wuweibi.bullet.res.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.IdDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.res.domain.UserPackageRightsParam;
import com.wuweibi.bullet.res.domain.UserPackageRightsVO;
import com.wuweibi.bullet.res.entity.UserPackageRights;
import com.wuweibi.bullet.res.service.UserPackageRightsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 用户套餐权益(UserPackageRights)表控制层
 *
 * @author marker
 * @since 2022-11-01 12:41:56
 */
@Slf4j
@RestController
@Tag(name = "用户套餐权益")
@RequestMapping("/admin/school/userPackageRights")
public class UserPackageRightsController  {
    /**
     * 服务对象
     */
    @Resource
    private UserPackageRightsService userPackageRightsService;
    
    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public R<Page<UserPackageRightsVO>> getPageList(PageParam page, UserPackageRightsParam params) {
        return R.ok(this.userPackageRightsService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @Operation(summary = "通过主键查询单条数据")
    @GetMapping("/detail")
    public R<UserPackageRights> detail(@RequestParam Serializable id) {
        return R.ok(this.userPackageRightsService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param userPackageRights 实体对象
     * @return 新增结果
     */
    @Operation(summary = "新增数据")
    @PostMapping
    public R<Boolean> save(@RequestBody UserPackageRights userPackageRights) {
        return R.ok(this.userPackageRightsService.save(userPackageRights));
    }

    /**
     * 修改数据
     *
     * @param userPackageRights 实体对象
     * @return 修改结果
     */
    @Operation(summary = "修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody UserPackageRights userPackageRights) {
        return R.ok(this.userPackageRightsService.updateById(userPackageRights));
    }
 	
    /**
     * 批量删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @Operation(summary = "批量删除数据")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestParam("idList") List<Long> idList) {
        return R.ok(this.userPackageRightsService.removeByIds(idList));
    }
    
    /**
     * 删除数据
     *
     * @param idDTO 主键
     * @return 删除结果
     */
    @Operation(summary = "删除数据")
    @DeleteMapping()
    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
        return R.ok(this.userPackageRightsService.removeById(idDTO.getId()));
    }

}
