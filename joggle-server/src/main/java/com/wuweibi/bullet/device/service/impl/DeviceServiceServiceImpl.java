package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.param.DeviceServiceParam;
import com.wuweibi.bullet.device.domain.vo.DeviceDetailVO;
import com.wuweibi.bullet.device.domain.vo.DeviceServiceVO;
import com.wuweibi.bullet.device.service.DeviceServiceService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMapper;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;


@Service
public class DeviceServiceServiceImpl extends ServiceImpl<DeviceMappingMapper, DeviceMapping> implements DeviceServiceService {


    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Page<DeviceServiceVO> getListPage(Page pageParams, DeviceServiceParam params) {

        DeviceDetailVO deviceInfo = deviceMapper.selectDeviceInfoById(params.getDeviceId());

        Page<DeviceMapping> page = this.baseMapper.selectServiceListPage(pageParams, params);
        Page<DeviceServiceVO> pageVO = new Page<>(page.getCurrent(),page.getPages(), page.getTotal());
        pageVO.setRecords( page.getRecords().stream().map(item -> {
            DeviceServiceVO deviceServiceVO = new DeviceServiceVO();
            deviceServiceVO.setId(item.getId());
            deviceServiceVO.setDeviceId(item.getDeviceId());
            deviceServiceVO.setStatus(item.getStatus());
            deviceServiceVO.setName(item.getName());
            //
            String domain = item.getDomain() + "." + deviceInfo.getServerAddr();
            Integer port = item.getRemotePort();
//            String protocol = ProtocolTypeEnum.getProtocol(item.getProtocol());
            if (StringUtil.isNotBlank(item.getHostname())) {
                domain = item.getHostname();
            }
            if(item.getDomainId() == null || item.getStatus() ==0) {// 空就显示本地
                domain = item.getHost();
                port = item.getPort();
                deviceServiceVO.setUri(String.format("%s:%d", domain, port));
            }else{ //显示远程uri
                if(ArrayUtils.contains(new Integer[]{1, 3, 4},item.getProtocol())){
                    deviceServiceVO.setUri(domain);
                }else{ // 其他协议，显示端口
                    deviceServiceVO.setUri(String.format("%s:%d", deviceInfo.getServerAddr(), port));
                }
            }
            deviceServiceVO.setType(item.getPortProtocol());
            return deviceServiceVO;
        }).collect(Collectors.toList()));
        return pageVO;
    }
}
