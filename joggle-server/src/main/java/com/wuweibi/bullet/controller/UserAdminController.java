package com.wuweibi.bullet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.annotation.JwtUser;
import com.wuweibi.bullet.annotation.ResponseMessage;
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.domain.domain.session.Session;
import com.wuweibi.bullet.domain.params.PasswordParam;
import com.wuweibi.bullet.entity.User;
import com.wuweibi.bullet.entity.api.R;
import com.wuweibi.bullet.exception.type.AuthErrorType;
import com.wuweibi.bullet.exception.type.SystemErrorType;
import com.wuweibi.bullet.flow.entity.UserFlow;
import com.wuweibi.bullet.flow.service.UserFlowService;
import com.wuweibi.bullet.oauth2.service.AuthenticationService;
import com.wuweibi.bullet.service.UserService;
import com.wuweibi.bullet.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author marker
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

    @Resource
    private UserService userService;

    @Resource
    private CoonPool pool;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 添加一个日期类型编辑器，也就是需要日期类型的时候，怎么把字符串转化为日期类型
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Resource
    private UserFlowService userFlowService;

    /**
     * 获取登录的用户信息
     */
    @RequestMapping(value = "/login/info", method = RequestMethod.GET)
    public R loginInfo(@JwtUser Session session) {

        if (session.isNotLogin()) {
            return R.fail(AuthErrorType.INVALID_LOGIN);
        }

        Long userId = session.getUserId();

        // 验证邮箱正确性
        User user = userService.getById(userId);
        UserFlow userFlow = userFlowService.getUserFlow(userId);
        user.setPassword(null);

        JSONObject result = (JSONObject) JSON.toJSON(user);

        result.put("connNums", pool.count());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        result.put("loginTime", sdf.format(user.getLoginTime()));
        result.put("balance", StringUtil.roundHalfUp(user.getBalance()));
        result.put("userFlow", userFlow.getFlow()/1024); //MB


        return R.success(result);
    }



    @Resource()
    ConsumerTokenServices consumerTokenServices;


    /**
     * 注销操作
     */
    @RequestMapping(value = "/loginout", method = RequestMethod.POST)
    public R loginout(HttpServletRequest request) {
        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);
        String tokenValue = StringUtils.substring(authentication, AuthenticationService.BEARER_BEGIN_INDEX);
        if (consumerTokenServices.revokeToken(tokenValue)) {
            return R.success();
        } else {
            return R.fail(AuthErrorType.INVALID_REQUEST);
        }
    }


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


}