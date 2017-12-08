

define(['app','jquery'], function (app, $) {// 加载依赖js, 
	
	return ['$scope','$location','fragmentService', function ($scope, $location, fragmentService) { 
		
		
		
		fragmentService.load('#user_menu',"/template/user/user_menu.htm");
		
		
		
		
		
		
		
	}];

});