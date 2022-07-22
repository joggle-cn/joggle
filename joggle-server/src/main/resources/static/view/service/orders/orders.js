/**
 * 订单列表页面
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery','layer', 'css!./orders.css'], function (app, $, layer) {//加载依赖js,


	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {


        $scope.orderNo = $routeParams.out_trade_no;


        // 确认支付信息
        confirm();

        /**
         * 计算价格
         * @param newVal
         */
        function confirm(){
            let params = {
                orderNo: $scope.orderNo,
            }
            faceinner.postJson(api["user.orders.confirm"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.orderInfo = res.data;
                    });
                }
            });
        }


 	}];
	

	return callback;
});