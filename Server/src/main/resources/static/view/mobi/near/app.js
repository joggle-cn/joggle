/**
 *
 * 平台登录模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','dropload','css!./app.css'], function (app) {// 加载依赖js,
	
    return ['$rootScope','$scope','$location','userService', '$utils',
        function ($rootScope, $scope, $location, userService, $utils) {
        $scope.user = {};
            $scope.secretList = [];
        $('body').on('click', '.btn-filter',function(){
            $('#my-actions').modal('open');
        });
        var point = {};


        // 监听数据
        $rootScope.$watch('location',function(newVal){
            console.log(newVal);
        });




        $('#widget-list').dropload({
            scrollArea : window,
            //distance:50,
            loadUpFn: function(me){

                $utils.getLocation(function(latitude,longitude){
                    point.latitude  = latitude;// 纬度
                    point.longitude = longitude;// 经度
                    /** 初始化附近 */
                    $scope.near();

                    // 每次数据加载完，必须重置
                    me.resetload();
                });
            },
            loadDownFn : function(me){

                $utils.getLocation(function(latitude,longitude){
                    point.latitude  = latitude;// 纬度
                    point.longitude = longitude;// 经度
                    /** 初始化附近 */
                    $scope.near();

                    // 每次数据加载完，必须重置
                    me.resetload();
                });
            }
        });


			 // btn-filter-bar


		 /**
		  *
		  * 附近的秘密
		  *
		  */
		 $scope.near = function(){
             $('#my-actions').modal('close');
             // 获取附近的秘密
             faceinner.get(api['secret.near'],point, function(res){
                 $scope.$apply(function(){

                     $.each(res.data,function(i,item){
                         $scope.secretList.push(item);
                     })

                     console.log(res);
                 });
             });
		 }




	}];

});