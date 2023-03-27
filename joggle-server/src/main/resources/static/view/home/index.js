/**
 *
 * Home 主页 模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','echarts','css!./index.css'], function (app, echarts) {//加载依赖js,
	var callback = ["$scope", function ($scope) {
        $scope.countInfo = {
            deviceNum: 0,
            deviceOnlineNum: 0,
            domainNum: 0,
            portNum: 0,
            tunnelNum: 0,
            userNum: 0
        }

		// 校验是否登录

        // 加载用户登录信息
        faceinner.get(api['user.login.info'], function(res){
            if(res.code == '040006'){ // 没有登录
                if(localStorage.token){
                    window.location.href='#/login';
                }
            }
        });

        // 统计数据
        faceinner.get(api['statistics'], function(res){
            if (res.code != 'S00') {
                return;
            }
            $scope.$apply(function(){
                $scope.countInfo = res.data;
            });

            //  数字的动态效果
            $('#index .counter-value').each(function(){
                $(this).prop('Counter',0).animate({
                    Counter: $(this).text()
                },{
                    duration: 1500,
                    easing: 'swing',
                    step: function (now){
                        $(this).text(Math.ceil(now));
                    }
                });
            });
        });




        var dom = document.getElementById("contain2er");
        var myChart = echarts.init(dom);
        var app = {};

        var option;



        option = {
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: [],
                axisLine: {show:false},//不显示坐标轴
                axisTick:{
                    show:false,//不显示坐标轴刻度线
                },
                axisLabel : {
                    formatter: function(){
                        return "";
                    }
                }
            },
            yAxis: {
                type: 'value',
                splitLine: {
                    show: false
                },
                axisLabel : {
                    formatter: function(){
                        return "";
                    }
                }

            },
            series: [
                {
                    data: [],
                    type: 'line',
                    smooth: true,
                    areaStyle: {},
                    // 此系列自己的调色盘。
                    color: [
                        'rgb(164,192,171)'
                    ]
                }
            ]
        };


        let params ={
        }
        // 加载用户登录信息
        faceinner.get(api['dashboard.flow.trend'], params, function(res){
            $scope.$apply(function(){
                option.xAxis.data = [];
                option.series[0].data = [];
                for(let i =0; i<res.data.length; i++){
                    let item = res.data[i];
                    option.xAxis.data.push(item.time);
                    option.series[0].data.push(item.flow);
                }

                if (option && typeof option === 'object') {
                    myChart.setOption(option);
                }
            })
        });

        window.addEventListener('resize', function() {
            myChart.resize();
        });





    }];


	app.controller('IndexController', callback );
	return callback;
});
