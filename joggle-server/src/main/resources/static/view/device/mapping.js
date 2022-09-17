/**
 *
 * 设备映射   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'layer','bootstrap-switch', 'css!./mapping.css'], function (app, $, layer) {//加载依赖js,

	let callback = ["$scope","$routeParams","$location","$rootScope", function ($scope, $routeParams,$location,$rootScope) {
        $scope.active = 'device';
        // 设备ID
        let deviceId = $routeParams.deviceId;
        $scope.domainNoBindList = [];
        $scope.features = {
            lineName: '-',
        }
        $scope.deviceInfo = {
            lineName:"",
            status: null
        }
        $scope.deviceUpdate = {
            id: null,
            name: null
        }
        $scope.deviceDoor = {
            enable : false
        }

        $scope.deviceProxy = {
            status : false
        }



        $("[name='my-checkbox']").bootstrapSwitch();



        function flushData(){
            faceinner.get(api['user.device.info'], {deviceId:deviceId}, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.deviceInfo = res.data.deviceInfo;
                        $scope.features = res.data.features;
                        $scope.portList = res.data.portList;
                        $scope.domainList = res.data.domainList;
                        $scope.deviceUpdate = {
                            id: $scope.deviceInfo.id,
                            name: $scope.deviceInfo.name,
                        }
                    });
                }
            });
        }
        flushData();


        // /**
        //  * 展示域名
        //  * @param item
        //  * @returns {*|string}
        //  */
        // $scope.showDomain = function (item) {
        //     if (item.domain) {// 子域名
        //         return item.domain + '.' + $rootScope.config.domain;
        //     }
        //     if (item.hostname) {// 自定义域名 CNAME指向
        //         return item.hostname;
        //     }
        // }

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
                }
            });

        }

        /**
         * 弹框退出
         */
		$scope.exit = function(){
            $("#addMapping").modal('hide');
        }
		$scope.closeDeviceDoorDialog = function(){
            $("#deviceDoorDialog").modal('hide');
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
        $scope.bindDomainDialog = function(item, type){
            let serverTunnelId = item.serverTunnelId;
            let params = {
                type: type,
                serverTunnelId:serverTunnelId
            }
            faceinner.get(api['user.domain.nobind'], params, function(res){
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
         * 内网代理弹框
         */
        $scope.openDeviceProxyDialog = function(item){
            $scope.deviceProxy.deviceId = item.id;
            $scope.deviceProxy.deviceProxyPort = 3000;

            let serverTunnelId = item.serverTunnelId;
            let params = {
                type:1,
                serverTunnelId:serverTunnelId
            }
            faceinner.get(api['user.domain.nobind'], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.domainNoBindList = res.data;
                    });
                }
            });

            $("#deviceProxyDialog").modal({
                backdrop: false
            });

            $('#deviceProxyDialog').on('shown.bs.modal', function () {
                $("#deviceProxyEnableCheckbox").bootstrapSwitch({
                    state: $scope.deviceProxy.status,
                    onSwitchChange:function (event, state) {
                        $scope.deviceProxy.status = state;
                    }
                });
                $("#deviceProxyEnableCheckbox").bootstrapSwitch('state', $scope.deviceProxy.status, true);
            })
        }
        /**
         * 提交代理
         */
        $scope.submitDeviceProxy = function(){
            $scope.deviceProxy.type = "socks5";
            $scope.deviceProxy.status = $scope.deviceProxy.status?1:0;
            faceinner.putJson(api['device.proxy.config'], $scope.deviceProxy , function(res) {
                if (res.code == 'S00') {
                    $("#deviceProxyDialog").modal('hide');
                    flushData();
                }else{
                    layer.msg(res.msg);
                }
            });
        }


        /**
         * 任意门弹框
         */
        $scope.openDeviceDoorDialog = function(item){
            $scope.deviceDoor = {
                deviceId: item.id,
                enable: false
            }

            let serverTunnelId = item.serverTunnelId;
            let params = {
                type: 2,
                serverTunnelId:serverTunnelId
            }
            faceinner.get(api['user.domain.nobind'], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.domainNoBindList = res.data;
                    });
                }


                faceinner.get(api['device.door.config'], {deviceId: item.id}, function(res){
                    if (res.code == 'S00') {
                        $scope.$apply(function() {
                            $scope.deviceDoor = res.data;
                            $scope.deviceDoor.enable = res.data.enable == 1;
                            $scope.domainNoBindList.push({id: res.data.domainId, domain: res.data.domain})

                            $("#deviceDoorEnableCheckbox").bootstrapSwitch('state', $scope.deviceDoor.enable, true);
                        });
                    }
                });
            });

            $("#deviceDoorEnableCheckbox").bootstrapSwitch({
                state: $scope.deviceDoor.enable,
                onSwitchChange:function (event, state) {
                    $scope.$apply(function() {
                        $scope.deviceDoor.enable = state;
                    });
                }
            });
            $("#deviceDoorEnableCheckbox").bootstrapSwitch('state', $scope.deviceDoor.enable, true);


            $("#deviceDoorDialog").modal({
                backdrop: false
            });

        }
        /**
         * 保存Device
         */
        $scope.submitDeviceDoor = function(){
            if(!$scope.deviceDoor.domainId){
                layer.msg("请选择域名")
                return;
            }

            $scope.deviceDoor.enable = $scope.deviceDoor.enable?1:0;
            faceinner.postJson(api['device.door.config'], $scope.deviceDoor , function(res) {
                if (res.code == 'S00') {
                    $("#deviceDoorDialog").modal('hide');
                    flushData();
                }else{
                    layer.msg(res.msg);
                }
            });
        }


        /**
         * 切换线路
         */
        $scope.switchLineDialog = function(item){
            faceinner.get(api["device.tunnel"], {}, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.list = res.data;
                    });
                }
            });

            $("#switchLineDialog").modal({
                backdrop: false
            });
        }


        /**
         * 网络唤醒
         */
        $scope.sendMigicWolDevice = function(){
            let macAdder = $scope.deviceInfo.macAddr;
            if (!macAdder) {
                layer.msg("mac地址未就绪");
                return
            }
            faceinner.post(api['user.device.wol'], {mac: macAdder}, function(res) {
                if (res.code == 'S00') {
                    layer.msg("唤醒完成，请等待机器上线");
                }
            });
        }


        /**
         * 切换线路提交
         */
        $scope.switchLine = function(){
            if(!$scope.selectTunnelId){
                layer.msg("请选择通道")
                return;
            }
            let params = {
                deviceId: $scope.deviceInfo.id,
                serverTunnelId: $scope.selectTunnelId,
            }
            faceinner.postJson(api["user.device.switch-line"], params , function(res) {
                if (res.code == 'S00') {
                    $scope.closeSwitchLine();
                    flushData(); // 刷新数据
                    setTimeout(function () {
                        flushData(); // 刷新数据
                    },2000);
                    delete $scope.selectTunnelId;
                } else {
                    layer.msg(res.msg);
                }
            });
        }



        /**
         * 关闭绑定域名弹框
         */
        $scope.closeBindDomain = function(){
            $("#bindDomainDialog").modal('hide');
        }

        $scope.closeSwitchLine = function(){
            $("#switchLineDialog").modal('hide');
        }
        /**
         * 关闭绑定域名弹框
         */
        $scope.closeDeviceProxyDialog = function(){
            $("#deviceProxyDialog").modal('hide');
        }




        /**
         * 设备绑定域名
         */
        $scope.deviceBindDomain = function(){
            if(!$scope.selectDomainId){
                layer.msg("请选择域名或端口")
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
            faceinner.postJson(api['user.device'], $scope.deviceUpdate , function(res) {
                if (res.code == 'S00') {
                    $("#editDevice").modal('hide');
                    flushData();
                }else{
                    layer.msg(res.msg);
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
                if (res.code == 'S00') {
                    $("#delDevice").modal('hide');
                   window.history.back();
                }
            });
        };



 	}];


	// app.controller('IndexController', callback );
	return callback;
});
