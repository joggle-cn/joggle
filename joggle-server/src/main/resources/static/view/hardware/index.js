/**
 *
 * Home 主页 模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./index.css'], function (app) {//加载依赖js,
	var callback = ["$scope", function ($scope) {

		// 校验是否登录

        // 加载用户登录信息
        faceinner.get(api['user.login.info'], function(res){
            if(res.code == '040006'){ // 没有登录
                if(localStorage.token){
                    window.location.href='#/login';
                }
            }
        });






 	}];


	app.controller('HardwareController', callback );
	return callback;
});
