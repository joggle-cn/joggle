package com.wuweibi.bullet.domain2.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.domain2.domain.DomainAdminParam;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.domain2.domain.DomainDetail;
import com.wuweibi.bullet.domain2.domain.DomainSearchParam;
import com.wuweibi.bullet.domain2.domain.vo.DomainListVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainReleaseVO;
import com.wuweibi.bullet.domain2.domain.vo.DomainVO;
import com.wuweibi.bullet.domain2.entity.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
@Mapper
public interface DomainMapper extends BaseMapper<Domain> {


    /**
     * 根据用户ID查询归属域名
     *
     * @param userId 用户ID
     * @return
     */
    List<DomainVO> selectByUserId(@Param("userId") Long userId);

    /**
     * 检查域名是否和用户绑定
     *
     * @param userId   用户ID
     * @param domainId 域名ID
     * @return
     */
    @Select("select count(1) from t_domain where id = #{domainId} and user_id = #{userId}")
    boolean existDomainUserId(@Param("userId") Long userId, @Param("domainId") Long domainId);


    /**
     * 获取未绑定的域名列表
     *
     * @param userId 用户ID
     * @param serverTunnelId 服务器通道id
     * @param type 类型
     * @return
     */
    List<DomainOptionVO> selectListNotBindByUserId(@Param("userId") Long userId,
                               @Param("serverTunnelId") Integer serverTunnelId, @Param("type") Integer type);

    /**
     * 更新域名有效期
     *
     * @param domainId
     * @param dueTime
     */
    @Update("update t_domain set due_time = #{dueTime}, buy_time=sysdate() where id = #{domainId}")
    void updateDueTime(@Param("domainId") Long domainId, @Param("dueTime") Date dueTime);

    @Update("update t_domain set due_time = #{dueTime} where id = #{domainId}")
    boolean updateDueTimeById(@Param("domainId") Long domainId, @Param("dueTime") Date dueTime);

    /**
     * 查询过期的域名
     *
     * @return
     */
    List<JSONObject> selectDueDomain();


    /**
     * 检查域名是否过期
     *
     * @param userId
     * @param domainId 域名ID
     * @return
     */
    @Select("select count(1) from t_domain where user_id = #{userId} and (due_time is null or due_time >= SYSDATE()) and id = #{domainId}")
    boolean checkDoaminIdDue(@Param("userId") Long userId, @Param("domainId") Long domainId);


    /**
     * 获取没有购买的域名列表
     *
     * @param pageParams 分页参数
     * @param params     搜索条件
     * @return Page<DomainBuyListVO>
     */
    Page<DomainBuyListVO> selectBuyList(Page pageParams, @Param("params") DomainSearchParam params);


    /**
     * 获取最大的端口号
     *
     * @return
     * @param serverTunnelId 通道id
     */
    Integer selectMaxPort(@Param("serverTunnelId") Integer serverTunnelId);


    DomainDetail selectDetail(@Param("domainId") Long domainId);

    /**
     * 查询指定过期天数时间的资源
     * @param days 天数
     * @return
     */
    List<DomainReleaseVO> selectByDueDay(@Param("days") int days);


    /**
     * 资源分页查询
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<DomainListVO> selectAdminList(Page pageInfo, @Param("params") DomainAdminParam params);


    /**
     * 批量更新用户所有权益域名的结束时间
     *
     * @param userId    用户id
     * @param bandwidth
     * @param endTime   过期时间
     * @return
     */
    boolean updateUserDueTime(@Param("userId") Long userId, @Param("bandwidth") Integer bandwidth, @Param("endTime") Date endTime);

    Domain selectByMappingId(Long mappingId);
}