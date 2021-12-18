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
                data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    data: [820, 932, 901, 934, 1290, 1330, 1320, 932, 901, 934, 1290, 1330, 1320, 932, 901, 934, 1290, 1330, 1320,10,100,200,300,500,50,20,0,0,10,10,50,1290,1290],
                    type: 'line',
                    areaStyle: {}
                }
            ]
        };

        if (option && typeof option === 'object') {
            myChart.setOption(option);
        }


    }];


	return callback;
});
