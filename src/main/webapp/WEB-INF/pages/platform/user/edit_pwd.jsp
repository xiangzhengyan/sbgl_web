<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user"/>
<title><fmt:message key="platform.user.user_mgr" bundle="${i18n_user}"/></title>
<script type="text/javascript">
	function validate(){
		var password = jQuery.trim(jQuery("#password").val());
		var rePassword = jQuery.trim(jQuery("#rePassword").val());
		if (password != rePassword){
			$msg.showError("<fmt:message key='platform.user.validate_equal_password'  bundle='${i18n_user}'/>");
			jQuery("#password").focus();
			return;
		}
		//$form.submit('userForm');
		$form.ajaxSubmit("userForm",function(data){
			if(data.success){
				$portal.closePage();
			}else{
				$msg.showInfo(data.info);
			}
		});
	}
	
	jQuery(function(){
		var tipMsg = '${tipMsg}';
		if (tipMsg != null && tipMsg != '') {
			$msg.showError(tipMsg);
		}
	})
</script>
</head>

<body>
<div class="$titleBar">
       <table cellpadding="0" cellspacing="0" border="0">
         <tr>
           <td class="tit_text">
   			<fmt:message key="platform.user.password_info"  bundle="${i18n_user}"/>
           </td>
         </tr>
       </table>
     </div>
<form action="${path}/user/modifyPwd.do" name="userForm" id="userForm" method="post" >

	<input type="hidden" id="id" name="id" value="${userInfo.id}"/>

	<table cellpadding="0" cellspacing="0" border="0" class="form_table">
	  <tr>
	    <th width="15%"><fmt:message key="platform.user.loginName"  bundle="${i18n_user}"/></th>
	    <td width="35%">${userInfo.loginName}</td>
	    <th width="15%"><fmt:message key="platform.user.real_name" bundle="${i18n_user}"/></th>
	    <td width="35%">${userInfo.realName}</td>
	  </tr>
	
	  <tr>
	    <th ><span class="font_red">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.password" bundle="${i18n_user}"/></th>
	    <td><input name="password" id="password" class="$input"  type="password" required="true"/></td>
	    <th ><span class="font_red">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.rePassword" bundle="${i18n_user}"/></th>
	    <td><input name="rePassword" id="rePassword" class="$input" type="password"  required="true"/></td>
	  </tr>
	  
	  
	 	<tr>
	         <td colspan="4" style="text-align: right;">
	         	<input type="button" value="<fmt:message key='platform.common.save'  bundle="${i18n_common}"/>" class="$button" onclick="validate();"/>&nbsp;&nbsp; 
	 			<input type="button" value="<fmt:message key='platform.common.back' bundle="${i18n_common}"/>" class="$button" onclick="javascript:$portal.closePage();" />
	         </td>
	   </tr>
	</table>
</form>
</body>
</html>