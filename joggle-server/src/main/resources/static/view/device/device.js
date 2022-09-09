/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'css!./device.css'], function (app, $) {//加载依赖js,


	var callback = ["$scope", function ($scope) {

        $scope.active = 'device';
        $scope.list =[];



	    function render(){
            faceinner.get(api['user.device'], function(res){
                if (res.code == "S00") {
                    $scope.$apply(function() {
                        $scope.list = res.data;
                        $.each($scope.list, function(i,item){
                            item.onlineTimeStr = (new Date(item.onlineTime))
                                .format("yyyy-MM-dd hh:mm:ss");
                        });
                    });
                }
            });
        }
        render();

        /**
         * 编辑设备
         */
        $scope.editDevice = function(item){

            item.createTimeStr = (new Date(item.createTime))
                .format("yyyy-MM-dd hh:mm:ss");
            $scope.item = item;
            $("#editDevice").modal({
                backdrop: false
            });
		}

		/**
         * 删除设备
         */
        $scope.delDevice = function(item){
            $scope.item = item;
            $("#delDevice").modal({
                backdrop: false
            });
		}


        /**
         * 确认删除设备
         */
		$scope.confirmDeleteDevice = function(){
            faceinner.delete(api['user.device'], $scope.item , function(res) {
                if (res.code == "S00") {
                    $("#delDevice").modal('hide');
                    render();
                    return
                }
                layer.msg(res.msg)
            });
        };



		 
		 
 	}];
	
	
	// app.controller('IndexController', callback );
	return callback;
});