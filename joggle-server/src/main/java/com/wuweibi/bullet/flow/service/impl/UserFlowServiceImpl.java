package com.wuweibi.bullet.flow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.flow.entity.UserFlow;
import com.wuweibi.bullet.flow.mapper.UserFlowMapper;
import com.wuweibi.bullet.flow.service.UserFlowService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户流量(UserFlow)表服务实现类
 *
 * @author marker
 * @since 2022-01-09 15:47:10
 */
@Service
public class UserFlowServiceImpl extends ServiceImpl<UserFlowMapper, UserFlow> implements UserFlowService {


    @Override
    public UserFlow getUserFlow(Long userId) {
        UserFlow entity = this.baseMapper.selectById(userId);
        synchronized (this){
            if(entity == null){
                entity =  new UserFlow();
                entity.setUserId(userId);
                entity.setFlow(0L);
                entity.setUpdatedTime(new Date());
                this.baseMapper.insert(entity);
            }
        }
        return entity;
    }

    @Override
    public boolean updateFLow(Long userId, Long bytes) {
        return this.baseMapper.updateFlow(userId, bytes);
    }

    @Override
    public boolean hasFlow(Long userId) {
        return this.baseMapper.selectCount(Wrappers.<UserFlow>lambdaQuery()
                .eq(UserFlow::getUserId, userId)
                .gt(UserFlow::getFlow, 0)
        ) > 0;
    }
}
