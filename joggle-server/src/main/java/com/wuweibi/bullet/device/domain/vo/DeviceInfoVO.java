package com.wuweibi.bullet.device.domain.vo;

import com.wuweibi.bullet.device.domain.DevicePeersVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备详情页响应对象
 *
 * @author marker
 */
@Data
@ApiModel("设备详情信息")
public class DeviceInfoVO {

    @ApiModelProperty("设备基本信息")
    private DeviceDetailVO deviceInfo;

    @ApiModelProperty("功能统计")
    private Features features;

    @ApiModelProperty("端口映射列表")
    private List<MappingDeviceVO> portList;

    @ApiModelProperty("域名映射列表")
    private List<MappingDeviceVO> domainList;

    @ApiModelProperty("P2P端到端列表")
    private List<DevicePeersVO> p2pList;


    @Data
    @ApiModel("设备功能统计")
    public static class Features {

        @ApiModelProperty("域名映射数量")
        private Integer domainCount;

        @ApiModelProperty("端口映射数量")
        private Integer portCount;

        @ApiModelProperty("P2P端到端数量")
        private Integer p2pCount;
    }
}
