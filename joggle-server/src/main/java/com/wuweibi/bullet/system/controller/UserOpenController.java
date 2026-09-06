package com.wuweibi.bullet.system.controller;

import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.config.swagger.annotation.WebApi;
import com.wuweibi.bullet.domain.message.FormFieldMessage;
import com.wuweibi.bullet.domain.message.MessageFactory;
import com.wuweibi.bullet.domain.message.MessageResult;
import com.wuweibi.bullet.service.UserForgetService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.domain.UserPassForgetApplyDTO;
import com.wuweibi.bullet.system.domain.UserPassForgetDTO;
import com.wuweibi.bullet.utils.HttpUtils;
import com.wuweibi.bullet.utils.SpringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


/**
 * 用户开放接口
 *
 * @author marker
 * @version 1.0
 */
@WebApi
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/open/user")
public class UserOpenController {

    @Resource
    private UserService userService;


    /**
     * 忘记密码-修改密码
     */
    @Operation(summary = "忘记密码-修改密码")
    @PostMapping(value = "/changepass")
    public MessageResult changepass(@RequestBody @Valid UserPassForgetDTO dto ) {
        // 根据code查询用户信息
        return userService.changePass4Code(dto.getCode(), dto.getPass());
    }


    /**
     * 忘记密码
     *
     * @return
     */
    @Operation(summary = "申请忘记密码", description = "忘记密码将收到修改密码的邮件")
    @PostMapping(value = "/forget")
    public MessageResult forget(@RequestBody UserPassForgetApplyDTO dto,
            HttpServletRequest request) {
        String email = dto.getEmail();
        // 验证邮箱正确性
        if (email.indexOf("@") == -1 && !SpringUtils.emailFormat(email)) {// 邮箱格式不正确
            FormFieldMessage ffm = new FormFieldMessage();
            ffm.setField("email");
            ffm.setStatus(State.RegEmailError);
            return MessageFactory.getForm(ffm);
        }
        String ip  = HttpUtils.getRemoteHost(request);
        String url = dto.getSiteUrl();

        // 验证最近5分钟是否申请过
        boolean applyStatus = userForgetService.checkApply(email, 5 * 60);
        if(applyStatus){
            return MessageFactory.getErrorMessage("最近申请过忘记密码，请查收等待一段时间再次操作");
        }
        return userService.applyChangePass(email, url, ip);
    }

    @Resource
    private UserForgetService userForgetService;


}
