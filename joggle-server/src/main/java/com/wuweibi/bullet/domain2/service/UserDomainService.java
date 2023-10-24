package com.wuweibi.bullet.domain2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.domain2.domain.dto.DomainCertUpdate;
import com.wuweibi.bullet.domain2.domain.dto.UserDomainAddDTO;
import com.wuweibi.bullet.domain2.domain.vo.UserDomainOptionVO;
import com.wuweibi.bullet.domain2.entity.UserDomain;
import com.wuweibi.bullet.domain2.domain.UserDomainVO;
import com.wuweibi.bullet.domain2.domain.UserDomainParam;
import com.wuweibi.bullet.entity.api.R;

import java.util.List;

/**
 * 用户域名(UserDomain)表服务接口
 *
 * @author marker
 * @since 2023-09-17 17:44:06
 */
public interface UserDomainService extends IService<UserDomain> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<UserDomainVO> getPage(Page pageInfo, UserDomainParam params);

    /**
     * 保存用户域名
     * @param addDTO
     * @return
     */
    R<Boolean> saveUserDomain(UserDomainAddDTO addDTO);


    /**
     * 检查域名是否正常
     *
     * @param userId
     * @param domain 域名
     * @return
     */
    boolean checkDomain(Long userId, String domain);


    /**
     * 更新域名证书
     * @param domainCertUpdate
     * @return
     */
    R<Boolean> updateDomainCert(DomainCertUpdate domainCertUpdate);

    /**
     * 删除用户域名
     * @param id id
     * @return
     */
    R<Boolean> removeDomain(Long id);

    /**
     * 检查用户域名是否存在
     * @param userId 用户id
     * @param userDomainId 用户域名id
     * @return
     */
    boolean checkUserDomain(Long userId, Long userDomainId);

    /**
     * 根据用户id查询自定义域名
     * @param userId 用户id
     * @return
     */
    List<UserDomainOptionVO> getOptionByUserId(Long userId);
}
