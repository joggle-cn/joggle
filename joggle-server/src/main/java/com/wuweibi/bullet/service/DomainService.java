package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.vo.DomainVO;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.domain2.domain.DomainDetail;
import com.wuweibi.bullet.domain2.domain.DomainSearchParam;
import com.wuweibi.bullet.domain2.domain.dto.ReleaseResourceDTO;
import com.wuweibi.bullet.domain2.domain.vo.DomainOptionVO;
import com.wuweibi.bullet.domain2.entity.Domain;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  域名与端口服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-09
 */
public interface DomainService extends IService<Domain> {


    /**
     * 根据用户ID查询归属域名
     * @param userId 用户ID
     * @return
     */
    List<DomainVO> getListByUserId(Long userId);

    /**
     * 检查域名是否和用户绑定
     * @param userId 用户ID
     * @param domainId 域名ID
     * @return
     */
    boolean checkDomain(Long userId, Long domainId);


    /**
     * 获取未绑定的域名列表
     * @param userId 用户ID
     * @param serverTunnelId
     * @param type
     * @return
     */
    List<DomainOptionVO> getListNotBindByUserId(Long userId, Integer serverTunnelId, Integer type);


    /**
     * 更新域名有效期
     * @param domainId
     * @param dueTime
     */
    void updateDueTime(Long domainId, Long dueTime);

    /**
     * 检查所有域名的状态
     */
    void checkStatus();

    /**
     * 更新用户
     * @param domainId
     * @param userId
     * @param status
     * @return
     */
    boolean updateUserId(Long domainId, Long userId, int status);

    /**
     * 获取没有购买的域名列表
     * @param pageParams 分页参数
     * @param keyword 关键字
     * @return
     */
    Page<DomainBuyListVO> getBuyList(Page pageParams, DomainSearchParam keyword);

    /**
     * 释放域名
     * @return
     * @param dto
     */
    boolean release(@Valid ReleaseResourceDTO dto);

    /**
     * 校验用户是否拥有域名
     * @param userId 用户id
     * @param domainId 域名id
     * @return
     */
    boolean exists(Long userId, Long domainId);

    DomainDetail getDetail(Long domainId);

    /**
     * 校验通道是否存在资源
     * @param serverTunnelId 服务器通道id
     * @return
     */
    boolean checkServerTunnelUse(Integer serverTunnelId);
}
