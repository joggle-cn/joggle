

define([
       'jquery'
 	 , 'angular'
	 , 'webchat'
	 , 'app'

], function ($, angular, WebChat, app) {//加载依赖js,


    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.format = function(fmt)
    { //author: meizz
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }


    /**
     * 显示日期
     */
    app.filter('showDate',[function(){
        return function(dateTime ){
            if(dateTime != undefined){
                return new Date(dateTime).format("yyyy-MM-dd hh:mm:ss");
            }else{
                return '';
            }
        }
    }]);

    /**
     * 显示协议 过滤器
     */
    app.filter('showProtocol',[function(){
        return function(protocol ){
            let protocolInt = parseInt(protocol);
            switch (protocolInt){
                case 1 : return "http";
                case 2 : return "tcp";
                case 3 : return "https";
                case 4 : return "https";
                default: return "-";
            }
            return "-";
        }
    }]);


    /**
     * 显示距离
     */
    app.filter('showDistance',[function(){
        return function(distance ){
            if(distance != undefined){
                return  parseInt(distance*1000)/1000;
            }else{
                return '0+';
            }
        }
    }]);





	/**
	 * Web PC 网页
	 * $routeParams
	 * */
	// 到航控制器
	app.controller('navController', ['$rootScope','$session', '$scope','$location',function ($rootScope, $session, $scope, $location) {

        $rootScope.config = {
            domain: 'joggle.cn' // 默认，如果服务还没有返回读取的这个值
        }

        // 初始化数据
        $rootScope.init = function () {


            // 加载系统初始化信息
            faceinner.get(api['init'], {_async: true}, function (res) {
                if (res.code == 'S00') {
                    $rootScope.$apply(function () {
                        $rootScope.config = res.data;
                    });
                }
            });


            faceinner.get(api['user.login.info'], {_async: true},function (res) {
                if (res.code == 'S00') {
                    $rootScope.user = res.data;
                    $rootScope.islogin = true;
                    $session.user = res.data;
                }
                $rootScope.$apply();
            });
        }

        $rootScope.init();


        /**
         * 退出登录
         */
        $scope.loginout = function(){
            faceinner.post(api['user.loginout'],function(res){
                if(res.code == 'S00'){
                    delete $rootScope.user;
                    localStorage.token = null;
                    $rootScope.$apply(function() {
                        $rootScope.islogin = false;
                        window.location.href = '#/login';
                    });
                }
            });
        }










    }])



	/**
	 * 手机端主页
	 *
	 * $routeParams
	 * */
	app.controller('indexController', ['$rootScope','$scope','$location','$utils',
        function ($rootScope, $scope, $location, $utils) {
 		var $menuCanvas = $('.am-offcanvas');

        /**
         *
         * 获取地理位置
         * (每间隔30秒重新获取一次)
         * */
        function initLocation(){
            $utils.getLocation(function(latitude,longitude){
                $rootScope.location = {
                    latitude  : latitude, // 纬度
                    longitude : longitude // 经度
                }

                setTimeout(function(){
                    initLocation();
                },30000);

            });
        }
        initLocation();






		/**
		 * 初始化函数
		 */
		$scope.init = function(){

			/**
			 * 菜单初始化
             */
			// 判断下是否是你需要的节点
			$('body').on('click', '.btn-menu-list', function() {
				$menuCanvas.offCanvas('open');
			});


			// 加载用户登录信息
			faceinner.get(api['user.login.info'], function(res){
				if(res.code == 'S00'){
					$scope.user = res.results;
					$scope.islogin = true;
					//$session.user = res.results;
				}
			});
			// 加载系统初始化信息
			faceinner.get(api['init'], function(res){
				if(res.code == 'S00'){
                    $rootScope.init = res.data;
				}
			});
		}


		/**
		 * goto login
		 */
		$scope.gotoLogin = function(){
			$menuCanvas.offCanvas('close');
			$location.path('/mobi/login.html');
		}


        /**
         * 退出登录
         */
        $scope.logOut = function(){
            $menuCanvas.offCanvas('close');
            faceinner.get(api['user.loginout'],function(res){
                if(res.status == 0){
                    $scope.$apply(function(){
                        delete $scope.user;
                        delete $scope.islogin;
                        $location.path('/mobi/login.html');
                    });
                }
            });
        }


		/**
		 * 个人资料
		 */
		$scope.profile = function(){
			$menuCanvas.offCanvas('close');
			$location.path('/mobi/profile.html');
		}

        /**
         * 附近的秘密
         */
        $scope.near = function(){
            $menuCanvas.offCanvas('close');
            $location.path('/mobi/near.html');
        }

        /**
         * 发布秘密
         */
        $scope.publish = function(){
            $menuCanvas.offCanvas('close');
            $location.path('/mobi/secret.html');
        }


	}])










	// 切换语言
	app.controller('languageController',['res','$scope','$location',
	           function (res, $scope, $location) {

		// 切换语言
		$scope.change = function(lang){
			res.set(lang);
		};

	}]);

});
