/**
 *
 * 系统配置   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'bootstrap-switch'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$location","$interval", function ($scope, $location,$interval) {
        $scope.active = 'system';



        $("[name='my-checkbox']").bootstrapSwitch();

        /**
         * 弹框退出
         */
		$scope.exit = function(){
            $("#editDevice").modal('hide');
        }
		 
		 
 	}];
	
	
	return callback;
});