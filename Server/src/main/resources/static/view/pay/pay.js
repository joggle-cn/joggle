/**
 *
 * 支付 模块
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery', 'css!./domain.css'], function (app, $) {//加载依赖js,


	var callback = ["$scope","$routeParams",  function ($scope, $routeParams) {

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
                time: newVal,
                domainId: $scope.domainId
            }
            faceinner.post(api["user.domain.calculate"], params, function(res){
                if (res.code == 'S00') {
                    $scope.$apply(function() {
                        $scope.payMoney = res.data.payMoney;
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
 	}];
	

	return callback;
});