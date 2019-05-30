/**
 *
 * 注册模块
 *
 * @author marker
 * @date 2016-08-13
 */
define(['css!./app.css'], function ( ) {// 加载依赖js,

	
	return ['$scope','$location','userService', '$AjaxService',
        function ($scope, $location, userService, $AjaxService) {


            $scope.user = {};





             /**
              * 注册账号
              */
             $scope.register = function(){
                 // 表单验证
                 if(!$scope.user.agree){
                     alert("您需要接受注册协议哦!");
                     return;
                 }


                 faceinner.post(api['user.register'], $scope.user , function(res){
                     if(res.status == 0){
                         window.location.href ='/mobi/login.html';
                     }
                     faceinner.handleFieldError($scope, res);
                 });

             }




	}];

});