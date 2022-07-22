package com.wuweibi.bullet.orders.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.orders.domain.OrdersListVO;
import com.wuweibi.bullet.orders.domain.OrdersParam;
import com.wuweibi.bullet.orders.entity.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Orders)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-21 16:16:06
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Orders> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Orders> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Orders> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Orders> entities);


    /**
     * 分页查询订单
     * @param pageParams 分页参数
     * @param params 检索参数
     * @return
     */
    Page<OrdersListVO> selectListPage(Page pageParams, @Param("params") OrdersParam params);
}

