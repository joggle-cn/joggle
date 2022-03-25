/**
 *
 * 我的域名 模块
 *
 * @author marker
 * @date 2019-12-26
 */
define(['app','jquery', 'css!./domain.css'], function (app, $) {//加载依赖js,


	var callback = ["$scope", function ($scope) {

        $scope.active = 'domain';

	    function render(){
            faceinner.get(api["user.domain"], function(res){
                if (res.status == 0) {
                    $scope.$apply(function() {
                        $scope.list = res.data;
                    });
                }
            });
        }
        render();

        /**
         * 购买域名
         */
        $scope.buyDomain = function(item){

		}





		 
		 
 	}];
	
	
	// app.controller('IndexController', callback );
	return callback;
});