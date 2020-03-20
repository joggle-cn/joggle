/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'layer','bootstrap-switch', 'css!./device.css'], function (app, $, layer) {//加载依赖js,




	let callback = ["$scope","$routeParams","$location", function ($scope, $routeParams,$location) {
        $scope.active = 'device';
        // 设备ID
        let deviceId = $routeParams.deviceId;
        $scope.domainNoBindList = [];
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
        $scope.addMapping = function(item, type){
            item.type = type;
            $scope.entity = item;

            $("#addMapping").modal({
                backdrop: false
            });
            $('#addMapping').on('shown.bs.modal', function () {


                $("#stautsCheckbox").bootstrapSwitch({
                    state: $scope.entity.status,
                    onSwitchChange:function (event, state) {
                        $scope.entity.status = state;
                    }
                });
                $("#stautsCheckbox").bootstrapSwitch('state', $scope.entity.status, true);
            })
		}

        /**
         * 删除设备与域名的映射关系
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
		        id : $scope.entity.id,
		        domain : $scope.entity.domain,
                protocol : $scope.entity.protocol,
		        port : $scope.entity.port,
		        remotePort : $scope.entity.remotePort,
                host : $scope.entity.host,
                hostname : $scope.entity.hostname,
                auth : $scope.entity.auth,
                deviceId: deviceId,
		        description : $scope.entity.description,
                status : $scope.entity.status?1:0,
            }




            faceinner.post(api['user.device.mapping'],params , function(res) {
                if (res.code == 'S00') {
                    $("#addMapping").modal('hide');
                    flushData();
                } else{
                    layer.msg(res.msg);
                    $scope.entity.status = false;

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
         * 绑定域名弹框
         */
        $scope.bindDomainDialog = function(item){
            faceinner.get(api['user.domain.nobind'], {}, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.domainNoBindList = res.data;
                    });
                }
            });

            $("#bindDomainDialog").modal({
                backdrop: false
            });
        }
        /**
         * 关闭绑定域名弹框
         */
        $scope.closeBindDomain = function(){
            $("#bindDomainDialog").modal('hide');
        }


        /**
         * 设备绑定域名
         */
        $scope.deviceBindDomain = function(){
            if(!$scope.selectDomainId){
                return;
            }
            let params = {
                deviceId: $scope.deviceInfo.id,
                domainId: $scope.selectDomainId,
            }

            faceinner.post(api['user.domain.bind.device'], params , function(res) {
                if (res.code == 'S00') {
                    $("#bindDomainDialog").modal('hide');
                    flushData(); // 刷新数据

                    delete $scope.selectDomainId;
                }
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