/**
 * 订单列表页面
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery','layer','pagintation', 'css!./orders.css'], function (app, $, layer, pagintation) {//加载依赖js,


	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {
        $scope.current = $routeParams.current;
        $scope.page = {
            current: 1,
        }
        $scope.orderNo = $routeParams.out_trade_no;


        // 确认支付信息
        queryOrderList();

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
        function queryOrderList(){
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
                            href: "#/user/orders/list?current={{number}}",
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


 	}];
	

	return callback;
});