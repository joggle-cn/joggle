package com.wuweibi.bullet.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.orders.entity.Orders;

/**
 * (Orders)表服务接口
 *
 * @author makejava
 * @since 2022-07-21 16:16:12
 */
public interface OrdersService extends IService<Orders> {

    Orders getByOrderNo(String outTradeNo);
}

