<%@ page language="java" pageEncoding="UTF-8" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <link href="${pageContext.request.contextPath}/content/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/content/bootstrap-switch.min.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/content/bootstrap-datetimepicker.min.js"></script>
    <script src="${pageContext.request.contextPath}/content/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${pageContext.request.contextPath}/content/bootstrap-switch.min.js"></script>
</head>

<body>
<form id="head-form" class="form-inline">
    <div class="form-group">
        <label class="sr-only">search</label>
        <div class="input-group" id="search-in">
            <input type="text" id="search-input" class="form-control" placeholder="请输入要搜索的关键词">
            <span class="input-group-btn">
        <button id="search-btn" class="btn btn-default" type="button">搜索</button>
      </span>
            <span class="input-group-addon"><span id="cleanAll-btn" class="glyphicon glyphicon-remove"></span></span>
        </div><!-- /input-group -->
    </div>
    <div class="form-group">
        <label>开始</label>
        <div class="input-group date form_date" data-date="" data-date-format="yyyy/MM/dd" data-link-field="dtp_input2"
             data-link-format="yyyy-mm-dd">
            <input id="startDate" class="form-control" size="7" type="text" value="开始时间" readonly>
            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
        </div>
    </div>
    <div class="form-group">
        <label>结束</label>
        <div class="input-group date form_date" data-date="" data-date-format="yyyy/MM/dd" data-link-field="dtp_input2"
             data-link-format="yyyy-mm-dd">
            <input id="endDate" class="form-control" size="7" type="text" value="结束时间" readonly>
            <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
        </div>
    </div>
    <div class="form-group">
        <label>选择地区</label>
        <div class="input-group">
            <input id="province" class="form-control" size="5" type="text" value="选择地区" readonly>
            <span class="input-group-addon"><span id="province-popover" class="glyphicon glyphicon-pushpin"
                                                  data-container="body" data-toggle="popover" data-placement="bottom"
                                                  data-content="<div id='smallmap'></div>"></span></span>
        </div>
        <div id="samllmap" class="collapse">
        </div>
    </div>
    <div class="form-group">
        <label>打开所有</label>
        <input id="search_showall" type="checkbox" class="showall"/>
    </div>
    <div class="form-group">
        <button type="button" class="btn btn-info" onclick="back()">返回
        </button>
    </div>
    <div class="form-group">
        <div class="btn-group">
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多
                <span class="caret"></span>
            </button>
            <div class="dropdown-menu pull-right">
                <div class="form-horizontal" role="form">
                    <div class="form-group">
                        <label>展会信息</label>
                        <input id="search_showtype_0" type="checkbox" class="showtype" onclick="searchIsClick(0)"/>
                    </div>
                    <div class="form-group">
                        <label>会议论坛</label>
                        <input id="search_showtype_7" type="checkbox" class="showtype" onclick="searchIsClick(7)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>演唱会</label>
                        &nbsp;&nbsp;
                        <input id="search_showtype_1" type="checkbox" class="showtype" onclick="searchIsClick(1)"/>
                    </div>
                    <div class="form-group">
                        <label>音乐会</label>
                        &nbsp;&nbsp;
                        <input id="search_showtype_2" type="checkbox" class="showtype" onclick="searchIsClick(2)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>话剧歌剧</label>
                        <input id="search_showtype_3" type="checkbox" class="showtype" onclick="searchIsClick(3)"/>
                    </div>
                    <div class="form-group">
                        <label>舞蹈芭蕾</label>
                        <input id="search_showtype_4" type="checkbox" class="showtype" onclick="searchIsClick(4)"/>
                    </div>
                    <div class="form-group">
                        <label>曲苑杂坛</label>
                        <input id="search_showtype_5" type="checkbox" class="showtype" onclick="searchIsClick(5)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>体育赛事</label>
                        <input id="search_showtype_6" type="checkbox" class="showtype" onclick="searchIsClick(6)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>天气预警</label>
                        <input id="search_showtype_8" type="checkbox" class="showtype" onclick="searchIsClick(8)"/>
                    </div>
                    <div class="form-group">
                        <label>卫星云图</label>
                        <input id="search_showtype_12" type="checkbox" class="showtype" onclick="searchIsClick(12)"/>
                    </div>
                    <div class="form-group">
                        <label>空气质量</label>
                        <input id="search_showtype_13" type="checkbox" class="showtype" onclick="searchIsClick(13)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>地震信息</label>
                        <input id="search_showtype_10" type="checkbox" class="showtype" onclick="searchIsClick(10)"/>
                    </div>
                    <div class="form-group">
                        <label>台风预警</label>
                        <input id="search_showtype_11" type="checkbox" class="showtype" onclick="searchIsClick(11)"/>
                    </div>
                    <div class="divider"></div>
                    <div class="form-group">
                        <label>航班信息</label>
                        <input id="search_showtype_9" type="checkbox" class="showtype" onclick="searchIsClick(9)"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div id="statistics-btn" class="btn btn-warning" data-toggle="modal" data-target="#statistics-modal">统计</div>
    </div>
    <div class="form-group">
        <div class="btn-group">
            <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">设置
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu pull-right">
                <li><a data-toggle="modal" data-target="#changemap">更改地图</a></li>
                <li class="divider"></li>
                <li><a data-toggle="modal" data-target="#setting">爬虫配置</a></li>
                <li class="divider"></li>
                <!-- <li><a data-toggle="modal" data-target="#download">下载数据</a></li>
                <li class="divider"></li> -->
                <li><a data-toggle="modal" data-target="#about">关于</a></li>
            </ul>
        </div>
    </div>
