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
			// 表单验证
			faceinner.post(api['user.login'], $scope.user , function(res){
				if(res.status == 0){
                    // 加载用户登录信息
                    faceinner.get(api['user.login.info'], function(res){
                        if(res.status == 0){
                            $scope.$apply(function() {
                                $rootScope.user = res.data;
                                $rootScope.islogin = true;
                                $location.path('/index').replace();
                            });
                        }
                    });
				}
				faceinner.handleFieldError($scope, res);
			});
		 }; 
	}];

});