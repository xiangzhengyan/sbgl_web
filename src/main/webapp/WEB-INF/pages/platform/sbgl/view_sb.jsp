<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>设备日志</title>

<link href="${path}/resource/platform/js/jqueryui/jquery-ui.css" rel="stylesheet"></link>
<script src="${path}/resource/platform/js/jqueryui/jquery-ui.js"></script>
<script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
	});
</script>
<style type="text/css">
body {
	overflow-x: hidden;
}

iframe {
	border: none;
	width: 100%;
	height: 400px;
}

th,td {
	white-space: nowrap;
}
</style>

</head>

<body>

  <div class="title2">查看设备数据：${code}</div>
  <div class="$toolBar" form-id="editFrom" id="toolbar">
    <item name="back" text="返回设备列表" action="javascript:$portal.closePage();"></item>
  </div>
  <div id="tabs">
    <ul>

      <li><a href="#tabs-0">最新数据</a></li>
      <li><a href="#tabs-2">告警日志</a></li>
      <li><a href="#tabs-3">24小时例行日志</a></li>
      <li><a href="#tabs-1">查询日志</a></li>
      <li><a href="#tabs-4">登录日志</a></li>
    </ul>

    <div id="tabs-0">
       <iframe src="${path}/sbgl/lastData.do?id=${id}&code=${code}"></iframe>
    </div>

    <div id="tabs-1">
      <iframe src="${path}/sbgl/viewDataLog.do?id=${id}&code=${code}&type=Query"></iframe>
    </div>
    <div id="tabs-2">
      <iframe src="${path}/sbgl/viewDataLog.do?id=${id}&code=${code}&type=Alarm"></iframe>
    </div>
    <div id="tabs-3">
      <iframe src="${path}/sbgl/viewDataLog.do?id=${id}&code=${code}&type=Gap"></iframe>
    </div>
    <div id="tabs-4">
      <iframe src="${path}/sbgl/viewLoginLog.do?id=${id}&code=${code}"></iframe>
    </div>
  </div>

  <script>
			function resizeIframe() {
				$("iframe").height(200);
				setTimeout(function() {

					var h = $(window).height() - 100;
					$("iframe").height(h);
				}, 1);
			}
			$(window).resize(function() {
				resizeIframe();
			});
			resizeIframe();
		</script>
</body>
</html>