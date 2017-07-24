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
	function showPreData(flag){
		if(flag){
			$('.preData').show();
		}else{
			$('.preData').hide();
		}
	}
	function showPowData(flag){
		if(flag){
			$('.powData').show();
		}else{
			$('.powData').hide();
		}
	}
	function showSetPowData(flag){
		if(flag){
			$('.setPowData').show();
		}else{
			$('.setPowData').hide();
		}
	}
</script>

<style type="text/css">
th,td{
white-space: nowrap;

}




</style>

</head>

<body>
  <div class="sp"></div>
  <form action="${path}/sbgl/viewDataLog.do" name="searchForm" id="searchForm" method="post">
    
    <input type="hidden" name="type" id="type" value="${type}">
    <table cellpadding="0" cellspacing="0" border="0" class="search_table">
      <tr>
        <th>时间&nbsp;</th>
        <td><input class="$input" id="beginTime" data-type="dateTime" style="width: 150px" name="beginTime" value="${beginTime}" />&nbsp;-&nbsp; <input class="$input" id="endTime" data-type="dateTime" style="width: 150px" name="endTime" value="${endTime}" />
          <button class="search_button" onclick="doSearch();">查询</button></td>
          
          <td><input type="checkbox" id="preDataCheck" checked="true" onclick="showPreData(this.checked)"/><label for="preDataCheck">前级数据</label></td>
          <td>&nbsp;<input type="checkbox" id="powDataCheck" checked="true" onclick="showPowData(this.checked)"/><label for="powDataCheck">后级数据</label></td>
          <td>&nbsp;<input type="checkbox" id="setPowDataCheck" checked="true" onclick="showSetPowData(this.checked)"/><label for="setPowDataCheck">设置数据</label></td>
      </tr>
    </table>
    <input type="hidden" value="${id}" name="id" id="id"></input>
    <input type="hidden"  value="${code}" name="code" id="code"></input>
  </form>
  <div class="sp"></div>
  <table id="gridTable1"  class="$gridTable">
    <tr>
      <th rowSpan="2">日志时间</th>
      <th colspan="10" class="preData" >前级数据</th>
       <c:forEach var="i" begin="1" end="${powDataSize}">
          <th colspan="9" class="powData" >后级功放${i}</th>
       </c:forEach>
        <th colspan="4" class="setPowData" >设置数据</th>
    </tr>
    <tr>
      
     
      <th class="preData" >整机<br/>输出功率</th>
      <th class="preData">整机<br/>反射功率</th>
      <th class="preData">AGC<br/>电压</th>
      <th class="preData">前级<br/>输出电平</th>
      <th class="preData">前级<br/>输入功率</th>
      <th class="preData">系统<br/>温度</th>
      <th class="preData">衰减<br/>值</th>
      <th class="preData">整机反射<br/>门限值</th>
      <th class="preData">整机<br/>输入下限</th>
      <th class="preData">整机<br/>输入上限</th>
      
      <c:forEach var="i" begin="1" end="${powDataSize}">
        <th class="powData" >功放<br/>模块数量</th>
        <th class="powData">功放<br/>下标</th>
        <th class="powData">功放<br/>功率</th>
        <th class="powData">反射<br/>功率</th>
        <th class="powData">50V<br/>电压</th>
        <th class="powData">功放1<br/>电流</th>
        <th class="powData">功放1<br/>温度</th>
        <th class="powData">功放2<br/>电流</th>
        <th class="powData">功放2<br/>温度</th>
      </c:forEach>
      
      <th class="setPowData" >风扇<br/>启动温度</th>
      <th class="setPowData">告警<br/>温度值</th>
      <th class="setPowData">告警<br/>电流值</th>
      <th class="setPowData">告警<br/>反射值</th>

    </tr>
    <c:forEach items="${page.pageList}" var="record">
      <tr>
        <td style="line-height: 16px">
           <c:if test="${record.alarm}">
            <img width="16" height="16" src="${path}/resource/platform/theme/blue/images/main/alarm.gif"></img>
          </c:if>
          <fmt:formatDate value="${record.time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        
        <c:forEach items="${record.preData}" var="val">
          <td class="preData">${val}</td>
        </c:forEach>
       
        <c:forEach items="${record.powDataList}" var="powData">
  	       <c:forEach items="${powData}" var="val">
            <td class="powData">${val}</td>
           </c:forEach>
        </c:forEach>
        
        <c:forEach items="${record.setPowData}" var="val">
          <td class="setPowData">${val}</td>
        </c:forEach>
        
      </tr>
    </c:forEach>
  </table>
  <div class="sp"></div>
  <div class="$pageBar" form-id="searchForm" total-rows="${page.totalRows}" page-size="${page.pageSize}" current-page="${page.pageNum}"></div>
</body>
</html>