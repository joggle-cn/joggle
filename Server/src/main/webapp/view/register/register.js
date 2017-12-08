'use strict';

define(['app','jquery','x18n'], function (app, $, x18n) {// 加载依赖模块
	
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
				if($scope.user.pass && ($scope.user.pass == $scope.password2)){

					faceinner.post(api['user.register'], $scope.user , function(result){
						if(result.status){// 注册成功
                            alert("注册成功!");

                            // 登陆当前注册用户
                            faceinner.post(api['user.login'], $scope.user , function(result){
								if(result.status){
									// 登录成功，跳转到主页
									window.location.href ='/index.html';
								}
							});
						} else{// 注册失败
							$scope.$apply(function () {
								var len = result.data.length;
								for(var i=0; i<len; i++){
									var code = result.data[i];
									if( res.code.RegEmailError == code ){// 邮箱错误
										$scope.emailMsg = res.error(res.code.RegEmailError);
									} else if ( res.code.RegEmailExist == code ){// 邮箱被注册
										$scope.emailMsg = res.error(res.code.RegEmailExist);
									}
								}
							});
						}




					});
				}else{ 
					$scope.password2Msg = res.error(res.code.PasswordInputNotEquals);
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
				$scope.emailMsg = res.error(res.code.MustFillInput); 
				$scope.isOk = false;
			}
			if(user.pass == null || '' == user.pass){ 
				$scope.passwordMsg = res.error(res.code.MustFillInput); 
				$scope.isOk = false;
			}
			
			if(!$scope.user.agree){ 
				alert("注册用户必须同意服务条款"); 
				$scope.isOk = false;
			} 
		};
	}];
	
});
