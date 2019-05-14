

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
            switch (protocol){
                case 1 : return "HTTP";
                case 2 : return "TCP";
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
	app.controller('navController', ['$rootScope', '$scope','$location',function ($rootScope, $scope, $location) {



        // 加载用户登录信息
        faceinner.get(api['user.login.info'], function(res){
            if(res.status == 0){
                $rootScope.$apply(function() {
                    $rootScope.user = res.data;
                    $rootScope.islogin = true;
                });
            }
        });



        /**
         * 退出登录
         */
        $scope.loginout = function(){
            faceinner.get(api['user.loginout'],function(res){
                if(res.status == 0){
                    $rootScope.$apply(function() {
                        delete $rootScope.user;
                        delete $rootScope.islogin;
                        $location.path('/login');
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
				if(res.status == 0){
					$scope.user = res.results;
					$scope.islogin = true;
					//$session.user = res.results;
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






	/**
	 *
	 * (PC端) 用户控制器模块
	 *
	 */
	.controller('userController', ['$scope', '$session' , '$location',function ($scope, $session, $location) {


		// 初始化数据
		$scope.init = function(){
			faceinner.get(api['user.login.info'],function(res){
				if(res.status == 0){
					$scope.user = res.data;
					$scope.islogin = true;
                    $session.user = res.data;
				}
			});
		}

        $scope.init();

        /**
		 * 退出登录
		 */
		$scope.loginout = function(){
			faceinner.get(api['user.loginout'],function(res){
				if(res.status == 0){
					window.location.href = '#/index';
				}
			});
		}





	}]);




	
	// 切换语言
	app.controller('languageController',['res','$scope','$location',
	           function (res, $scope, $location) {

		// 切换语言
		$scope.change = function(lang){ 
			res.set(lang);
		};
		
	}]);
	
});
