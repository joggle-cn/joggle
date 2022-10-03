package com.wuweibi.bullet.orders.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.orders.domain.OrdersRefundParam;
import com.wuweibi.bullet.orders.domain.OrdersRefundVO;
import com.wuweibi.bullet.orders.entity.OrdersRefund;
import com.wuweibi.bullet.orders.mapper.OrdersRefundMapper;
import com.wuweibi.bullet.orders.service.OrdersRefundService;
import org.springframework.stereotype.Service;

/**
 * 退款单(OrdersRefund)表服务实现类
 *
 * @author marker
 * @since 2022-09-19 10:31:15
 */
@Service
public class OrdersRefundServiceImpl extends ServiceImpl<OrdersRefundMapper, OrdersRefund> implements OrdersRefundService {


    @Override
    public Page<OrdersRefundVO> getPage(Page pageInfo, OrdersRefundParam params) {

        Page<OrdersRefundVO> page = this.baseMapper.selectAdminList(pageInfo, params);


        return page;
    }

    @Override
    public Long countStatusByOrderId(Long orderId, int status) {
        return this.baseMapper.selectCount(Wrappers.<OrdersRefund>lambdaQuery()
                .eq(OrdersRefund::getOrderId, orderId)
                .eq(OrdersRefund::getStatus, status));
    }
}
