/**
 *
 * 问题反馈 模块
 *
 * @author marker
 * @date 2021-04
 */
define(['app','css!./index.css'], function (app) {//加载依赖js,
	var callback = ["$scope", function ($scope) {

		// 校验是否登录

        // 加载用户登录信息
        faceinner.get(api['user.login.info'], function(res){
            if(res.code == '040006'){ // 没有登录
                window.location.href='#/login';
            }
        });



		 
		 
		 
 	}];
	
	
	app.controller('FeedbackController', callback );
	return callback;
});