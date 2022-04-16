/**
 *
 * 问题反馈 模块
 *
 * @author marker
 * @date 2021-04
 */
define(['app','layer','css!./index.css'], function (app, layer) {//加载依赖js,
	var callback = ["$scope", function ($scope) {

        $scope.entity = {
            title: "",
            content: "",
        }
		// 校验是否登录

        // 加载用户登录信息
        faceinner.get(api['user.login.info'], function(res){
            if(res.code == '040006'){ // 没有登录
                window.location.href='#/login';
            }
            $scope.$apply(function(){
                $scope.entity.contact = res.data.email;
            })

        });

        /* 提交反馈 */
        $scope.submit = function(){
            faceinner.postJson(api['user.feedback'], $scope.entity , function(res){
                if(res.code == "S00"){
                    layer.open({
                        type: 1 // Page层类型
                        ,area: ['300px', '200px']
                        ,title: '感谢您的反馈'
                        ,shade: 0.6 //遮罩透明度
                        ,maxmin: false //允许全屏最小化
                        ,anim: 0 //0-6的动画形式，-1不开启
                        ,content: '<div style="padding:30px;">' +
                            '提交反馈成功！<br/>' +
                            '作者收到您的反馈后会根据意见价值给予一定的流量奖励！' +
                            '注意：邮箱对应的账号才能收到奖励！' +
                            '</div>'
                    });
                    $scope.$apply(function () {
                        $scope.entity = {}
                    })
                } else {
                    if(res.code == '000006'){
                        layer.msg(res.msg)
                    }
                }
            });
        };

 	}];
	
	
	app.controller('FeedbackController', callback );
	return callback;
});