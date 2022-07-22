/**
 * 支付结果页面
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery','layer', 'css!./pay-result.css'], function (app, $, layer) {//加载依赖js,


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


        /**
         * 调用支付
         */
		$scope.pay = function(){
            let params = {
                resourceType: 3,
                amount: $scope.amount,
                payType: $scope.payType,
            }
            faceinner.postJson(api["user.orders.create"], params, function(res){
                if (res.code == 'S00') {
                    if (params.payType == 2) {// 支付宝
                        layer.msg('正在跳转支付宝付款网页');
                        let  url = faceinner.server + '/api/open/orders/alipay?orderId=' + res.data;

                        window.open(url, "前往支付宝付款", "_self", null);
                    }
                } else { //错误提示
                    layer.msg(res.msg);
                }
            });



        }
 	}];
	

	return callback;
});