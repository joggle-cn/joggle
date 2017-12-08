

define(['app','jquery'], function (app, $) {// 加载依赖js, 
	
	return ['$scope', function ($scope) { 
		$.openloading();
		$scope.currentPageNo = 1;
		$.ajax({
			url:'/api/wishwall/.face',
			type: 'GET',
			data: "currentPageNo=" + $scope.currentPageNo ,
			dataType:"json", 
			success: function(json){
				if(json.status){
					var page = json.data;
					$scope.$apply(function(){
						$scope.wishwalls = page.data;
						console.log(page.data);
						$.closeloading();
					});
				}
			}
		});
		
		 
	}];

});