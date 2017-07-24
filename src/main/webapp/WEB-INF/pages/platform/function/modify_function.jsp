<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_function" var="i18n_function" />
<title><fmt:message key="platform.function.newFunction" bundle="${i18n_function}" /></title>
<script type="text/javascript">
	function save(){
		$form.ajaxSubmit("myform",function(data){
			if(data.success){
				var node = data.data;
				parent.updateNode(node.nodeId, node.nodeName);
			}else{
				$msg.showWaring(data.info);
			}
		});
	}
</script>
</head>
<body>
	<div class="$borderLayout" style="width: 100%; height: 100% ;">
		<div class="top" style="height: 25px">
			<div class="$titleBar">
				<table cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="tit_text"><fmt:message key="platform.function.modifyFunction" bundle="${i18n_function}" /></td>
          </tr>
        </table>
			</div>
		</div>
		<div class="center">
		<form action="${path}/function/modifyFunc.do" id="myform" method="post">
			<table cellpadding="0" cellspacing="0" border="0" class="form_table">
				<tr>
					<th><span class="label_requried">*</span><fmt:message key="platform.function.functionName" bundle="${i18n_function}" /></th>
		    	<td><input name="label" id="label" class="$input" type="text" value="${function.label}" required="true"/>
		    									<input type="hidden" name="id" id="id" value="${function.id}" /></td>
		    									<input type="hidden" name="icoName" id="icoName" value="${function.icoName}" /></td>
		    	<th><fmt:message key="platform.function.functionIndex" bundle="${i18n_function}" /></th>
		    	<td ><input name="menuIndex" id="menuIndex" class="$input" type="text" value="${function.menuIndex }"/></td>
				</tr>
				<tr>
					<th ><span class="label_requried">*</span><fmt:message key="platform.function.functionApp" bundle="${i18n_function}" /></th>
		    	<td><input name="appName" id="appName" class="$input" type="text" value="${function.app.label}" readonly="readonly" required="true"/>
		    									<input type="hidden" name="appId" id="appId" value="${function.app.id}" /></td>
		    	<th><span class="label_requried">*</span><fmt:message key="platform.function.parentName" bundle="${i18n_function}" /></th>
		    	<td><input name="parentName" id="parentName" class="$input" type="text" readonly="readonly" value="${function.parent.label}"/>
		    									<input type="hidden" name="parentId" id="parentId" value="${function.parent.id}" /></td>
				</tr>
				<tr>
					<th><span class="label_requried">*</span>URL</th>
		    	<td colspan="3"><input name="url" id="url" class="$input" type="text" style="width: 41%" value="${function.url }" required="true"/></td>
				</tr>
				<tr>
					<th><fmt:message key="platform.function.enable" bundle="${i18n_function}" /></th>
		    	<td colspan="3"><fmt:message key="platform.function.able" bundle="${i18n_function}" />：<input name="enable" id="enable" type="radio" value="1" <c:if test="${function.enable == 1}">checked</c:if>/> &nbsp;&nbsp;<fmt:message key="platform.function.unable" bundle="${i18n_function}" />：<input name="enable" id="enable" type="radio" value="0" <c:if test="${function.enable == 0}">checked</c:if>/></td>
				</tr>
			</table>
			<div style="text-align:right;"> <input type="button" value="<fmt:message key='platform.common.save' bundle='${i18n_common}' />" class="$button" onclick="save()"/>&nbsp;&nbsp;
																			<input type="button" value="<fmt:message key='platform.common.back' bundle='${i18n_common}' />" class="$button" onclick="javascript:history.go(-1);" /></div> 
			</form>
		</div>
	</div>
</body>
</html>