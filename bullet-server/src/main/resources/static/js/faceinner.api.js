/**
 * api 接口地址
 *
 *
 * @type {{}}
 */




let api = {

    /** 初始化数据  */
    "init": '/api/open/init',
    /* 网站数据统计 */
    "statistics": '/api/open/statistics',

    /* 忘记密码 */
    "user.forget": '/api/open/forget',

    /* 注册 */
    "user.register": '/api/open/register',

    /* 修改密码 */
    "user.changepass": '/api/user/changepass',

    /* 登录接口 */
    "user.login": '/api/login',

    /** 获取Token */
    "user.token": '/oauth/token',




    /* 登录用户的信息 */
    "user.login.info": '/api/user/login/info',
    // 修改密码
    "user.login.password": '/api/user/password',

    /* 注销 */
    "user.loginout": '/api/user/loginout',


    /* 发布秘密 */
    "secret.publish": '/api/secret/publish',
    /* 附近秘密 */
    "secret.near": '/api/secret/near',


    /* 用户设备 */
    "user.device": '/api/user/device',
    "user.device.info": '/api/user/device/info',
    "user.device.validate": '/api/user/device/validate',
    "user.device.mapping": '/api/user/device/mapping/',
    "user.device.wol": '/api/user/device/wol',
    "user.device.discovery": '/api/user/device/discovery',
    "system.ngrokd.check": '/api/system/ngrokd/check',

    /* 用户域名与端口 */
    "user.domain": '/api/user/domain/',
    "user.domain.info": '/api/user/domain/info',
    "user.domain.calculate": '/api/user/domain/calculate',
    "user.domain.pay": '/api/user/domain/pay',
    "user.domain.nobind": '/api/user/domain/nobind',
    "user.domain.bind.device": '/api/user/domain/bind',

    "user.dashboard.statistics": '/api/dashboard/statistics',
    "user.dashboard.device.rank": '/api/dashboard/device/rank',





};


/**
 * 对外暴露接口
 */
define([], function () {
    return api;

});
