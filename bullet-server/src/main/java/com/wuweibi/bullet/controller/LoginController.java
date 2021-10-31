package com.wuweibi.bullet.controller;


import com.wuweibi.bullet.alias.MessageCode;
import com.wuweibi.bullet.alias.SessionAttr;
import com.wuweibi.bullet.alias.State;
import com.wuweibi.bullet.annotation.ResponseMessage;
import com.wuweibi.bullet.controller.validator.LoginParamValidator;
import com.wuweibi.bullet.domain.params.LoginParam;
import com.wuweibi.bullet.oauth2.domain.OauthUser;
import com.wuweibi.bullet.oauth2.service.OauthUserService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.SessionHelper;
import com.wuweibi.bullet.utils.ValidationUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 登录读物
 *
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @Resource
    private OauthUserService oauthUserService;

    /**
     * 消息通知
     */
//	@Autowired private NoticeService noticeService;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        binder.setValidator(new LoginParamValidator()); //添加一个spring自带的validator
    }


    /**
     * 登录操作
     */
    @ResponseMessage
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request,
                        @Validated @ModelAttribute("user") LoginParam user, Errors errs
            , HttpSession session) {

        if (errs.hasErrors()) {
            return errs;
        }
        // 验证码鉴定，超过三次登录则提示验证码
        String email = user.getName();


        int code = oauthUserService.login(email, user.getPass());
        switch (code) {
            case MessageCode.login_suc_user:
                OauthUser loginUser = oauthUserService.getByEmail(email);
                session.setAttribute(SessionAttr.LOGIN_USER, loginUser);
                session.setAttribute(SessionHelper.USER_ID, loginUser.getId());


//			String msg = "登录用户: "+email+
//					"昵称："+loginUser.getName()+
//					"设备： PC\n" +
//					"时间： " + new Date() +
//					"【维护中心】";
//
//			 noticeService.send(msg);

                return State.OperationSuccess;// 登录成功

            default:
                ValidationUtils.reject(errs, "name", State.AccountOrPassError);
                return errs;
        }
    }

    @Resource
    private UserService userService;




}
