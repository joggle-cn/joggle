package com.wuweibi.bullet.orders.controller;


import com.alipay.easysdk.kernel.Config;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.business.OrderPayBiz;
import com.wuweibi.bullet.business.domain.OrderPayInfo;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.orders.domain.OrdersDTO;
import com.wuweibi.bullet.orders.entity.Orders;
import com.wuweibi.bullet.orders.enums.OrdersStatusEnum;
import com.wuweibi.bullet.orders.service.OrdersService;
import com.wuweibi.bullet.service.DomainService;
import com.wuweibi.bullet.utils.CodeHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * (Orders)表控制层
 *
 * @author makejava
 * @since 2022-07-21 16:16:05
 */
@RestController
@RequestMapping("/api/orders")
public class OrdersController     {
    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderPayBiz orderPayBiz;

    @Resource
    private DomainService domainService;


    /**
     * 计算价格
     * @param session
     * @return
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public Object calculate(@JwtUser Session session, @RequestBody OrdersDTO ordersDTO){

        Long userId = session.getUserId();

        // 检查是否该用户的域名
//        if(!domainService.checkDomain(userId, domainId)){
//            return R.fail(SystemErrorType.DOMAIN_NOT_FOUND);
//        }
        return orderPayBiz.calculate(ordersDTO);
    }

    @Resource
    private Config alipayConfig;

    /**
     * 下单接口
     * @param session
     * @return
     */
    @PostMapping(value = "/create")
    public R create(@JwtUser Session session, @RequestBody OrdersDTO ordersDTO) throws Exception {

        Long userId = session.getUserId();

        // 校验数据

        // 计算价格
        R<OrderPayInfo> r = orderPayBiz.calculate(ordersDTO);
        if(r.isFail()){
            return r;
        }
        OrderPayInfo orderPayInfo = r.getData();


        Orders orders = new Orders();

        // TODO 订单号生成
        long id = CodeHelper.getIdWorker().nextId();
        orders.setOrderNo(String.valueOf(id));
        orders.setName(orderPayInfo.getName());
        orders.setAmount(orderPayInfo.getAmount());
        orders.setPayType(ordersDTO.getPayType());
        orders.setUserId(userId);
        orders.setResourceType(ordersDTO.getResourceType());
        orders.setDomainId(ordersDTO.getResId());
        orders.setPayAmount(orderPayInfo.getPayAmount());
        orders.setDiscountAmount(orderPayInfo.getDiscountAmount());
        orders.setPriceAmount(orderPayInfo.getPriceAmount());
        orders.setStatus(OrdersStatusEnum.WAIT_PAY.getStatus());
        orders.setCreateTime(new Date());
        orders.setUpdateTime(orders.getCreateTime());

        ordersService.save(orders);

        // 调用支付接口

        Integer payType = orders.getPayType();
        if(payType == 2){

            return R.success(orders.getId());
        }



        return R.success();
    }





    }

