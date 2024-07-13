package com.wuweibi.bullet.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wuweibi.bullet.config.cache.RedisTemplateConfig;
import com.wuweibi.bullet.dashboard.domain.*;
import com.wuweibi.bullet.domain.vo.CountVO;
import com.wuweibi.bullet.mapper.CountMapper;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.service.CountService;
import com.wuweibi.bullet.utils.BigDecimalUtils;
import com.wuweibi.bullet.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_FLOW_TODAY;
import static com.wuweibi.bullet.alias.CacheCode.DEVICE_MAPPING_STATISTICS_LINK_TODAY;


@Service
public class CountServiceImpl implements CountService {

    @Resource
    private CountMapper countMapper;
    @Resource
    private DeviceMappingMapper deviceMappingMapper;

    @Resource(name = RedisTemplateConfig.BEAN_REDIS_TEMPLATE)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public CountVO getCountInfo() {
        return countMapper.selectCountInfo();
    }

    @Override
    public UserCountVO getUserCountInfo(Long userId) {
        UserFlowCountDTO userFlowCountDTO =  countMapper.selectUserCountInfo(userId);

        UserCountVO userCountVO = new UserCountVO();
        BeanUtils.copyProperties(userFlowCountDTO, userCountVO);

        if (userCountVO == null) {
            userCountVO = new UserCountVO();
        }

        // 数据库查询今日数据为0， 获取今日流量
        // 查询用户得maping 列表， 遍历redis
        List<Integer> idList = deviceMappingMapper.getMappingIdByUserId(userId);
        if(CollectionUtils.isEmpty(idList)){
            return userCountVO;
        }
        // redis 获取今日流量
        String date = DateUtil.format(new Date(), "yyyyMMdd");
        String keyBytes = String.format(DEVICE_MAPPING_STATISTICS_FLOW_TODAY, date);
        String keyLink = String.format(DEVICE_MAPPING_STATISTICS_LINK_TODAY, date);
        BoundHashOperations<String, Object, Object> keyBytesMap = redisTemplate.boundHashOps(keyBytes);
        BoundHashOperations<String, Object, Object> keyLinkMap = redisTemplate.boundHashOps(keyLink);

        Map<String, BigDecimal> todayData = idList.stream().flatMap(mappingId -> {
            Integer flowKb = (Integer) keyBytesMap.get(mappingId.toString());
            Integer linkNum = (Integer) keyLinkMap.get(mappingId.toString());

            return Stream.of(new TodayFlowInfo("flow",
                    new BigDecimal(flowKb == null ? 0 : flowKb)
                            .divide(BigDecimal.valueOf(1024))
            ), new TodayFlowInfo("link", new BigDecimal(flowKb == null ? 0 : linkNum)));
        }).collect(Collectors.groupingBy(TodayFlowInfo::getType,
                Collectors.mapping(TodayFlowInfo::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                )
        ));
        userCountVO.setTodayFlow(todayData.get("flow").setScale(2, RoundingMode.HALF_UP));
        userCountVO.setTodayLink(todayData.get("link").intValue());
        userCountVO.setTodayFlowOn(BigDecimalUtils.getChainRatio(todayData.get("flow"), userFlowCountDTO.getTodayFlow2()));

        return userCountVO;
    }



    @Data
    @AllArgsConstructor
    static class TodayFlowInfo{
        private String type;
        private BigDecimal value;
    }



    @Override
    public List<DeviceCountInfoVO> getUserDeviceRank(Long userId, Integer type) {
        if(Objects.equals(1, type)){
            // 查询用户设备清单和mappingId
            List<DeviceMappingInfoDTO> deviceMappingInfoDTOList = deviceMappingMapper.getDeviceMappingIdByUserId(userId);
            if(deviceMappingInfoDTOList.isEmpty()){
                return Collections.EMPTY_LIST;
            }
            // redis 获取今日流量
            String date = DateUtil.format(new Date(), "yyyyMMdd");
            String keyBytes = String.format(DEVICE_MAPPING_STATISTICS_FLOW_TODAY, date);
            BoundHashOperations<String, Object, Object> keyBytesMap = redisTemplate.boundHashOps(keyBytes);

            // 匹配流量数据
            deviceMappingInfoDTOList.forEach(item->{
                List<Integer> mappingId = StringUtil.splitInt(item.getMappingIds(),",");
                mappingId.forEach(id -> {
                    Integer flowKb = (Integer) keyBytesMap.get(id.toString());
                    BigDecimal val = new BigDecimal(flowKb == null ? 0 : flowKb)
                            .divide(BigDecimal.valueOf(1024));
                    item.setFlow(item.getFlow().add(val));
                });
            });
            return deviceMappingInfoDTOList.stream().map(item -> {
                DeviceCountInfoVO deviceCountInfoVO = new DeviceCountInfoVO();
                BeanUtils.copyProperties(item, deviceCountInfoVO);
                return deviceCountInfoVO;
            }).collect(Collectors.toList());
        }

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
