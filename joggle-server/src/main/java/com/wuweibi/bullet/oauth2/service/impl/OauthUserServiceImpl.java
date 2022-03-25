package com.wuweibi.bullet.oauth2.service.impl;

import com.wuweibi.bullet.alias.MessageCode;
import com.wuweibi.bullet.oauth2.dao.OauthUserMapper;
import com.wuweibi.bullet.oauth2.domain.OauthUser;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Service
public class OauthUserServiceImpl implements OauthUserService {

    @Resource
    private OauthUserMapper baseMapper;



    @Override
    public OauthUser getByUsername(String username) {
        return this.baseMapper.getByUsername(username);
    }



    @Override
    public OauthUser getByEmail(String email) {
        // 验证是否存在
        return this.baseMapper.getByEmail(email);
    }

    @Override
    public int login(String email, String pass) {
        OauthUser user = getByEmail(email);
        if(user != null){
            String userPass = user.getPassword();
            if(userPass != null && userPass.equals(pass)){

                this.baseMapper.updateLoginTime(user.getId());

                return MessageCode.login_suc_user;
            }
            return MessageCode.login_err_pass;
        }
        return MessageCode.login_err_email;
    }

    @Override
    public void updateLoginTime(Long id) {
        this.baseMapper.updateLoginTime(id);
    }


}
