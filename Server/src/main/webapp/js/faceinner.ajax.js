/**
 * api 接口地址
 *
 * faceinner全局对象
 *
 * @type {{}}
 */




var faceinner = {
    /** 服务器地址 */
    server: '',


    errorfunc : function(state){
        console.log(state);
        alert(state.statusText);
    },


    /**
     * 表单错误捕获
     * (需要表单验证的请求中调用)
     *
     * @param $scope
     * @param data
     */
    handleFieldError: function($scope, res){
        if(res.status == 2){// 表单错误
            $scope.$apply(function(){
                var len = res.data.length;
                for (var i = 0; i < len; i++) {
                    var item = res.data[i];
                    var input = $("input[name=" + item.field+"]");
                    input.css("border-color", 'red');
                    input.on('keyup',function(){
                        $(this).css("border-color", "#cccccc");
                        $('#error_'+item.field).remove();
                    });
                    var errorsEl = input.next();
                    if($('#error_'+item.field).length == 0){
                        errorsEl.append(
                            '<span id="error_'+item.field+'" class="error" >'+item.msg+'</span>');
                    }
                }
            });
        }




    },



    /**
     * get 请求
     *
     * @param url URL地址
     * @param data 参数
     * @param func 回调函数
     */
    get: function(url, data, func){
        var options = {
            url: faceinner.server + url + '.face',
            type: 'get',
            data: data ,
            dataType: "json",
            success:func,
            error: faceinner.errorfunc
        }
        if(func === undefined){
            delete options.data;
            options.success = data;
        }


        $.ajax(options);
    },



    /**
     * post 请求
     *
     * @param url URL地址
     * @param data 参数
     * @param func 回调函数
     */
    post: function(url, data, func){
        var options = {
            url: faceinner.server + url + '.face',
            type: 'post',
            data: data ,
            dataType: "json",
            success:func,
            error: faceinner.errorfunc
        }
        if(func === undefined){
            delete options.data;
            options.success = data;
        }
        $.ajax(options);
    },


    /**
     * 获取 IP:端口
     */
    getHost : function(){
        var host = window.location.hostname;
        if(window.location.port != 80){
            host += ":" + window.location.port;
        }
        return host;
    }

};




