package com.wuweibi.bullet.device.domain.vo;

import com.wuweibi.bullet.device.domain.DevicePeersVO;
import lombok.Data;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备详情页响应对象
 *
 * @author marker
 */
@Data
@Schema(description = "设备详情信息")
public class DeviceInfoVO {

    @Schema(description = "设备基本信息")
    private DeviceDetailVO deviceInfo;

    @Schema(description = "功能统计")
    private Features features;

    @Schema(description = "端口映射列表")
    private List<DeviceMappingClientVO> portList;

    @Schema(description = "域名映射列表")
    private List<DeviceMappingClientVO> domainList;

    @Schema(description = "P2P端到端列表")
    private List<DevicePeersVO> p2pList;

    @Data
    @Schema(description = "设备功能统计")
    public static class Features {

        @Schema(description = "域名映射数量")
        private Integer domainCount;

        @Schema(description = "端口映射数量")
        private Integer portCount;

        @Schema(description = "P2P端到端数量")
        private Integer p2pCount;
    }
}
