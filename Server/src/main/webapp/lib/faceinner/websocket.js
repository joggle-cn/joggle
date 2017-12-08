define([ ], function () {//加载依赖js,


	return function WebChat(ws){
		this.callback;
		var instance = this;
		
		ws.onopen = function(){
			console.log('websocket opened!');
			
			// 发送登录消息
			if(instance.ready){
				instance.ready();
			}
			
		};
		
		ws.onmessage = function(evt){
			console.log(evt.data); 
			var msg = JSON.parse(evt.data);
			if(instance.callback){
				switch(msg.command){
				case 0x80007: 
					instance.callback.fetchUser(msg);
					break;
				case 0x80001: 
					instance.callback.message(msg);
					break;
				case 0x80009:
					instance.callback.online(msg); 
					break;
				}
			
			}
			
		};
		
		ws.onerror = function(evt){console.log("WebSocketError!");};
		
		this.login = function(user){
			var loginMsg = {
				command: 0x80002
				,name: user
			};
			ws.send(JSON.stringify(loginMsg));
		};
		
		
		this.send = function(msg){
			ws.send(msg);
		};
		
		
		
		this.fetchChatUser = function(){
			var msg = {
				command: 0x80007
			};
			ws.send(JSON.stringify(msg));
		};
		
		this.iAdapter = function(callback){
			this.callback = callback;
		};
		
		
		this.close = function(func){
			func();
		};
		
		this.ready = function(func){
			func();
		};
		
		
		this.shutdown = function(){
			
			ws.close(); 
		};
	};

});
 