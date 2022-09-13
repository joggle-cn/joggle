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
        $scope.amount  = 7; // 天
        $scope.payType  = 2; // 天
        $scope.data = {
            type: 1,
        }

        $scope.domainId = $routeParams.domainId;
        let params = {
            domainId: $scope.domainId,
        }
	    function render(){
            faceinner.get(api["user.domain.info"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.data = res.data;
                        calculate();
                    });
                }
            });
        }
        render();


        $scope.fixDays = function (){
            if($scope.amount){
                $scope.amount = parseInt(($scope.amount+"").replace(/^(0+)|[^\d]+/g,''))

            }
        }


        $scope.$watch('amount', function(newVal, b){
            $scope.amount  = parseInt(newVal);
            calculate();
        });
        $scope.$watch('payType', function(newVal, b){
            $scope.payType  = newVal;
            calculate();
        });



        /**
         * 计算价格
         * @param newVal
         */
        function calculate( ){
            let params = {
                resourceType: $scope.data.type,
                amount: $scope.amount,
                resId: $scope.domainId,
                payType: $scope.payType,
            }
            faceinner.postJson(api["user.orders.calculate"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.payMoney = res.data.payAmount;
                        $scope.dueTime = res.data.dueTime;
                    });
                }else{
                    layer.msg(res.msg.replaceAll('\n', "<br/>"), {icon: 9});
                }
            });
        }


        /**
         * 调用支付
         */
		$scope.pay = function(){
            let params = {
                resourceType: $scope.data.type,
                amount: $scope.amount,
                resId: $scope.domainId,
                payType: $scope.payType,
            }
            faceinner.postJson(api["user.orders.create"], params, function(res){
                if (res.code == 'S00') {
                    if(params.payType == 2){// 支付宝
                        window.location.href = faceinner.server + '/api/open/orders/alipay?orderId='+ res.data;
                        layer.msg('正在跳转支付宝付款网页');
                        return;
                    }
                    window.location.href = "#/user/domain";
                    layer.msg("购买成功");
                }else{ //错误提示
                    layer.msg(res.msg.replaceAll('\n', "<br/>"), {icon: 9});
                }
            });


        }
 	}];
	

	return callback;
});