<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<title><c:choose>
	   <c:when test="${empty role}">
	   		<fmt:message key="platform.role.addrole" bundle="${i18n_role}"/>
	   </c:when>
	   <c:otherwise>
	   		<fmt:message key="platform.role.modifyrole" bundle="${i18n_role}"/>
	   </c:otherwise>
	</c:choose></title>
<script type="text/javascript">
	
	function saveRole(){
		if(!$form.validate("roleForm")){
			$msg.showError("请确认输入都符合规范");
			return;
		}
		$form.ajaxSubmit("roleForm",function(data){
			if(data.success){
				$msg.showInfo('<fmt:message key="platform.role.modifysuccess" bundle="${i18n_role}"/>');
				$portal.closePage(data);
			}else{
				$msg.showWarning(data.info);
			}
		});
	}
</script>
</head>
<body style="padding: 0 5px 0 5px;">
 <div class="$toolBar">
    <item name="save" action="javascript:saveRole()"></item>
    <item name="back" action="javascript:$portal.closePage();"></item>
  </div>
	 
	
	<form action="${path}/role/SaveRole.do" name="roleForm" id="roleForm" method="post" >
		<input type="hidden" id="id" name="id" value="${role.id}"/>
		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
		<c:if test="${empty role.id}">   <caption><fmt:message key="platform.role.addrole" bundle="${i18n_role}"/></caption> </c:if>
		<c:if test="${!empty role.id}">   <caption><fmt:message key="platform.role.modifyrole" bundle="${i18n_role}"/></caption> </c:if>
			<tr>
				<th><span class="label_requried">*</span><fmt:message key="platform.role.code" bundle="${i18n_role}"/></th>
				<td><input name="code" id="code" class="$input" type="text" value="${role.code}" required="true"/></td>
				<th><span class="label_requried">*</span><fmt:message key="platform.role.roleName" bundle="${i18n_role}"/></th>
	    	<td><input name="name" id="name" class="$input" type="text" value="${role.name}" required="true"/></td>
			</tr>
			<tr>
				<th><fmt:message key="platform.role.isable" bundle="${i18n_role}"/></th>
	    	<td  colspan="3"><fmt:message key="platform.role.able" bundle="${i18n_role}"/>:<input name="enable" id="enable" type="radio" value="1" <c:if test="${role.enable == 1}">checked</c:if><c:if test="${empty role}">checked</c:if>/> &nbsp;&nbsp;<fmt:message key="platform.role.unable" bundle="${i18n_role}"/>:<input name="enable" id="enable" type="radio" value="0" <c:if test="${role.enable == 0}">checked</c:if>/></td>
			</tr>
			<tr>
			 
			</tr>
		</table>
	</form>
</body>
</html>