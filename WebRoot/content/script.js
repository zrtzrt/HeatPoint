$(function () {
    $(".form_date").datetimepicker({
        format: "yyyy/mm/dd",
        language: "zh-CN",
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });
    $(".showtype").bootstrapSwitch({
        onText: "启动",
        offText: "停止",
        onColor: "success",
        offColor: "info",
        size: "small",
        onSwitchChange: function (event, state) {
            $(this).val(state);
            search(event.target.id, state);
        }
    });
    $(".showall").bootstrapSwitch({
        onText: "启动",
        offText: "停止",
        onColor: "success",
        offColor: "info",
        size: "small",
        onSwitchChange: function (event, state) {
            $(this).val(state);
            if (state)
                for (var i = 0; i < 8; i++)
                    (function (i) {
                        setTimeout(function () {
                            $("#search_showtype_" + i).bootstrapSwitch('state', true);
//       					alert(i);
                        }, i * 500);
                    })(i);
//       		$(".showtype").bootstrapSwitch('state', true);
//       		$("#search_showtype_0").bootstrapSwitch('state', true);
            else
//       		for(var i=0;i<8;i++)
//      			 $("#search_showtype_"+i).bootstrapSwitch('state', false);
                $(".showtype").bootstrapSwitch('state', false);
//            setTimeout(search("_"+i,true),100);
        }
    });
    $("#search-btn").on("click", function () {
        getTime();
        var key = $("#search-input").val();
        if (key != null)
            $.ajax({
                url: "servlet/LocationServlet?action=5&key=" + key + "&start=" + start + "&end=" + end,
                dataType: "text",
                success: function (res) {
//					alert(res);
                    locatJson = JSON.parse(res);//  text/*
                    if (locatJson.size == 0)
                        alert("很抱歉，搜索不到你想要的内容！");
                    else {
                        for (var i = 0; i < 8; i++) {
                            deletePoint(i);
                            Overlay[i] = [];
                        }
                        index = 0;
                        showlocat();
                    }
                }
            });
    });
    $("#cleanAll-btn").on("click", function () {
        $("#search-input").val("");
        for (var i = 0; i < 8; i++)
            deletePoint(i);
    });
    $('#province-popover').popover({html: true});
    $('#province-popover').on('shown.bs.popover', function () {
        if (!hasGetP || !getTime())
            getProvince();
    });
//    $('#province-popover').on('hide.bs.popover', function () {
//    	for(var i=0;i<8;i++)
//    		deletePoint(i);
//    });
    showMap();
    $('#statistics-btn').on('click', function () {
        if (!hasStatistics)
            showStatistics();
    });
    sendInfo();
});

function sendInfo() {
    // $.ajax({url: "servlet/LocationServlet?action=9"});
}

var hasStatistics = false;

