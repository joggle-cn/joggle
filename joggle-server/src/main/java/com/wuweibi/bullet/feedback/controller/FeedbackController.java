package com.wuweibi.bullet.feedback.controller;

import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.feedback.domain.FeedbackDTO;
import com.wuweibi.bullet.feedback.entity.Feedback;
import com.wuweibi.bullet.feedback.service.FeedbackService;


import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * 意见反馈(Feedback)表控制层
 *
 * @author marker
 * @since 2022-04-01 17:32:37
 */
@RestController
@RequestMapping("/api/open/feedback")
public class FeedbackController {
    /**
     * 服务对象
     */
    @Resource
    private FeedbackService feedbackService;

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

        this.feedbackService.insert(feedback);
        return R.success("");
    }

}