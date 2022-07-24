package com.wuweibi.bullet.domain2.controller;
/**
 * Created by marker on 2017/12/6.
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain2.domain.DomainBuyListVO;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DomainService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 我的域名接口
 *
 * @author marker
 * @create 2019-12-26 下午9:19
 **/
@Slf4j
@RestController
@RequestMapping("/admin/domain")
public class DomainAdminController {


    @Resource
    private CoonPool coonPool;


    /**
     * 域名管理
     */
    @Resource
    private DomainService domainService;

    @Resource
    private DeviceMappingService deviceMappingService;

    /**
     * 搜索可购买的域名
     */
    @ApiOperation(value = "发放域名", tags="后台")
    @PostMapping("/release")
    public R<Page<DomainBuyListVO>> releaseDomain() {
        boolean status = domainService.release();
        return R.success();
    }


}