function showStatistics() {
    hasStatistics = true;
    $.ajax({
        url: "servlet/LocationServlet?action=6",
        async: false,
        dataType: "text",
        success: function (res) {
//			alert(res);
            var data = JSON.parse(res);
            for (var i = 0; i < data.series.length; i++) {
                data.series[i].type = 'bar3D';
                data.series[i].label = {
                    show: false,
                    textStyle: {
                        fontSize: 16,
                        borderWidth: 1
                    }
                };
                data.series[i].itemStyle = {
                    opacity: 0.4
                };
                data.series[i].emphasis = {
                    label: {
                        textStyle: {
                            fontSize: 20,
                            color: '#900'
                        }
                    },
                    itemStyle: {
                        color: '#900'
                    }
                };
            }
            var chart = echarts.init(document.getElementById("statistics-body"));
            chart.setOption({
                tooltip: {
                    formatter: function (params) {
                        return '日期: ' + new Date().getFullYear() + '年' + (params.value[1] + 1) + '月' + params.value[0] +
                            '日  \n 类型: ' + params.seriesName + '  \n 数量: ' + params.value[2];
                    }
                },
                legend: {
                    left: 'left',
                    data: ["展会", "演唱会", "音乐会", "话剧歌剧", "舞蹈芭蕾", "曲苑杂坛", "体育赛事", "会议"]
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    left: 'left',
                    top: 'center',
                    feature: {
                        dataView: {readOnly: false},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                visualMap: {
                    max: data.max,
                    text: ['多', '少'],           // 文本，默认为数值文本
                    calculable: true,
                    inRange: {
                        color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8',
                            '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
                    }
                },
                xAxis3D: {
                    name: '日期',
                    type: 'value'
                },
                yAxis3D: {
                    name: '月份',
                    type: 'category',
                    data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
                },
                zAxis3D: {
                    name: '数量',
                    type: 'value'
                },
                grid3D: {
                    boxWidth: 200,
                    boxDepth: 80,
                    light: {
                        main: {
                            intensity: 1.2
                        },
                        ambient: {
                            intensity: 0.3
                        }
                    }
                },
                series: data.series
            });

        }
    });
}

var typeName = ["展会", "演唱会", "音乐会", "话剧歌剧", "舞蹈芭蕾", "曲苑杂坛", "体育赛事", "会议"];

function showsmallmap(data) {
//	alert(data);
    var chart = echarts.init(document.getElementById("smallmap"));
//	chart.showLoading();
    chart.setOption({
        tooltip: {
            trigger: 'item'
        },
        legend: {
            left: 'left',
            data: typeName
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        visualMap: {
            max: data.max,
            text: ['多', '少'],           // 文本，默认为数值文本
            calculable: true,
            inRange: {
                color: ['lightskyblue', 'yellow', 'orangered']
            }
        },
        series: data.series
    });
    chart.on('click', function (params) {
        $("#province").val(params.name);
        for (var i = 0; i < 8; i++)
            deletePoint(i);
        for (var i = 0; i < 8; i++) {
//			getLocat(i,params.name);
            (function (i) {
                setTimeout(function () {
                    getLocat(i, params.name);
                }, i * 100);
            })(i);
        }
        map.setCenter(params.name);
        map.setZoom(8);
    });
}

var hasGetP = false;

function getProvince() {
    hasGetP = true;
    getTime();
    $.ajax({
        url: "servlet/LocationServlet?action=4&start=" + start + "&end=" + end,
        async: false,
        dataType: "text",
        success: function (res) {
//			alert(res);
            var data = JSON.parse(res);
            for (var i = 0; i < data.series.length; i++) {
                data.series[i].type = 'map';
                data.series[i].map = 'china';
                data.series[i].roam = true;
            }
            showsmallmap(data);
        }
    });
}

//function saveP(p,c,id){
//	$.ajax({url:"servlet/LocationServlet?action=5&id="+id+"&p="+p+"&c="+c});
//}
//function wait(){
//	while(doing){
//		
//	}
//}
var map;
var myChart;

function showMap() {
    myChart = echarts.init(document.getElementById("allmap"));
    myChart.setOption({
        bmap: {
            center: [104.114129, 37.550339],
            zoom: 5,
            roam: true,
            enableMapClick: false,
        }
    });
    map = myChart.getModel().getComponent('bmap').getBMap();
//	map = new BMap.Map("allmap",{enableMapClick:false});           // 创建Map实例
//	map.centerAndZoom("中国",5);
    map.enableScrollWheelZoom();
    //map.addControl(new BMap.ScaleControl());
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function (r) {
        if (this.getStatus() == BMAP_STATUS_SUCCESS) {
            var myIcon = new BMap.Icon("http://api0.map.bdimg.com/images/geolocation-control/point/position-icon-14x14.png"
                , new BMap.Size(14, 14));
            var mk = new BMap.Marker(r.point, {icon: myIcon});
            map.addOverlay(mk);
            //map.panTo(r.point);
        }
    }, {enableHighAccuracy: true});
    changeMap('default');
}

function back() {
    map.setCenter("中国");
    map.setZoom(5);
}

var myIcon;
var locatJson = "";
var t = -1;
var start = "";
var end = "";
var Overlay = [];
var index = 0;

//var windows=[];
function getTime() {
    var befs = start, befe = end;
    start = document.getElementById("startDate").value;
    var d;
    if (start == "开始时间" || start == null) {
        d = new Date();
        start = d.toLocaleDateString();
    }
    end = document.getElementById("endDate").value;
    if (end == "结束时间" || end == null) {
        d.setDate(new Date().getDate() + 30);
        end = d.toLocaleDateString();
    }
    if (befs != start || befe != end) {
        hasGetp = false;
        return false;
    }
//	alert(type+start+end);
}

var doing = false;

function getLocat(type, p) {
//	alert(type);
    doing = true;
    getTime();
    index = 0;
    if (type < 9) {
        var url = "servlet/LocationServlet?action=0&type=" + type + "&start=" + start + "&end=" + end;
        if (p)
            url += "&p=" + p;
        $.ajax({
            url: url,
            async: false,
            dataType: "text",
//						alert(res);
//						windows[type]=[];
            success: function (res) {
                locatJson = JSON.parse(res);//  text/*
                t = type;
                Overlay[type] = [];
                if (type < 8) {
                    for (var i = 0; i < locatJson.location.length; i++)
                        locatJson.location[i].type = type;
//							showScatter();
                    setIcon(type);
                    showlocat();
//						doing = false;
                } else {
                    weatheralarm();
                }
            }
        });
    } else if (type == 9) {
        var ind = 0;
        Overlay[9] = [];
        for (var i in geoCoordMap) {
            if (geoCoordMap[i][4] == "CN") {
                showAirport(i, geoCoordMap[i], ind);
                ind++;
            }
        }
        myChart.off('click');
        myChart.on('click', function (params) {
            var line = params.data;
//	  		alert(JSON.stringify(line));
            $.ajax({
                url: "servlet/LocationServlet?action=7&dep=" + getAirCode(line.fromName) + "&arr=" + getAirCode(line.toName) + "&start=" + start,
                dataType: "text",
                success: function (res) {
//					alert(res);
//					eval("var airplant="+res+";");
                    showAirDetail(line.fromName, line.toName, JSON.parse(res).data);
                }
            });
        });
    } else if (type == 10) {
        $.ajax({
            url: "servlet/LocationServlet?action=8&url=http://news.ceic.ac.cn/ajax/google",
            dataType: "text",
            success: function (res) {
                showeq(JSON.parse(res));
            }
        });
        myChart.off('click');
        myChart.on('click', function (params) {
            showeqinfo(params.name, params.value);
        });
    } else if (type == 11) {
        $.ajax({
            url: "servlet/LocationServlet?action=8&url=http://typhoon.zjwater.gov.cn/Api/TyphoonList/" + new Date().getFullYear(),
            dataType: "text",
            success: function (res) {
                showTyphoon(JSON.parse(res));
            }
        });
    } else if (type == 12) {
        $.ajax({
            url: "servlet/LocationServlet?action=8&url=http://typhoon.zjwater.gov.cn/Api/LeastCloud",
            dataType: "text",
            success: function (res) {
                showcloud(JSON.parse(res)[0]);
            }
        });
    } else if (type == 13) {
        $.ajax({
            // url: "content/all_cities.json",
            url: "servlet/LocationServlet?action=10",
            dataType: "text",
            success: function (res) {
                if(res==null||res=="{\"error\":\"Sorry，您这个小时内的API请求次数用完了，休息一下吧！\"}")
                    $.ajax({
                        url: "content/all_cities.json",
                        dataType: "text",
                        success: function (res) {
                            showAirQuality(JSON.parse(res));
                        }
                    });
                else showAirQuality(JSON.parse(res));
            }
        });
    }
}
var AirQuality;
var aqGPS;
function showAirQuality(res) {
    AirQuality = res;
    var index = 0;
    var points = [];  // 添加海量点数据
    var cityGPS = [];
    var data = [
        {onclick: "setHeatmap('aqi')", text: "空气质量指数"},
        {onclick: "setHeatmap('co')", text: "一氧化碳"},
        {onclick: "setHeatmap('co24')", text: "24小时CO"},
        {onclick: "setHeatmap('no2')", text: "二氧化氮"},
        {onclick: "setHeatmap('no224')", text: "24小时NO2"},
        {onclick: "setHeatmap('o3')", text: "臭氧"},
        {onclick: "setHeatmap('o38')", text: "8小时O3"},
        {onclick: "setHeatmap('o324')", text: "24小时O3"},
        {onclick: "setHeatmap('o3824')", text: "平均8小时O3"},
        {onclick: "setHeatmap('pm10')", text: "大颗粒物"},
        {onclick: "setHeatmap('pm1024')", text: "24小时PM10"},
        {onclick: "setHeatmap('pm25')", text: "小颗粒物"},
        {onclick: "setHeatmap('pm2524')", text: "24小时PM2.5"},
        {onclick: "setHeatmap('so2')", text: "二氧化硫"},
        {onclick: "setHeatmap('so224')", text: "24小时SO2"}
    ];
    addControl(data);
    $.ajax({
        url: "content/CitiesGPS.json",
        dataType: "text",
        success: function (gps) {
            cityGPS = JSON.parse(gps);
            aqGPS = cityGPS;
            for (var i in cityGPS) {
                points.push(cityGPS[i].point);
                // for(var j in res)
                //     if(res[j].area+res[j].position_name==cityGPS[i].city)
                //         cityGPS[i].code=res[j].station_code;
            }
            // console.log(JSON.stringify(cityGPS));
            showheatmap();
        }
    });

    // getpoints();

    function getpoints() {
        if (index < res.length && res[index]) {
            var cityName = res[index].area + res[index].position_name;
            index++;
            getgeo(cityName);
        }
        // else{
        //     console.log("finished:"+index-1);
        //     // showheatmap();
        // }

    }

    var all = 0;

    function getgeo(city) {
        // alert(city);
        var myGeo = new BMap.Geocoder();
        // alert(point);
        myGeo.getPoint(city, function (point) {
            if (point) {
                points.push(point);
                cityGPS.push({city: city, point: point});
                // gotpoint(point);
            } else {
                console.log(city + "not found");
            }
            all++;
            console.log(all);
            if (all > 1600) {
                console.log("finished");
                console.log(JSON.stringify(cityGPS));
                showheatmap();
            }
        });
        getpoints();
    }

    // function gotpoint(point){
    //     points.push(point);
    //     cityGPS.push({city:city, point:point});
    // }

    function showheatmap() {
        var options = {
            size: BMAP_POINT_SIZE_SMALL,
            shape: BMAP_POINT_SHAPE_STAR,
            color: '#d340c3'
        }
        var pointCollection = new BMap.PointCollection(points, options);  // 初始化PointCollection
        pointCollection.addEventListener('click', function (e) {
            alert(cityGPS[points.indexOf(e.point)].city);  // 监听点击事件
            showAQInfo(cityGPS[points.indexOf(e.point)].code);
        });
        map.addOverlay(pointCollection);  // 添加Overlay
        setHeatmap("aqi");
    }

    function showAQInfo(code) {

        var info;
        for (var i in res)
            if (res[i].station_code == code)
                info = res[i];
        var str = "<div class=\"panel panel-info\"><div id=\"info-aq\" class=\"panel-heading\"><h1 class=\"panel-title\">" + info.area;
        str += "空气质量详情</h1></div><div class=\"panel-body\">";
            str = str + "<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_aq\"><h3 class=\"panel-title\">";
            str = str + info.position_name + "监测点信息</h3></div><div id=\"info_body_aq\" class=\"panel-collapse collapse in\"> <div class=\"panel-body\">";
            str = str + "<ul class=\"list-group\"><li class=\"list-group-item\"><b>监测点编号:</b>" + info.station_code + "</li>";
            str = str + "<li class=\"list-group-item\"><b>空气质量指数:</b>" + info.aqi + "</li>";
            str = str + "<li class=\"list-group-item\"><b>空气质量等级:</b>" + info.quality + "</li>";
            if(info.primary_pollutant!=null)
            str = str + "<li class=\"list-group-item\"><b>主要污染物:</b>" + info.primary_pollutant + "</li>";
        str = str + "<li class=\"list-group-item\"><b>二氧化硫1小时平均:</b>" + info.so2 + "</li>";
        str = str + "<li class=\"list-group-item\"><b>二氧化硫24小时滑动平均:</b>" + info.so2_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>二氧化氮1小时平均:</b>" + info.no2 + "</li>";
        str = str + "<li class=\"list-group-item\"><b>二氧化氮24小时滑动平均:</b>" + info.no2_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>颗粒物（粒径小于等于10μm）1小时平均:</b>" + info.pm10 + "</li>";
        str = str + "<li class=\"list-group-item\"><b>颗粒物（粒径小于等于10μm）24小时滑动平均:</b>" + info.pm10_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>颗粒物（粒径小于等于2.5μm）1小时平均:</b>" + info.pm2_5 + "</li>";
        str = str + "<li class=\"list-group-item\"><b>颗粒物（粒径小于等于2.5μm）24小时滑动平均:</b>" + info.pm2_5_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>一氧化碳1小时平均:</b>" + info.co + "</li>";
        str = str + "<li class=\"list-group-item\"><b>一氧化碳24小时滑动平均:</b>" + info.co_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>臭氧1小时平均:</b>" + info.o3 + "</li>";
        str = str + "<li class=\"list-group-item\"><b>臭氧日最大1小时平均:</b>" + info.o3_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>臭氧8小时滑动平均:</b>" + info.o3_8h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>臭氧日最大8小时滑动平均臭氧日最大8小时滑动平均:</b>" + info.o3_8h_24h + "</li>";
        str = str + "<li class=\"list-group-item\"><b>数据发布的时间:</b>" + info.time_point + "</li>";
        str = str + "<li class=\"list-group-item\"><a href='http://www.pm25.in/"+ info.area + "' target=\\\"_blank\\\" class='btn btn-info'><b>查看详情</b></a></li></ul></div></div></div>";

        document.getElementById("info").innerHTML = str + "</div></div>";
    }


}
function setHeatmap(type) {
    var data = [];
    for (var i in aqGPS) {
        var count = 0;
        var max = 0;
        for (var j in AirQuality)
            if (AirQuality[j].station_code == aqGPS[i].code) {
                if (type == 'aqi')
                    count = AirQuality[j].aqi;
                else if (type == 'co')
                    count = AirQuality[j].co;
                else if (type == 'co24')
                    count = AirQuality[j].co_24h;
                else if (type == 'no2')
                    count = AirQuality[j].no2;
                else if (type == 'no224')
                    count = AirQuality[j].no2_24h;
                else if (type == 'o3')
                    count = AirQuality[j].o3;
                else if (type == 'o324')
                    count = AirQuality[j].o3_24h;
                else if (type == 'o38')
                    count = AirQuality[j].o3_8h;
                else if (type == 'o3824')
                    count = AirQuality[j].o3_8h_24h;
                else if (type == 'pm10')
                    count = AirQuality[j].pm10;
                else if (type == 'pm1024')
                    count = AirQuality[j].pm10_24h;
                else if (type == 'pm25')
                    count = AirQuality[j].pm2_5;
                else if (type == 'pm2524')
                    count = AirQuality[j].pm2_5_24h;
                else if (type == 'so2')
                    count = AirQuality[j].so2;
                else if (type == 'so224')
                    count = AirQuality[j].so2_24h;
                if(count>max)
                    max=count;
            }
        data.push([aqGPS[i].point.lng, aqGPS[i].point.lat, count]);

    }

    myChart.setOption({
        animation: false,
        visualMap: {
            show: false,
            top: 'top',
            min: 0,
            max: max,
            seriesIndex: 0,
            calculable: true,
            inRange: {
                color: ['blue', 'blue', 'green', 'yellow', 'red']
            }
        },
        series: [{
            type: 'heatmap',
            coordinateSystem: 'bmap',
            data: data,
            pointSize: 8,
            blurSize: 20,
            maxOpacity:0.65
        }]
    });

}


var cloud;
var control;

function showcloud(c) {
    cloud = c;
    // 西南角和东北角
    var SW = new BMap.Point(80, 2);
    var NE = new BMap.Point(140, 45);
    // 初始化GroundOverlay
    var groundOverlay = new BMap.GroundOverlay(new BMap.Bounds(SW, NE));
    // 设置GroundOverlay的图片地址
    groundOverlay.setImageURL(c.cloudFullPath + "/" + c.cloudname);
    map.addOverlay(groundOverlay);
    Overlay[12] = groundOverlay;
    var data = [
        {onclick: "changcloud(0)", text: "现在"},
        {onclick: "changcloud(1)", text: "1小时前"},
        {onclick: "changcloud(2)", text: "3小时前"},
        {onclick: "changcloud(3)", text: "6小时前"},
    ];
    addControl(data);
}

function addControl(data) {
// 定义一个控件类,即function
    function ZoomControl() {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
        this.defaultOffset = new BMap.Size(10, 10);
    }

// 通过JavaScript的prototype属性继承于BMap.Control
    ZoomControl.prototype = new BMap.Control();
// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
    ZoomControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        var div = document.createElement("div");
        // 添加文字说明
        var str = "<div class=\"btn-group\">";
        for (var i = 0; i < data.length; i++) {
            str += "<button type=\"button\" class=\"btn btn-default\" onclick=\"" + data[i].onclick + "\">" + data[i].text + "</button>";
        }
        str += "</div>";
        div.innerHTML = str;
        // 添加DOM元素到地图中
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    };
// 创建控件
    control = new ZoomControl();
// 添加到地图当中
    map.addControl(control);

}

function changcloud(i) {
    if (i == 0)
        Overlay[12].setImageURL(cloud.cloudFullPath + "/" + cloud.cloudname);
    if (i == 1)
        Overlay[12].setImageURL(cloud.cloudFullPath + "/" + cloud.cloud1h);
    if (i == 2)
        Overlay[12].setImageURL(cloud.cloudFullPath + "/" + cloud.cloud3h);
    if (i == 3)
        Overlay[12].setImageURL(cloud.cloudFullPath + "/" + cloud.cloud6h);
}

var mapstyle = 'HYBRID';

function changeMap(style) {
    if (style == 'default')
        changeMap(mapstyle);
    else if (style == 'SATELLITE') {
        map.setMapType(BMAP_SATELLITE_MAP);
        mapstyle = style;
    }
    else if (style == 'HYBRID') {
        map.setMapType(BMAP_HYBRID_MAP);
        mapstyle = style;
    } else {
        map.setMapType(BMAP_NORMAL_MAP);
        map.setMapStyle({style: style});
        mapstyle = style;
    }
}

function showTyphoon(tl) {
    Overlay[11] = [];
    var str = "<div class=\"panel panel-info\"><div id=\"info-ph\" class=\"panel-heading\"><h1 class=\"panel-title\">" + new Date().getFullYear();
    str += "年发生的所有台风<span class='infosize'>（目前共有" + tl.length + "个）</span></h1></div><div class=\"panel-body\">";
    for (var i = tl.length - 1; i >= 0; i--) {
        str = str + "<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_" + i + "\"><h3 class=\"panel-title\">第";
        var isin = "";
        if (tl[i].isactive == 1) {
//			showTyphoonLine(tl[i].tfid);
            isin = " in ";
        }
        str = str + (i + 1) + "号台风“" + tl[i].name + "”</h3></div><div id=\"info_body_" + i + "\" class=\"panel-collapse collapse" + isin + "\"> <div class=\"panel-body\">";
        str = str + "<ul class=\"list-group\"><li class=\"list-group-item\"><b>台风名称:</b>" + tl[i].name + "</li>";
        str = str + "<li class=\"list-group-item\"><b>英文名称:</b>" + tl[i].enname + "</li>";
        str = str + "<li class=\"list-group-item\"><b>生成时间:</b>" + tl[i].starttime + "</li>";
        str = str + "<li class=\"list-group-item\"><b>结束时间:</b>" + tl[i].endtime + "</li>";
        str = str + "<li class=\"list-group-item\"><b>显示路径:</b><input id=\"Typhoon_" + tl[i].tfid + "\" type=\"checkbox\" class=\"Typhoon\"/></li></ul></div></div></div>";
    }
    document.getElementById("info").innerHTML = str + "</div></div>";
    $(".Typhoon").bootstrapSwitch({
        onText: "启动",
        offText: "停止",
        onColor: "success",
        offColor: "info",
        size: "small",
        onSwitchChange: function (event, state) {
            $(this).val(state);
            var id = event.target.id.lastIndexOf('_');
            id = event.target.id.substr(id + 1, 6);
            if (state)
                showTyphoonLine(id);
            else
                for (var i = 0; i < Overlay[11][id].length + 1; i++) {
                    map.removeOverlay(Overlay[11][id][i]);
                }
        }
    });
    for (var i = tl.length - 1; i >= 0; i--) {
        if (tl[i].isactive == 1) {
            $("#Typhoon_" + tl[i].tfid).bootstrapSwitch('state', true);
        }
    }
}

function showTyphoonLine(tid) {
    $.ajax({
        url: "servlet/LocationServlet?action=8&url=http://typhoon.zjwater.gov.cn/Api/TyphoonInfo/" + tid,
        dataType: "text",
        success: function (res) {
            res = JSON.parse(res)[0];
            var pl = [];
            for (var i = 0; i < res.points.length; i++) {
                pl[i] = new BMap.Point(res.points[i].lng, res.points[i].lat);
//				if(res.isactive==0&&res.points[i].forecast.length>0){
//					showforecast(res.points[i].forecast);
//				}
            }
            Overlay[11][tid] = [];
            Overlay[11][tid][0] = new BMap.Polyline(pl);
            var ico = new BMap.Marker(pl[0], {icon: new BMap.Icon("http://typhoon.zjwater.gov.cn/images/typhoon.gif", new BMap.Size(40, 40))});
            Overlay[11][tid][1] = ico;
            for (var i = 0; i < res.land.length; i++) {
                Overlay[11][tid][i + 2] = new BMap.Label(res.land[i].info, {
                    position: new BMap.Point(res.land[i].lng, res.land[i].lat),
                    offset: new BMap.Size(-250, 20)
                });
                Overlay[11][tid][i + 2].setStyle({color: "white"});
            }
            for (var i = 0; i < res.points.length; i++) {
                var circle = new BMap.Circle(pl[i], res.points[i].power * 5000, {
                    fillColor: getcolor(res.points[i].strong),
                    strokeWeight: 2
                });
                circlelabel(circle, res.points[i]);
                Overlay[11][tid][i + 2 + res.land.length] = circle;
            }
            if (res.isactive == 1 && res.points[res.points.length - 1].forecast.length > 0) {
                for (var i = 0; i < res.points[res.points.length - 1].forecast.length; i++) {
                    showforecast(res.points[res.points.length - 1].forecast[i], tid, res.name);
                }
            }
            for (var i = 0; i < Overlay[11][tid].length; i++) {
                map.addOverlay(Overlay[11][tid][i]);
            }

            function resetMkPoint(i) {
                ico.setPosition(pl[i]);
                if (i < pl.length) {
                    setTimeout(function () {
                        i++;
                        resetMkPoint(i);
                    }, 200);
                } else if (res.isactive == 0) {
                    map.removeOverlay(ico);
                }
            }

            setTimeout(function () {
                resetMkPoint(1);
            }, 200);
        }
    });
}

function showforecast(fc, id, name) {
    var pl = [];
    for (var i = 0; i < fc.forecastpoints.length; i++) {
        pl[i] = new BMap.Point(fc.forecastpoints[i].lng, fc.forecastpoints[i].lat);
        if (i != 0) {
            var p = new BMap.Marker(pl[i]);
            var time = fc.forecastpoints[i].time.split(' ');
            var str = "<div><ul class=\"list-group\"><li class=\"list-group-item\"><b>台风名称:</b>"
                + name + "</li><li class=\"list-group-item\"><b>发生日期:</b>"
                + time[0] + "</li><li class=\"list-group-item\"><b>发生时间:</b>" + time[1] + "</li>";
            if (fc.forecastpoints[i].pressure.length > 0) {
                str += "<li class=\"list-group-item\"><b>中心气压:</b>" + fc.forecastpoints[i].pressure + "百帕</li>";
            }
            if (fc.forecastpoints[i].speed.length > 0) {
                str += "<li class=\"list-group-item\"><b>最大风速:</b>" + fc.forecastpoints[i].speed + "米每秒</li>";
            }
            if (fc.forecastpoints[i].power.length > 0) {
                str += "<li class=\"list-group-item\"><b>风力:</b>" + fc.forecastpoints[i].power + "级</li>";
            }
            if (fc.forecastpoints[i].strong.length > 0) {
                str += "<li class=\"list-group-item\"><b>等级:</b>" + fc.forecastpoints[i].strong + "</li>";
            }
            str += "<li class=\"list-group-item\"><b>预报来自:</b>" + fc.tm + "</li></div>";
            var infoWindow = new BMap.InfoWindow(str, {width: 0, height: 0});
            p.addEventListener("click", function (e) {
                this.openInfoWindow(infoWindow);
            });
            Overlay[11][id][Overlay[11][id].length] = p;
        }
    }
    var sc;
    if (fc.tm == "中国")
        sc = "red";
    else if (fc.tm == "中国香港")
        sc = "orange";
    else if (fc.tm == "中国台湾")
        sc = "pink";
    else if (fc.tm == "日本")
        sc = "green";
    else if (fc.tm == "美国")
        sc = "blue";
    Overlay[11][id][Overlay[11][id].length] = new BMap.Polyline(pl, {strokeColor: sc, strokeStyle: "dashed"});
}

function circlelabel(cir, data) {
    var time = data.time.split(' ');
    var str = "<div class='lable'><ul class=\"list-group\"><li class=\"list-group-item\"><b>发生日期:</b>"
        + time[0] + "</li><li class=\"list-group-item\"><b>发生时间:</b>"
        + time[1] + "</li><li class=\"list-group-item\"><b>中心气压:</b>"
        + data.pressure + "百帕</li><li class=\"list-group-item\"><b>最大风速:</b>"
        + data.speed + "米每秒</li><li class=\"list-group-item\"><b>风力:</b>"
        + data.power + "级</li><li class=\"list-group-item\"><b>等级:</b>"
        + data.strong + "</li><li class=\"list-group-item\"><b>移动速度:</b>"
        + data.movespeed + "公里每小时</li><li class=\"list-group-item\"><b>移动方向:</b>"
        + data.movedirection + "</li>";
    if (data.radius7.length > 0) {
        str += "<li class=\"list-group-item\"><b>七级半径:</b>" + data.radius7 + "公里</li>";
        if (data.radius10.length > 0) {
            str += "<li class=\"list-group-item\"><b>十级半径:</b>" + data.radius10 + "公里</li>";
        }
    }
    str += "</ul></div>";
    var label = new BMap.Label(str, {position: new BMap.Point(data.lng, data.lat), offset: new BMap.Size(30, -300)});
    label.setStyle({
        border: "0", opacity: "0.8"
    });
    cir.addEventListener("mouseover", function (e) {
        map.addOverlay(label);
    });
    cir.addEventListener("mouseout", function (e) {
        map.removeOverlay(label);
    });
}

function getcolor(strong) {
    if (strong == "热带低压")
        return "green";
    else if (strong == "热带风暴")
        return "blue";
    else if (strong == "强热带风暴")
        return "yellow";
    else if (strong == "台风")
        return "orange";
    else if (strong == "强台风")
        return "pink";
    else if (strong == "超强台风")
        return "red";
}

function showeq(eq) {
    var data = [];
    for (var i = 0; i < eq.length; i++) {
        data.push({
            name: eq[i].LOCATION_C,
            value: [eq[i].EPI_LON, eq[i].EPI_LAT, eq[i].M, eq[i].EPI_DEPTH, eq[i].O_TIME, eq[i].CATA_ID]
        });
    }
    myChart.setOption({
//		tooltip : {},
        series: [
            {
                name: '地震',
                type: 'effectScatter',
                coordinateSystem: 'bmap',
                data: data,
                symbolSize: function (val) {
                    return val[2] * 5;
                },
                showEffectOn: 'render',
                rippleEffect: {
                    brushType: 'stroke'
                },
                hoverAnimation: true,
                itemStyle: {
                    normal: {
                        color: "red",
                        shadowBlur: 10,
                        shadowColor: '#333'
                    }
                },
                zlevel: 1
            }
        ]
    });
}

function showeqinfo(name, val) {
    var str = "<div class=\"panel panel-info\"><div class=\"panel-heading\"><h1 class=\"panel-title\">" + name
        + "发生" + val[2] + "级地震</h1></div><div class=\"panel-body\"><ul class=\"list-group\"><li class=\"list-group-item\"><b>地震位置:</b>"
        + name + "</li><li class=\"list-group-item\"><b>地震级别:</b>"
        + val[2] + "</li><li class=\"list-group-item\"><b>震源深度:</b>" + val[3] + "</li><li class=\"list-group-item\"><b>地震时间:</b>" + val[4] +
        "</li><li class=\"list-group-item\"><a href=\"http://news.ceic.ac.cn/"
        + val[5] + ".html\" target=\"_blank\" class='btn btn-info'><b>查看详情</b></a></li></ul></div></div>";
    document.getElementById("info").innerHTML = str;
}

//var series = [{
////	name: typeName[0],
//    type: 'effectScatter',
//    coordinateSystem: 'bmap',
//    hoverAnimation: true,
//    itemStyle: {
//        normal: {
////            color: 'purple',
//            shadowBlur: 10,
//            shadowColor: '#333'
//        }
//    }
//}];

//function showScatter(){
//	series[0].name=typeName[locatJson.location[0].type];
//	series[0].data=convertData();
//	series[0].
//	myChart.setOption({
//		series : series
//	});
//}
//function convertData(){
//	var data;
//	for(var i in locatJson.location){
//		data[i].name = locatJson.location[i].name;
//		data[i].value = [locatJson.location[i].x,locatJson.location[i].y,locatJson.location[i].hot];
//	}
//	return data;
//}
function showAirport(name, add, ind) {
    var myIcon = new BMap.Icon("img/airport.png", new BMap.Size(32, 37));
    myIcon.setAnchor(new BMap.Size(16, 37));
    var marker = new BMap.Marker(new BMap.Point(add[0], add[1]),
        {icon: myIcon, title: add[2]});
    Overlay[9][ind] = marker;
    map.addOverlay(marker);
    marker.setAnimation(BMAP_ANIMATION_DROP);
    marker.addEventListener("click", function (e) {
        for (var i in Overlay[9]) {
            Overlay[9][i].setAnimation(null);
        }
        map.setCenter(marker.getPosition());
        this.setAnimation(BMAP_ANIMATION_BOUNCE);
        getTime();
        $.ajax({
            url: "servlet/LocationServlet?action=7&arr=" + name + "&start=" + start,
            dataType: "text",
            success: function (res) {
//				alert(res);
                drawLine(JSON.parse(res).data);
            }
        });
    });
}

function getAirPort(id) {
    for (var i in geoCoordMap) {
        if (i == id) {
            return geoCoordMap[i];
        }
    }
}

function getAirCode(name) {
    for (var i in geoCoordMap) {
        if (geoCoordMap[i][2] == name) {
            return i;
        }
    }
}

function drawLine(airline) {
    var data = [];
    for (var i = 0; i < airline.length; i++) {
        var fromPort = getAirPort(airline[i][0]);
        var toPort = getAirPort(airline[i][1]);
        // alert(airline[i][0]+fromPort);
        var fromCoord = [fromPort[0], fromPort[1]];
        var toCoord = [toPort[0], toPort[1]];
        data.push({
            fromName: fromPort[2],
            toName: toPort[2],
            coords: [fromCoord, toCoord]
        });
    }
    var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';
    myChart.setOption({
        tooltip: {
            position: function (point, params, dom, rect, size) {
                return point;
            }
        },
        series: [{
            type: 'lines',
            coordinateSystem: 'bmap',
            zlevel: 1,
            effect: {
                show: true,
                period: 6,
                trailLength: 0.7,
                color: '#fff',
                symbolSize: 3
            },
            lineStyle: {
                normal: {
                    color: '#a6c84c',
                    width: 0,
                    curveness: 0.2
                }
            },
            data: data
        },
            {
                type: 'lines',
                coordinateSystem: 'bmap',
                zlevel: 2,
                symbol: ['none', 'arrow'],
                symbolSize: 10,
                effect: {
                    show: true,
                    period: 6,
                    trailLength: 0,
                    symbol: planePath,
                    symbolSize: 15
                },
                lineStyle: {
                    normal: {
                        color: '#a6c84c',
                        width: 1,
                        opacity: 1,
                        curveness: 0.2
                    }
                },
                data: data
            }]
    });
}

function setIcon(type) {
    var size = new BMap.Size(32, 37);
    if (type == 0)
        myIcon = new BMap.Icon("img/trade.png", size);
    if (type > 0 && type < 3)
        myIcon = new BMap.Icon("img/music.png", size);
    if (type > 2 && type < 6)
        myIcon = new BMap.Icon("img/show.png", size);
    if (type == 6)
        myIcon = new BMap.Icon("img/sport.png", size);
    if (type == 7)
        myIcon = new BMap.Icon("img/meet.png", new BMap.Size(24, 24));
    var a = new BMap.Size(16, 37);
    myIcon.setAnchor(a);
    myIcon.setInfoWindowAnchor(a);
}

function weatheralarm() {
//	var add = locatJson.weatheralarm[index];
    var add = locatJson.data[index];
    geoweather(add);
    index++;
}

function geoweather(add) {
//	if(index < locatJson.weatheralarm.length){
//		setTimeout(weatheralarm,1);
//	}
    var tl;
    if (index < locatJson.count - 1) {
        setTimeout(weatheralarm, 1);
    }
//	var ind=add.file.lastIndexOf('-');
//	var gif=add.file.substr(ind+1,4);
//	add.index=index;
//	myGeo.getPoint(add.locat, function(point){
//		if (point){
//			if(gif.substr(0,1)=="9")
//				gif="0000";
//			myIcon = new BMap.Icon("http://www.weather.com.cn/m2/i/alarm_n/"+gif+".gif", new BMap.Size(32,37));
//			var marker=new BMap.Marker(point,{icon:myIcon,title:add.name});
//			Overlay[8][add.index]=marker;
//			map.addOverlay(marker);
//			marker.addEventListener("click",function(e){getalarm(add,gif);});
//		}
    var ind = add[1].lastIndexOf('-');

    var gif = add[1].substr(ind + 1, 4);
    add[2] = index;
//	});
    var myGeo = new BMap.Geocoder();
    myGeo.getPoint(add[0], function (point) {
        if (point) {
            if (gif.substr(0, 1) == "9")
                myIcon = new BMap.Icon("http://www.weather.com.cn/m2/i/alarm_n/0000.gif", new BMap.Size(32, 37));
            myIcon = new BMap.Icon("http://www.weather.com.cn/m2/i/alarm_n/" + gif + ".gif", new BMap.Size(32, 37));
            var marker = new BMap.Marker(point, {icon: myIcon, title: add[0]});
            Overlay[8][add[2]] = marker;
            map.addOverlay(marker);
            marker.addEventListener("click", function (e) {
                getalarm(add, gif);
            });
        }
    });
}

var tl = ["台风", "暴雨", "暴雪", "寒潮", "大风", "沙尘暴", "高温", "干旱", "雷电", "冰雹", "霜冻", "大雾", "霾", "道路结冰"];
var tl9 = ["寒冷", "灰霾", "雷雨大风", "森林火险", "降温", "道路冰雪", "干热风", "空气重污染", "低温"];
var cl = ["蓝色", "黄色", "橙色", "红色", "白色"];

function getalarm(add, gif) {
//	var str = "<div class=\"panel panel-info\"><div class=\"panel-heading\"><h1 class=\"panel-title\">"+add.type
//		+"预警</h1></div><div class=\"panel-body\"><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
//		+add.file+"\" class=\"thumbnail\" target=\"_blank\"><img src=\"http://www.weather.com.cn/m2/i/alarm_n/"+gif+".gif\"></a><h3><b>"
//		+add.locat+"发布"+add.color+add.type+"预警</b></h3><div><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
//		+add.file+"\" target=\"_blank\" class='btn btn-info'><b>查看详情</b></a></div></div></div>";
    var type;
    if (gif.substr(0, 1) == "9") {
        type = tl9[Number(gif.substr(1, 1))];
        alert(gif.substr(1, 2) + type);
    } else type = tl[Number(gif.substr(0, 2))];

    var str = "<div class=\"panel panel-info\"><div class=\"panel-heading\"><h1 class=\"panel-title\">" + type
        + "预警</h1></div><div class=\"panel-body\"><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
        + add[1] + "\" class=\"thumbnail\" target=\"_blank\"><img src=\"http://www.weather.com.cn/m2/i/alarm_n/" + gif + ".gif\"></a><h3><b>"
        + add[0] + "发布" + cl[Number(gif.substr(2, 2))] + type + "预警</b></h3><div><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
        + add[1] + "\" target=\"_blank\" class='btn btn-info'><b>查看详情</b></a></div></div></div>";

    document.getElementById("info").innerHTML = str;
}

function showlocat() {
    var add = locatJson.location[index];
    if (typeof(index) == "undefined")
        index = 0;
    add.index = index;
    index++;
    geocodeSearch(add);
//		add.type=t;
}

function geocodeSearch(add) {
    if (add.index < locatJson.location.length) {
        setTimeout(showlocat, 1);
    }
    var marker;
//		add.index=index;
    if (t != add.type)
        setIcon(add.type);
//		if(!add.x)
//			marker = geo(add);
//		else
    marker = new BMap.Marker(new BMap.Point(add.x, add.y),
        {icon: myIcon, title: add.name});
//				if(add.x&&add.x>0){
//					marker=new BMap.Marker(new BMap.Point(add.x, add.y),
//						{icon:myIcon,title:add.name});
//				}else geo(add,marker);
    Overlay[add.type][add.index] = marker;
    map.addOverlay(marker);
    marker.setAnimation(BMAP_ANIMATION_DROP);
    var infoWindow = new BMap.InfoWindow(add.name + "<div id='windows-" +
        add.type + "-" + add.index + "' class='infowindow'></div>");
//				windows[t][index]=infoWindow;
//				infoWindow.addEventListener("open",function(e){
//					getInfo(add.id,e.target.getTitle(),type,add.index);
//				});
    marker.addEventListener("click", function (e) {
//										cleanAnimation();
        this.openInfoWindow(infoWindow);
        map.setZoom(15);
        map.setCenter(marker.getPosition());
        this.setAnimation(BMAP_ANIMATION_BOUNCE);
        getInfo(add.id, e.target.getTitle(), add.type, add.index);
    });
    marker.addEventListener("infowindowclose", function (e) {
        this.setAnimation(null);
        map.setZoom(8);
    });
}

function geo(add) {
//	alert("point"+add.id+add.name+add.x);
    var myGeo = new BMap.Geocoder();
    if (add.name == "null") {
        return;
    }
//									add.index=index;
    myGeo.getPoint(add.name, function (point) {
        if (point) {
            var marker = new BMap.Marker(point, {icon: myIcon, title: add.name});
//											Overlay[add.type][add.index]=marker;
            //p.add(marker.getPosition());  //获取marker的位置
            var p = marker.getPosition();
            // setLocat(p.lng, p.lat, add.id);
            //alert(p.lng+","+p.lat+","+add.id);
            //setTimeout(function () {waitForConnection(callback, interval);}, interval);
            return marker;
        }
//										else{
//											alert(add.id+add.name+"您选择地址没有解析到结果!");
//											$.ajax({
//												url:"servlet/LocationServlet?action=3&id="+add.id,
//												dataType:"text",
//												success:function(res){
////													alert(res);
//													var j=JSON.parse(res);
//													if(j.det){
//														add.name=j.det;
////														geocodeSearch(add);
//													}else{
////														add.x=j.x;
////														add.y=j.y;
////														geocodeSearch(add);
//													}
//												}
//											});
//										}
    });

}

//function cleanAnimation(){
//	for(var i=0; i<Overlay.size; i++)
//		for(var j=0; j<Overlay[i].size; j++)
//			if(Overlay[i][j])
//				Overlay[i][j].setAnimation(null);
//}
function setLocat(lng, lat, id) {
    $.ajax({url: "servlet/LocationServlet?action=1&lng=" + lng + "&lat=" + lat + "&id=" + id});
}

function getInfo(id, name, type, ind) {
    $.ajax({
        url: "servlet/LocationServlet?action=2&id=" + id + "&type=" + type + "&start=" + start + "&end=" + end,
        dataType: "text",
        success: function (res) {
//			alert(res);
            var InfoJson = JSON.parse(res);//  text/*
            showInfo(name, InfoJson);
            showWindow(type, ind, InfoJson);
        }
    });
}

function showWindow(type, ind, InfoJson) {
//	windows[type][ind].setContent("hello");
//	alert("windows-"+type+"-"+ind);
    echarts.init(document.getElementById("windows-" + type + "-" + ind)).setOption(
        {
            tooltip: {
                position: 'top',
                formatter: function (params) {
                    return '日期: ' + params.value[0];
                }
            },
            visualMap: {
                min: 0,
                max: 1,
                show: false,
                inRange: {
                    color: ['white', 'green']
                }
            },
            calendar: {
//	                   orient: 'vertical',
                monthLabel: {
                    nameMap: 'cn'
                },
                dayLabel: {
                    firstDay: 1,
                    nameMap: 'cn'
                },
                range: [start, end],
                cellSize: ['auto', 'auto']
            },
            series: {
                type: 'heatmap',
                coordinateSystem: 'calendar',
                calendarIndex: 0,
                data: toData(InfoJson)
            }
        }
    );
}

function toData(json) {
    var data = [];
    for (var i = 0; i < json.size; i++) {
        data.push([json.show[i].startDate, 1]);
    }
    return data;
}

function showAirDetail(dep, arr, res) {
//	res=res.data;
//	alert(res[0][2]);
    var str = "<div class=\"panel panel-info\"><div id=\"info-ph\" class=\"panel-heading\"><h1 class=\"panel-title\">"
        + start + "从" + dep + "到" + arr + "的所有航班";
    str += "</h1></div><div class=\"panel-body\">";
    var showed = [];
    for (var i = 0; i < res.length; i++) {
        var show = true;
        // for (var j = 0; j < i; j++) {
        //     if (res[i][0] == res[j][0] || res[i][0] == res[j][1] || res[i][1] == res[j][1])
        //         show = false;
        // }
        for (var j = 0; j < showed.length; j++) {
            if (res[i][2][0] == showed[j][0] && res[i][2][1] == showed[j][1])
                show = false;
        }
//		if($("#airplant-"+res[i][2]).exist()){
//			var str = "<li class=\"list-group-item\"><b>航班代码:</b>"+res[i][0]+"</li>";
//			if(res[i][1]!="")
//				str += "<li class=\"list-group-item\"><b>航班代码:</b>"+res[i][1]+"</li>";
//			$("#airplant-"+res[i][2]).appendChild(str);
        if (show) {
            if (i >= 1)
                str += "</ul></div></div>";
//		}else{
            str = str + "<div class=\"panel panel-default\"><div class=\"panel-heading\"><h3 class=\"panel-title\">"
                + res[i][2] + "</h3></div><div class=\"panel-body\"><ul id=\"airplant-" + res[i][2] +
                "\" class=\"list-group\"><li class=\"list-group-item\"><img src=\"https://res.tianxun.com/flight/images/airline_large/"
                + res[i][1].substring(0, 2) + ".png\"><span class=\"aircode\">" + res[i][1] + "</span></li>";

            // if (res[i][1] != "")
            showed.push(res[i][2]);
            //     str += "<li class=\"list-group-item\"><img src=\"https://res.tianxun.com/flight/images/airline_large/"
            //         + res[i][1].substring(0, 2) + ".png\"><span class=\"aircode\">" + res[i][1] + "</span></li>";
        } else {
            str += "<li class=\"list-group-item\"><img src=\"https://res.tianxun.com/flight/images/airline_large/"
                + res[i][1].substring(0, 2) + ".png\"><span class=\"aircode\">" + res[i][1] + "</span></li>";
        }
    }
    str += "</ul></div></div>";
    // alert(str);
    document.getElementById("info").innerHTML = str + "</div></div>";
}

function showInfo(name, info) {
    var str = "<div class=\"panel panel-info\"><div id=\"info-ph\" class=\"panel-heading\"><h1 class=\"panel-title\">" + name;
    str += "<span class='infosize'>（共" + info.size + "个）</span></h1></div><div class=\"panel-body\">";
    for (var i = 0; i < info.size; i++) {
        if (info.show[i].showtype == 0) {
            str = str + "<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_" + i + "\"><h3 class=\"panel-title\">";
            var isin = "";
            if (i == 0)
                isin = " in ";
            str = str + info.show[i].name + "</h3></div><div id=\"info_body_" + i + "\" class=\"panel-collapse collapse" + isin + "\"> <div class=\"panel-body\">";
            if (info.show[i].picture) {
                str = str + "<a href=\"" + info.show[i].urls[0].url + "\" class=\"thumbnail\" target=\"_blank\"><img src=\"" + info.show[i].picture + "\" alt=" + info.show[i].name + "></a>";
            }
            str = str + "<ul class=\"list-group\"><li class=\"list-group-item\"><b>名称:</b>" + info.show[i].name + "</li>";
            if (info.show[i].en_name != "null")
                str = str + "<li class=\"list-group-item\"><b>英文名称:</b>" + info.show[i].en_name + "</li>";
            if (info.show[i].industry != "null")
                str = str + "<li class=\"list-group-item\"><b>所属行业:</b>" + info.show[i].industry + "</li>";
            if (info.show[i].detail != "null")
                str = str + "<li class=\"list-group-item\"><b>地点:</b>" + info.show[i].locat + "(" + info.show[i].detail + ")</li>";
            else str = str + "<li class=\"list-group-item\"><b>地点:</b>" + info.show[i].locat + "</li>";
            str = str + "<li class=\"list-group-item\"><b>开始时间:</b>" + info.show[i].startDate + "</li>";
            if (info.show[i].endDate != "null")
                str = str + "<li class=\"list-group-item\"><b>结束时间:</b>" + info.show[i].endDate + "</li>";
            if (info.show[i].host != "null")
                str = str + "<li class=\"list-group-item\"><b>主办方:</b>" + info.show[i].host + "</li>";
            if (info.show[i].area != "null")
                str = str + "<li class=\"list-group-item\"><b>展会面积:</b>" + info.show[i].area + "</li>";
            if (info.show[i].times != "null")
                str = str + "<li class=\"list-group-item\"><b>展会界数:</b>" + info.show[i].times + "</li>";
            if (info.show[i].frequency != "null")
                str = str + "<li class=\"list-group-item\"><b>展会频率:</b>" + info.show[i].frequency + "</li>";
            if (info.show[i].used != "null")
                str = str + "<li class=\"list-group-item\"><b>使用展馆:</b>" + info.show[i].used + "</li>";
            if (info.show[i].urls.length > 0) {
                for (var j = 0; j < info.show[i].urls.length; j++) {
                    str = str + "<li class=\"list-group-item\">";
                    str = str + "<a href=\"" + info.show[i].urls[j].url + "\" target=\"_blank\">" + info.show[i].urls[j].url + "</a>";
                    str = str + "</li>";
                }
            }
            str = str + "</ul></div></div></div>";
        } else if (info.show[i].showtype > 0 && info.show[i].showtype < 8) {
            str = str + "<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_" + i + "\"><h3 class=\"panel-title\">";
            var isin = "";
            if (i == 0)
                isin = " in ";
            str = str + info.show[i].name + "</h3></div><div id=\"info_body_" + i + "\" class=\"panel-collapse collapse" + isin + "\"> <div class=\"panel-body\">";
            if (info.show[i].picture) {
                str = str + "<a href=\"" + info.show[i].urls[0].url + "\" class=\"thumbnail\" target=\"_blank\"><img src=\"" + info.show[i].picture + "\" alt=" + info.show[i].name + "></a>";
            }
            str = str + "<ul class=\"list-group\"><li class=\"list-group-item\"><b>名称:</b>" + info.show[i].name + "</li>";
            if (info.show[i].info != "null")
                str = str + "<li class=\"list-group-item\"><b>介绍:</b>" + info.show[i].info + "</li>";
            if (info.show[i].detail)
                str = str + "<li class=\"list-group-item\"><b>地点:</b>" + info.show[i].locat + "(" + info.show[i].detail + ")</li>";
            else str = str + "<li class=\"list-group-item\"><b>地点:</b>" + info.show[i].locat + "</li>";
            str = str + "<li class=\"list-group-item\"><b>开始时间:</b>" + info.show[i].startDate + "</li>";
            if (info.show[i].endDate != "null")
                str = str + "<li class=\"list-group-item\"><b>结束时间:</b>" + info.show[i].endDate + "</li>";
            if (info.show[i].MINprice != "null")
                str = str + "<li class=\"list-group-item\"><b>最低价格:</b>" + info.show[i].MINprice + "</li>";
            if (info.show[i].MAXprice != "null")
                str = str + "<li class=\"list-group-item\"><b>最高价格:</b>" + info.show[i].MAXprice + "</li>";
            if (info.show[i].urls.length > 0) {
                for (var j = 0; j < info.show[i].urls.length; j++) {
                    str = str + "<li class=\"list-group-item\">";
                    str = str + "<a href=\"" + info.show[i].urls[j].url + "\" target=\"_blank\">" + info.show[i].urls[j].url + "</a>";
                    str = str + "</li>";
                }
            }
            str = str + "</ul></div></div></div>";
        }
    }
    //var newChild = document.createTextNode(str);
    //document.getElementById("info").appendChild(newChild);
    document.getElementById("info").innerHTML = str + "</div></div>";
}

function deletePoint(type) {
//	if(Overlay[type]!=null){
    if (type < 9 && Overlay[type] != null) {
        for (var i = 0; i < Overlay[type].length + 1; i++) {
            map.removeOverlay(Overlay[type][i]);
        }
    } else if (type == 9 || type == 10 || type==13) {
        $(".showtype").bootstrapSwitch('state', false);
        myChart.dispose();
        showMap();
    } else if (type == 11 && Overlay[type] != null) {
        for (var i = 0; i < Overlay[type].length + 1; i++) {
            if (Overlay[type][i])
                for (var j = 0; j < Overlay[type][i].length + 1; j++) {
                    map.removeOverlay(Overlay[type][i][j]);
                }
        }
    } else if (type == 12 && Overlay[type] != null) {
        map.removeOverlay(Overlay[12]);
        map.removeControl(control);
	}
}

function searchIsClick(type) {
    if (document.getElementById("search_showtype_" + type).isclick == 0)
        getLocat(type);
    else deletePoint(type);
}

function search(id, state) {
    var i = id.lastIndexOf('_');
    i = id.substr(i + 1, 2);
    if (state == true) {
        getLocat(i);
    } else deletePoint(i);
}

function crawler(type) {
    if ($("#crawlerPassword").val) {
        $.ajax({
            url: "servlet/CrawlerServlet?action=1&type=" + type + "&pw=" + $("#crawlerPassword").val,
            success: function (res) {
                if (res == "true")
                    alert("你选择的类型正在爬取，请耐心等待，稍后刷新查看即可，具体内容请查看服务器日志");
                if (res == "false")
                    alert("密码错误，请咨询管理员后重试");
            }
        });
    } else alert("密码不可为空，请咨询管理员后重试");
}