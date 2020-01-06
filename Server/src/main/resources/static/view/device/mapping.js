/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'bootstrap-switch', 'css!./device.css'], function (app, $) {//加载依赖js,




	let callback = ["$scope","$routeParams","$location", function ($scope, $routeParams,$location) {
        $scope.active = 'device';
        // 设备ID
        let deviceId = $routeParams.deviceId;

        $scope.features = {
            lineName: '-',
        }



        $("[name='my-checkbox']").bootstrapSwitch();



        function flushData(){
            faceinner.get(api['user.device.info'], {deviceId:deviceId}, function(res){
                if (res.status == 0) {
                    $scope.$apply(function() {
                        $scope.deviceInfo = res.data.deviceInfo;
                        $scope.features = res.data.features;
                        $scope.portList = res.data.portList;
                        $scope.domainList = res.data.domainList;
                    });
                }
            });
        }
        flushData();


        /**
         * 展示域名
         * @param item
         * @returns {*|string}
         */
        $scope.showDomain = function(item){
            if(item.domain){
                return item.domain;
            }
            if(item.hostname){
               return item.hostname;
            }
        }

        /**
         * 展示映射状态
         * @param item
         * @returns {*|string}
         */
        $scope.showStatus = function(item){
            if(item.status == 1){
                return '启用';
            }else{
                return '-';
            }
        }

        /**
         * 编辑设备
         */
        $scope.addMapping = function(item){
            $scope.item = item;
            $("#addMapping").modal({
                backdrop: false
            });
            $('#addMapping').on('shown.bs.modal', function () {


                $("#my-checkbox1").bootstrapSwitch({
                    state: item.status,
                    onSwitchChange:function (event, state) {
                        $scope.item.status = state;
                    }
                });
                $("#my-checkbox1").bootstrapSwitch('state', item.status, true);
            })
		}

        /**
         * 删除
         * @param id
         */
		$scope.delMapping = function(id, $index){

            faceinner.delete(api['user.device.mapping'], {id:id} , function(res) {
                if (res.status == 0) {
                    flushData();
                }
            });

        }

        /**
         * 保存
         */
		$scope.save = function(){
		    var params = {
		        id : $scope.item.id,
		        domain : $scope.item.domain,
                protocol : $scope.item.protocol,
		        port : $scope.item.port,
		        remotePort : $scope.item.remotePort,
                host : $scope.item.host,
                hostname : $scope.item.hostname,
                auth : $scope.item.auth,
                deviceId: deviceId,
		        description : $scope.item.description,
                status : $scope.item.status?1:0,
            }




            faceinner.post(api['user.device.mapping'],params , function(res) {
                if (res.status == 0) {
                    $("#addMapping").modal('hide');
                    flushData();
                } else if(res.status ==  200100){// 域名被绑定
                    alert(res.msg);
                } else{
                    alert(res.msg);
                }
            });

        }

        /**
         * 弹框退出
         */
		$scope.exit = function(){
            $("#addMapping").modal('hide');
        }




        /**
         * 编辑设备
         */
        $scope.editDevice = function(item){

            item.createTimeStr = (new Date(item.createTime))
                .format("yyyy-MM-dd hh:mm:ss");

            $("#editDevice").modal({
                backdrop: false
            });
        }

        /**
         * 弹框退出
         */
        $scope.exitDevice = function(){
            $("#editDevice").modal('hide');
        }
        /**
         * 保存Device
         */
        $scope.saveDevice = function(){
            faceinner.post(api['user.device'], $scope.deviceInfo , function(res) {
                if (res.status == 0) {
                    $("#editDevice").modal('hide');
                }
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
            faceinner.delete(api['user.device'], $scope.deviceInfo , function(res) {
                if (res.status == 0) {
                    $("#delDevice").modal('hide');
                   window.history.back();
                }
            });
        };
		 
		 
 	}];
	
	
	// app.controller('IndexController', callback );
	return callback;
});