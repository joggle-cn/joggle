package com.wuweibi.bullet.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.system.domain.dto.UserCertAdminParam;
import com.wuweibi.bullet.system.domain.vo.UserCertificationAdminListVO;
import com.wuweibi.bullet.system.entity.UserCertification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户实名认证(UserCertification)表数据库访问层
 *
 * @author marker
 * @since 2022-09-14 13:54:43
 */
public interface UserCertificationMapper extends BaseMapper<UserCertification> {

    boolean checkIdcardAndPhone(@Param("phone") String phone, @Param("idcard") String idcard);

    List<UserCertification> selectProgressList(int limit);

    UserCertification selectLastResult(@Param("userId") Long userId);

    /**
     * 分页查询用户的实名认证记录
     * @param pageInfo 分页参数
     * @param params 条件参数
     * @return
     */
    Page<UserCertificationAdminListVO> selectAdminList(Page pageInfo,@Param("params") UserCertAdminParam params);

}
