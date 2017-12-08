'use strict';

/* Directives */

define([
   	  'angular' 
  	, 'jquery'
  	], function (angular, $) {//加载依赖js,
	
	
return angular.module('app.directives', [])

	.directive('appVersion', ['version', function(version) {
	    return function(scope, elm, attrs) {
	      elm.text(version);
	    };
	  }])



    /**
     * 验证密码是否一致
     * (只有注册界面和修改密码界面使用)
     */
	 .directive('password', function() {
		return {
			require: 'ngModel',
			link: function (scope, elm, attrs, ctrl) {
				ctrl.$parsers.unshift(function (viewValue) {
					if (scope.user.pass == viewValue) {// 验证两次密码是否一致
						ctrl.$setValidity('password', true);
						return viewValue;
					}
					ctrl.$setValidity('password', false);
					return undefined;
				});
			}
		};
	})


     /**
      * 邮箱地址验证
      */
    .directive('email', function() {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/ ;
                    if (re.test(viewValue) ) {// 验证两次密码是否一致
                        ctrl.$setValidity('email', true);
                        return viewValue;
                    }
                    ctrl.$setValidity('email', false);
                    return undefined;
                });
            }
        };
    })




    .directive('loadFragment', ['version', function(version) {
    return {
    	 restrict : 'A',
    	 controller: function($scope, $element, $attrs, $transclude){ 
    		 
    		 
    		 
    	 } 


    };
  }]);
});