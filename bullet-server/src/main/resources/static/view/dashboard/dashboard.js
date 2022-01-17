/**
 *
 * Dashboard 模块
 *
 * @author marker
 * @date 2016-06-05
 */
define(['app','echarts','css!./dashboard.css'], function (app, echarts) {//加载依赖js,
	var callback = ["$scope", function ($scope) {
        $scope.statistics = {
            monthFlow: 0,
            monthFlowOn: 0,
            monthLink: 0,
            monthLinkOn: 0,
            todayFlow: 0,
            todayFlowOn:0,
            yearFlow: 0,
            yearFlowOn: 0,
        }
        $scope.deviceRank = [];
		// 校验是否登录

        // 加载用户登录信息
        faceinner.get(api['user.dashboard.statistics'], function(res){
            if(res.code == '040006'){ // 没有登录
                window.location.href='#/login';
            }
            $scope.$apply(function(){
                $scope.statistics = res.data;
            })
        });

        $scope.tab = 1;/*设置默认*/
        $scope.selectTab = function (setTab) {/*设置tab点击事件*/
            this.tab = setTab;

            let params ={
                type: setTab
            }

            // 加载用户登录信息
            faceinner.get(api['user.dashboard.device.rank'], params, function(res){
                if(res.code == '040006'){ // 没有登录
                    window.location.href='#/login';
                }
                $scope.$apply(function(){
                    $scope.deviceRank = res.data;
                })
            });
        };
        $scope.isSelected = function (checkedTab) {/*页面的切换*/
            return this.tab === checkedTab;
        }
        $scope.selectTab(1);




        var dom = document.getElementById("contain1er");
        var myChart = echarts.init(dom);
        var app = {};

        var option;



        option = {
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [],
                    type: 'line',
                    smooth: true,
                    areaStyle: {}
                }
            ]
        };


        let params ={
            deviceId: null,
        }
        // 加载用户登录信息
        faceinner.get(api['user.dashboard.device.trend'], params, function(res){
            if(res.code == '040006'){ // 没有登录
                window.location.href='#/login';
            }
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




    }];


	return callback;
});
