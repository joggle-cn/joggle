

define(['app','jquery','handlebars'], function (app, $, Handlebars) {// 加载依赖js, 
	
	return ['$scope','$routeParams', function ($scope, $routeParams) { 
		$.openloading();
		var oid = $routeParams.objectId;
		
		$.ajax({
			url:'/api/wishwall/'+oid+'.face',
			type: 'GET', 
			dataType:"json", 
			success: function(json){ 
				if(json.status){
					var item = json.data;
					$scope.$apply(function(){ 
						var background = item.background;
						$scope.wallId = item._id;
						$(".wishwall_wrapper").css("background-image", "url("+background+")");
						
					});
				}
				$.closeloading();
			} 
		});
		
		

		$("#createWishModal").modal();
		var src = $("#study-createWish").attr("data-src");
		$("#study-createWish").attr("src", src);
		
		
		$('#show_wishwall_wrapper').dblclick(function(e){
			// 判断是否登录
			$scope.wish = {
				wall: $scope.wallId
			};
			$scope.wish.offsetX = e.offsetX;
			$scope.wish.offsetY = e.offsetY;
			x = e.clientX ,  y = e.clientY ;
			
			var height = $('#show_wishwall_wrapper').height();
			var width  = $('#show_wishwall_wrapper').width();
			
			if(x > 0 && x < (width - 300)
				&& y > 0 && y < (height - 80)	 ){
				
				var source = $('#createWish-template').html();

				var template = Handlebars.compile(source);
				 
				var context = {title: "My New Post",
						style: "top:" + e.offsetY + "px; left:" + e.offsetX + "px; width:300px;",
						body: "This is my first post!"};
				var html    = template(context);
				 
				$('#show_wishwall_wrapper').append(html);
			}
		});
		
		
		$('#show_wishwall_wrapper').on('dblclick','.wish-edit .msg',function(e){
			var content = $.trim($(this).text());
			$(this).text("");
			$(this).append("<textarea class='msg-textarea'>"+content+"</textarea>");
			
		});
		
		$('#show_wishwall_wrapper').on('dblclick','.wish-edit',function(e){ 
			e.stopPropagation();
		});
		
		
		$('#show_wishwall_wrapper').on('mousedown','.wish-edit .icon',function(e){
			// 防止默认事件发生 
			var $wishEdit = $(this).parent(); 
			x = e.clientX , 
			y = e.clientY ;
			
			var pos = $wishEdit.position();
			var height = $('#show_wishwall_wrapper').height() - $wishEdit.height();
			var width  = $('#show_wishwall_wrapper').width()  - $wishEdit.width();
			
			$(document).bind("mousemove", function(e){ 
	            // 宇宙超级无敌运算中... 
				var newX = pos.left + e.clientX - x;
				var newY = pos.top  + e.clientY - y;
				if(newX > 0 && newX < width){
					$wishEdit.css("left",newX+ 'px');
				}
				if(newY > 0 && newY < height){
					$wishEdit.css("top" ,newY+ 'px');
				} 
	        });
			
			$(document).bind("mouseup", function(e){
				 $(document).unbind("mousemove").unbind("mouseup");
	        });
			 
			
			
			
			e.stopPropagation();
			//防止默认事件发生 
			e.preventDefault();
		});
		
		
		var x,y;
		$('#show_wishwall_wrapper').on('mousedown','.wish-edit .msg',function(e){
			// 防止默认事件发生 
			e.stopPropagation(); 
		});
		$("#show_wishwall_wrapper").on('mousedown','.wish-edit',function(e){
			var el = this;
			var els = el.style;
			 x = e.clientX - this .offsetWidth, 
			 y = e.clientY - this .offsetHeight; 
			$(document).bind("mousemove", function(e){
	            // 宇宙超级无敌运算中... 
				var newW = e.clientX - x;
				var newH = e.clientY - y;
				if(newW > 300){
				    els.width  = newW + 'px';
		            var msgs = $(el).find(".msg")[0].style; 
		            msgs.width = (e.clientX - x - 70) + 'px';
				}
				if(newH > 80){
					els.height = newH + 'px';
				}
	        });
			$(document).bind("mouseup", function(e){
				 $(document).unbind("mousemove").unbind("mouseup");
	        });
			 
			//防止默认事件发生 
			 e.preventDefault();
		});
		
		
	   
		
		$scope.confirmation = function(){
			
			
		};
		
		
		$scope.cancel = function(){
			$("#createWishModal").modal('hide');
		};
		
	}];

});