</form>

<div class="modal fade" id="changemap" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog  modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title">
                    更改地图
                </h4>
            </div>
            <div class="modal-body">
                <button class="btn btn-default" type="button" onclick="changeMap('SATELLITE')" data-dismiss="modal">
                    卫星地图
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('HYBRID')" data-dismiss="modal">卫星混合
                </button>
                <br>
                <br>
                <button class="btn btn-info" type="button" onclick="changeMap('light')" data-dismiss="modal">清新蓝风格
                </button>
                <button class="btn btn-danger" type="button" onclick="changeMap('redalert')" data-dismiss="modal">
                    红色警戒风格
                </button>
                <button class="btn btn-primary" type="button" onclick="changeMap('midnight')" data-dismiss="modal">
                    午夜蓝风格
                </button>
                <button class="btn btn-warning" type="button" onclick="changeMap('pink')" data-dismiss="modal">浪漫粉风格
                </button>
                <button class="btn btn-success" type="button" onclick="changeMap('darkgreen')" data-dismiss="modal">
                    青春绿风格
                </button>
                <br>
                <br>
                <button class="btn btn-default" type="button" onclick="changeMap('normal')" data-dismiss="modal">
                    普通地图风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('dark')" data-dismiss="modal">黑夜风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('googlelite')" data-dismiss="modal">
                    精简风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('grassgreen')" data-dismiss="modal">
                    自然绿风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('bluish')" data-dismiss="modal">
                    清新蓝绿风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('grayscale')" data-dismiss="modal">
                    高端灰风格
                </button>
                <button class="btn btn-default" type="button" onclick="changeMap('hardedge')" data-dismiss="modal">
                    强边界风格
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="statistics-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog  modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title">
                    统计信息
                </h4>
            </div>
            <div class="modal-body">
                <div id="statistics-body"></div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="setting" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title">
                    爬虫配置
                </h4>
            </div>
            <div class="modal-body">
                <div class="input-group">
                    <div class="input-group-addon">请输入管理员密码</div>
                    <input type="password" class="form-control" id="crawlerPassword">
                </div>
                <div>请选择要更新的事件类型</div>
                <button class="btn btn-default" type="button" onclick="crawler(0)" data-dismiss="modal">展会</button>
                <button class="btn btn-default" type="button" onclick="crawler(1)" data-dismiss="modal">演唱会</button>
                <button class="btn btn-default" type="button" onclick="crawler(2)" data-dismiss="modal">音乐会</button>
                <button class="btn btn-default" type="button" onclick="crawler(3)" data-dismiss="modal">话剧歌剧</button>
                <button class="btn btn-default" type="button" onclick="crawler(4)" data-dismiss="modal">舞蹈芭蕾</button>
                <button class="btn btn-default" type="button" onclick="crawler(5)" data-dismiss="modal">曲苑杂坛</button>
                <button class="btn btn-default" type="button" onclick="crawler(6)" data-dismiss="modal">体育赛事</button>
                <button class="btn btn-default" type="button" onclick="crawler(7)" data-dismiss="modal">会议</button>
                <button class="btn btn-default" type="button" onclick="crawler(8)" data-dismiss="modal">天气预警</button>
                <br>
                <button class="btn btn-danger" type="button" onclick="crawler(9)" data-dismiss="modal">全部更新</button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="download" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title">
                    下载数据
                </h4>
            </div>
            <div class="modal-body">
                <div>请选择要下载的文件类型</div>
                <div>
                    <a href="${pageContext.request.contextPath}/servlet/CrawlerServlet?action=0&file=xls">xls</a>
                    <a href="${pageContext.request.contextPath}/servlet/CrawlerServlet?action=0&file=xlsx">xlsx</a>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="about" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×
                </button>
                <h4 class="modal-title">
                    HeatPoint
                </h4>
            </div>
            <div class="modal-body">
                <div><b>名称：</b>地理位置综合信息平台</div>
                <div><b>功能：</b>智能抓取互联网上所有包含地理位置信息的内容，并分类建模最终可视化于地图上，使用户可以方便快速的获取到需要的信息。</div>
                <div><b>介绍：</b>地理位置综合信息平台“HeatPoint”（以下简称GLIP），是中国民航信息集团主导建设的网络化地理信息共享与服务门户，集成了展会会议、活动门票、航班机票、气象资讯、灾害预警等信息，以及相关政府部门、企事业单位 、社会团体、公众的地理信息公共服务资源，向各类用户提供权威、标准、实时、动态的在线地理信息综合服务。</div>
                <div><b>版本：</b>0.4.2</div>
                <br>
                <div><b>作者：</b>张仁童</div>
                <div><b>邮箱：</b>519539110@qq.com</div>
                <br>
                <div><b>背景：</b>第六届“中国软件杯”大学生软件设计大赛</div>
                <div><b>赛题：</b>基于互联网大数据的事件智能抓取和画像</div>
                <br>
                <div><b>所获荣誉：</b></div>
                <div>第六届“中国软件杯”大学生软件设计大赛一等奖</div>
                <div>第十二届‘发明杯’大学生创新创业大赛二等奖</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

</body>
</html>
