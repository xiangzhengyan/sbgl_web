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
		if(!$form.validate("editFrom")){
			$msg.showError("请确认所有的输入都符合规范");
			return;
		}
		$form.ajaxSubmit("editFrom", function(data) {
			if (data.success) {
				$msg.showInfo(data.info);
				window.parent.doReset();
			} else {
				$msg.showError(data.info);
			}
		});
	}		
</script>
<body>
	<div class="$toolBar" form-id="editFrom" id="toolbar">
		<item name="save" action="javascript:save()"></item>
		<item name="back" action="javascript:$portal.closePage();"></item>
	</div>
	<form action="${path}/zd/saveOrUpdateZdItem.do" name="editFrom" id="editFrom">
		<input type="hidden" id="id" name="id" value="${zdItem.id }">
		<input type="hidden" id="code" name="code" value="${zdItem.code }">
		<input type="hidden" id="zdGroupCode" name="zdGroupCode" value="${groupCode }">
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			<c:if test="${empty zdItem }"> <caption>新建字典信息</caption></c:if>
			<c:if test="${!empty zdItem }"><caption>编辑字典信息</caption></c:if>
			<tr>
				<th><span class="label_requried">*</span>&nbsp;&nbsp;名称</th>
				<td><input class="$input" required="true" id="label" name="label" value="${zdItem.label}" /></td>
				<th>&nbsp;&nbsp;排序号</th>
				<td><input class="$input" data-type="integer-pos" id="index" name="index" value="${zdItem.index}" /></td>
			</tr>
			<c:if test="${!empty zdGroup}">
					<tr>
					<th><span class="label_requried">*</span>&nbsp;&nbsp;从属${zdGroup.groupLabel }</th>
					<td><select class="$select" group-code="${zdGroup.groupCode }" id="parentCode" name="parentCode" val="${zdItem.parentCode}"></select></td>
					<th>&nbsp;</th>
					<td>&nbsp;</td>
					</tr>
				</c:if>
				
			
		</table>
	</form>
</body>
</html>