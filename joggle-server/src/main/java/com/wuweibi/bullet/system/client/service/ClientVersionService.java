package com.wuweibi.bullet.system.client.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.domain.dto.ClientInfoDTO;
import com.wuweibi.bullet.system.client.domain.ClientVersionAdminListVO;
import com.wuweibi.bullet.system.client.domain.NgrokVersionVO;
import com.wuweibi.bullet.system.client.entity.ClientVersion;
import com.wuweibi.bullet.system.domain.dto.ClientVersionParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2021-08-12
 */
public interface ClientVersionService extends IService<ClientVersion> {

    /**
     * 获取新版本
     * @return
     * @param clientInfoDTO
     */
    ClientVersion getNewVersion(ClientInfoDTO clientInfoDTO);

    /**
     * 更新checksum
     *
     * @param version
     * @param os
     * @param arch
     * @param binFilePath 二进制文件路径
     * @param checksum
     * @param type 类型
     * @param signature Tauri 签名
     * @return
     */
    int updateChecksumByOsArch(String version, String os, String arch, String binFilePath, String checksum, String type, String signature);

    /**
     * 获取最大版本
     * @return
     */
    NgrokVersionVO getMaxVersion();

    /**
     * 分页查询客户端
     * @param pageInfo 分页参数
     * @param params 条件参数
     * @return
     */
    Page<ClientVersionAdminListVO> getAdminList(Page pageInfo, ClientVersionParam params);

    /**
     * 获取指定操作系统和架构的最新版本
     *
     * @param os  操作系统
     * @param arch 架构
     * @return 最新版本实体
     */
    ClientVersion getLatestVersion(String os, String arch);

    /**
     * 获取 Tauri 更新清单所需的最新版本所有平台记录
     *
     * @return 最新版本的所有平台记录
     */
    List<ClientVersion> getUpdateManifestList();
}
