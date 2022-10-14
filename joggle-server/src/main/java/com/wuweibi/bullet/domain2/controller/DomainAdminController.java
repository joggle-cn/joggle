package com.wuweibi.bullet.domain2.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.AdminApi;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.device.entity.ServerTunnel;
import com.wuweibi.bullet.device.service.ServerTunnelService;
import com.wuweibi.bullet.domain2.domain.DomainAdminParam;
import com.wuweibi.bullet.domain2.domain.dto.ReleaseResourceDTO;
import com.wuweibi.bullet.domain2.domain.vo.DomainListVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DomainService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 我的域名接口
 *
 * @author marker
 * @create 2019-12-26 下午9:19
 **/
@Slf4j
@AdminApi
@RestController
@RequestMapping("/admin/domain")
public class DomainAdminController {


    @Resource
    private WebsocketPool websocketPool;


    /**
     * 域名管理
     */
    @Resource
    private DomainService domainService;
    @Resource
    private ServerTunnelService serverTunnelService;

    @Resource
    private DeviceMappingService deviceMappingService;



    /**
     * 域名分页查询
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("域名分页查询")
    @GetMapping("/list")
    public R<Page<DomainListVO>> getAdminList(PageParam page, DomainAdminParam params) {
        return R.ok(this.domainService.getAdminList(page.toMybatisPlusPage(), params));
    }




    /**
     * 搜索可购买的域名
     */
    @ApiOperation(value = "发放域名", tags="后台")
    @PostMapping("/release")
    public R<Boolean> releaseDomain(@RequestBody @Valid ReleaseResourceDTO dto) {
        // 校验参数
        if (!ArrayUtils.contains(new int[]{1, 2}, dto.getType())) {
            return R.fail("资源类型错误");
        }

        // 校验通道师傅存在
        ServerTunnel serverTunnel = serverTunnelService.getById(dto.getServerTunnelId());
        if (serverTunnel == null) {
            return R.fail("服务器通道不存在");
        }

        boolean status = domainService.release(dto);
        return R.ok(status);
    }



}
