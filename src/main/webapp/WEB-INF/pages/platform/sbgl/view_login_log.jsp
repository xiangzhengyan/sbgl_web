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
	
</script>


</head>

<body>
  <div class="sp"></div>
  <form action="${path}/sbgl/viewLoginLog.do" name="searchForm" id="searchForm" method="post">
    <table cellpadding="0" cellspacing="0" border="0" class="search_table">
      <tr>
        <th>时间&nbsp;</th>
        <td><input class="$input" id="beginTime" data-type="dateTime" style="width: 150px" name="beginTime" value="${beginTime}" />&nbsp;-&nbsp; <input class="$input" id="endTime" data-type="dateTime" style="width: 150px" name="endTime" value="${endTime}" />
          <button class="search_button" onclick="doSearch();">查询</button></td>
      </tr>
    </table>
    <input type="hidden" value="${id}" name="id" id="id"></input>
    <input type="hidden"  value="${code}" name="code" id="code"></input>
  </form>
  <div class="sp"></div>
  <table id="gridTable1"  class="$gridTable">
    <tr>
      <th>登录时间</th>
      <th>是否登录成功</th>
      
 
    </tr>
    <c:forEach items="${page.pageList}" var="log">
      <tr >
        <td><fmt:formatDate value="${log.time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        <td>${log.value=="1"?"成功":"失败"}</td>
  

       
      </tr>
    </c:forEach>
  </table>
  <div class="sp"></div>
  <div class="$pageBar" form-id="searchForm" total-rows="${page.totalRows}" page-size="${page.pageSize}" current-page="${page.pageNum}"></div>
</body>
</html>