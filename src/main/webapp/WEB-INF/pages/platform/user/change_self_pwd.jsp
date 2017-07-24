<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user"/>
<title><fmt:message key="platform.user.user_mgr" bundle="${i18n_user}"/></title>


 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<fmt:setBundle basename="com/zfysoft/platform/local/portal/i18n" var="i18n_portal"/>
 <title><fmt:message key="platform.portal.updatepassword" /></title>
<script type="text/javascript">
 
 

function saveOrUpdate() {
	if (!$form.validate("userself")) {
		$msg.showWarning(NLS.VALIDATE_ERROR);
		return;
	} 

	var org = $("#orginalpwd").val();
	var rs = $("#password").val();
	var re = $("#repassword").val();

	if (rs != re) {
		$msg.showWarning("二次输入的密码必须一致");
		return;
	}
	
	if(org==rs){
		$msg.showWarning("原密码和新密码不能相同 ");
		return;
		
	}
	
	$form.ajaxSubmit("userself", function(data) {
		if (data.success) {
			$msg.showInfo(data.info);
			//window.parent.openHomePage();
			setTimeout(function(){
				window.parent.location.reload(true);
			},500);
			
		} else {
			$msg.showWarning(data.info);
		}
	});
}

</script>
</head>

<body style="padding: 0 5px 0 5px;">
  <div class="top" style="margin-left:8px"><form action="${path}/user/modifymyselfpwd.do" method="post"  id="userself" >
 <div class="$toolBar">
    <item name="save" action="saveOrUpdate()" text="确定修改"></item>
    <item name="back" action="window.parent.openHomePage()" text="取消"></item>
  </div>
 <input type="hidden"  value="${user.id}" name="id"/>
  <div class="top">
 <table class="form_table" cellpadding="0" cellspacing="0" border="0">
   <tr>
            <th class="title" colspan="4"><fmt:message key="platform.portal.updatepassword"  bundle="${i18n_portal}"/></th>
          </tr>
 <tr>
 	
    <th><fmt:message key="platform.portal.loginname"  bundle="${i18n_portal}"/></th>
    <td>  ${user.loginName}</td>
    <th><span class="label_requried">*</span> <fmt:message key="platform.portal.orginalpassword" bundle="${i18n_portal}" /></th>
    <td><input class="$input" type="password" id="orginalpwd"  name="orginalpwd"   required="true" min-length="6" max-length="20" placeholder="6-20个字符"/></td>
 </tr>  

<tr>
    <th><span class="label_requried">*</span><fmt:message key="platform.portal.newpassword" bundle="${i18n_portal}" /></th>
    <td><input class="$input" type="password" name="password" id="password" required="true" min-length="6" max-length="20" placeholder="6-20个字符"/></td>

  <th><span class="label_requried">*</span><fmt:message key="platform.portal.confirmpassword" bundle="${i18n_portal}" /></th>
            <td><input class="$input" type="password" name="repassword" id="repassword" required="true" min-length="6" max-length="20" placeholder="6-20个字符"/></td>
 </tr>  

  
  </table>
  </div>
    
  </form>
  </div> 
</body>
</html>