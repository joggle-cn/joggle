package com.wuweibi.bullet.service.impl;

import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.mapper.CountMapper;
import com.wuweibi.bullet.service.CountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class CountServiceImpl implements CountService {

    @Resource
    private CountMapper countMapper;

    @Override
    public CountVO getCountInfo() {
        return countMapper.selectCountInfo();
    }
}
