<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <%
	response.setContentType("text/html;charset=utf-8");
	request.setCharacterEncoding("utf-8");		
	%>
    <title>地理位置综合信息平台HeatPoint</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/bootstrap.css">
	<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
	<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/content/styles.css">
	<script src="//cdn.bootcss.com/echarts/3.6.2/echarts.min.js"></script>
	<script src="//cdn.bootcss.com/echarts/3.6.2/extension/bmap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/echarts-gl.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/china.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/script.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/content/airportGPS.js"></script>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=ALeBoFSYVxY6lExIZgptQGGK7skwRE8M"></script>
	  <script type="text/javascript" src="http://api.map.baidu.com/library/Heatmap/2.0/src/Heatmap_min.js"></script>
  </head>
  
  <body>
   		<div id="search">
			<%@ include file="pages/search.jsp" %>
		</div>
		<div id="map">
				<div id="allmap" class="panel-body"></div>
		</div>
		<div id="info">
		</div>
  </body>
</html>
