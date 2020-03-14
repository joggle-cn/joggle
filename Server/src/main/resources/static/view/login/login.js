/**
 *
 * 平台登录模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./login.css'], function (app) {// 加载依赖js,
	
	return ['$rootScope','$scope','$location','userService', '$AjaxService',
	        function ($rootScope, $scope, $location, userService, $AjaxService, $session) {
		// 登录操作
		$scope.login = function(){
			let params = {
				username: $scope.user.name,
				password: $scope.user.pass,
				grant_type: 'password',
			}


			// 表单验证
			faceinner.post(api['user.token'], params , function(res){
				console.log(res);

				if(res.access_token){
					localStorage.token = res.access_token;
					localStorage.tokenExpires = res.expires_in; // 有效期 单位：秒
					localStorage.tokenTime = new Date().getTime(); // 当前时间
					// 加载用户登录信息
					faceinner.get(api['user.login.info'], function(res){
						if(res.code == 'S00'){
							$scope.$apply(function() {
								$rootScope.user = res.data;
								$rootScope.islogin = true;
								$location.path('/index').replace();
							});
						}
					});
				}else{
					alert(res.msg);
				}
				// faceinner.handleFieldError($scope, res);
			});
		 }; 
	}];

});