//package com.wuweibi.bullet.system.controller;
//
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.jiayu.yoga.common.core.util.R;
//import com.jiayu.yoga.common.core.vo.IdDTO;
//import com.jiayu.yoga.common.core.vo.PageParam;
//import com.wuweibi.bullet.system.domain.SysConfigParam;
//import com.wuweibi.bullet.system.domain.SysConfigVO;
//import com.wuweibi.bullet.system.entity.SysConfig;
//import com.wuweibi.bullet.system.service.SysConfigService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.annotation.Resource;
//import jakarta.validation.Valid;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * 系统配置表(SysConfig)表控制层
// *
// * @author marker
// * @since 2024-06-23 20:35:30
// */
//@Slf4j
//@RestController
//@Tag(name = "系统配置表")
//@RequestMapping("/admin/school/sysConfig")
//public class SysConfigController  {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private SysConfigService sysConfigService;
//
//    /**
//     * 分页查询所有数据2
//     *
//     * @param page 分页对象
//     * @param params 查询实体
//     * @return 所有数据
//     */
//    @Operation(summary = "分页查询")
//    @GetMapping("/list")
//    public R<Page<SysConfigVO>> getPageList(PageParam page, SysConfigParam params) {
//        return R.ok(this.sysConfigService.getPage(page.toMybatisPlusPage(), params));
//    }
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @Operation(summary = "通过主键查询单条数据")
//    @GetMapping("/detail")
//    public R<SysConfig> detail(@RequestParam Serializable id) {
//        return R.ok(this.sysConfigService.getById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param sysConfig 实体对象
//     * @return 新增结果
//     */
//    @Operation(summary = "新增数据")
//    @PostMapping
//    public R<Boolean> save(@RequestBody SysConfig sysConfig) {
//        return R.ok(this.sysConfigService.save(sysConfig));
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param sysConfig 实体对象
//     * @return 修改结果
//     */
//    @Operation(summary = "修改数据")
//    @PutMapping
//    public R<Boolean> update(@RequestBody SysConfig sysConfig) {
//        return R.ok(this.sysConfigService.updateById(sysConfig));
//    }
//
//    /**
//     * 批量删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @Operation(summary = "批量删除数据")
//    @DeleteMapping("/batch")
//    public R<Boolean> deleteBatch(@RequestParam("idList") List<Long> idList) {
//        return R.ok(this.sysConfigService.removeByIds(idList));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param idDTO 主键
//     * @return 删除结果
//     */
//    @Operation(summary = "删除数据")
//    @DeleteMapping()
//    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
//        return R.ok(this.sysConfigService.removeById(idDTO.getId()));
//    }
//
//}
