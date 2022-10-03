package com.wuweibi.bullet.orders.controller;


import com.alipay.easysdk.factory.AliFactory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.IdLongDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.common.exception.RException;
import com.wuweibi.bullet.config.properties.AlipayProperties;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersAdminParam;
import com.wuweibi.bullet.orders.domain.OrdersDetailAdminVO;
import com.wuweibi.bullet.orders.domain.OrdersListAdminVO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.enums.PayTypeEnum;
import com.wuweibi.bullet.orders.enums.ResourceTypeEnum;
import com.wuweibi.bullet.orders.service.OrdersRefundService;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("订单分页查询")
    @GetMapping("/list")
    public R<Page<OrdersListAdminVO>> getPageList(PageParam page, OrdersAdminParam params) {
        return R.ok(this.ordersService.getAdminPage(page.toMybatisPlusPage(), params));
    }

    @Resource
    private UserService userService;

    @Resource
    private OrdersRefundService ordersRefundService;

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


        User user = userService.getById(entity.getUserId());
        vo.setBuyerEmail(user.getEmail());
        vo.setBuyerNickname(user.getNickname());


        // 查询退款信息



        return R.ok(vo);
    }

    @Resource
    private Config alipayConfig;
    @Resource
    private AlipayProperties alipayProperties;

    /**
     * 取消订单
     *
     * @return 修改结果
     */
    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    @Transactional
    public R<Boolean> cancel(@RequestBody @Valid IdLongDTO dto) throws Exception {
        Long orderId = dto.getId();

        Orders orders = this.ordersService.getById(orderId);
        if (orders == null) {
            return R.fail("订单不存在");
        }
        if (orders.getStatus() != OrdersStatusEnum.WAIT_PAY.getStatus()) {
            return R.fail("订单状态不支持取消");
        }

        boolean status = ordersService.updateStatus(orderId, OrdersStatusEnum.CANCEL.getStatus());
        if (!status) {
            return R.fail("订单更新失败");
        }

        if (StringUtil.isBlank(orders.getOrderNo())) {
            return R.ok();
        }

        log.debug("orders[{}] close[{}] request", orders.getId(), orders.getOrderNo());
        if (orders.getPayType() == PayTypeEnum.ALIPAY.getType()) {
            AliFactory.setOptions(alipayConfig);
            AlipayTradeCloseResponse result = AliFactory.Common().closeTradeNo(orders.getOrderNo().trim());
            log.debug("orders[{}] close[{}] status={}", orders.getId(), orders.getOrderNo(), result.getHttpBody());
            if (result.code != "10000") {
                throw new RException("支付宝：" + result.getMsg() + result.getSubMsg());
            }
        }

        return R.ok();
    }


}
