

define(['app','css!./video.css'], function (app) {// 加载依赖js,
	
	return ['$scope','$location','$session',  '$AjaxService',
        function ($scope, $location, $session,  $AjaxService) {


            // 判断是否登录
            if(!$session.user){// 没有登录的
                console.log('user no login');
                $('#videoFramework').html('please login!');


                 return;
            }







            // 由于浏览器实现不同，一个兼容版本
            var localStream;
            var getUserMedia = (navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia);


            //兼容浏览器的PeerConnection写法
            var PeerConnection = (window.PeerConnection ||
            window.webkitPeerConnection00 ||
            window.webkitRTCPeerConnection ||
            window.mozRTCPeerConnection);



            getUserMedia.call(navigator, {
                video: true,
                audio: true
            }, function(localMediaStream) {
                var video = document.getElementById("video");
                video.src = URL.createObjectURL(localMediaStream);
                video.onloadedmetadata = function(e) {
                };
                video.play();
                localStream = localMediaStream;
            }, function(e) {
                console.log("错误！", e);
            });


            var pc;
            var ws;
            var isCall = false;


            /**
             * 建立Socket 链接
             */
            var userId = $session.user.id;

            var target = "wss://" + faceinner.getHost() +"/ws/video/" + userId;
            if ('WebSocket' in window) {
                ws = new WebSocket(target);
            } else if ('MozWebSocket' in window) {
                ws = new MozWebSocket(target);
            } else {
                alert('WebSocket is not supported by this browser.');
                return;
            }

            //
            //function createObjectURL ( file ) {
            //    if ( window.webkitURL ) {
            //        return window.webkitURL.createObjectURL( file );
            //    } else if ( window.URL && window.URL.createObjectURL ) {
            //        return window.URL.createObjectURL( file );
            //    } else {
            //        return null;
            //    }
            //}

            // 打开链接执行
            ws.onopen = function(){
                console.log("websocket open");
                var ice = {"iceServers": [
                    {"url": "stun:stun.stunprotocol.org:3478"} ,
                    {"url": "stun:stun.l.google.com:19302"}
                ]};
                pc = new PeerConnection(ice);
                console.log("Adding Local Stream to peer connection");

                pc.onaddstream = function(e){
                    var a = document.getElementById("remote");
                    a.src = webkitURL.createObjectURL(e.stream);
                    console.log("Received remote stream" );
                    a.play();
                };


                pc.onicecandidate = function(event){
                    if (event.candidate) {
                        console.log("onicecandidate");
                        var to = document.getElementById("to").value;
                        var msg = {
                            command: 0x80004,
                            fromUser: $session.user.id,
                            toUser: to,
                            candidate: event.candidate
                        };
                        sendMsg(JSON.stringify(msg));
                    }
                };

            };

            ws.onmessage = function(evt){
                var signal = JSON.parse(evt.data);

                 document.getElementById("to").value = signal.fromUser;

                switch(signal.command){
                    case 0x80004: // Candidate
                        if(signal.candidate){
                            console.log(signal.candidate);
                            pc.addIceCandidate(new RTCIceCandidate(signal.candidate));
                        }
                        ;break;
                    case 0x80005:// offer req
                        var a = confirm(signal.fromUser + "请求与您视频");
                        if(a){
                            pc.setRemoteDescription(new RTCSessionDescription(signal.offer));

                            pc.createAnswer(function(offer){
                                pc.setLocalDescription(offer);
                                var msg = {
                                    command:0x80006,
                                    fromUser: signal.toUser,
                                    toUser: signal.fromUser,
                                    offer: offer
                                };
                                // resp
                                sendMsg(JSON.stringify(msg));


                                // 创建offer与对方开视频
                                if(!isCall){
                                    setTimeout("call()", 2000);
                                }
                            },function(error){
                                console.log('Failed to create session description: ' + error.toString());
                            });
                        }
                        ;break;

                    case 0x80006:// 对方应答的信息
                        pc.setRemoteDescription(new RTCSessionDescription(signal.offer));
                        break;
                }
            };

            ws.onclose = function(evt){console.log("WebSocketClosed!");};

            ws.onerror = function(evt){console.log("WebSocketError!");};




            /**
             * 播放
             */
            $scope.call = call =  function(){
                    isCall = true;
                    pc.addStream(localStream);
                    pc.createOffer(function(offer){
                        pc.setLocalDescription(offer);
                        var to = document.getElementById("to").value;
                        var msg = {
                            command: 0x80005,
                            fromUser: $session.user.id,
                            toUser: to,
                            offer: offer
                        };
                        sendMsg(JSON.stringify(msg));
                    },function(a){
                        console.log(a);
                    });
                }



                function sendMsg(msg){
                    ws.send(msg);
                }


                function quxiao(){
                    pc.close();

                }




            }];

});