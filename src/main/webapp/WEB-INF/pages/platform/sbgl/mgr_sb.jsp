<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>管理设备</title>
</head>
<script type="text/javascript">
	/*保存 */
	function save() {
		
		if(!$("#radio_on").is(':checked') && !$("#radio_off").is(':checked')){
			$msg.showError("请确选择开关机状态");
			return;
		}
		
		if (!$form.validate("editFrom")) {
			$msg.showError("请确认所有的输入都符合规范");
			return;
		}
		if (!$msg.showConfirm("确定要对设备进行设置吗？")) {
			return;
		}
		$form.ajaxSubmit("editFrom", function(data) {

			if (data.success) {
				$msg.showInfo(data.info);
				

			} else {
				$msg.showError(data.info);
			}
		});
	}
</script>
<style>
.form_table td{
  width:280px;
}



</style>
<body>

  <div class="$toolBar" form-id="editFrom" id="toolbar">
    <c:if test="${record!=null}">
      <item name="save" text="确认修改" action="javascript:save()"></item>
    </c:if>
    <item name="back" text="返回设备列表" action="javascript:$portal.closePage();"></item>
  </div>
  <div class="sp"></div>
  <div class="sp"></div>
  <c:if test="${record==null}">
    <span style="color: red; padding-left: 30px">还没有上传数据，无法进行设置。请先确认设备已经联通并上传数据。</span>
  </c:if>
  <c:if test="${record!=null}">
    <form action="${path}/sbgl/setSBParam.do" name="editFrom" id="editFrom" method="post">
      <input type="hidden" id="id" name="id" value="${id}"> <input type="hidden" id="code" name="code" value="${code}">
      <table cellpadding="0" cellspacing="0" border="0" class="form_table">
        <caption>管理设备：${code}</caption>

        <tr>
          <th>状态</th>
          <td width="500"><input id="radio_on" name="turn" type="radio" value="1" ${heart.status==1?"checked='checked'":""}></input><label for="radio_on">开机</label> <input id="radio_off" name="turn" type="radio" value="0" ${heart.status==0?"checked='checked'":""}></input><label for="radio_off">关机</label></td>
        </tr>

        <tr>
          <th>衰减值</th>
          <td><input class="$input" id="padValue" name="padValue" data-type="integer-pos" min-value="0" max-value="31" value="${record.preData[6]}" />&nbsp;&nbsp;0-31</td>
        </tr>
        <tr>
          <th>整机反射门限值</th>
          <td><input class="$input" id="totalRefLimit" name="totalRefLimit"  data-type="integer-pos" min-value="0" max-value="30" value="${record.preData[7]}" />&nbsp;&nbsp;0-30</td>
        </tr>
        <tr>
          <th>整机输入下限</th>
          <td><input class="$input" id="totalInLowerLimit" name="totalInLowerLimit"  data-type="integer-pos" min-value="0" max-value="30" value="${record.preData[8]}" />&nbsp;&nbsp;0-30</td>
        </tr>
        <tr>
          <th>整机输入上限</th>
          <td><input class="$input" id="totalInUpperLimit" name="totalInUpperLimit"  data-type="integer-pos" min-value="0" max-value="30" value="${record.preData[9]}" />&nbsp;&nbsp;0-30</td>
        </tr>

        <tr>
          <th>风扇启动温度</th>
          <td><input class="$input" id="fanTemp" name="fanTemp" value="${record.setPowData[0]}"  max-length="4" data-type="number-pos" min-value="20" max-value="40" />&nbsp;&nbsp;20.0-40.0</td>
        </tr>
        <tr>
          <th>告警温度值</th>
          <td><input class="$input" id="warnTemp" name="warnTemp"  data-type="integer-pos" min-value="0" max-value="85" value="${record.setPowData[1]}" />&nbsp;&nbsp;0-85</td>
        </tr>
        <tr>
          <th>告警电流值</th>
          <td><input class="$input" id="warnCurr" name="warnCurr"  data-type="integer-pos" min-value="0" max-value="50" value="${record.setPowData[2]}" />&nbsp;&nbsp;0-50</td>
        </tr>
        <tr>
          <th>告警反射值</th>
          <td><input class="$input" id="warnRef" name="warnRef"  data-type="integer-pos" min-value="0" max-value="30" value="${record.setPowData[3]}" />&nbsp;&nbsp;0-10</td>
        </tr>

      </table>
      <div class="sp"></div>
             <div style="padding-left:150px">数据更新时间：<fmt:formatDate value="${record.time}" pattern="yyyy-MM-dd HH:mm:ss" /></div> 

    </form>
  </c:if>
</body>
</html>