//package com.wuweibi.bullet.metrics.controller;
//
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.jiayu.yoga.common.core.util.R;
//import com.jiayu.yoga.common.core.vo.IdDTO;
//import com.jiayu.yoga.common.core.vo.PageParam;
//import com.wuweibi.bullet.metrics.domain.DataMetricsHourParam;
//import com.wuweibi.bullet.metrics.domain.DataMetricsHourVO;
//import com.wuweibi.bullet.metrics.entity.DataMetricsHour;
//import com.wuweibi.bullet.metrics.service.DataMetricsHourService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.validation.Valid;
//import java.io.Serializable;
//import java.util.List;
//
///**
// * 数据收集(小时)(DataMetricsHour)表控制层
// *
// * @author marker
// * @since 2024-07-17 18:02:16
// */
//@Slf4j
//@RestController
//@Api(value = "数据收集(小时)", tags = "数据收集(小时)")
//@RequestMapping("/admin/school/dataMetricsHour")
//public class DataMetricsHourController  {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private DataMetricsHourService dataMetricsHourService;
//
//    /**
//     * 分页查询所有数据2
//     *
//     * @param page 分页对象
//     * @param params 查询实体
//     * @return 所有数据
//     */
//    @ApiOperation("分页查询")
//    @GetMapping("/list")
//    public R<Page<DataMetricsHourVO>> getPageList(PageParam page, DataMetricsHourParam params) {
//        return R.ok(this.dataMetricsHourService.getPage(page.toMybatisPlusPage(), params));
//    }
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @ApiOperation("通过主键查询单条数据")
//    @GetMapping("/detail")
//    public R<DataMetricsHour> detail(@RequestParam Serializable id) {
//        return R.ok(this.dataMetricsHourService.getById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param dataMetricsHour 实体对象
//     * @return 新增结果
//     */
//    @ApiOperation("新增数据")
//    @PostMapping
//    public R<Boolean> save(@RequestBody DataMetricsHour dataMetricsHour) {
//        return R.ok(this.dataMetricsHourService.save(dataMetricsHour));
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param dataMetricsHour 实体对象
//     * @return 修改结果
//     */
//    @ApiOperation("修改数据")
//    @PutMapping
//    public R<Boolean> update(@RequestBody DataMetricsHour dataMetricsHour) {
//        return R.ok(this.dataMetricsHourService.updateById(dataMetricsHour));
//    }
//
//    /**
//     * 批量删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @ApiOperation("批量删除数据")
//    @DeleteMapping("/batch")
//    public R<Boolean> deleteBatch(@RequestParam("idList") List<Long> idList) {
//        return R.ok(this.dataMetricsHourService.removeByIds(idList));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param idDTO 主键
//     * @return 删除结果
//     */
//    @ApiOperation("删除数据")
//    @DeleteMapping()
//    public R<Boolean> deleteById(@RequestBody @Valid IdDTO idDTO) {
//        return R.ok(this.dataMetricsHourService.removeById(idDTO.getId()));
//    }
//
//}
