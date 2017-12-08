/**
 *
 * 平台登录模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','css!./app.css'], function (app) {// 加载依赖js,
	
	return ['$scope','$location', function ($scope, $location  ) {
             $scope.entity = {};
            // 百度地图API功能
             var map = new BMap.Map("allmap");

             //单击获取点击的经纬度
             //map.addEventListener("click",function(e){
             //    alert(e.point.lng + "," + e.point.lat);
             //});

             var geolocation = new BMap.Geolocation();
             geolocation.getCurrentPosition(function(r){
                 if(this.getStatus() == BMAP_STATUS_SUCCESS){
                     var mk = new BMap.Marker(r.point);
                     map.addOverlay(mk);
                     map.centerAndZoom(r.point, 15);
                     //map.panTo(r.point);
                     $scope.entity.longitude = r.point.lng;
                     $scope.entity.latitude  = r.point.lat;
                 }
                 else {
                     alert('failed'+this.getStatus());
                 }
             },{enableHighAccuracy: true})


				function getLocation(callback){
					if(navigator.geolocation){
						navigator.geolocation.getCurrentPosition(
							function(p){
								callback(p.coords.latitude, p.coords.longitude);
							},
							function(e){
								var msg = e.code + "\n" + e.message;
								console.log(msg)
							}
						);
					}
				}
				getLocation(function(latitude,longitude){
					$scope.$apply(function(){
						$scope.entity.longitude = longitude;
					 	$scope.entity.latitude  = latitude;
                        $scope.locationStr = longitude+","+latitude;

					});
				});


			 /**
			  *
			  * 发布秘密
			  *
			  */
			 $scope.save = function(){

				 faceinner.post(api['secret.publish'], $scope.entity , function(res){
					 if(res.status == 0){
                         alert("发布成功");
                         window.location.href ='/mobi/near.html';
					 }
					 faceinner.handleFieldError($scope, res);
				 });
			 }


             /**
              * 移除百度地图logo
              */
             function removeBaiduLogo(){
                 if($('.anchorBL').length > 0){
                     $('.anchorBL').remove(); return ;
                 }
                 setTimeout(removeBaiduLogo, 100);
             }
             removeBaiduLogo();
	}];

});