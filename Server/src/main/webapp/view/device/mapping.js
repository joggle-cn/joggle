/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery'], function (app, $) {//加载依赖js,


	var callback = ["$scope", function ($scope) {


        $scope.list = [
            {id:1,name:"家里1", deviceId:"2321323"},
            {id:2,name:"家里2", deviceId:"2321323"},
            {id:3,name:"家里3", deviceId:"2321323"},
            {id:4,name:"家里4", deviceId:"2321323"}
        ]


        /**
         * 编辑设备
         */
        $scope.editDevice = function(item){
            $scope.item = item;

            $("#editDevice").modal({
                backdrop: false
            });
		}

        /**
         * 保存
         */
		$scope.save = function(){
		    console.log("baocu")

        }

        /**
         * 弹框退出
         */
		$scope.exit = function(){
            $("#editDevice").modal('hide');
        }
		 
		 
 	}];
	
	
	// app.controller('IndexController', callback );
	return callback;
});