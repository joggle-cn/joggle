package com.wuweibi.bullet.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.orders.domain.OrdersRefundParam;
import com.wuweibi.bullet.orders.domain.OrdersRefundVO;
import com.wuweibi.bullet.orders.entity.OrdersRefund;
import com.wuweibi.bullet.orders.mapper.OrdersRefundMapper;
import com.wuweibi.bullet.orders.service.OrdersRefundService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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

        LambdaQueryWrapper<OrdersRefund> qw = Wrappers.lambdaQuery();
        Page<OrdersRefund> page = this.baseMapper.selectPage(pageInfo, qw);

        Page<OrdersRefundVO> pageResult = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
        pageResult.setRecords(page.getRecords().stream().map(entity->{
            OrdersRefundVO vo = new OrdersRefundVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    public Long countStatusByOrderId(Long orderId, int status) {
        return this.baseMapper.selectCount(Wrappers.<OrdersRefund>lambdaQuery()
                .eq(OrdersRefund::getOrderId, orderId)
                .eq(OrdersRefund::getStatus, status));
    }
}
