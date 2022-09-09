/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {


        $scope.active = 'bind';
        $scope.discovery = [];

        /**
         * 校验设备是否存在
         */
		$scope.validate = function(){
		    $("#bindButton").attr("disabled",true);
            $scope.info = "正在为您链接校验服务器...";
            $("#progress-bar").css({visibility:"visible"});
            faceinner.get(api['user.device.validate'], {deviceId: $scope.deviceId}, function(res) {

                if (res.code == 'S00') {
                    $scope.$apply(function(){
                        $("#progress-bar .progress-bar").removeClass("progress-bar-danger");
                        $("#progress-bar .progress-bar").addClass("progress-bar-success");
                        $("#progress-bar .progress-bar").css({width:"100%"});
                        $scope.info = "绑定完成！ 等待1秒跳转到 > 我的设备。";
                    })


                    // 准备跳转
                    $scope.time = 1;
                    let timer = $interval(function(){
                        let num = $scope.time - 1;
                        if (num >= 0) {
                            $scope.time = num;
                        }
                        $scope.info = "绑定完成！ 等待" + $scope.time + "秒跳转到 > 我的设备。";
                        if($scope.time <= 0){
                            $("#bindButton").attr("disabled", false);
                            $location.path("/user/device");
                            $interval.cancel(timer);
                        }
                    }, 500);

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





        /**
         * 设备发现
         */
        $scope.selectDeviceNo = function(deviceNo){
            $scope.deviceId = deviceNo;
        }
        $scope.deviceDiscovery = function(){
            faceinner.ajax({
                url : api['user.device.discovery'],
                headers: {
                    'connection': 'close'
                },
                beforeSend: function ( R) {
                    R.setRequestHeader('Connection' , 'close' ) ;
                },
                success: function(res) {
                    if (res.code == 'S00') {
                        $scope.$apply(function(){
                            $scope.discovery = res.data;
                        })
                    }
                }
            });
        }
        $scope.deviceDiscovery();

 	}];


	return callback;
});
