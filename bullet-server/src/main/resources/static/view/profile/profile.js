/**
 *
 * 我的资料
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./login.css'], function (app) {// 加载依赖js,
	
	return ['$rootScope','$scope','$location','userService', '$AjaxService',
	        function ($rootScope, $scope, $location, userService, $AjaxService, $session) {

		// 加载用户登录信息
		faceinner.get(api['user.login.info'], function(res){
			if(res.code == 'S00'){
				$scope.$apply(function() {
					$rootScope.user = res.data;
				});
			}
		});
	}];

});