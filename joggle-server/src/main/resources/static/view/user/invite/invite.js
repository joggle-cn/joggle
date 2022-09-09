/**
 *
 * 激活用户 模块
 *
 * @author marker
 * @date 2020-03-18
 */
define(['app', 'layer','css!./invite.css'], function (app, layer) {
	let callback = ["$scope","$routeParams","$rootScope", function ($scope, $routeParams, $rootScope) {

	    // URL地址
        let url = $rootScope.config.websiteUrl + "/#/register?c="+$rootScope.user.activateCode;

        $scope.inviteUrl = url;

        // 设置参数方式
        new QRCode('imgQRcode', {
            text: url,
            width: 256,
            height: 256,
            colorDark : '#000000',
            colorLight : '#ffffff',
            correctLevel : QRCode.CorrectLevel.H
        });



        /**
         * 复制邀请链接
         */
        $scope.copyInviteUrl = function(){
            let transfer = document.getElementById('inviteUrlInput');
            transfer.focus();
            transfer.select();
            if (document.execCommand('copy')) {
                document.execCommand('copy');
            }
            layer.msg("复制成功")
        }

    }];
	return callback;
});
