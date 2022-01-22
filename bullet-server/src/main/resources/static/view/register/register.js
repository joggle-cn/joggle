'use strict';

/**
 * 注册账号模块
 *
 * @author marker
 */
define(['app','jquery','x18n', 'layer', 'css!./register.css'], function (app, $, x18n,layer) {// 加载依赖模块

	return ['$scope','$http','$location','res','userService',"$routeParams",
		function ($scope, $http, $location, res, userService, $routeParams) {
		$scope.agree = false; // 协议
		$scope.isOk = false; // 是否可以提交表单
		$scope.userLogin = {};
		$scope.user = {
		    pass: ""
		    ,sex: 0
		    ,age: 18
			,inviteCode: $routeParams.c
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
							// 弹出窗口提示去查看激活邮件
							layer.open({
								type: 1 //Page层类型
								,area: ['500px', '300px']
								,title: '欢迎您使用Bullet'
								,shade: 0.6 //遮罩透明度
								,maxmin: true //允许全屏最小化
								,anim: 1 //0-6的动画形式，-1不开启
								,content: '<div style="padding:50px;">' +
									'恭喜您，<br/>&nbsp; &nbsp; &nbsp; 您的账号'+ $scope.user.email +'已注册成功! 还差一步即可激活账号，' +
									'请登录您的邮箱查看激活邮件并确认激活。' +
									'</div>'
							});

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
			// 邮箱校验
			let myReg = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
			if(!myReg.test(user.email)){
				$scope.emailMsg = res.error(res.code.mustFillInput);
				$scope.isOk = false;
			}
			if(user.nickname == null || '' == user.nickname){
				$scope.nicknameMsg = res.error(res.code.mustFillInput);
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
