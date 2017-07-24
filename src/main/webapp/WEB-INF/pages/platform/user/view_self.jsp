<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user"/>
<title><fmt:message key="platform.user.user_mgr" bundle="${i18n_user}"/></title>
</head>

<body>
 <div class="sp"></div>
 <div class="top">
	<table cellpadding="0" cellspacing="0" border="0" class="form_table"  style="margin-left:8px">
	 <tr>
           <th class="title" colspan="4">
   			<fmt:message key="platform.user.user_info"  bundle="${i18n_user}"/>
           </th>
         </tr>
	  <tr>
	    <th><fmt:message key="platform.user.loginName"  bundle="${i18n_user}"/></th>
	    <td>&nbsp;${userInfo.loginName}</td>
	    <th><fmt:message key="platform.user.real_name" bundle="${i18n_user}"/></th>
	    <td>&nbsp;${userInfo.realName}</td>
	  </tr>
	
	  <tr>
	    <th ><fmt:message key="platform.user.email" bundle="${i18n_user}"/></th>
	    <td>&nbsp;${userInfo.email}</td>
	    <th ><fmt:message key="platform.user.telphone" bundle="${i18n_user}"/></th>
	    <td>&nbsp;${userInfo.telphone}</td>
	  </tr>
	  
	  <tr>
	    <th ><fmt:message key="platform.user.sex" bundle="${i18n_user}"/></th>
	    <td>&nbsp;
	    	<c:if test="${userInfo.sex == 1}">
	    		<fmt:message key="platform.user.male" bundle="${i18n_user}"/>
	    	</c:if>
	    	<c:if test="${userInfo.sex == 0}">
	    		<fmt:message key="platform.user.female" bundle="${i18n_user}"/>
	    	</c:if>
	    	
	    </td>
	    <th ><fmt:message key="platform.user.status" bundle="${i18n_user}"/></th>
	    <td>
	    	&nbsp;
	    	<c:set var="status_name" value="status_invalid"></c:set>
	    	<c:if test="${userInfo.status == 1}">
	    		<c:set var="status_name" value="status_normal"></c:set>
	    	</c:if>
	    	<c:if test="${userInfo.status == 2}">
	    		<c:set var="status_name" value="status_lock"></c:set>
	    	</c:if>
	    	<fmt:message key="platform.user.${status_name}" bundle="${i18n_user}"/>
	    </td>
	    
	  </tr>
	 	 
	</table>
	</div>
</body>
</html>