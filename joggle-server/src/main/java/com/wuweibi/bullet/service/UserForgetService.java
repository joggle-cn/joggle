package com.wuweibi.bullet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuweibi.bullet.entity.UserForget;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
public interface UserForgetService extends IService<UserForget> {


    /**
     * 检查最近是否申请过忘记密码
     * @param email 邮箱地址
     * @param time 最近时间 单位秒
     * @return
     */
    boolean checkApply(String email, int time);
}
