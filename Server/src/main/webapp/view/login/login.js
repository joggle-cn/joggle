/**
 *
 * 平台登录模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./login.css'], function (app) {// 加载依赖js,
	
	return ['$scope','$location','userService', '$AjaxService',
	        function ($scope, $location, userService, $AjaxService) { 
		// 登录操作
		$scope.login = function(){
			// 表单验证


			faceinner.post(api['user.login'], $scope.user , function(res){
				if(res.status == 0){
					window.location.href ='/index.html';
				}
				faceinner.handleFieldError($scope, res);
			});
		 }; 
	}];

});