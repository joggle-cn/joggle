package com.wuweibi.bullet.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.orders.entity.OrdersRefund;
import com.wuweibi.bullet.orders.domain.OrdersRefundVO;
import com.wuweibi.bullet.orders.domain.OrdersRefundParam;

/**
 * 退款单(OrdersRefund)表服务接口
 *
 * @author marker
 * @since 2022-09-19 10:31:15
 */
public interface OrdersRefundService extends IService<OrdersRefund> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<OrdersRefundVO> getPage(Page pageInfo, OrdersRefundParam params);

    int countStatusByOrderId(Long orderId, int status);
}
