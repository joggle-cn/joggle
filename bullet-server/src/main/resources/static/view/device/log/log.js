/**
 *
 * Home   模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','jquery', 'layer','bootstrap-switch', 'css!./log.css'], function (app, $, layer) {//加载依赖js,




	let callback = ["$scope","$routeParams","$location", function ($scope, $routeParams,$location) {
        $scope.active = 'device';
        // 设备ID
        let deviceId = $routeParams.deviceId;
        let mappingId = $routeParams.mappingId;
        $scope.logs = [];

        var target = "ws://localhost:8081/_ws/log/"+ mappingId;
        var ws;
        if ('WebSocket' in window) {
            ws = new WebSocket(target);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(target);
        } else {
            con('WebSocket is not supported by this browser.');
            return;
        }

        ws.onopen = function() {
            console.log("connected websocket for real-time updates");
        };

        ws.onmessage = function(message) {
            $scope.$apply(function() {
                console.log(message)
                $scope.logs.push(message.data);
            });
        };

        ws.onerror = function(err) {
            console.log("Web socket error:")
            console.log(err);
        };

        ws.onclose = function(cls) {
            console.log("Web socket closed:" + cls);
        };






		 
 	}];
	
	
	// app.controller('IndexController', callback );
	return callback;
});