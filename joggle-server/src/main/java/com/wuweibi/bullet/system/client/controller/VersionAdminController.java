package com.wuweibi.bullet.system.client.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.controller.validator.LoginParamValidator;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.system.client.domain.ClientVersionAdminListVO;
import com.wuweibi.bullet.system.client.service.ClientVersionService;
import com.wuweibi.bullet.system.domain.dto.ClientVersionParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 后台版本管理
 *
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/client/version")
public class VersionAdminController {

    @Resource
    private ClientVersionService clientVersionService;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        binder.setValidator(new LoginParamValidator()); //添加一个spring自带的validator
    }


    /**
     * 分页查询所有数据2
     *
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("客户端分页查询")
    @GetMapping("/list")
    public R<Page<ClientVersionAdminListVO>> getPageList(PageParam page, ClientVersionParam params) {
        return R.ok(this.clientVersionService.getAdminList(page.toMybatisPlusPage(), params));
    }



    /**
     * 客户端摘要更新
     * @return
     */
    @PostMapping(value = "/update")
    public R update(@RequestBody String text, HttpServletRequest request) {
        String[] lines = text.split("\n");
        for(String line: lines){
            String[] strings = line.split(":");
            String[] binPath = strings[0].split("/");
            String[] oss = binPath[0].split("_");
            String os = oss[0];
            String arch = oss[1];
            String checksum = strings[1];
            String version = strings[2].trim();
            String binFilePath = strings[0];
            clientVersionService.updateChecksumByOsArch(version, os, arch,binFilePath, checksum);
        }
        return R.success();
    }



}
