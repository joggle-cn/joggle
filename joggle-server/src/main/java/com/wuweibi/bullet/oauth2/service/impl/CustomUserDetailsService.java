package com.wuweibi.bullet.oauth2.service.impl;


import cn.hutool.core.text.AntPathMatcher;
import com.wuweibi.bullet.exception.BaseException;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.oauth2.consts.ClientScope;
import com.wuweibi.bullet.oauth2.domain.OauthUser;
import com.wuweibi.bullet.oauth2.domain.Role;
import com.wuweibi.bullet.oauth2.exception.LoginException;
import com.wuweibi.bullet.oauth2.security.UserDetail;
import com.wuweibi.bullet.oauth2.service.Oauth2RoleService;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring权限框架用户详情服务
 *
 * @author marker
 */
@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private OauthUserService oauthUserService;

    @Resource
    private Oauth2RoleService oauth2RoleService;

    @Resource
    private ClientDetailsService clientDetailsService;

    /**
     * springbootAdmin server 配置项
     */
    @Resource
    private AdminServerProperties adminServerProperties;

    private AntPathMatcher monitorAntPathMatcher = new AntPathMatcher();

    /**
     * 包含注册账号（做了事务）
     *
     * @param username
     * @return
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 支持手机号和账号登录
        OauthUser user = oauthUserService.getByUsername(username);
        if (user == null) {
            throw new BaseException(AuthErrorType.ACCOUNT_PASSWORD_ERROR);
        }
        log.info("loadByUsername:{}", user.toString());

        // 【monitor】 仅仅管理员可以登录
        if (monitorAntPathMatcher.match(adminServerProperties.path("/**"),request.getRequestURI()) && 1 == user.getUserAdmin()){
            return getUserDetail(user);
        }

        // 【joggle】 登录的管理端
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            throw new LoginException("无权限登录");
        }
        String clientId = authentication.getName();
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        // 校验管理端登录必须有管理标识的用户
        if (clientDetails.getScope().contains(ClientScope.SCOPE_ADMIN) && 1 != user.getUserAdmin()) {
            throw new LoginException("无权限登录");
        }


        // 判断用户是哪个端的用户
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String clientName = authentication.getName();
//        if("manager".equals(clientName)){// 运营端
//            Set<GrantedAuthority> authList = this.obtainGrantedAuthorities(user);
//            if(!hasRoleCode(authList, "operating")){// 没有运营端权限
//                throw new BaseException(AuthErrorType.ACCOUNT_NOT_FOUND);
//            }
//        }

        // 验证验证码是否匹配
//        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();


        // 返回用户
        return getUserDetail(user);
    }


    /**
     * 包装UserDetail
     * @param user joggle用户信息
     * @return
     */
    @NotNull
    private UserDetail getUserDetail(OauthUser user) {
        return new UserDetail(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getMobile(),
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                this.obtainGrantedAuthorities(user));
    }


    /**
     * 获得登录者所有角色的权限集合.
     *
     * @param user 用户对象
     * @return
     */
    protected Set<GrantedAuthority> obtainGrantedAuthorities(OauthUser user) {
        Set<Role> roles = oauth2RoleService.queryUserRolesByUserId(user.getId());
        log.info("user:{},roles:{}", user.getUsername(), roles);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toSet());
    }

    /**
     * 是否包含指定的角色码
     *
     * @param authorities
     * @param roleCode
     * @return
     */
    protected boolean hasRoleCode(Set<GrantedAuthority> authorities, String roleCode) {
        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals(roleCode)) {
                return true;
            }
        }
        return false;
    }
}
