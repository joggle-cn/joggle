package com.wuweibi.bullet.orders.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.domain2.entity.Domain;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersRefundDTO;
import com.wuweibi.bullet.orders.domain.OrdersRefundParam;
import com.wuweibi.bullet.orders.domain.OrdersRefundVO;
import com.wuweibi.bullet.orders.domain.RefundAuditDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.entity.OrdersRefund;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.service.OrdersRefundService;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款单(OrdersRefund)表控制层
 *
 * @author marker
 * @since 2022-09-19 10:31:15
 */
@Slf4j
@RestController
@Api(value = "退款单", tags = "退款单")
@RequestMapping("/admin/order/refund")
public class OrdersRefundController  {
    /**
     * 服务对象
     */
    @Resource
    private OrdersRefundService ordersRefundService;
    
    /**
     * 分页查询所有数据2
     *
     * @param page 分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询")
    @GetMapping("/list")
    public R<Page<OrdersRefundVO>> getPageList(PageParam page, OrdersRefundParam params) {
        return R.ok(this.ordersRefundService.getPage(page.toMybatisPlusPage(), params));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<OrdersRefund> detail(@RequestParam Serializable id) {
        return R.ok(this.ordersRefundService.getById(id));
    }

    @Resource
    private OrdersService ordersService;

    @Resource
    private DomainService domainService;

    /**
     * 申请退款
     *
     * @return 新增结果
     */
    @ApiOperation("申请退款")
    @PostMapping("/apply")
    @Transactional
    public R<Boolean> applyRefund(@RequestBody @Valid OrdersRefundDTO dto) {
        Long orderId = dto.getOrderId();
        Orders orders = ordersService.getById(orderId);
        if (orders == null) {
            return R.fail("订单不存在");
        }
        if(orders.getStatus() != OrdersStatusEnum.PAYED.getStatus()){
            return R.fail("订单状态不支持退款");
        }
        if(ordersRefundService.countStatusByOrderId(orderId, 0)>0){
            return R.fail("存在正在进行的退款单");
        }

        Integer resType = orders.getResourceType();
        switch (resType){
            case 1: // 端口
            case 2: // 域名
                // 时间
                if ((orders.getAmount() / 24 / 60 / 60) < dto.getAmount()) {
                    return R.fail("退款数量超额");
                }
                Domain domain = domainService.getById(orders.getDomainId());
                Date dueTime = domain.getDueTime();
                long moreDays = DateUtil.betweenDay(new Date(), dueTime, true);// 最大剩余天数
                if (moreDays < dto.getAmount()) {
                    return R.fail("资源剩余天数不足退款");
                }
                String refundNo = "R"+ CodeHelper.getIdWorker().nextId();

                OrdersRefund ordersRefund = new OrdersRefund();
                ordersRefund.setOrderId(orderId);
                ordersRefund.setCreateTime(new Date());
                ordersRefund.setPriceAmount(domain.getSalesPrice());
                ordersRefund.setAmount(dto.getAmount()*24*60*60);
                ordersRefund.setRefundAmount(ordersRefund.getPriceAmount().multiply(new BigDecimal(dto.getAmount())));
                ordersRefund.setRefundNo(refundNo);
                ordersRefund.setStatus(0);
                ordersRefund.setUpdateTime(ordersRefund.getCreateTime());
                ordersRefund.setReason(dto.getReason());
                ordersRefund.setUserId(orders.getUserId());
                ordersRefundService.save(ordersRefund);

                // 到期时间扣除
                Date newDueTime = DateUtils.addDays(dueTime, -dto.getAmount().intValue());
                domainService.updateDueTimeById(domain.getId(), newDueTime);

                break;
            case 3:

            default:
                return R.fail("暂不支持退款");
        }

        return R.ok( );
    }


    /**
     * 退款审核
     *
     * @return 新增结果
     */
    @ApiOperation("退款审核")
    @PostMapping("/audit")
    @Transactional
    public R<Boolean> auditRefund(@RequestBody @Valid RefundAuditDTO dto) {

        return R.fail("");
    }

    /**
     * 修改数据
     *
     * @param ordersRefund 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody OrdersRefund ordersRefund) {
        return R.ok(this.ordersRefundService.updateById(ordersRefund));
    }


}
