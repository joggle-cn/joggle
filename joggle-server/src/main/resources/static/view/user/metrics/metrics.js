/**
 * 流量明细列表页面
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery','layer','pagintation', 'css!./metrics.css'], function (app, $, layer, pagintation) {//加载依赖js,


	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {
        $scope.current = $routeParams.current;
        $scope.page = {
            current: 1,
        }
        $scope.orderNo = $routeParams.out_trade_no;
        $scope.params ={
            serverTunnelId: '',
        }


        faceinner.get('/api/server/tunnel/options', {}, function(res) {
            if (res.code == 'S00') {
                $scope.$apply(function() {
                    $scope.tunnels = res.data;
                });
            }
        });

        // 流量明细
        queryMetricsList(1);

        // //获取子控制器当中的跳转页数
        // $scope.$watch("current", function(event,val){
        //     console.log(val)
        //     $scope.page.current = val;
        //     queryOrderList()
        // });

        $scope.queryMetricsList = queryMetricsList;
        /**
         * 流量明细
         * @param newVal
         */
        function queryMetricsList(page){
            $scope.params.current = page;
            faceinner.get(api["user.metrics.list"], $scope.params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.page = res.data;
                        if(res.data.records.length == 0){
                            return
                        }
                        let $pager = $('#pagination');
                        $pager.empty();
                        $pager.removeData("twbs-pagination");
                        $pager.unbind("page");
                        $pager.twbsPagination({
                            totalPages: res.data.pages,
                            startPage: res.data.current,
                            visiblePages: 10,
                            // href: "#/user/metrics/list?current={{number}}",
                            first:"首页",
                            prev:"上一页",
                            next :"下一页",
                            last :"尾页",
                            hideOnlyOnePage : true,
                            onPageClick :function(event,page){
                                queryMetricsList(page)
                            }
                        });
                    });
                }
            });
        }


 	}];
	

	return callback;
});