/**
 *
 * 我的资料
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','layer','css!./profile.css'], function (app,layer) {// 加载依赖js,

	return ['$rootScope','$scope','$location','userService', '$AjaxService',
	        function ($rootScope, $scope, $location, userService, $AjaxService, $session) {
		$scope.entity= {

		}
		// 加载用户登录信息
		faceinner.get(api['user.login.info'], function(res){
			if(res.code == 'S00'){
				$scope.$apply(function() {
					$rootScope.user = res.data;
				});
			}
		});


		$scope.save= function() {

			let params = {
				newPassword : $scope.entity.newPassword,
				oldPassword : $scope.entity.oldPassword,
			}

			faceinner.post(api['user.login.password'],params , function(res) {
				if (res.code == 'S00') {
					$("#addMapping").modal('hide');
					layer.msg('修改成功');
					$("#modifyPasswordDialog").modal('hide');
				} else{
					layer.msg(res.msg);
				}
			});

		}

		$scope.modifyPasswordDialog= function() {
			$("#modifyPasswordDialog").modal({
				backdrop: false
			});

		}
		$scope.closeModifyPasswordDialog= function() {
			$("#modifyPasswordDialog").modal('hide');
			$scope.entity.newPassword = "";
			$scope.entity.oldPassword = "";

		}
	}];

});
