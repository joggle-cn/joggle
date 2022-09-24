package com.wuweibi.bullet.orders.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.orders.domain.OrdersAdminParam;
import com.wuweibi.bullet.orders.domain.OrdersListAdminVO;
import com.wuweibi.bullet.orders.domain.OrdersListVO;
import com.wuweibi.bullet.orders.domain.OrdersParam;
import com.wuweibi.bullet.orders.entity.Orders;

/**
 * (Orders)表服务接口
 *
 * @author makejava
 * @since 2022-07-21 16:16:12
 */
public interface OrdersService extends IService<Orders> {

    Orders getByOrderNo(String outTradeNo);

    /**
     * 分页查询订单
     * @param pageParams 分页参数
     * @param params 检索参数
     * @return
     */
    Page<OrdersListVO> getListPage(Page pageParams, OrdersParam params);

    /**
     * 后台订单分页查询
     * @param pageInfo 分页对象
     * @param params 条件参数
     * @return
     */
    Page<OrdersListAdminVO> getAdminPage(Page pageInfo, OrdersAdminParam params);


    /**
     * 更新订单状态
     * @param orderId 订单id
     * @param status 订单状态
     * @return
     */
    boolean updateStatus(Long orderId, int status);
}

