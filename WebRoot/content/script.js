$(function (){
    $(".form_date").datetimepicker({
        format: "yyyy/mm/dd",
        language: "zh-CN",
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });
    $(".showtype").bootstrapSwitch({  
         onText:"启动",  
         offText:"停止",  
         onColor:"success",  
         offColor:"info",  
         size:"small",  
         onSwitchChange:function(event,state){  
        	 $(this).val(state);
             search(event.target.id,state);
       }  
	});
    $(".showall").bootstrapSwitch({  
        onText:"启动",  
        offText:"停止",  
        onColor:"success",  
        offColor:"info",  
        size:"small",  
        onSwitchChange:function(event,state){  
       	 $(this).val(state);
       	 if(state)
       		 for(var i=0;i<8;i++)
       			(function(i){
       				setTimeout(function(){
       						$("#search_showtype_"+i).bootstrapSwitch('state', true);
//       					alert(i);
       					},i*500);
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
    	if(key!=null)
    		$.ajax({
    			url:"servlet/LocationServlet?action=5&key="+key+"&start="+start+"&end="+end,
				dataType:"text",
				success:function(res){
//					alert(res);
					locatJson = JSON.parse(res);//  text/*
					for(var i=0;i<8;i++){
			    		deletePoint(i);
			    		Overlay[i]=[];
					}
//					setIcon();
					index=0;
					showlocat();
				}
    		});
    });
    $("#cleanAll-btn").on("click", function () {
    	$("#search-input").val("");
    	for(var i=0;i<8;i++)
    		deletePoint(i);
    });
    $('#province-popover').popover({html : true });
    $('#province-popover').on('shown.bs.popover', function () {
    	if(!hasGetP||!getTime())
    	getProvince();
    });
//    $('#province-popover').on('hide.bs.popover', function () {
//    	for(var i=0;i<8;i++)
//    		deletePoint(i);
//    });
    showMap();
    $('#statistics-btn').on('click',function () {
    	if(!hasStatistics)
    		showStatistics();
    });
});
//function openBTN(index){
//	$("#search_showtype_"+index).bootstrapSwitch('state', true);
//}
var hasStatistics = false;
function showStatistics(){
	hasStatistics = true;
	$.ajax({
		url:"servlet/LocationServlet?action=6",
		async: false,
		dataType:"text",
		success:function(res){
//			alert(res);
			var data=JSON.parse(res);
			for ( var i = 0; i < data.series.length; i++) {
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
			            return '日期: '+new Date().getFullYear()+'年'+(params.value[1]+1)+'月'+params.value[0]+
			            '日  \n 类型: '+params.seriesName+'  \n 数量: '+params.value[2];
			        }
			    },
			    legend: {
					left: 'left',
					data:["展会","演唱会","音乐会","话剧歌剧","舞蹈芭蕾","曲苑杂坛","体育赛事","会议"]
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
			        text: ['多','少'],           // 文本，默认为数值文本
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
			        data: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
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
var typeName = ["展会","演唱会","音乐会","话剧歌剧","舞蹈芭蕾","曲苑杂坛","体育赛事","会议"];
function showsmallmap(data){
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
			text: ['多','少'],           // 文本，默认为数值文本
			calculable: true,
			inRange: {
                color: ['lightskyblue','yellow', 'orangered']
            }
		},
		series: data.series
	});
	chart.on('click', function (params) {
		$("#province").val(params.name);
		for(var i=0;i<8;i++)
			deletePoint(i);
		for(var i=0;i<8;i++){
//			getLocat(i,params.name);
			(function(i){
   				setTimeout(function(){
   						getLocat(i,params.name);
   					},i*100);
   			})(i);
		}
    	map.setCenter(params.name);
    	map.setZoom(8);
	});
}
var hasGetP = false;
function getProvince(){
	hasGetP = true;
	getTime();
	$.ajax({
		url:"servlet/LocationServlet?action=4&start="+start+"&end="+end,
		async: false,
		dataType:"text",
		success:function(res){
//			alert(res);
			var data=JSON.parse(res);
			for ( var i = 0; i < data.series.length; i++) {
				data.series[i].type= 'map';
				data.series[i].map= 'china';
				data.series[i].roam= true;
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
function showMap(){
	myChart = echarts.init(document.getElementById("allmap"));
	myChart.setOption({
			bmap: {
		        center: [104.114129, 37.550339],
		        zoom: 5,
		        roam: true
		    }
	});
	map = myChart.getModel().getComponent('bmap').getBMap();
//	map = new BMap.Map("allmap",{enableMapClick:false});           // 创建Map实例
//	map.centerAndZoom("中国",5);
	map.enableScrollWheelZoom();    
	//map.addControl(new BMap.ScaleControl());
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			var myIcon = new BMap.Icon("http://api0.map.bdimg.com/images/geolocation-control/point/position-icon-14x14.png"
			, new BMap.Size(14,14));
			var mk = new BMap.Marker(r.point,{icon:myIcon});
			map.addOverlay(mk);
			//map.panTo(r.point);
		}       
	},{enableHighAccuracy: true});
  	map.setMapStyle({style:'midnight'});
  	myChart.on('click', function (params) {
  		var line = params.data;
//  		alert(JSON.stringify(line));
  		$.ajax({
			url:"servlet/LocationServlet?action=7&dep="+getAirCode(line.fromName)+"&arr="+getAirCode(line.toName)+"&start="+start,
			dataType:"text",
			success:function(res){
//				alert(res);
//				eval("var airplant="+res+";");
				showAirDetail(line.fromName,line.toName,eval(res));
			}
  		});
  	});
}
function back(){
	map.setCenter("中国");
	map.setZoom(5);
}
var myIcon;
var locatJson="";
var t=-1;
var start="";
var end="";
var Overlay=[];
var index = 0;
//var windows=[];
function getTime(){
	var befs = start , befe = end;
	start = document.getElementById("startDate").value;
	var d;
	if(start=="开始时间"||start==null){
		d=new Date();
		start=d.toLocaleDateString();
	}
	end = document.getElementById("endDate").value;
	if(end=="结束时间"||end==null){
		d.setMonth(d.getMonth()+1, d.getDay());
		end=d.toLocaleDateString();
	}
	if(befs!=start||befe!=end){
		hasGetp=false;
		return false;
	}
//	alert(type+start+end);
}
var doing = false;
function getLocat(type,p){
	doing = true;
	getTime();
	index=0;
	if(type<9){
		var url="servlet/LocationServlet?action=0&type="+type+"&start="+start+"&end="+end;
		if(p) 
			url+="&p="+p;
				$.ajax({
					url:url,
					async: false,
					dataType:"text",
					success:function(res){
						locatJson = JSON.parse(res);//  text/*
//						alert(res);
						t=type;
						Overlay[type]=[];
//						windows[type]=[];
						if(type<8){
							for(var i = 0;i<locatJson.location.length;i++)
								locatJson.location[i].type=type;
//							showScatter();
							setIcon(type);
							showlocat();
						}else {
							weatheralarm();
						}
//						doing = false;
					}
				});
	}else {
		var ind = 0;
		Overlay[9]=[];
		for(var i in geoCoordMap){
			if(geoCoordMap[i][4]=="CN"){
				showAirport(i,geoCoordMap[i],ind);
				ind++;
			}
		}
	}
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
function showAirport(name,add,ind){
	var myIcon = new BMap.Icon("img/airport.png", new BMap.Size(32,37));
	myIcon.setAnchor(new BMap.Size(16,37));
	var marker=new BMap.Marker(new BMap.Point(add[0], add[1]),
			{icon:myIcon,title:add[2]});
	Overlay[9][ind]=marker;
	map.addOverlay(marker);
	marker.setAnimation(BMAP_ANIMATION_DROP);
	marker.addEventListener("click",function(e){
		for(var i in Overlay[9]){
			Overlay[9][i].setAnimation(null);
		}
		map.setCenter(marker.getPosition());
		this.setAnimation(BMAP_ANIMATION_BOUNCE);
		getTime();
		$.ajax({
			url:"servlet/LocationServlet?action=7&arr="+name+"&start="+start,
			dataType:"text",
			success:function(res){
//				alert(res);
				drawLine(JSON.parse(res));
			}
		});
	});
}
function getAirPort(id){
	for(var i in geoCoordMap){
		if(i==id){
			return geoCoordMap[i];
		}
	}
}
function getAirCode(name){
	for(var i in geoCoordMap){
		if(geoCoordMap[i][2]==name){
			return i;
		}
	}
}
function drawLine(airline){
	var data = [];
	for (var i = 0; i < airline.length; i++) {
		var fromPort = getAirPort(airline[i][0]);
		var toPort = getAirPort(airline[i][1]);
//		alert(airline[i][0]+fromPort);
		var fromCoord = [fromPort[0],fromPort[1]];
		var toCoord = [toPort[0],toPort[1]];
		data.push({
			fromName: fromPort[2],
            toName: toPort[2],
            coords: [fromCoord, toCoord]
		});
	}
	var planePath = 'path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z';
	myChart.setOption({
		tooltip : {position: 'top'},
		series : [{
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
function setIcon(type){
	var size= new BMap.Size(32,37);
	if(type==0)
		myIcon = new BMap.Icon("img/trade.png", size);
	if(type>0&&type<3)
		myIcon = new BMap.Icon("img/music.png", size);
	if(type>2&&type<6)
		myIcon = new BMap.Icon("img/show.png", size);
	if(type==6)
		myIcon = new BMap.Icon("img/sport.png", size);
	if(type==7)
		myIcon = new BMap.Icon("img/meet.png", new BMap.Size(24,24));
	var a=new BMap.Size(16,37);
	myIcon.setAnchor(a);
	myIcon.setInfoWindowAnchor(a);
}
function weatheralarm(){
	var add = locatJson.weatheralarm[index];
	geoweather(add);
	index++;
}
function geoweather(add){
	if(index < locatJson.weatheralarm.length){
		setTimeout(weatheralarm,1);
	}
	var myGeo = new BMap.Geocoder();
	var ind=add.file.lastIndexOf('-');
	var gif=add.file.substr(ind+1,4);
	add.index=index;
	myGeo.getPoint(add.locat, function(point){
		if (point){
			if(gif.substr(0,1)=="9")
				gif="0000";
			myIcon = new BMap.Icon("http://www.weather.com.cn/m2/i/alarm_n/"+gif+".gif", new BMap.Size(32,37));
			var marker=new BMap.Marker(point,{icon:myIcon,title:add.name});
			Overlay[8][add.index]=marker;
			map.addOverlay(marker);
			marker.addEventListener("click",function(e){getalarm(add,gif);});
		}
	});
}
function getalarm(add,gif){
	var str = "<div class=\"panel panel-info\"><div class=\"panel-heading\"><h1 class=\"panel-title\">"+add.type
		+"预警</h1></div><div class=\"panel-body\"><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
		+add.file+"\" class=\"thumbnail\" target=\"_blank\"><img src=\"http://www.weather.com.cn/m2/i/alarm_n/"+gif+".gif\"></a><h3><b>"
		+add.locat+"发布"+add.color+add.type+"预警</b></h3><div><a href=\"http://www.weather.com.cn/alarm/newalarmcontent.shtml?file="
		+add.file+"\" target=\"_blank\" class='btn btn-info'><b>查看详情</b></a></div></div></div>";
	document.getElementById("info").innerHTML=str;
}
function showlocat(){
		var add = locatJson.location[index];
		if(typeof(index)=="undefined")
			index=0;
		add.index=index;
		index++;
		geocodeSearch(add);
//		add.type=t;
	}
function geocodeSearch(add){
		if(add.index < locatJson.location.length){
			setTimeout(showlocat,1);
		}
		var marker;
//		add.index=index;
		if(t!=add.type)
			setIcon(add.type);
//		if(!add.x)
//			marker = geo(add);
//		else
			marker=new BMap.Marker(new BMap.Point(add.x, add.y),
					{icon:myIcon,title:add.name});
//				if(add.x&&add.x>0){
//					marker=new BMap.Marker(new BMap.Point(add.x, add.y),
//						{icon:myIcon,title:add.name});
//				}else geo(add,marker);
				Overlay[add.type][add.index]=marker;
				map.addOverlay(marker);
				marker.setAnimation(BMAP_ANIMATION_DROP);
				var infoWindow = new BMap.InfoWindow(add.name+"<div id='windows-"+
						add.type+"-"+add.index+"' class='infowindow'></div>");
//				windows[t][index]=infoWindow;
//				infoWindow.addEventListener("open",function(e){
//					getInfo(add.id,e.target.getTitle(),type,add.index);
//				});
				marker.addEventListener("click",function(e){
//										cleanAnimation();
					this.openInfoWindow(infoWindow);
					map.setZoom(15);
					map.setCenter(marker.getPosition());
					this.setAnimation(BMAP_ANIMATION_BOUNCE);
					getInfo(add.id,e.target.getTitle(),add.type,add.index);
				});
				marker.addEventListener("infowindowclose",function(e){
					this.setAnimation(null);
					map.setZoom(8);
				});
	}
	
function geo(add){
//	alert("point"+add.id+add.name+add.x);
									var myGeo = new BMap.Geocoder();
									if(add.name=="null"){
										return;
									}
//									add.index=index;
									myGeo.getPoint(add.name, function(point){
										if (point) {
											var marker = new BMap.Marker(point,{icon:myIcon,title:add.name});
//											Overlay[add.type][add.index]=marker;
											//p.add(marker.getPosition());  //获取marker的位置
											var p = marker.getPosition();
											setLocat(p.lng,p.lat,add.id);
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
function setLocat(lng,lat,id){
	$.ajax({url:"servlet/LocationServlet?action=1&lng="+lng+"&lat="+lat+"&id="+id});
}

function getInfo(id,name,type,ind){
	$.ajax({
		url:"servlet/LocationServlet?action=2&id="+id+"&type="+type+"&start="+start+"&end="+end,
		dataType:"text",
		success:function(res){
//			alert(res);
			var InfoJson = JSON.parse(res);//  text/*
			showInfo(name,InfoJson);
			showWindow(type,ind,InfoJson);
		}
	});
}

function showWindow(type,ind,InfoJson){
//	windows[type][ind].setContent("hello");
//	alert("windows-"+type+"-"+ind);
	echarts.init(document.getElementById("windows-"+type+"-"+ind)).setOption(
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
			show:false,
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
	                   range: [start,end],
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
function toData(json){
	var data=[];
	for(var i=0; i<json.size; i++) {
		data.push([json.show[i].startDate,1]);
	}
	return data;
}
function showAirDetail(dep,arr,res){
//	res=res.data;
//	alert(res[0][2]);
	var str = "<div class=\"panel panel-info\"><div id=\"info-ph\" class=\"panel-heading\"><h1 class=\"panel-title\">"+start+"从"+dep+"到"+arr+"的所有航班";
	str += "</h1></div><div class=\"panel-body\">";
	for(var i in res) {
//		if($("#airplant-"+res[i][2]).exist()){
//			var str = "<li class=\"list-group-item\"><b>航班代码:</b>"+res[i][0]+"</li>";
//			if(res[i][1]!="")
//				str += "<li class=\"list-group-item\"><b>航班代码:</b>"+res[i][1]+"</li>";
//			$("#airplant-"+res[i][2]).appendChild(str);
//		}else{
			str = str+"<div class=\"panel panel-default\"><div class=\"panel-heading\"><h3 class=\"panel-title\">"
				+res[i][2]+"</h3></div><div class=\"panel-body\"><ul id=\"airplant-"+res[i][2]+
				"\" class=\"list-group\"><li class=\"list-group-item\"><img src=\"https://res.tianxun.com/flight/images/airline_large/"
				+res[i][0].substring(0,2)+".png\"><span class=\"aircode\">"+res[i][0]+"</span></li>";
			if(res[i][1]!="")
				str += "<li class=\"list-group-item\"><img src=\"https://res.tianxun.com/flight/images/airline_large/"
				+res[i][1].substring(0,2)+".png\"><span class=\"aircode\">"+res[i][1]+"</span></li>";
			str += "</ul></div></div>";
//		}
	}
	document.getElementById("info").innerHTML=str+"</div></div>";
}
function showInfo(name,info){
var str = "<div class=\"panel panel-info\"><div id=\"info-ph\" class=\"panel-heading\"><h1 class=\"panel-title\">"+name;
str+= "<span class='infosize'>（共"+info.size+"个）</span></h1></div><div class=\"panel-body\">";
	for(var i=0; i<info.size; i++) {
		if(info.show[i].showtype==0){
			str = str+"<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_"+i+"\"><h3 class=\"panel-title\">";
			var isin="";
			if(i==0)
				isin=" in ";
			str = str+info.show[i].name+"</h3></div><div id=\"info_body_"+i+"\" class=\"panel-collapse collapse"+isin+"\"> <div class=\"panel-body\">";
			if(info.show[i].picture){
				str = str+"<a href=\""+info.show[i].urls[0].url+"\" class=\"thumbnail\" target=\"_blank\"><img src=\""+info.show[i].picture+"\" alt="+info.show[i].name+"></a>";
			}
			str = str+"<ul class=\"list-group\"><li class=\"list-group-item\"><b>名称:</b>"+info.show[i].name+"</li>";
			if(info.show[i].en_name!="null")
				str = str+"<li class=\"list-group-item\"><b>英文名称:</b>"+info.show[i].en_name+"</li>";
			if(info.show[i].industry!="null")
				str = str+"<li class=\"list-group-item\"><b>所属行业:</b>"+info.show[i].industry+"</li>";
			if(info.show[i].detail!="null")
				str = str+"<li class=\"list-group-item\"><b>地点:</b>"+info.show[i].locat+"("+info.show[i].detail+")</li>";
			else str = str+"<li class=\"list-group-item\"><b>地点:</b>"+info.show[i].locat+"</li>";
			str = str+"<li class=\"list-group-item\"><b>开始时间:</b>"+info.show[i].startDate+"</li>";
			if(info.show[i].endDate!="null")
				str = str+"<li class=\"list-group-item\"><b>结束时间:</b>"+info.show[i].endDate+"</li>";
			if(info.show[i].host!="null")
				str = str+"<li class=\"list-group-item\"><b>主办方:</b>"+info.show[i].host+"</li>";
			if(info.show[i].area!="null")
				str = str+"<li class=\"list-group-item\"><b>展会面积:</b>"+info.show[i].area+"</li>";
			if(info.show[i].times!="null")
				str = str+"<li class=\"list-group-item\"><b>展会界数:</b>"+info.show[i].times+"</li>";
			if(info.show[i].frequency!="null")
				str = str+"<li class=\"list-group-item\"><b>展会频率:</b>"+info.show[i].frequency+"</li>";
			if(info.show[i].used!="null")
				str = str+"<li class=\"list-group-item\"><b>使用展馆:</b>"+info.show[i].used+"</li>";
			if(info.show[i].urls.length>0){
				for(var j=0;j<info.show[i].urls.length;j++){
					str = str+"<li class=\"list-group-item\">";
					str = str+"<a href=\""+info.show[i].urls[j].url+"\" target=\"_blank\">"+info.show[i].urls[j].url+"</a>";
					str = str+"</li>";
				}
			}
			str = str+"</ul></div></div></div>";	
		}else if(info.show[i].showtype>0&&info.show[i].showtype<8){
			str = str+"<div class=\"panel panel-default\"><div class=\"panel-heading\" data-toggle=\"collapse\" data-target=\"#info_body_"+i+"\"><h3 class=\"panel-title\">";
			var isin="";
			if(i==0)
				isin=" in ";
			str = str+info.show[i].name+"</h3></div><div id=\"info_body_"+i+"\" class=\"panel-collapse collapse"+isin+"\"> <div class=\"panel-body\">";
			if(info.show[i].picture){
				str = str+"<a href=\""+info.show[i].urls[0].url+"\" class=\"thumbnail\" target=\"_blank\"><img src=\""+info.show[i].picture+"\" alt="+info.show[i].name+"></a>";
			}
			str = str+"<ul class=\"list-group\"><li class=\"list-group-item\"><b>名称:</b>"+info.show[i].name+"</li>";
			if(info.show[i].info!="null")
				str = str+"<li class=\"list-group-item\"><b>介绍:</b>"+info.show[i].info+"</li>";
			if(info.show[i].detail)
				str = str+"<li class=\"list-group-item\"><b>地点:</b>"+info.show[i].locat+"("+info.show[i].detail+")</li>";
			else str = str+"<li class=\"list-group-item\"><b>地点:</b>"+info.show[i].locat+"</li>";
			str = str+"<li class=\"list-group-item\"><b>开始时间:</b>"+info.show[i].startDate+"</li>";
			if(info.show[i].endDate!="null")
				str = str+"<li class=\"list-group-item\"><b>结束时间:</b>"+info.show[i].endDate+"</li>";
			if(info.show[i].MINprice!="null")
				str = str+"<li class=\"list-group-item\"><b>最低价格:</b>"+info.show[i].MINprice+"</li>";
			if(info.show[i].MAXprice!="null")
				str = str+"<li class=\"list-group-item\"><b>最高价格:</b>"+info.show[i].MAXprice+"</li>";
			if(info.show[i].urls.length>0){
				for(var j=0;j<info.show[i].urls.length;j++){
					str = str+"<li class=\"list-group-item\">";
					str = str+"<a href=\""+info.show[i].urls[j].url+"\" target=\"_blank\">"+info.show[i].urls[j].url+"</a>";
					str = str+"</li>";
				}
			}
			str = str+"</ul></div></div></div>";
		}
	}
	//var newChild = document.createTextNode(str);
	//document.getElementById("info").appendChild(newChild);
	document.getElementById("info").innerHTML=str+"</div></div>";
}

function deletePoint(type){
	if(Overlay[type]!=null){
		for (var i = 0; i < Overlay[type].length+1; i++){
			map.removeOverlay(Overlay[type][i]);
		}
		if(type==9){
			$(".showtype").bootstrapSwitch('state', false);
			myChart.dispose();
			showMap();
		}
	}
}

function searchIsClick(type){
	if(document.getElementById("search_showtype_"+type).isclick==0)
		getLocat(type);
	else deletePoint(type);
}

function search(id,state){
	var i = id.lastIndexOf('_');
	i=id.substr(i+1,1);
	if(state==true){
		getLocat(i);
	}else deletePoint(i);
}

function crawler(type){
	if($("#crawlerPassword").val){
	$.ajax({
		url:"servlet/CrawlerServlet?action=1&type="+type+"&pw="+$("#crawlerPassword").val,
		success:function(res){
			if(res=="true")
				alert("你选择的类型正在爬取，请耐心等待，稍后刷新查看即可，具体内容请查看服务器日志");
			if(res=="false")
				alert("密码错误，请咨询管理员后重试");
		}
	});
	}else alert("密码不可为空，请咨询管理员后重试");
}