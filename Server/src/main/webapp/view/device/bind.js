/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {



        /**
         * 校验设备是否存在
         */
		$scope.validate = function(){
		    $("#bindButton").attr("disabled",true);
            $scope.info = "正在为您链接校验服务器...";
            $("#progress-bar").css({visibility:"visible"});
            faceinner.get(api['user.device.validate'], {deviceId: $scope.deviceId}, function(res) {

                if (res.status == 0) {
                    $scope.$apply(function(){
                        $("#progress-bar .progress-bar").removeClass("progress-bar-danger");
                        $("#progress-bar .progress-bar").addClass("progress-bar-success");
                        $("#progress-bar .progress-bar").css({width:"100%"});
                        $scope.info = "绑定完成！ 等待3秒跳转到 > 我的设备。";
                    })


                    // 准备跳转
                    $scope.time = 3;
                    var timer = $interval(function(){
                        $scope.time = $scope.time -1;

                        $scope.info = "绑定完成！ 等待" + $scope.time + "秒跳转到 > 我的设备。";
                        if($scope.time < 0){
                            $("#bindButton").attr("disabled", false);
                            $location.path("/user/device");
                            $interval.cancel(timer);
                        }
                    }, 1000);

                } else {
                    $scope.$apply(function(){
                        $("#progress-bar .progress-bar").addClass("progress-bar-danger");
                        $scope.info = res.msg;
                        $("#bindButton").attr("disabled", false);

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
		 
		 
 	}];
	
	
	return callback;
});