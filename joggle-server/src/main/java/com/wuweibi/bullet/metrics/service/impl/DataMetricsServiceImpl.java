package com.wuweibi.bullet.metrics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.business.DeviceBiz;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.device.domain.DeviceDetail;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.entity.UserFlow;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.metrics.domain.DataMetricsDTO;
import com.wuweibi.bullet.metrics.domain.DataMetricsListVO;
import com.wuweibi.bullet.metrics.domain.DataMetricsParam;
import com.wuweibi.bullet.metrics.entity.DataMetrics;
import com.wuweibi.bullet.metrics.mapper.DataMetricsMapper;
import com.wuweibi.bullet.metrics.service.DataMetricsService;
import com.wuweibi.bullet.res.service.UserPackageService;
import com.wuweibi.bullet.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 数据收集(DataMetrics)表服务实现类
 *
 * @author marker
 * @since 2021-11-07 14:17:49
 */
@Slf4j
@Service
public class DataMetricsServiceImpl extends ServiceImpl<DataMetricsMapper, DataMetrics> implements DataMetricsService {

    @Override
    public boolean generateDayByTime(Date date) {
        // 不支持今日统计
        if (DateUtils.truncatedCompareTo(date, new Date(), Calendar.DATE) == 0) {
            throw new RuntimeException("不支持当日统计");
        }
        return this.baseMapper.generateDayByTime(date);
    }

    @Resource
    private DeviceService deviceService;
    @Resource
    private ServerTunnelService serverTunnelService;

    @Resource
    private UserFlowService userFlowService;

    @Resource
    private BulletConfig bulletConfig;

    @Resource
    private DeviceBiz deviceBiz;


    @Override
    @Transactional
    public R uploadData(DataMetricsDTO dataMetrics) {
        String deviceNo = dataMetrics.getDeviceNo();
        DeviceDetail deviceDetail = deviceService.getDetailByDeviceNo(deviceNo);
        if (deviceDetail == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_EXIST);
        }

        ServerTunnel serverTunnel = serverTunnelService.getById(deviceDetail.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail(SystemErrorType.DEVICE_NOT_EXIST);
        }

        DataMetrics entity = new DataMetrics();
        BeanUtils.copyProperties(dataMetrics, entity);
        entity.setCreateTime(new Date());
        entity.setServerTunnelId(deviceDetail.getServerTunnelId());
        entity.setDeviceId(deviceDetail.getId());
        entity.setUserId(deviceDetail.getUserId());
        entity.setOpenTime(new Date(dataMetrics.getOpenTime()));
        entity.setCloseTime(new Date(dataMetrics.getCloseTime()));
        entity.setDuration(dataMetrics.getCloseTime() - dataMetrics.getOpenTime());
        entity.setRemoteAddr(dataMetrics.getRemoteAddr());
        this.save(entity);

        Long userId = deviceDetail.getUserId();

        // 扣取流量
        if (serverTunnel.getEnableFlow() == 1) {
            Long bytes = dataMetrics.getBytesIn() + dataMetrics.getBytesOut();

            long byteKb = bytes / 1024;
            if (byteKb == 0) {
                byteKb = 1; // 至少消耗1KB
            }
            // 优先扣套餐流量 (带有保护，不支持负数)
            boolean status = userPackageService.updateFLow(userId, -byteKb);
            if (status){
                return R.ok();
            }
            // 套餐流量扣失败了，扣充值流量（没有保护只有成功）
            status = userFlowService.updateFLow(userId, -bytes / 1024);
            if (!status) { // 后面判断基本不走
                log.warn("流量扣取失败 userId={}", userId);
                return R.fail(SystemErrorType.FLOW_IS_PAY_FAIL);
            }
            UserFlow userFlow = userFlowService.getUserFlowAndPackageFlow(userId); // 套餐流量和充值流量
            if (userFlow.getFlow() < -(1024)){ // 如果流量超出1兆，关闭映射
                // 由于用户没有流量了，默认关闭所有映射
                deviceBiz.closeAllMappingByUserId(userId);
            }
        }
        return R.ok();
    }

    @Resource
    private UserPackageService userPackageService;

    @Override
    public Page<DataMetricsListVO> getList(Page<DataMetrics> page, DataMetricsParam params) {
        return this.baseMapper.selectUserList(page, params);
    }


    /**
     * 每天凌晨0.30执行昨日数据
     *
     * @return
     */
    @Scheduled(cron = "0 1 0 * * *")
    public void generateDayByTimeYesterday() {
        log.info("处理昨日流量数据。。。。");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        this.baseMapper.generateDayByTime(date);
    }
}
