package com.wuweibi.bullet.orders.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.orders.domain.OrdersRefundParam;
import com.wuweibi.bullet.orders.domain.OrdersRefundVO;
import com.wuweibi.bullet.orders.entity.OrdersRefund;
import org.apache.ibatis.annotations.Param;

/**
 * 退款单(OrdersRefund)表数据库访问层
 *
 * @author marker
 * @since 2022-09-19 10:31:15
 */
public interface OrdersRefundMapper extends BaseMapper<OrdersRefund> {

    Page<OrdersRefundVO> selectAdminList(Page pageInfo, @Param("params") OrdersRefundParam params);
}
