

define(['app','jquery'], function (app,$) {// 加载依赖js, 
	
	return ['$scope','addressService', function ($scope,addressService) { 
		$scope.wishwall = {country:{}, province:{}, city:{}};// 初始化wishwall
		$scope.country = [];
		$scope.province = [];
		$scope.city = [];
		selectStyleItem($($("#wishwallstyle").find("div")[0]));
		
		
		
		
		// 加载国家信息
		addressService.fetchContry(function(array){
			$scope.$apply(function(){
				$scope.country = array;
				$scope.wishwall.countryId = array[0].id;
			}); 
		});
		
		
		$scope.changeCountry = function(){
			var countryId = $scope.wishwall.countryId;
			addressService.fetchProvince(countryId, function(array){
				$scope.$apply(function(){ 
					$scope.province = array;
					$scope.wishwall.provinceId = array[0].id;
				});
			});
		};
		
		$scope.changeProvince = function(){
			var provinceId = $scope.wishwall.provinceId;
			addressService.fetchCity(provinceId, function(array){
				$scope.$apply(function(){ 
					$scope.city = array;
					$scope.wishwall.cityId = array[0].id;
				});
			});
		};
		
		$scope.changeCity = function(){
			
		};
		
		
		
		// 登录操作
		$scope.create = function(){ 
			alert(JSON.stringify($scope.wishwall));
			var wishwall = $scope.wishwall;
			$.ajax({
				url:'/api/wishwall/.face',
				type: 'POST',
				data: wishwall ,
				dataType:"json", 
				success: function(json){
					alert(JSON.stringify(json));
					window.location.href ='/wishwall/index.html'; 
				}
			});
			
			
		};
		
		function selectStyleItem(dom){
			var data = dom.attr("data");
			$(".wishwall_wrapper").css("background-image", "url("+data+")");
			dom.css("border","2px solid #419641");
			$scope.wishwall.background = data;// 设置背景
		}
		
		$scope.selectBackground = function(event){
			var dom = $(event.target);
			var ul = dom.parent().parent(); 
			var divs = ul.find("div"); 
			divs.each(function(){
				$(this).css("border","");
			});
			if( event.target.tagName == "DIV" ){
				var data = dom.attr("data");
				if(data){
					selectStyleItem(dom);
				}
				if(dom.attr("addItem")){
					alert("上传");
				}
				 
				
				
			}
			
			
			
		};
	}];

});