/**
 *
 * 激活用户 模块
 *
 * @author marker
 * @date 2020-03-18
 */
define(['app', 'layer','css!./activate.css'], function (app, layer) {
	let callback = ["$scope","$routeParams", function ($scope, $routeParams) {

	    // 激活码
        let code = $routeParams.code;
        let inviteCode = $routeParams.ic; // 邀请码

        /**
         * 激活用户
         */
        $scope.activateUser = function(){
            let params = {
                code: code,
                inviteCode: inviteCode
            }

            faceinner.post('/api/open/user/activate', params,function(result){
                if(result.code == 'S00'){
                    layer.msg('激活成功，请登录您的账号！');
                    setTimeout(function(){
                        window.location.href='#/login';
                    },3000)
                }else{
                    layer.msg('激活失败，请联系管理员！');
                }
            });

        }


 	}];
	return callback;
});
