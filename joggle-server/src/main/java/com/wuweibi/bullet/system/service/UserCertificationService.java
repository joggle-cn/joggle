package com.wuweibi.bullet.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.system.domain.UserCertificationParam;
import com.wuweibi.bullet.system.domain.UserCertificationVO;
import com.wuweibi.bullet.system.domain.dto.UserCertAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserCertificationAdminListVO;
import com.wuweibi.bullet.system.entity.UserCertification;

/**
 * 用户实名认证(UserCertification)表服务接口
 *
 * @author marker
 * @since 2022-09-14 13:54:42
 */
public interface UserCertificationService extends IService<UserCertification> {


    /**
     * 分页查询数据
     * @param pageInfo 分页对象
     * @param params 参数
     * @return
     */
    Page<UserCertificationVO> getPage(Page pageInfo, UserCertificationParam params);


    /**
     * 检查是否可以申请认证
     * @param userId 用户id
     * @return 2 可申请， 0有待审核记录 1审核通过的记录
     */
    int checkCertRepeatOk(Long userId);

    boolean checkIdcardAndPhone(String phone, String idcard);

    UserCertification getLastResult(Long userId);

    /**
     * 分页查询用户的实名认证记录
     * @param pageInfo 分页参数
     * @param params 条件参数
     * @return
     */
    Page<UserCertificationAdminListVO> getAdminList(Page pageInfo, UserCertAdminParam params);

}
