<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


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
      <input type="text" id="search-input" class="form-control" placeholder="Search for...">
      <span class="input-group-btn">
        <button id="search-btn" class="btn btn-default" type="button">搜索</button>
      </span>
      <span class="input-group-addon"><span id="cleanAll-btn" class="glyphicon glyphicon-remove"></span></span>
    </div><!-- /input-group -->
    </div>
    <div class="form-group">
    <label>开始</label>
                <div class="input-group date form_date" data-date="" data-date-format="yyyy/MM/dd" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                    <input id="startDate" class="form-control" size="7" type="text" value="开始时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
     </div>
     <div class="form-group">
    <label>结束</label>
                <div class="input-group date form_date" data-date="" data-date-format="yyyy/MM/dd" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
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
        <div id="samllmap"  class="collapse">
        </div>
	</div>
	<div class="form-group">
    <label>打开所有</label>
		<input id="search_showall" type="checkbox" class="showall"/>
	</div>
	<div class="form-group">
	<button type="button" class="btn btn-info" onclick="back()">返回
	</button></div>
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
    			<label>会议论坛</label>
				<input id="search_showtype_7" type="checkbox" class="showtype" onclick="searchIsClick(7)"/>
			</div>
			<div class="divider"></div>
	 <div class="form-group">
    <label>天气预警</label>
		<input id="search_showtype_8" type="checkbox" class="showtype" onclick="searchIsClick(8)"/>
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
		<li><a data-toggle="modal" data-target="#setting">爬虫配置</a></li>
		<li class="divider"></li>
		<li><a data-toggle="modal" data-target="#download">下载数据</a></li>
		<li class="divider"></li>
		<li><a data-toggle="modal" data-target="#about">关于</a></li>
	</ul></div></div>
	</form>

<div class="modal fade" id="statistics-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
				<div>请选择要更新的事件类型</div>
				<button class="btn btn-default"  type="button" onclick="crawler(0)" data-dismiss="modal">展会</button>
				<button class="btn btn-default"  type="button" onclick="crawler(1)" data-dismiss="modal">演唱会</button>
				<button class="btn btn-default"  type="button" onclick="crawler(2)" data-dismiss="modal">音乐会</button>
				<button class="btn btn-default"  type="button" onclick="crawler(3)" data-dismiss="modal">话剧歌剧</button>
				<button class="btn btn-default"  type="button" onclick="crawler(4)" data-dismiss="modal">舞蹈芭蕾</button>
				<button class="btn btn-default"  type="button" onclick="crawler(5)" data-dismiss="modal">曲苑杂坛</button>
				<button class="btn btn-default"  type="button" onclick="crawler(6)" data-dismiss="modal">体育赛事</button>
				<button class="btn btn-default"  type="button" onclick="crawler(7)" data-dismiss="modal">会议</button>
				<button class="btn btn-default"  type="button" onclick="crawler(8)" data-dismiss="modal">天气预警</button>
				<br>
				<button class="btn btn-default"  type="button" onclick="crawler(9)" data-dismiss="modal">全部更新</button>
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
				<div>名称：heatpoint</div>
				<div>介绍：大型活动采集、分析及可视化系统</div>
				<div>版本：0.3.1</div>
				<br>
				<div>作者：张仁童</div>
				<div>邮箱：519539110@qq.com</div>
				<br>
				<div>比赛：第六届“中国软件杯”大学生软件设计大赛</div>
				<div>赛题：基于互联网大数据的事件智能抓取和画像</div>
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
