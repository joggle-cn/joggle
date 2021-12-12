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




        var dom = document.getElementById("contain1er");
        // var myChart = echarts.init(dom);
        var app = {};

        var option;



        option = {
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [820, 932, 901, 934, 1290, 1330, 1320],
                    type: 'line',
                    areaStyle: {}
                }
            ]
        };

        // if (option && typeof option === 'object') {
        //     myChart.setOption(option);
        // }


    }];


	return callback;
});
