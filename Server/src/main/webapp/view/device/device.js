/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery'], function (app, $) {//加载依赖js,


	var callback = ["$scope", function ($scope) {




	    function render(){
            faceinner.get(api['user.device'], function(res){
                if (res.status == 0) {
                    $scope.$apply(function() {
                        $scope.list = res.data;
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
                if (res.status == 0) {
                    $("#delDevice").modal('hide');
                    render();
                }
            });
        };

        /**
         * 保存
         */
		$scope.save = function(){
            faceinner.post(api['user.device'], $scope.item , function(res) {
                if (res.status == 0) {
                    $("#editDevice").modal('hide');
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
	
	
	// app.controller('IndexController', callback );
	return callback;
});