/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery','layer'], function (app, $, layer) {//加载依赖js,

	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {
        $scope.info = "获取验证码";
        $scope.data = {
            phone: "",
            type : 1,
        }

        // 获取短信验证码
		$scope.getSmsCode = function( ){
            if($scope.data.phone == ""){
                layer.msg("请填写手机号")
                return;
            }
            let params = {
                "phone": $scope.data.phone,
                "type": "AUTH"
            }

            $("#btnGetSms").attr("disabled",true);
            faceinner.postJson(api['user.auth.sms'], params, function(res) {
                if (res.code == 'S00') {
                    // 准备跳转
                    $scope.time = 90;
                    $scope.info = "等待" + $scope.time + "秒可重发";
                    $scope.$apply(function(){});
                    let timer = $interval(function(){
                        let num = $scope.time - 1;
                        if (num >= 0) {
                            $scope.time = num;
                        }
                        $scope.info = "等待" + $scope.time + "秒可重发";
                        if($scope.time <= 0){
                            $("#btnGetSms").attr("disabled", false);
                            $scope.info = "获取验证码";
                            $interval.cancel(timer);
                        }
                    }, 1000);

                } else {
                    $scope.$apply(function(){
                        layer.msg(res.msg);
                        $("#btnGetSms").attr("disabled", false);
                    });
                }
            });

        }

        /**
         * 弹框退出
         */
		$scope.exit = function(){
            $("#editDevice").modal('hide');
        }



        /**
         * 设备发现
         */
        $scope.submitData = function(){
            // 校验
            if($scope.data.realName == ""){
                layer.msg("姓名不能为空"); return
            }
            if($scope.data.idcard == ""){
                 layer.msg("身份证号码不能为空"); return;
            }
            if($scope.data.phone == ""){
                 layer.msg("手机号不能为空");  return;
            }
            if($scope.data.code == ""){
                layer.msg("验证码不能为空"); return;
            }

            $("#submitUcData").attr("disabled", true);
            faceinner.postJson(api['user.auth.submit'], $scope.data, function(res) {
                if (res.code == 'S00') {
                    layer.msg("提交成功，请等待系统审核。", {icon:1});
                }else{
                    layer.msg(res.msg, {icon:9});
                }
                setTimeout(function (){
                    $("#submitUcData").attr("disabled", false);
                },1000)
            })
        }

 	}];


	return callback;
});
