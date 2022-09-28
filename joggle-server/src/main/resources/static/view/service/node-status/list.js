/**
 * 购买二级域名页面
 *
 * @author marker
 * @date 2022-07-23
 */
define(['app','jquery','layer','pagintation', 'css!./list.css'], function (app, $, layer, pagintation) {//加载依赖js,

	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {
        $scope.orderNo = $routeParams.out_trade_no;
        $scope.payMoney = 2;
        $scope.payType = 2;
        $scope.params = {
            serverTunnelId: 1,
            type : "",
            keyword: "",
        }


        /**
         *  搜索可购买域名
         * @param
         */
        $scope.searchDomain = function(page){
            $scope.params.current = page;
            faceinner.get(api["device.tunnel.nodes"], $scope.params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.page = res.data;
                        let $pager = $('#pagination');
                        $pager.empty();
                        $pager.removeData("twbs-pagination");
                        $pager.unbind("page");
                        $pager.twbsPagination({
                            totalPages: res.data.pages,
                            startPage: res.data.current,
                            visiblePages: 10,
                            href: "",
                            first:"首页",
                            prev:"上一页",
                            next :"下一页",
                            last :"尾页",
                            hideOnlyOnePage : false,
                            onPageClick :function(event,page){
                                $scope.searchDomain(page)
                                // console.log(page)
                            }
                        });
                    });
                }
            });
        }
        $scope.searchDomain(1);





 	}];

	return callback;
});