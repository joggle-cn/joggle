package com.wuweibi.bullet.service.impl;

import com.wuweibi.bullet.dashboard.domain.DeviceCountInfoVO;
import com.wuweibi.bullet.dashboard.domain.DeviceDateItemVO;
import com.wuweibi.bullet.dashboard.domain.UserCountVO;
import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.mapper.CountMapper;
import com.wuweibi.bullet.service.CountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CountServiceImpl implements CountService {

    @Resource
    private CountMapper countMapper;

    @Override
    public CountVO getCountInfo() {
        return countMapper.selectCountInfo();
    }

    @Override
    public UserCountVO getUserCountInfo(Long userId) {
        UserCountVO userCountVO =  countMapper.selectUserCountInfo(userId);
        if(userCountVO == null){
            userCountVO = new UserCountVO();
        }
        return userCountVO;
    }

    @Override
    public List<DeviceCountInfoVO> getUserDeviceRank(Long userId, Integer type) {
        List<DeviceCountInfoVO> userCountVO = countMapper.selectUserDeviceRank(userId, type);
        return userCountVO;
    }

    @Override
    public List<DeviceDateItemVO> getUserDeviceTrend(Long userId, Long deviceId) {
        return countMapper.selectUserDeviceTrend(userId, deviceId);
    }

    @Override
    public List<DeviceDateItemVO> getAllFlowTrend(int day) {
        return countMapper.selectAllFlowTrend(day);
    }
}
