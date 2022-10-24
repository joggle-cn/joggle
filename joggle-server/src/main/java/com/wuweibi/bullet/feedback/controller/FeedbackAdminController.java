package com.wuweibi.bullet.feedback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuweibi.bullet.common.domain.IdDTO;
import com.wuweibi.bullet.common.domain.PageParam;
import com.wuweibi.bullet.config.properties.BulletConfig;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.feedback.domain.FeedbackListVO;
import com.wuweibi.bullet.feedback.domain.FeedbackParam;
import com.wuweibi.bullet.feedback.domain.FeedbackReplyDTO;
import com.wuweibi.bullet.feedback.entity.Feedback;
import com.wuweibi.bullet.feedback.service.FeedbackService;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @Resource
    private BulletConfig bulletConfig;


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


    /**
     * 删除反馈
     * @return
     */
    @ApiOperation("删除反馈")
    @DeleteMapping(value = "")
    public R delete(@RequestBody @Valid IdDTO dto) {
        Integer id = dto.getId();
        this.feedbackService.removeById(id);
        return R.ok();
    }



    /**
     * 回复意见反馈
     *
     * @return 单条数据
     */
    @PostMapping("/reply")
    public R reply(@RequestBody @Valid FeedbackReplyDTO dto, HttpServletRequest request) {
        Feedback feedback = this.feedbackService.getById(dto.getFeedbackId());
        if (feedback == null) {
            return R.fail("反馈不存在");
        }
        if (StringUtils.isNotBlank(feedback.getReply())) {
            return R.fail("该反馈已回复");
        }
        feedback.setReply(dto.getReply());
        feedback.setReplyTime(new Date());
        this.feedbackService.updateById(feedback);

        // 发送反馈邮件
        Long userId = feedback.getUserId();
        User user = userService.getById(userId);
        String email = user.getEmail();

        Map<String, Object> params = new HashMap<>(2);
        params.put("url", bulletConfig.getServerUrl());
        params.put("feedback", feedback);
        mailService.send(email, "Joggle意见反馈-回复", params, "feedback_reply.htm");

        return R.ok();
    }



}