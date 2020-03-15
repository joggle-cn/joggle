/**
 *
 * 平台登录模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./app.css'], function (app) {// 加载依赖js,
	
	return ['$scope','$location','userService', '$AjaxService',
	     function ($scope, $location, userService, $AjaxService) {
   			$scope.user = {};

			 /**
			  *
			  * 发布秘密
			  *
			  */
			 $scope.login = function(){
				 // 表单验证
				 faceinner.post(api['user.login'], $scope.user , function(res){
					 if(result.status == 0){
						 window.location.href ='/mobi/secret.html';
					 }
					 faceinner.handleFieldError($scope, res);
				 });

			 }




	}];

});