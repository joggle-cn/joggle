package com.wuweibi.bullet.feedback.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.feedback.domain.FeedbackDTO;
import com.wuweibi.bullet.feedback.entity.Feedback;
import com.wuweibi.bullet.feedback.service.FeedbackService;
import com.wuweibi.bullet.service.MailService;
import com.wuweibi.bullet.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * 意见反馈(Feedback)表控制层
 *
 * @author marker
 * @since 2022-04-01 17:32:37
 */
@WebApi
@Api(tags = "意见反馈")
@RestController
@RequestMapping("/api/open/feedback")
public class FeedbackController {
    /**
     * 服务对象
     */
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;


    /**
     * 提交意见反馈
     *
     * @return 单条数据
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody @Valid FeedbackDTO dto, @JwtUser Session session) {
        Long userId = 0l;
        if(session.isLogin()){
            userId = session.getUserId();
        }
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(dto, feedback);
        feedback.setUserId(userId);
        feedback.setCreateTime(new Date());

        this.feedbackService.save(feedback);
        return R.ok("");
    }


}