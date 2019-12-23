'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.




define([
        'jquery'
   	  ,	'angular' 
   	  , 'x18n'
     ], function ($, angular, x18n) {//加载依赖js,
	
	

return angular.module('app.services', [])
/** 版本 */
.value('version', '0.1')
/** API后缀 */
.value('sufix', '.face')

  
    
/**
 * Ajax 业务对象
 * */  
.factory("$AjaxService",['$http','sufix', function ($http, sufix) {
	return {
		API:{
			login: "/api/login"
			
			
		},
		handleFieldError: function($scope, data){
			if(data.status == 1){// 失败	
				$scope.$apply(function(){
					var arr = data.results;
					if(arr instanceof Array){
						for(var i=0; i<arr.length;i++){
							var fieldMsg = arr[i];
							$scope[fieldMsg.field+'Msg'] = fieldMsg.msg;
						};
					};
				});
			};
		},
		
		
		/* POST 请求  */
		post: function(url, params, func){ 
			$.ajax({
				url: url + sufix,
				type: 'POST',
				data: params ,
				dataType: "json",
				success: func,
				error: function(){
					alert('network error!');
				},
				handleFieldError: this.handleFieldError
			});
		},
		
		/* GET 请求 */
		get: function(url, params, func){ 
			$.ajax({
				url: url + sufix,
				type: 'GET',
				data: params ,
				dataType: 'json',
				success: func,
				error: function(){
					alert('network error!');
				},
				handleFieldError: this.handleFieldError
			});
		}
	};
	
	
}])
  
  
/**
 * 用户业务对象
 * */  
.factory("userService",['$http','sufix', function ($http, sufix) {
	
	// 当前登录的用户
	 
	
	return {
		
		/* 注册用户 */
		register: function(user, func){
			$.ajax({
				url: api['user.register'],
				type: 'POST',
				data: user ,
				dataType: "json",
				success:func
			});
			
		},
		getUserInfo: function(from, func){ 
			$.ajax({
				url: '/api/user/internet' + sufix,
				type: 'POST',
				data: "from=" + from ,
				dataType: "json",
				success: func
			});
		},
		login: function(user , func){
			$.ajax({
				url: '/api/login' + sufix,
				type: 'POST',
				data: user ,
				dataType: "json",
				success: func 
			});
		},
		loginout: function(func){
			$.ajax({
				url: '/api/loginout' + sufix,
				type: 'POST',
				dataType: "json",
				success: func
			});
		}
		
		
		
		
		
		
		
	};
	
}])




/**
 * 工具业务对象
 * */
.factory("$utils",['$http', function ($http ) {

    // 工具
    var utils = {
        _locationData: undefined,
		/**
         * 获取地理位置
         * */
		getLocation: function(callback){
            if(navigator.geolocation){// gps定位
				navigator.geolocation.getCurrentPosition(
					function(p){
                        utils._locationData = {};
                        utils._locationData.latitude = p.coords.latitude;
                        utils._locationData.longitude = p.coords.longitude;

						callback(p.coords.latitude, p.coords.longitude);
					},
					function(e){
						var msg = e.code + "\n" + e.message;
						console.log(msg)
					},
                    {
                        // 指示浏览器获取高精度的位置，默认为false
                        enableHighAcuracy: true,
                        // 指定获取地理位置的超时时间，默认不限时，单位为毫秒
                        timeout: 20000,
                        // 最长有效期，在重复获取地理位置时，此参数指定多久再次获取位置。
                        maximumAge: 300000
                    }
				);
            }else {// 百度(基于IP定位)

                var geolocation = new BMap.Geolocation();
                geolocation.getCurrentPosition(function(r){
                    if(this.getStatus() == BMAP_STATUS_SUCCESS){
                        callback(r.point.lat, r.point.lng);
                    }
                    else {
                        alert('failed' + this.getStatus());
                    }
                },{enableHighAccuracy: true})
            }
		},

	};
    return utils;

}])



/**
 * Session 会话
 * */
.factory("$session",['$http', function ($http) {

		// 获取cookies


		return {



		};


}])


/**
 * 业务
 * */  
.factory("fragmentService",['$http','sufix', function ($http, sufix) {

	return {
		load:function(el, url){
			// 加载前
			
			$(el).load(url, function(){ 
				// 加载后
				
			});
		}		
		
	};
	
	
}])


/**
 * 地址服务
 */
.factory("addressService",['$http','sufix', function ($http, sufix) {
	
	return {
		
		/* 注册用户 */
		fetchContry: function(func){
			$.ajax({
				url:'/api/country.face',
				type: 'get',
				dataType:"json",
				success: func
			});
			
		},
		fetchProvince: function(cid, func){
			$.ajax({
				url:'/api/province.face',
				type: 'get',
				data: "cid="+cid,
				dataType:"json",
				success: func
			});
		},
		fetchCity: function(pid, func){
			$.ajax({
				url:'/api/city.face',
				type: 'get',
				data: "pid="+pid,
				dataType:"json",
				success: func
			});
		}
	};
	
	
}])
	
/**
 * 
 * 国际化资源加载
 * (采用js-coder的x18n库实现项目国际化)
 * 
 */
.factory('res',['$rootScope','$http','$log', function($rootScope, $http, $log) {
	// 获取系统使用语言
	$rootScope.res = x18n; 


	
	var data =  {
		current: "zh",
		data: {},
		config: {
			language: 'zh',
			path : 'resource/i18n/'
		},
		init: function(){
			var lang = this.config.language;
			this.set(lang);
		},
		/**
		 * 
		 * 设置当前语言
		 * 
		 * */
		set: function(lang){
			var defaultLang =  this.config.language;// 默认语言
			var data = this.data;
			if(!this.data[lang]){
				$http.get(this.config.path + lang + '.json?'+Math.random())
				.success(function(json){// success
					x18n.register(lang, json);
					data[lang] = {};
					x18n.set(lang);
					$log.info('http load language=' + lang);
				}).error(function(){// error
					$log.error('set language ' + lang + ' faild!');
					x18n.set(defaultLang);// 设置为默认语言
				}); 
			}else{
				x18n.set(lang);
			}
		},
		t: function(a){
			return x18n.t(a);
		}
		,msg : function(a){
			return x18n.t(a);
		},error: function(key){
			return x18n.t('error.' + key);
		}
	};
	
	// 初始化错误码绑定
	$http.get('resource/code/code.json')
		.success(function(json){// success 
			data.code = json; 
		}
	); 
	return data;
}])


 

//execute once!

.run(['$rootScope','$log','res', function($rootScope, $log, res) {
	// 执行国际化资源加载
	$log.info('init resouces...'); 
	
	
	// 初始化语言
	res.init();
	
	
	

}]);

});
