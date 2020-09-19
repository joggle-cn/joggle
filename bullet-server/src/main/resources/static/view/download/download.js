/**
 *
 * Home 主页 模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app', 'layer', 'css!./download.css'], function (app, layer) {//加载依赖js,
	var callback = ["$scope", function ($scope) {


		/**
		 * 下载应用（客户端和服务端)
		 */
		$scope.downloadServer = function(){


			//在这里面输入任何合法的js语句
			layer.open({
				type: 1 //Page层类型
				,area: ['500px', '300px']
				,title: '欢迎下载 Bullet Server '
				,shade: 0.6 //遮罩透明度
				,maxmin: true //允许全屏最小化
				,anim: 1 //0-6的动画形式，-1不开启
				,content: '<div style="padding:50px;">' +
					'下载地址：<a href="https://pan.baidu.com/s/1Xy5_R_ezPFft9vsZrLNBSA" target="_blank">' +
					' https://pan.baidu.com/s/1Xy5_R_ezPFft9vsZrLNBSA</a><br/> 密码: wq2e' +
					'' +
					'</div>'
			});

		}

		 
		 
 	}];
	
	
	app.controller('IndexController', callback ); 
	return callback;
});