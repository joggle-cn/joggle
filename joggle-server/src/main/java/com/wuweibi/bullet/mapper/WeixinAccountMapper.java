package com.wuweibi.bullet.mapper;/**
 * Created by marker on 2019/6/5.
 */


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuweibi.bullet.entity.WeixinAccount;

/**
 * @author marker
 * @create 2019-06-05 17:54
 **/
public interface WeixinAccountMapper extends BaseMapper<WeixinAccount> {
    boolean existsOpenId(String openId);

    void updateByOpenId(WeixinAccount account);
}
