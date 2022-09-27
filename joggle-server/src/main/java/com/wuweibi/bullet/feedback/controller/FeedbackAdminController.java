package com.wuweibi.bullet.feedback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.feedback.domain.FeedbackListVO;
import com.wuweibi.bullet.feedback.domain.FeedbackParam;
import com.wuweibi.bullet.feedback.entity.Feedback;
import com.wuweibi.bullet.feedback.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 意见反馈(Feedback)表控制层
 *
 * @author marker
 * @since 2022-04-01 17:32:37
 */
@WebApi
@Api(tags = "意见反馈")
@RestController
@RequestMapping("/admin/feedback")
public class FeedbackAdminController {
    /**
     * 服务对象
     */
    @Resource
    private FeedbackService feedbackService;

    /**
     * 分页查询所有数据2
     *
     * @param page   分页对象
     * @param params 查询实体
     * @return 所有数据
     */
    @ApiOperation("意见反馈分页查询")
    @GetMapping("/list")
    public R<Page<FeedbackListVO>> getPageList(PageParam page, FeedbackParam params) {
        return R.ok(this.feedbackService.getAdminList(page.toMybatisPlusPage(), params));
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("/detail")
    public R<Feedback> detail(@RequestParam Integer id) {
        return R.ok(this.feedbackService.queryById(id));
    }



}