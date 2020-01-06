package com.wuweibi.bullet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.WeixinAccount;
import com.wuweibi.bullet.jwt.domain.JwtSession;
import com.wuweibi.bullet.jwt.utils.JWTUtil;
import com.wuweibi.bullet.mapper.WeixinAccountMapper;
import com.wuweibi.bullet.service.WeixinAccountService;
import com.wuweibi.bullet.utils.HTTPRequest;
import com.wuweibi.bullet.weixin.WXBizDataCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author marker
 * @since 2017-12-08
 */
@Service
public class WeixinAccountServiceImpl extends ServiceImpl<WeixinAccountMapper, WeixinAccount> implements WeixinAccountService {



    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;


    @Override
    public MessageResult bind(User user, String code, String encryptedData, String iv) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String params = "appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code";
        String result = HTTPRequest.sendGet(url, params);

        JSONObject obj = JSON.parseObject(result);

        if(obj.getIntValue("errcode") == 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String sessionKey = obj.getString("session_key");
            String openId = obj.getString("openid");

            WXBizDataCrypt wxBizDataCrypt = new WXBizDataCrypt(appId, sessionKey);
            try {
                JSONObject json = wxBizDataCrypt.decrypt(encryptedData,iv);




                WeixinAccount account = new WeixinAccount();
                account.setCode(code);
                account.setTime(new Date());
                account.setOpenId(openId);
                account.setUnionId(obj.getString("unionid"));
                account.setSessionKey(sessionKey);
                account.setUserId(user.getId());

                account.setCountry(  json.getString("country"));
                account.setCity(json.getString("city"));
                account.setGender(json.getIntValue("gender"));
                account.setAvatarUrl(json.getString("avatarUrl"));
                account.setNickName(json.getString("nickName"));
                account.setLanguage(json.getString("language"));



                // 生成Session信息
                String token = JWTUtil.sign(user.getPassword(), JwtSession.builder().userId(user.getId()));


                account.setJwtToken(token);



                if(this.baseMapper.existsOpenId(openId)){
                    this.baseMapper.updateByOpenId(account);
                }else{
                    insert(account);
                }

                return MessageFactory.get(token);
            } catch (Exception e) {
                return MessageFactory.getExceptionMessage(e);
            }
        }
        return MessageFactory.getErrorMessage(obj.getString("errmsg"));
    }
}
