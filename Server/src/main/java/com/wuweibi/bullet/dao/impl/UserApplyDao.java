package com.wuweibi.bullet.dao.impl;

import com.faceinner.dao.AliasDao;
import com.faceinner.domain.UserApply;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * 用户修改密码申请
 */
@Repository(AliasDao.USERAPPLY)
public class UserApplyDao{

	@Autowired
	private SqlSessionTemplate template;




	public int save(UserApply entity) {
		return template.insert("mapper.userapply.insert", entity);
	}






	public boolean existActiveCode(String code) {
		return template.selectOne("mapper.userapply.existActiveCode", code);
	}






	public void updateStatus(String code) {
		template.update("mapper.userapply.updateStatus", code);
	}






	public void updateEmailStatus(String email) {
		template.update("mapper.userapply.updateEmailStatus", email);

	}



    /**
     * 根据Code查询申请信息
     * @param code 申请码
     * @return
     */
    public UserApply findByCode(String code){
        return template.selectOne("mapper.userapply.findByCode", code);
    }

}
