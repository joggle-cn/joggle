package com.wuweibi.bullet.device.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuweibi.bullet.device.domain.param.DeviceServiceParam;
import com.wuweibi.bullet.device.domain.vo.DeviceServiceVO;
import com.wuweibi.bullet.device.service.DeviceServiceService;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.mapper.DeviceMappingMapper;
import com.wuweibi.bullet.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class DeviceServiceServiceImpl extends ServiceImpl<DeviceMappingMapper, DeviceMapping> implements DeviceServiceService {

    @Override
    public Page<DeviceServiceVO> getListPage(Page pageParams, DeviceServiceParam params) {
        Page<DeviceMapping> page = this.baseMapper.selectServiceListPage(pageParams, params);
        Page<DeviceServiceVO> pageVO = new Page<>(page.getCurrent(),page.getPages(), page.getTotal());
        pageVO.setRecords( page.getRecords().stream().map(item -> {
            DeviceServiceVO deviceServiceVO = new DeviceServiceVO();
            deviceServiceVO.setId(item.getId());
            deviceServiceVO.setDeviceId(item.getDeviceId());
            deviceServiceVO.setStatus(item.getStatus());
            deviceServiceVO.setName(item.getName());
            //
            String domain = item.getDomain();
            Integer port = item.getRemotePort();
//            String protocol = ProtocolTypeEnum.getProtocol(item.getProtocol());
            if (StringUtil.isNotBlank(item.getHostname())) {
                domain = item.getHostname();

            }
            if(item.getDomainId() == null) {// 空就显示本地
                domain = item.getHost();
                port = item.getPort();
            }
            deviceServiceVO.setUri(String.format("%s:%d", domain, port));
            deviceServiceVO.setType(item.getPortProtocol());
            return deviceServiceVO;
        }).collect(Collectors.toList()));
        return pageVO;
    }
}
