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
            if($location.search().code){
                // 修改密码代码
                $scope.user.code = $location.search().code;
                $scope.showChangePass = true;
            }else{
                $scope.showChangePass = false;
            }



            /**
             * 修改密码
             */
            $scope.changePassword = function(){

                faceinner.post(api['user.changepass'], $scope.user,  function(res){
                    if(res.status == 0){
                        alert("重置成功!")
                        window.location.href ='/mobi/login.html';
                    }else if(res.status == 100014){// code不存在
                        // 不存在直接跳转重置申请页面.
                        alert("您的code已经失效了.")
                        window.location.href ='/mobi/forget.html';
                    }
                    faceinner.handleFieldError($scope, res);
                });

            }



            /**
             * 提交重置密码申请
             */
            $scope.submitRestPass = function(){
                var data = {email: $scope.email};

                faceinner.post(api['user.forget'], data,  function(res){
                    if(res.status == 0){
                        alert("申请成功,请查看您的邮箱");
                        window.location.href ='/mobi/login.html';
                    }
                    faceinner.handleFieldError($scope, res);
                });
            }

	}];

});