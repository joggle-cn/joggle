define(['app','webchat','jquery','umeditor','handlebars'], function (app, WebChat, $, UM, Handlebars) {// 加载依赖js, 

	
	return ['$location','$scope','$timeout', '$interval','$window',
	        function ($location,$scope,$timeout,$interval,$window) {
		$scope.time = 0;
		$scope.nickname = "..."; 
		$scope.message = "欢迎使用faceinner聊天";
		$scope.user = "";
		$scope.usercount = 1;// 在线用户
		$scope.waitcount = 1;// 等待用户
		// 富文本编辑器
		if($window.editor){
			$window.editor.destroy();
		}
		var editor = $window.editor = UM.getEditor('myEditor');
		
		//
		var target = "wss://" + faceinner.getHost() + "/ws/websocket/chat";
		var ws;
		if ('WebSocket' in window) {
		    ws = new WebSocket(target);
		} else if ('MozWebSocket' in window) {
		    ws = new MozWebSocket(target);
		} else {
		    con('WebSocket is not supported by this browser.');
		    return;
		}
		
		var chat = new WebChat(ws);
		
		
		
		
		
		
		//chat.send(data);
		
		// chat.fetchChatUser();// 随机获取一个聊天对象
		var inta = $interval(function(){
			$scope.time++; 
		},1000);
		
		
		
		chat.iAdapter({ 
			message: function(msg){
				
				var cm = $("#chat-message");
				
				var html = createYouMessage({
					user: msg.fromUser,
					content: msg.content
				});
				cm.html(cm.html() + html);
				scrollBottom();
			},
			fetchUser:function(user){
				console.log("chat:"+user.name);
				$scope.nickname = user.name; 
				
				// 关闭模块
				$("#findUserModal").modal('hide');
				
				$interval.cancel(inta);
			},
			online:function(msg){
				$scope.usercount = msg.totalCount;
				$scope.waitcount = msg.waitCount;
				
				
			}
		});
		
		// 关闭链接回调函数
		chat.close(function(){
			
		});
		
		chat.ready = function(){
			$scope.user = parseInt(Math.random()*10000);
			chat.login($scope.user);
			console.log("login with webchat!");
	//		$timeout(function(){
	//			// 获取一个在线用户
	//			chat.fetchChatUser();
	//			
	//		},5000);
			
			
			
			
		};
		
		
		// 
		$("#findUserModal").modal({
			 backdrop: false
		});
		$scope.finduser = "正在寻找聊天对象,请骚等...";
		
		
		// 启动周期任务
	//	$timeout(function(){
	//		
	//		
	//		
	//		
	//	},2000);
		
		
		
		
		// 发送消息
		$scope.send = function(){ 

			var content = editor.getContent(); 
			if(content){
				var html = createMeMessage({
					user: $scope.user,
					content: content
				});
				
				
				var msg =  {
				    command: 0x80001,
				    toUser: $scope.nickname,
				    fromUser: $scope.user,
				    content: content
				};
				
				
				
				chat.send(JSON.stringify(msg)); 
				var cm = $("#chat-message");
				cm.html(cm.html() + html);
				editor.execCommand('cleardoc');
				scrollBottom();
			}
		};
		
		
		$scope.onece = function(){
			if($scope.waitcount > 1){
				chat.fetchChatUser();
			}else{
				alert("请耐心等待神秘网友上线...");
			}
		};
		
		
		$scope.exit = function(){ 
			chat.shutdown();
			$location.path('/index.html'); 
		};
		
		$scope.retry = function(){
			if($scope.waitcount > 1){
				chat.fetchChatUser();
			}else{
				alert("请耐心等待神秘网友上线...");
			}
			
		};
		
		
		 
		
	}];
	
	
	function scrollBottom(){
		var h = $('#chat-message')[0].scrollHeight ; 
		$(".chat-message").animate({scrollTop: h },500);
		// $(".chat-message").scrollTop(h);
		
	}
	
	
	/**
	 * 发送消息
	 */
	function createMeMessage(context){
		var source = $("#msg-me-template").html();
		source = source.replace("[content]", context.content);
		var template = Handlebars.compile(source);
		var html    = template(context);
		return html;
	}

	
	/**
	 * 构建对方消息
	 */
	function createYouMessage(context){
		var source = $("#msg-you-template").html();
		source = source.replace("[content]", context.content);
		var template = Handlebars.compile(source);
		var html    = template(context);
		return html;
	}
});