package com.wuweibi.bullet.orders.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.orders.domain.OrdersAdminParam;
import com.wuweibi.bullet.orders.domain.OrdersListAdminVO;
import com.wuweibi.bullet.orders.domain.OrdersListVO;
import com.wuweibi.bullet.orders.domain.OrdersParam;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.enums.PayTypeEnum;
import com.wuweibi.bullet.orders.enums.ResourceTypeEnum;
import com.wuweibi.bullet.orders.mapper.OrdersMapper;
import com.wuweibi.bullet.orders.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * (Orders)表服务实现类
 *
 * @author makejava
 * @since 2022-07-21 16:16:16
 */
@Service("ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Override
    public Orders getByOrderNo(String orderNo) {
        return this.baseMapper.selectOne(Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getOrderNo, orderNo));
    }

    @Override
    public Page<OrdersListVO> getListPage(Page pageParams, OrdersParam params) {
        Page<OrdersListVO> page = this.baseMapper.selectListPage(pageParams, params);

        page.getRecords().forEach(item->{
            item.setStatusName(OrdersStatusEnum.toName(item.getStatus()));
            item.setResourceTypeName(ResourceTypeEnum.toName(item.getResourceType()));
            item.setPayTypeName(PayTypeEnum.toName(item.getPayType()));
        });
        return page;
    }

    @Override
    public Page<OrdersListAdminVO> getAdminPage(Page pageInfo, OrdersAdminParam params) {
        Page<OrdersListAdminVO> page = this.baseMapper.selectAdminList(pageInfo, params);

        page.getRecords().forEach(item->{
            item.setStatusName(OrdersStatusEnum.toName(item.getStatus()));
            item.setResourceTypeName(ResourceTypeEnum.toName(item.getResourceType()));
            item.setPayTypeName(PayTypeEnum.toName(item.getPayType()));
        });
        return page;
    }
}

