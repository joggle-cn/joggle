package com.wuweibi.bullet.dao.impl;

import com.faceinner.dao.AliasDao;
import com.faceinner.domain.Secret;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秘密Dao
 * 所有人都可以发布秘密,发布秘密后可以被别人发现秘密.
 *
 *
 * Created by marker on 16/8/3.
 */
@Repository(AliasDao.Secret)
public class SecretDao {

    @Autowired
    private SqlSessionTemplate template;


    /**
     * 保存
     * @param entity
     */
    public int save(Secret entity) {
        return template.insert("mapper.secret.insert", entity);
    }


    /**
     * 查询附近的
     * @return
     */
    public List<Secret> findNear(double longitude, double latitude){
        Map<String,Object> params = new HashMap<String,Object>(3);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("lat", 20);


        return template.selectList("mapper.secret.selectNear",params);
    }

}
