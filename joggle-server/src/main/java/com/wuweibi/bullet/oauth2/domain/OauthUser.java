package com.wuweibi.bullet.oauth2.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wuweibi.bullet.common.domain.BasePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * 用户模型
 *
 * @author marker
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_users")
public class OauthUser extends BasePo {
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 账号过期
     */
    private Boolean accountNonExpired;

    /**
     * 密码过期
     */
    private Boolean credentialsNonExpired;
    /**
     * 锁定账号
     */
    private Boolean accountNonLocked;

    /**
     * 是否 后台账号 1 是 0 否
     */
    private Integer userAdmin;
}
