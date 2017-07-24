<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主页</title>
<style type="text/css">
.home_title_bar {
	width: 100%;
	height: 30px;
	line-height: 30px;
	background: #ECECEC;
	font-size: 12px;
	font-weight: bold;
	color: #666666;
}

.content {
	min-width: 800px;
	width: expression(document.body.clientWidth <   800 ?   "800px" :   "100%");
	height: 100%;
}

.left {
	width: 50%;
	height: 100%;
	float: left;
	position: relative;
}

.right {
	width: 49.5%;
	margin-left: 1px;
	float: right;
	position: relative;
}

.map {
	height: 90%;
	padding: 0px;
}

.right_table_div1 {
	margin-top: 20px;
	height: 300px;
}

.right_table_div2 {
	height: 158px;
}

.hdTr {
	display: none
}
table {
white-space: normal;
line-height: normal;
font-weight: normal;
font-size: medium;
font-variant: normal;
font-style: normal;
color: -webkit-text;
text-align: start;
}
</style>
<script type="text/javascript">
	function afterFrameLoaded() {
		//var leftBox = document.getElementById("left");
		//var rightBox = document.getElementById("right");
		//var leftBoxHeight = parseInt(leftBox.scrollHeight);
		//var rightBoxHeight = parseInt(rightBox.scrollHeight);
		//var trHeight = $(".tab_row")[0].scrollHeight;    这个为24
		//if(leftBoxHeight-rightBoxHeight>=22){
		//	$(".hdTr:first").removeClass("hdTr");
		//	afterFrameLoaded();
		//	}
	}
	
	$(function(){
		$(".objbox").css("overflow-x","hidden");
	});
</script>
</head>
<body>
  哈哈
</body>
</html>