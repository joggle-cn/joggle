/**
 *
 * Ngrokd服务   模块
 *
 * @author marker
 * @date 2019-11-24
 */
define(['app','jquery', 'bootstrap-switch'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {
        $scope.active = 'system/ngrokd';
        $scope.install = false;


        /**
         * 弹框退出
         */
		$scope.installNgrokd = function(){
		    alert('install')
        }

        // 加载用户登录信息
        faceinner.get(api["system.ngrokd.check"], function(res) {
            console.log(res);
            if (res.status == 100500) { // 未安装
                $scope.install = false;
            } else {
                $scope.install = true;
            }
        });

		 
		 
 	}];
	
	
	return callback;
});