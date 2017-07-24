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
function queryFrom(){
	$("#queryBtn").attr("disabled","disabled");
	$("#wait").show();
	$ajax.post("${path}/sbgl/queryFrom.do",{code:"${code}"},function(data){
		if(data.success){
			$msg.showInfo(data.info);
			window._waitQuery = true;
		}else{
			$msg.showError(data.info);
			$("#queryBtn").removeAttr("disabled");
			$("#wait").hide();
			window._waitQuery = false;
		}
	});
	
	setTimeout(function(){
		if(window._waitQuery){
    		$("#queryBtn").removeAttr("disabled");
    		window._waitQuery = false;
    		$msg.showError("等待超时，设备没有响应");
    		$("#wait").hide();
		};
	},15000);
	
}

setInterval(function(){
	if(window._waitQuery){
    	$ajax.post("${path}/sbgl/hasNewData.do",{code:"${code}",time:"${record.time}"},function(data){
    		if(data.success && data.data){
    			$msg.showInfo("更新数据");
    			location.reload(); 
    		}
    	});
	}
	
},2000);
</script>


</head>

<body>
  <div class="sp"></div>
  <div class="sp"></div>
  <table>
    <tr>
      <td> <button style="height:30px" id="queryBtn" type="button" onclick="queryFrom()">从设备获取数据</button></td>
      <td><div id="wait" class="min_wait" style="display:none"></div></td>
      <td> 
        <c:if test="${record!=null }">
          <span> &nbsp;数据更新时间：<fmt:formatDate value="${record.time}" pattern="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;
                            
          <c:if test="${record.type=='Query' }">
                                  数据类型：查询
          </c:if>
          <c:if test="${record.type=='Alarm' }">
            <span style="color:red">数据类型：告警</span>
              <img width="16" height="16" src="${path}/resource/platform/theme/blue/images/main/alarm.gif"></img>
          </c:if>
          <c:if test="${record.type=='Gap' }">
                                 数据类型：例行
          </c:if>
          </span>
         </c:if>
        <c:if test="${record==null }">
          <span style="color:red"> &nbsp;还没有上传数据</span>
        </c:if>
      </td>
    </tr>
  </table>
 
 
  <div class="sp"></div>
  <table cellpadding="0" cellspacing="0" border="0" class="form_table">
    <caption>前级数据</caption>
    <tr>
      <th>整机输出功率</th>
      <td>${record.preData[0]} W</td>
      <th>整机反射功率</th>
      <td>${record.preData[1]} W</td>
      <th>AGC电压</th>
      <td>${record.preData[2]} V</td>
    </tr>

    <tr>
      <th>前级输出电平</th>
      <td>${record.preData[3]} dBuv</td>
      <th>前级输入功率</th>
      <td>${record.preData[4]} W </td>
      <th>系统温度</th>
      <td>${record.preData[5]} °C</td>
    </tr>

    <tr>
      <th>衰减值</th>
      <td>${record.preData[6]} dB</td>
      <th>整机反射门限值</th>
      <td>${record.preData[7]} W</td>
      <th>整机输入下限</th>
      <td>${record.preData[8]} dBuv</td>
    </tr>

    <tr>
      <th>整机输入上限</th>
      <td>${record.preData[9]} dBuv</td>
      <th></th>
      <td></td>
      <th></th>
      <td></td>
    </tr>
  </table>

  <c:forEach items="${record.powDataList}" var="powData">
    <div class="sp"></div>
    <div class="sp"></div>
    <table cellpadding="0" cellspacing="0" border="0" class="form_table">
      <caption>后级功放${powData[1]}</caption>
      <tr>
        <th class="powData">功放模块数量</th>
        <td>${powData[0]}</td>
        <th class="powData">功放下标</th>
        <td>${powData[1]}</td>
        <th class="powData">功放功率</th>
        <td>${powData[2]} W</td>
      </tr>
      <tr>
        <th class="powData">反射功率</th>
        <td>${powData[3]} W</td>
        <th class="powData">50V电压</th>
        <td>${powData[4]} V</td>
        <th class="powData">功放1电流</th>
        <td>${powData[5]} A</td>
      </tr>
      <tr>
        <th class="powData">功放1温度</th>
        <td>${powData[6]} °C</td>
        <th class="powData">功放2电流</th>
        <td>${powData[7]} A</td>
        <th class="powData">功放2温度</th>
        <td>${powData[8]} °C</td>
      </tr>
    </table>
  </c:forEach>
  <div class="sp"></div>
  <div class="sp"></div>
  <table cellpadding="0" cellspacing="0" border="0" class="form_table">
    <caption>设置数据</caption>
    <tr>
      <th class="setPowData">风扇启动温度</th>
      <td>${record.setPowData[0]} °C</td>
      <th class="setPowData">告警温度值</th>
      <td>${record.setPowData[1]} °C</td>
      <th class="setPowData">告警电流值</th>
      <td>${record.setPowData[2]} A</td>
    </tr>
    <tr>
      <th class="setPowData">告警反射值</th>
      <td>${record.setPowData[3]} W</td>
      <th></th>
      <td></td>
      <th></th>
      <td></td>
    </tr>
  </table>


</body>
</html>