/**
 * 购买二级域名页面
 *
 * @author marker
 * @date 2022-07-23
 */
define(['app','jquery','layer','pagintation', 'css!./domain-buy.css'], function (app, $, layer, pagintation) {//加载依赖js,

	var callback = ["$scope","$routeParams",'$location',  function ($scope, $routeParams, $location) {
        $scope.orderNo = $routeParams.out_trade_no;
        $scope.payMoney = 2;
        $scope.payType = 2;
        $scope.type = "";
        $scope.keyword = "";

        /**
         *  搜索可购买域名
         * @param
         */
        $scope.searchDomain = function(page){
            let params = {
                keyword: $scope.keyword,
                type: $scope.type,
                current: page,
            }
            faceinner.get(api["user.domain.search"], params, function(res){
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