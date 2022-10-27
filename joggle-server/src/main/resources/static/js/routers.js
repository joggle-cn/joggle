
// Declare app level module which depends on filters, and services



define([
   	    'angularAMD'
   	   , 'app'

  	], function (angularAMD, app) {//加载依赖js,


/* 配置路由器 */

app.config(['$routeProvider','$locationProvider', function($routeProvider, $locationProvider) {

	/* 主页 */
	$routeProvider
		// 主页
		.when('/index', router({
	        templateUrl: 'view/home/index.htm'
	      , controllerUrl: 'view/home/index.js'
		}))
		// 文档
		.when('/document', router({
			templateUrl: 'view/document/document.htm'
			, controllerUrl: 'view/document/document.js'
		}))
		// 通道
		.when('/tunnels', router({
			templateUrl: 'view/tunnels/tunnels.htm'
			, controllerUrl: 'view/tunnels/tunnels.js'
		}))
		// 硬件
		.when('/hardware', router({
			templateUrl: 'view/hardware/index.htm'
			, controllerUrl: 'view/hardware/index.js'
		}))
		// 关于我们
		.when('/about', router({
			templateUrl: 'view/about/about.htm'
			, controllerUrl: 'view/about/about.js'
		}))
		// 用户协议
		.when('/user/agreement', router({
			templateUrl: 'view/user/agreement/agreement.htm'
			, controllerUrl: 'view/user/agreement/agreement.js'
		}))
		// 下载中心
		.when('/download', router({
			templateUrl: 'view/download/download.htm'
			, controllerUrl: 'view/download/download.js'
		}))

		// 用户反馈
		.when('/feedback', router({
			templateUrl: 'view/feedback/index.htm'
			, controllerUrl: 'view/feedback/index.js'
		}))

		// 登录
		.when('/login', router({
			minTitle: "登录",
			templateUrl: 'view/login/login.htm'
		  , controllerUrl: 'view/login/login.js'
		}))
        /** 忘记密码 */
        .when('/forget', router({
            templateUrl: 'view/forget/forget.htm'
            , controllerUrl: 'view/forget/forget.js'
        }))



		.when('/server-tunnel/nodes', router({
			minTitle: "通道节点"
			, templateUrl: 'view/service/node-status/list.htm'
			, controllerUrl: 'view/service/node-status/list.js'
		}))

		/* 用户路由  */
		.when('/user/profile', router({
			templateUrl: 'view/profile/profile.htm'
			, controllerUrl: 'view/profile/profile.js'
		}))
		/* 邀请界面  */
		.when('/user/invite', router({
			templateUrl: 'view/user/invite/invite.htm'
			, controllerUrl: 'view/user/invite/invite.js'
		}))
		// 激活用户界面
		.when('/user/activate', router({
			templateUrl: 'view/user/activate/activate.htm'
			, controllerUrl: 'view/user/activate/activate.js'
		}))
		// 实名认证界面
		.when('/user/certification', router({
			templateUrl: 'view/user/certification/certification.htm'
			, controllerUrl: 'view/user/certification/certification.js'
		}))

		// 我的设备
		.when('/user/device', router({
            minTitle: "我的设备"
            , templateUrl: 'view/device/device.htm'
            , controllerUrl: 'view/device/device.js'
		}))
        .when('/user/device/bind', router({
            minTitle: "绑定设备"
            , templateUrl: 'view/device/bind.htm'
            , controllerUrl: 'view/device/bind.js'
        }))
        .when('/user/device/wol', router({
            minTitle: "网络唤醒"
            , templateUrl: 'view/device/wol/wol.htm'
            , controllerUrl: 'view/device/wol/wol.js'
        }))
        // 端口映射
        .when('/user/device/:deviceId/mapping', router({
            minTitle: "设备端口映射"
            , templateUrl: 'view/device/mapping.htm'
            , controllerUrl: 'view/device/mapping.js'
        }))
        // 映射日志
        .when('/user/device/:deviceId/mapping/:mappingId/log', router({
            minTitle: "端口映射日志"
            , templateUrl: 'view/device/log/log.htm'
            , controllerUrl: 'view/device/log/log.js'
        }))

		// 我的域名
		.when('/user/domain', router({
			minTitle: "我的域名"
			, templateUrl: 'view/domain/domain.htm'
			, controllerUrl: 'view/domain/domain.js'
		}))
		.when('/user/domain/buy', router({
			minTitle: "购买域名"
			, templateUrl: 'view/service/domain-buy/domain-buy.htm'
			, controllerUrl: 'view/service/domain-buy/domain-buy.js'
		}))
		.when('/user/domain/pay', router({
			minTitle: "支付"
			, templateUrl: 'view/pay/pay.htm'
			, controllerUrl: 'view/pay/pay.js'
		}))
		.when('/user/flow/cash', router({
			minTitle: "购买流量"
			, templateUrl: 'view/service/flow-cash/pay.htm'
			, controllerUrl: 'view/service/flow-cash/pay.js'
		}))
		.when('/user/orders/pay/result', router({
			minTitle: "支付结果"
			, templateUrl: 'view/service/pay-result/pay-result.htm'
			, controllerUrl: 'view/service/pay-result/pay-result.js'
		}))
		.when('/user/orders/list', router({
			minTitle: "订单列表"
			, templateUrl: 'view/service/orders/orders.htm'
			, controllerUrl: 'view/service/orders/orders.js'
		}))
		.when('/user/metrics/list', router({
			minTitle: "流量明细"
			, templateUrl: 'view/user/metrics/metrics.htm'
			, controllerUrl: 'view/user/metrics/metrics.js'
		}))



        // 系统配置
        .when('/system', router({
            minTitle: "系统配置"
            , templateUrl: 'view/system/system.htm'
            , controllerUrl: 'view/system/system.js'
        }))
		// Dashboard
		.when('/user/dashboard', router({
			templateUrl: 'view/dashboard/dashboard.htm'
			, controllerUrl: 'view/dashboard/dashboard.js'
		}))

        // ngrokd服务
        .when('/system/ngrokd', router({
            minTitle: "Ngrok服务"
            , templateUrl: 'view/system/ngrokd/ngrokd.htm'
            , controllerUrl: 'view/system/ngrokd/ngrokd.js'
        }))


		.when('/register', router({
			templateUrl: 'view/register/register.htm'
		  , controllerUrl: 'view/register/register.js'
		}))


		.when('/user/archives', router({
			templateUrl: '/template/user/archives.htm'
		  , controllerUrl: '/js/controllers/user/archives.js'

		}))
		.when('/user/photos', router({
			templateUrl: '/template/user/photos.htm'
		}))
		.when('/user/messages', router({
			templateUrl: '/template/user/messages.htm'
		}))
		.when('/user/friends', router({
			templateUrl: '/template/user/friends.htm'
		}))
		.when('/user/wishs', router({
			templateUrl: '/template/user/wishs.htm'
		}))

		// p2p 点对点映射
		.when('/p2p/mapping/list', router({
			templateUrl: 'view/p2p/mapping/mapping.htm'
			, controllerUrl: 'view/p2p/mapping/mapping.js'
		}))



		/* 闪聊模式 */
		.when('/flashchat/index', router({
			templateUrl: '/template/flashchat/index.htm'
		  , controllerUrl: '/js/controllers/flashchat.js'
		}))

		/* 心愿墙 */
    	.when('/wishwall/build', router({
			templateUrl: '/template/wishwall/build.htm'
		  , controllerUrl: '/js/controllers/wishwall.js'
		}))

		// 在线视频
		.when('/app/video/index', router({
			minTitle: "登录",
			templateUrl: '/view/video/video.htm'
			, controllerUrl: '/view/video/video.js'
		}))



		.when('/wishwall/item/:objectId', router({
			templateUrl: '/template/wishwall/item.htm'
		  , controllerUrl: '/js/controllers/wishwall/item.js'
		}))

		.when('/wishwall/index', router({
			templateUrl: '/template/wishwall/index.htm'
		  , controllerUrl: '/js/controllers/wishwall/index.js'
		}))




		/** ****************************************
		 *
		 *    手机端(AmazeUI)
		 *
		 * ****************************************
		 * */
		.when('/mobi/secret', router({
			templateUrl: '/view/mobi/secret/app.htm'
			, controllerUrl: '/view/mobi/secret/app.js'
		}))
		/** 登录 */
		.when('/mobi/login', router({
			templateUrl: '/view/mobi/login/app.htm'
			, controllerUrl: '/view/mobi/login/app.js'
		}))
        /** 注册账号 */
        .when('/mobi/register', router({
            templateUrl: '/view/mobi/register/app.htm'
            , controllerUrl: '/view/mobi/register/app.js'
        }))
        /** 忘记密码 */
        .when('/mobi/forget', router({
            templateUrl: '/view/mobi/forget/app.htm'
            , controllerUrl: '/view/mobi/forget/app.js'
        }))

		/** 个人资料 */
		.when('/mobi/profile', router({
			templateUrl: '/view/mobi/profile/app.htm'
			, controllerUrl: '/view/mobi/profile/app.js'
		}))
		/** 附近 */
		.when('/mobi/near', router({
			templateUrl: '/view/mobi/near/app.htm'
			, controllerUrl: '/view/mobi/near/app.js'
		}))





		// 访问其他地址，自动跳转至主页
		.otherwise({redirectTo: '/index'});



	// 重写地址支持
    // $locationProvider5Mode(true).hashPrefix('#');



}]);




/**
 * 实现AMD规范异步加载数据
 */
function router(config){

	// 生成独立URL，保证不缓存页面数据
	var random = "?t=" + Math.random();
	config.templateUrl = config.templateUrl + random;
	return angularAMD.route(config);
}


//.run(['$rootScope', function($rootScope) {
//
//
//    $rootScope.$on('$routeChangeStart', function(evt, next, current) {
//        // 如果用户未登录
//    	var control = next.$$route.controller;// 控制器名称
//
//        // alert(JSON.stringify(next.$$route.controller));
//
////        console.log(control);
////        if(control  == 'RegisterController'){
////
////
////	        require([control],function(c){
////
////				console.log(c);
////			});
////        }
//
//
//
//    });
//
//
//
//}]);



});
