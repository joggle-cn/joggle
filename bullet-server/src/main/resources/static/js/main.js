/**
 *
 * reuqirejs 配置文件
 *
 * 在引用的js中需要配置mode属性来指定采用视图模式.
 *
 *
 *
 *
 * @type {{baseUrl: string, paths: {jquery: string, bootstrap: string, angular: string, x18n: string, angular-animate: string, angular-route: string, angularAMD: string, webchat: string, umeditor: string, umeditorConfig: string, handlebars: string, loading: string, amazeui: string, app: string, moudleRouter: string, moudleServices: string, moudleController: string, moudleFilters: string, moudleDirectives: string, ajax: string, api: string}, map: {*: {css: string}}, shim: {angular: {exports: string}, angularAMD: {deps: string[]}, bootstrap: {deps: string[]}, angular-route: {deps: string[], exports: string}, angular-animate: {deps: string[], exports: string}, loading: {deps: string[]}, umeditor: {deps: string[]}}, deps: string[]}}
 */
var options = {
	baseUrl: './', //js文件存放的路径
    paths: {//配置加载路径
          'jquery': 'lib/jquery/jquery-1.10.2.min'
        , 'bootstrap': 'lib/bootstrap3/js/bootstrap.min'
        , 'angular': 'lib/angular/angular.min' // requirejs的一个文本插件
        , 'x18n': 'lib/x18n/x18n'
        , 'angular-animate': 'lib/angular/angular-animate.min'
        , 'angular-route': 'lib/angular/angular-route.min'
        , 'angularAMD': 'lib/angular/angularAMD.min'
        , 'webchat': 'lib/faceinner/websocket'
        , 'umeditor': 'lib/umeditor/umeditor'	        	
        , 'umeditorConfig': 'lib/umeditor/umeditor.config'
        , 'handlebars': 'lib/handlebars/handlebars-v1.3.0'
        , 'loading': 'lib/jquery/jquery.loading'
        , 'amazeui':'lib/amazeui/js/amazeui.min'
        , 'dropload':'lib/jquery/plugins/dropload-gh-pages/dropload'
        , 'bootstrap-switch':'lib/bootstrap3/plugins/bootstrap-switch/js/bootstrap-switch.min'
        , 'layer':'lib/layer-v3.1.1/layer/layer'


       
        
        /* 自定义JavaScript */	
        , 'app': 'js/app'
        , 'moudleRouter': 'js/routers'
        , 'moudleServices': 'js/services'
        , 'moudleController': 'js/controller'
        , 'moudleFilters': 'js/filters'
        , 'moudleDirectives': 'js/directives'
        , 'ajax': 'js/faceinner.ajax'
        , 'api': 'js/faceinner.api'



    },
    map: {
  	  '*': {
  	      'css': 'lib/require/plugins/css.min' // or whatever the path to require-css is
  	   }
  	},
    shim: {
       'angular': {
    	   exports: 'angular'
       }
       , 'angularAMD':{
    	   deps:['angular']
       }
       , 'bootstrap':{
    	   deps:['jquery']
       }
       , 'angular-route':{
           deps:['angular'],
           exports: 'angular-route'
       }
       , 'angular-animate':{
           deps:['angular'],
           exports: 'angular-animate'
       }
       , 'loading':{
    	   deps:['jquery']
       }
       , 'umeditor':{
           deps:['jquery','umeditorConfig']
       }
        
    },
    deps: [ 'bootstrap', 'loading','api','ajax']
    , urlArgs: "time=" + Math.random() // 防止读取缓存，调试用
};



var mode = getViewMode();// 获取视图模式;
switch (mode){
    case 'pc':// pc端使用的bootstrap


        break;
    case 'pad':// 平板端使用jquerymobile


        break;
    case 'mobile': // 手机端依赖于amazon ui
        options.deps = [ 'amazeui', 'loading', 'api', 'ajax']
        break;
}

// 初始化requrejs
require.config(options);



/**
 * 启动App
 * */
require([
        'angular'
      , 'angularAMD'
      , 'app'
      , 'moudleRouter'
      , 'moudleController'
      , 'loading'
      
    ], function(angular, angularAMD, app, loading) {
	console.log('ready bootstrap angular');
	angularAMD.bootstrap(app);
	     
	
	
	
});


/**
 * 获取视图模式
 * (mobile | pc | pad)
 *
 * @author marker
 * @date 2016-08-13
 */
function getViewMode(){
    var els= document.getElementsByTagName("script");
    var len = els.length;
    for(var i=0; i < len; i++){
        var dom = els[i];
        if(dom.getAttribute("data-main")){
            var mode = dom.getAttribute("mode");
            if(mode){ return mode; }
        };
    }
    return "pc";// 默认显示电脑版本
}