/**
 *
 * 注册模块
 *
 * @author marker
 * @date 2016-08-13
 */
define(['layer', 'css!./app.css'], function (layer) {// 加载依赖js,a


    return ['$scope', '$location', 'userService', '$AjaxService',
        function ($scope, $location, userService, $AjaxService) {
            $scope.user = {};
            if ($location.search().code) {
                // 修改密码代码
                $scope.user.code = $location.search().code;
                $scope.showChangePass = true;
            } else {
                $scope.showChangePass = false;
            }


            /**
             * 修改密码
             */
            $scope.changePassword = function () {

                faceinner.postJson(api['user.changepass'], $scope.user, function (res) {
                    $scope.$apply(function () {
                        if (res.status == 0) {
                            layer.msg("重置成功!");
                            $location.path('/login').replace();
                        } else if (res.status == 100014) { // code不存在
                            // 不存在直接跳转重置申请页面.
                            layer.msg("您的code已经失效了.");
                            $location.path('/forget').replace();
                        }
                        faceinner.handleFieldError($scope, res);
                    });
                });

            }


            /**
             * 提交重置密码申请
             */
            $scope.submitRestPass = function () {
                let siteUrl = window.location.protocol + "//" + window.location.host;
                if (window.location.port == 80 || window.location.port == 443) {
                    siteUrl += ":" + window.location.port
                }
                siteUrl += window.location.pathname

                console.log(siteUrl);

                let data = {
                    email: $scope.email,
                    siteUrl: siteUrl
                };

                faceinner.postJson(api['user.forget'], data, function (res) {
                    if (res.status == 0) {
                        layer.msg("申请成功,请查看您的邮箱");
                        window.location.href = '#/login';
                    } else {
                        layer.msg(res.msg);
                    }
                    faceinner.handleFieldError($scope, res);
                });
            }

        }];

});