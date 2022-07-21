/**
 *
 * 支付 模块
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery','layer', 'css!./pay.css'], function (app, $, layer) {//加载依赖js,


	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {

        $scope.active = 'domain';
        $scope.payMoney = 0;
        $scope.payTime  = 7; // 天

        $scope.domainId = $routeParams.domainId;
        let params = {
            domainId: $scope.domainId,
        }
	    function render(){
            faceinner.get(api["user.domain.info"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.data = res.data;
                    });
                }
            });
        }
        render();

        // 初始化价格
        calculate($scope.payTime);


        $scope.$watch('payTime', function(newVal, b){
            calculate(newVal);
        });


        /**
         * 计算价格
         * @param newVal
         */
        function calculate(newVal){
            let params = {
                resourceType: 1,
                time: newVal,
                resId: $scope.domainId
            }
            faceinner.postJson(api["user.orders.calculate"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.payMoney = res.data.payAmount;
                        $scope.dueTime = res.data.dueTime;
                    });
                }
            });
        }



        /**
         * 去支付
         */
        $scope.orderPay = function(item){

            item.createTimeStr = (new Date(item.createTime))
                .format("yyyy-MM-dd hh:mm:ss");
            $scope.item = item;
            $("#editDevice").modal({
                backdrop: false
            });
		}


        /**
         * 调用支付
         */
		$scope.pay = function(){

            let params = {
                resourceType: 1,
                time: $scope.payTime,
                resId: $scope.domainId,
                payType: 2,
            }
            faceinner.postJson(api["user.orders.create"], params, function(res){
                if (res.code == 'S00') {
                    if(params.payType == 2){// 支付宝
                        window.location.href = faceinner.server + '/api/open/orders/alipay?orderId='+ res.data;
                    }

                    layer.msg('正在跳转支付宝付款网页');
                }else{ //错误提示
                    layer.msg(res.msg);
                }
            });



        }
 	}];
	

	return callback;
});