/**
 *
 * 网络唤醒WOL 模块
 *
 * @author marker
 * @date 2021-02-12
 */
define(['app','jquery'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {


        $scope.active = 'bind';

        /**
         * 校验设备是否存在
         */
		$scope.wolDevice = function(){
		    $("#bindWolButton").attr("disabled",true);
            $scope.info = "正在广播唤醒设备...";
            $("#progress-bar").css({visibility:"visible"});
            faceinner.post(api['user.device.wol'], {mac: $scope.macAddress}, function(res) {

                if (res.code == 'S00') {
                    $scope.$apply(function(){
                        $("#progress-bar .progress-bar").removeClass("progress-bar-danger");
                        $("#progress-bar .progress-bar").addClass("progress-bar-success");
                        $("#progress-bar .progress-bar").css({width:"100%"});
                        $scope.info = "唤醒完成！";
                    })
                } else {
                    $scope.$apply(function(){
                        $("#progress-bar .progress-bar").addClass("progress-bar-danger");
                        $scope.info = res.msg;
                        $("#bindButton").attr("disabled", false);

                    });
                }

                $("#bindWolButton").attr("disabled",false);
            });

        }

		 
 	}];
	
	
	return callback;
});