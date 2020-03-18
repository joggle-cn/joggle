'use strict';

/**
 * 注册账号模块
 *
 * @author marker
 */
define(['app','jquery','x18n', 'layer'], function (app, $, x18n,layer) {// 加载依赖模块
	
	return ['$scope','$http','$location','res','userService', function ($scope, $http, $location, res, userService) {
		$scope.agree = false; // 协议
		$scope.isOk = false; // 是否可以提交表单
		$scope.user = {
		    pass: ""
		    ,sex: 0
		    ,age: 18
		};// 密码
		//$scope.user.name = "marker";
		
		// 获取用户信息
		var params = $location.search();
		if(params.from){// 如果from存在则调用,第三方平台注册
			userService.getUserInfo(params.from,function(json){
				if(json.status){
					$scope.$apply(function () {
						$scope.user.name = "" + json.data.name;
						$scope.user.sex = json.data.sex;
						$scope.user.icon = json.data.icon;
						$scope.user.openId = json.data.openId;
						
					});
				}
			});
		} 
		
		
		
		
		/* 注册 */
		$scope.reg = function(){
			
			validate($scope.user);// 验证表单
			
			if($scope.isOk){
				// 判断两次密码是否一致
				if($scope.user.password && ($scope.user.password == $scope.password2)){

					faceinner.post(api['user.register'], $scope.user , function(result){
						if(result.code == "S00"){// 注册成功
                            layer.msg(res.t('register.success'));


						} else {// 注册失败
							$scope.$apply(function () {
								let len = result.data.length;
								for(let i=0; i<len; i++){
									let fieldErrorInfo = result.data[i];
									$scope[fieldErrorInfo.field+"Msg"] = fieldErrorInfo.msg;
								}
							});
						}

					});
				} else {
					$scope.password2Msg = res.error(res.code.passwordInputNotEquals);
				}  
					
	//					$("#registerModal").modal({// 弹出注册确认界面
	//						backdrop: false
	//					});
				 
			
			}
			 
		}; 
		
		/* 确认注册 */ 
		
		
		$scope.cancel = function(){
			$scope.token = null;
			$scope.password2 = "";// 清空密码数据
			$scope.user.pass = "";
			$("#registerModal").modal('hide');
		};
		
		
		
		
		
		
		/* 
		 * 验证表单正确性
		 * */
		function validate(user){
			$scope.isOk = true; 
			if(user.email == null || '' == user.email){
				$scope.emailMsg = res.error(res.code.mustFillInput);
				$scope.isOk = false;
			}
			if(user.password == null || '' == user.password){
				$scope.passwordMsg = res.error(res.code.mustFillInput);
				$scope.isOk = false;
			}
			
			if(!$scope.user.agree){ 
				layer.msg(res.t('register.alertAgreeService'));
				$scope.isOk = false;
			} 
		};
	}];
	
});
