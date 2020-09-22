/**
 * api 接口地址
 *
 * faceinner全局对象
 *
 * @type {{}}
 */

// 读取服务器地址
let server = 'http://localhost:8081';
if(window.SERVER_URL && window.SERVER_URL != '${url}'){ // 如果配置了
    server = window.SERVER_URL;
}

let faceinner = {
    /** 服务器地址 */
    server: server,
    ws: server.replace("http", "ws"),


    errorfunc : function(state){
        console.log(state);
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

                    $scope[item.field+'Msg'] = item.msg;
                    var input = $("input[name=" + item.field+"]");
                    input.css("border-color", 'red');
                    input.on('keyup',function(){
                        $(this).css("border-color", "#cccccc");
                        $('#error_'+item.field).remove();
                    });
                    var errorsEl = input.next();
                    errorsEl.append(
                        '<span id="error_'+item.field+'" class="error" >'+item.msg+'</span>');
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
     * @param funcerror 调用失败函数
     */
    get: function(url, data, func, funcerror){
        let options = {
            url: faceinner.server + url ,
            type: 'get',
            data: data ,
            dataType: "json",
            success:func,
            error: faceinner.errorfunc,
            crossDomain: true,
            xhrFields: {
                withCredentials: true
            },
        }
        if(func === undefined){
            delete options.data;
            options.success = data;
        }
        if(funcerror){
            options.error = funcerror;
        }
        // 处理Token
        this.progressToken(options);

        $.ajax(options);
    },


    /**
     * 处理Token
     * @param options
     */
    progressToken : function(options){
        if(localStorage.token && localStorage.token != 'null'){
            let tokenTime    = parseInt(localStorage.tokenTime);
            let tokenExpires = parseInt(localStorage.tokenExpires);
            let curTime      = new Date().getTime();

            let tmp = (curTime - tokenTime)/1000; // 转换为秒
            if(tmp < tokenExpires){ // 没过期
                options.headers ={
                    'Authorization': 'Bearer ' + localStorage.token,
                }
            } else {
                console.log('登录过期了, 请重新登录！');
            }
        }
    },


    /**
     * post 请求
     *
     * @param url URL地址
     * @param data 参数
     * @param func 回调函数
     */
    post: function(url, data, func){
        let options = {
            url: faceinner.server + url ,
            type: 'post',
            data: data ,
            dataType: "json",
            success: func,
            error: faceinner.errorfunc,
            // crossDomain: true,
            // xhrFields: {
            //     withCredentials: true
            // },
        }
        if(func === undefined){
            delete options.data;
            options.success = data;
        }
        if (url != '/oauth/token') {// 非登录接口
            this.progressToken(options);// 处理Token
        } else {// 登录接口，加入客户端Authorization头
            options.headers = {
                'Authorization': 'Basic Y2xpZW50X2FwcDpwYXNzd29yZA==',
            }
        }
        $.ajax(options);
    },


    /**
     * delete 请求
     *
     * @param url URL地址
     * @param data 参数
     * @param func 回调函数
     */
    delete: function(url, data, func){
        if(!data){
            data = {}
        }
        data._method = "DELETE";
        let options = {
            url: faceinner.server + url  ,
            type: 'DELETE',
            data: data ,
            dataType: "json",
            success:func,
            error: faceinner.errorfunc,
            crossDomain: true,
            xhrFields: {
                withCredentials: true
            },
        }
        if(func === undefined){
            delete options.data;
            options.success = data;
        }

        // 处理Token
        this.progressToken(options);

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




