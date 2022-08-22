package com.wuweibi.bullet.orders.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersAdminParam;
import com.wuweibi.bullet.orders.domain.OrdersDetailAdminVO;
import com.wuweibi.bullet.orders.domain.OrdersListAdminVO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.enums.PayTypeEnum;
import com.wuweibi.bullet.orders.enums.ResourceTypeEnum;
import com.wuweibi.bullet.orders.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * (Orders)表控制层
 *
 * @author marker
 * @since 2022-08-22 15:27:31
 */
@Slf4j
@RestController
@AdminApi
@Api(value = "", tags = "订单管理")
@RequestMapping("/admin/orders")
public class OrdersAdminController {
    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;

    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("订单分页查询")
    @GetMapping("/list")
    public R<Page<OrdersListAdminVO>> getPageList(PageParam page, OrdersAdminParam params) {
        return R.ok(this.ordersService.getAdminPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<OrdersDetailAdminVO> detail(@RequestParam Serializable id) {
        Orders entity = this.ordersService.getById(id);

        OrdersDetailAdminVO vo = new OrdersDetailAdminVO();
        BeanUtils.copyProperties(entity, vo);

        vo.setStatusName(OrdersStatusEnum.toName(vo.getStatus()));
        vo.setResourceTypeName(ResourceTypeEnum.toName(vo.getResourceType()));
        vo.setPayTypeName(PayTypeEnum.toName(vo.getPayType()));

        return R.ok(vo);
    }



    /**
     * 修改数据
     *
     * @param orders 实体对象
     * @return 修改结果
     */
//    @ApiOperation("修改数据")
//    @PutMapping
//    public R<Boolean> update(@RequestBody Orders orders) {
//        return R.ok(this.ordersService.updateById(orders));
//    }


}
