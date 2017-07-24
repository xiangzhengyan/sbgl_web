<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>保存字典</title>
</head>
<script type="text/javascript">
	/*保存 */
	function save() {
		if (!$form.validate("editFrom")) {
			$msg.showError("请确认所有的输入都符合规范");
			return;
		}
		$form.ajaxSubmit("editFrom", function(data) {

			if (data.success) {
				$msg.showInfo(data.info);
				window.parent.location.reload(true);

			} else {
				$msg.showError(data.info);
			}
		});
	}
</script>
<body>
  <div class="$toolBar" form-id="editFrom" id="toolbar">
    <item name="save" action="javascript:save()"></item>
    <item name="back" text="返回设备列表" action="javascript:$portal.closePage();"></item>
  </div>
  <form action="${path}/sbgl/save.do" name="editFrom" id="editFrom" method="post">
    <input type="hidden" id="id" name="id" value="${sb.id}"> <input type="hidden" id="orgId" name="orgId" value="${orgId}">
    <table cellpadding="0" cellspacing="0" border="0" class="form_table">
      <c:if test="${empty sb }">
        <caption>新增设备</caption>
      </c:if>
      <c:if test="${!empty sb }">
        <caption>修改设备基本信息</caption>
      </c:if>
      <tr>
        <th><span class="label_requried">*</span>&nbsp;&nbsp;编码</th>
        <td colspan="3">
        <c:if test="${sb==null}">
                <input class="$input" style="width: 100%" required="true"                 id="code" name="code" value="${sb.code}"  />
        </c:if>
        <c:if test="${sb!=null}">
                <input class="$input" style="width: 100%" required="true" readonly="true" id="code" name="code" value="${sb.code}"  />
        </c:if>
        </td>
      </tr>
      
            <tr>
        <th><span class="label_requried">*</span>&nbsp;&nbsp;设备名称</th>
        <td colspan="3">
               <input class="$input" style="width: 100%" required="true"  id="name" name="name" value="${sb.name}"  />
        </td>
      </tr>

      <tr>
        <th><span class="label_requried">*</span>&nbsp;&nbsp;密码</th>
        <td><input class="$input" required="true" id="password" name="password" value="${sb.password}" /></td>

        <th><span class="label_requried">*</span>&nbsp;&nbsp;安装时间</th>
        <td><input class="$input" required="true" data-type="dateTime" id="installTime" name="installTime" value="${sb.installTime}" /></td>
      </tr>
      <tr>
        <th>&nbsp;&nbsp;负责人</th>
        <td><input class="$input"  id="mgrPersion" name="mgrPersion" value="${sb.mgrPersion}" /></td>
        <th>&nbsp;&nbsp;联系电话</th>
        <td><input class="$input" id="phone" name="phone" value="${sb.phone}" /></td>
      </tr>
      <tr>
        <th>&nbsp;&nbsp;安裝位置</th>
        <td colspan="3"><input style="width: 100%;" class="$input" id="addr" name="addr" value="${sb.addr}" /></td>
      </tr>
      <tr>
        <th>&nbsp;&nbsp;备注</th>
        <td colspan="3"><textarea style="width: 100%; height: 200px" class="$textArea" id="remarks" name="remarks">${sb.remarks}</textarea></td>

      </tr>

    </table>
  </form>
</body>
</html>