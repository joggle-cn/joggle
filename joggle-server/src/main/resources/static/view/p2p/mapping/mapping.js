/**
 * P2P映射管理
 *
 * @author marker
 * @date 2022-08-08
 */
define(['app','jquery','layer','pagintation', 'css!./mapping.css'], function (app, $, layer, pagintation) {//加载依赖js,


	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {
        $scope.current = $routeParams.current;
        $scope.page = {
            current: 1,
        }


        // 确认支付信息
        flushData();

        // //获取子控制器当中的跳转页数
        // $scope.$watch("current", function(event,val){
        //     console.log(val)
        //     $scope.page.current = val;
        //     queryOrderList()
        // });

        /**
         * 计算价格
         * @param newVal
         */
        function flushData(){
            let params = {
                current: $scope.current
                // orderNo: $scope.orderNo,
            }
            faceinner.get(api["user.orders.list"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.page = res.data;

                        let $pager = $('#pagination');
                        $pager.empty();
                        $pager.removeData("twbs-pagination");
                        $pager.unbind("page");
                        $pager.twbsPagination({
                            totalPages: res.data.pages,
                            visiblePages: 10,
                            href: "#/p2p/mapping/list?current={{number}}",
                            first:"首页",
                            prev:"上一页",
                            next :"下一页",
                            last :"尾页",
                            hideOnlyOnePage : true,
                            // onPageClick :function(event,page){
                            //     renderPage(page)
                            // }
                        });
                    });
                }
            });
        }


        $scope.createP2pMapping = function () {
            faceinner.get(api["device.tunnel"], {}, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.list = res.data;
                    });
                }
            });

            $("#createP2pMappingDialog").modal({
                backdrop: false
            });

        }

        // 关闭弹框
        $scope.closeP2pMappingDialog = function () {
            $("#createP2pMappingDialog").modal('hide');
        }

        /**
         * 保存Device
         */
        $scope.submitP2pMapping = function(){
            $scope.deviceDoor.enable = $scope.deviceDoor.enable?1:0;
            faceinner.postJson(api['device.door.xxx'], $scope.deviceDoor , function(res) {
                if (res.code == 'S00') {
                    $("#deviceDoorDialog").modal('hide');
                    flushData();
                }else{
                    layer.msg(res.msg);
                }
            });
        }

 	}];
	

	return callback;
});