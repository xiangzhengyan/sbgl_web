<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user" />
<title><fmt:message key="platform.user.user_mgr" bundle="${i18n_user}" /></title>
<script type="text/javascript">
	jQuery(function() {
		var tipMsg = '${tipMsg}';
		if (tipMsg != null && tipMsg != '') {
			$msg.showError(tipMsg);
		}
	});
	
	function resetPassword() {
		$ajax.post("${path}/user/resetPassword.do",{userId:'${userInfo.id}'},function(data){
			if(data.success){
				$msg.showInfo(data.info);
				$portal.closePage();
			}else{
				$msg.showWarning(data.info);
			}
		});
	}

	function saveOrUpdate() {
		if (!$form.validate("userForm")) {
			$msg.showWarning(NLS.VALIDATE_ERROR);
			return;
		}
		$form
				.ajaxSubmit(
						"userForm",
						function(data) {
							if (data.success) {
								$msg
										.showInfo('<fmt:message key="platform.user.save_success"  bundle="${i18n_user}"/>');
								$portal.closePage(data);
							} else {
								$msg.showWarning(data.info);
							}
						});
	}
</script>
</head>

<body style="padding: 0 5px 0 5px;">
  <div class="$toolBar">
    <item name="save" action="javascript:saveOrUpdate()"></item>
    <c:if test="${!empty user.id}">
     <item name="view" action="javascript:resetPassword()" text="重置密码"></item>
    </c:if>
    <item name="back" action="javascript:$portal.closePageWithConfirm();"></item>
  </div>


  <form action="${path}/user/mgrUser.do" name="userForm" id="userForm" method="post">

    <input type="hidden" id="id" name="id" value="${userInfo.id}" /> <input type="hidden" name="password" value="${userInfo.password}" />

    <table cellpadding="0" cellspacing="0" border="0" class="form_table">
      <c:if test="${empty user.id}">
        <caption>
          <fmt:message key="platform.user.user_add" bundle="${i18n_user}" />
        </caption>
      </c:if>
      <c:if test="${!empty user.id}">
        <caption>
          <fmt:message key="platform.user.user_modify" bundle="${i18n_user}" />
        </caption>
      </c:if>
      <tr>
        <th><span class="label_requried">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.loginName" bundle="${i18n_user}" /></th>
        <td><input name="loginName" id="loginName" class="$input" type="text" value="${fn:escapeXml(userInfo.loginName)}" required="true" /></td>
        <th><span class="label_requried">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.real_name" bundle="${i18n_user}" /></th>
        <td><input name="realName" id="realName" class="$input" value="${fn:escapeXml(userInfo.realName)}" required="true" /></td>
      </tr>

      <tr>
        <th><fmt:message key="platform.user.email" bundle="${i18n_user}" /></th>
        <td><input name="email" id="email" class="$input" value="${fn:escapeXml(userInfo.email)}" /></td>
        <th><fmt:message key="platform.user.telphone" bundle="${i18n_user}" /></th>
        <td><input name="telphone" id="telphone" class="$input" value="${fn:escapeXml(userInfo.telphone)}" /></td>
      </tr>
      <c:if test="${!empty user.id}">
        <tr>
            <th><fmt:message key="platform.user.status" bundle="${i18n_user}" /></th>
            <td colspan="3"><select style="width:200px" name="status" id="status" class="$select" group-code="USER_STATUS" val="${userInfo.status}">
            </select></td>
        </tr>
      </c:if>
     <tr>
        <th><span class="label_requried">*</span>角色</th>
        <td colspan="3">
          <input  name="roleCode" id="role_normal" value="normal" type="radio" ${(role.code=="normal"||role==null)?"checked='checked'":""}/><label for="role_normal">普通用户</label>
          <input  name="roleCode" id="role_admin" value="admin" type="radio" ${role.code=="admin"?"checked='checked'":""}/><label for="role_admin">管理员</label>
        </td>
      </tr>
      <tr>
        <th><span class="label_requried">*</span>所属机构</th>
        <td colspan="3">
        <input readonly="true" name="orgName" class="$input" required="true" id="orgName" value="${org.name }"/>
        <input readonly="true" name="orgId" type="hidden" class="$input" required="true" id="orgId" value="${org.id }"/>
        <button type="button" onclick="openOrgDlg()">选择</button></td>
      </tr>

    </table>
  </form>
  <script>
			function openOrgDlg() {
				//_url,title,width,height,callback,parentWindow
				$dialog.open("${path}/organization/selectOrganization.do",
						"请选择机构", 600, 400, function(data) {
							if (data) {
								$("#orgId").val(data.id);
								$("#orgName").val(data.name);
								$com("orgName").verify();
							}
						},window.parentWindow); 
			}
		</script>
</body>
</html>