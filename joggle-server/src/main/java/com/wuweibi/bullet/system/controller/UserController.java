package com.wuweibi.bullet.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.annotation.ResponseMessage;
import com.wuweibi.bullet.conn.WebsocketPool;
import com.wuweibi.bullet.domain.ResultMessage;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.params.PasswordParam;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.entity.UserFlow;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.oauth2.utils.SecurityUtils;
import com.wuweibi.bullet.res.service.ResourcePackageService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.system.domain.dto.NoticeSwitchParam;
import com.wuweibi.bullet.system.domain.vo.UserDetailVO;
import com.wuweibi.bullet.system.entity.User;
import com.wuweibi.bullet.system.entity.UserCertification;
import com.wuweibi.bullet.system.service.UserCertificationService;
import com.wuweibi.bullet.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private WebsocketPool websocketPool;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Resource
    private UserFlowService userFlowService;

    @Resource
    private UserCertificationService userCertificationService;

    @Resource
    private ResourcePackageService resourcePackageService;

    /**
     * 获取登录的用户信息
     */
    @ApiOperation("获取登录的用户信息")
    @GetMapping("/login/info")
    public R loginInfo() {
        if (SecurityUtils.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }

        Long userId = SecurityUtils.getUserId();

        // 验证邮箱正确性
        UserDetailVO user = userService.getDetailById(userId);
        UserFlow userFlow = userFlowService.getUserFlow(userId);
        user.setPassword(null);

        JSONObject result = (JSONObject) JSON.toJSON(user);

        result.put("connNums", websocketPool.count());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        result.put("loginTime", sdf.format(user.getLoginTime()));
        result.put("balance", StringUtil.roundHalfUp(user.getBalance()));
        result.put("userFlow", userFlow.getFlow()/1024); //MB
        result.put("userPackageFlow", user.getUserPackageFlow()/1024); //MB
        result.put("userCertification", user.getUserCertification());
        result.put("systemNotice", user.getSystemNotice());

        if (user.getUserCertification() != 1) {
            UserCertification userCertification = userCertificationService.getLastResult(userId);
            if (userCertification != null) {
                result.put("ucResultMsg", userCertification.getResultMsg());
                result.put("ucExamineTime", userCertification.getExamineTime());
            }
        }

        return R.success(result);
    }


    @RequestMapping(value = "/internet", method = RequestMethod.POST)
    public ResultMessage register(HttpServletRequest request, @RequestParam("from") String from) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = new User();
            String accessToken = (String) session.getAttribute("demo_access_token");
            String openID = (String) session.getAttribute("demo_openid");
            if ("qq".equals(from)) {
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean;
                try {
                    userInfoBean = qzoneUserInfo.getUserInfo();
                    if (userInfoBean.getRet() == 0) {
//						user.setName(userInfoBean.getNickname());

//						user.setOpenId(openID);
                        return new ResultMessage(true, user);
                    } else {
                        return new ResultMessage(false, "很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                    }
                } catch (QQConnectException e) {
                    e.printStackTrace();
                }
            }

        } else {
            new ResultMessage(false, "操作失败 session invalid");
        }


        return new ResultMessage(false, "操作失败");
    }


    @Resource()
    ConsumerTokenServices consumerTokenServices;


    /**
     * 注销登录操作
     */
    @ApiOperation("注销登录操作")
    @PostMapping(value = "/loginout")
    public R loginOut(HttpServletRequest request) {
        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        String tokenValue = StringUtils.substring(authentication, AuthenticationService.BEARER_BEGIN_INDEX);
        if (consumerTokenServices.revokeToken(tokenValue)) {
            return R.success();
        } else {
            return R.fail(AuthErrorType.INVALID_REQUEST);
        }
    }


//    /**
//     * 修改密码
//     */
//    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
//    public MessageResult changepass(@RequestParam String pass,
//                                    @RequestParam String code,
//                                    HttpServletRequest request) {
//        // 根据code查询用户信息
//        return userService.changePass4Code(code, pass);
//    }
//


    /**
     * 修改密码
     */
    @ResponseMessage
    @PostMapping(value = "/password")
    public R password(@Valid PasswordParam dto, @JwtUser Session session) {
        if (!session.isLogin()) {
            return R.fail(SystemErrorType.LOGIN_INVALID);
        }
        Long userId = session.getUserId();
        boolean status = userService.updatePassword(userId, dto);
        if (status) {
            return R.success();
        }
        return R.fail();
    }

    /**
     * 系统通知开关
     */
    @ApiOperation("系统通知开关")
    @ResponseMessage
    @PostMapping(value = "/notice/switch")
    public R<Boolean> noticeSwitch(@RequestBody @Valid NoticeSwitchParam dto, @JwtUser Session session) {
        Long userId = session.getUserId();
        boolean status = userService.updateSystemNotice(userId, dto.getStatus());
        if (status) {
            return R.success();
        }
        return R.fail();
    }


}
