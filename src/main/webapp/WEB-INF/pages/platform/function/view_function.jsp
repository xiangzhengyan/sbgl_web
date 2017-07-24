<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_function" var="i18n_function" />
<title><fmt:message key="platform.function.functionInfo" bundle="${i18n_function}" /></title>
</head>
<body>
	<div class="$borderLayout" style="width: 100%; height: 100% ;">
		<div class="top" style="height: 25px">
			<div class="$titleBar">
				<table cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="tit_text"><fmt:message key="platform.function.functionInfo" bundle="${i18n_function}" /></td>
          </tr>
        </table>
			</div>
		</div>
		<div class="center">
			<table cellpadding="0" cellspacing="0" border="0" class="form_table">
				<tr>
					<th><span class="label_requried">*</span><fmt:message key="platform.function.functionName" bundle="${i18n_function}" /></th>
		    	<td>${func.label }</td>
		    	<th><fmt:message key="platform.function.functionIndex" bundle="${i18n_function}" /></th>
		    	<td>${func.menuIndex }</td>
				</tr>
				<tr>
					<th><span class="label_requried">*</span><fmt:message key="platform.function.functionApp" bundle="${i18n_function}" /></th>
		    	<td>${func.app.label }</td>
		    	<th><span class="label_requried">*</span><fmt:message key="platform.function.parentName" bundle="${i18n_function}" /></th>
		    	<td>${func.parent.label }</td>
				</tr>
				<tr>
					<th><span class="label_requried">*</span>URL</th>
		    	<td colspan="3">${func.url }</td>
				</tr>
				<tr>
					<th><fmt:message key="platform.function.enable" bundle="${i18n_function}" /></th>
		    	<td colspan="3">
		    		<c:if test="${func.enable == 1}"><fmt:message key="platform.function.able" bundle="${i18n_function}" /></c:if>
		    		<c:if test="${func.enable == 0}"><fmt:message key="platform.function.unable" bundle="${i18n_function}" /></c:if>
		    	</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>