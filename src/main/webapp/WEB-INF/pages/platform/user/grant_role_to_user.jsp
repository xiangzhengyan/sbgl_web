<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user"/>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<title><fmt:message key="platform.user.role_to_user_title" bundle="${i18n_user}"/></title>
<script type="text/javascript">
	jQuery(function(){
		var tipMsg = '${tipMsg}';
		if (tipMsg != null && tipMsg != '') {
			$msg.showError(tipMsg);
		}
	});
	
	//过滤角色
	function filterRole(roleName){
		roleName = jQuery.trim(roleName);
		if (roleName == null || roleName == "" || roleName == undefined) {
			return;
		}
		jQuery("#roleNameTd").each(function(){
			var html = jQuery.trim(jQuery(this).html());
			if (html != null && html != "" && html != undefined){
				if (html.indexOf(roleName) >= 0){	//如果文件包含输入的参数
					var trObj = jQuery(this).parents("tr:first");
					jQuery(trObj).trigger("mouseover");
					jQuery(this).find("input[type='checkbox']").attr("checked","checked");
				}
			}
		});
	}
	
	//绑定全选
	$(function(){
		  $("#selectAll").click(function(){
			    var checked = $(this).is(':checked');
			    $("input[name='roleId']").each(function(){
			    	 this.checked = checked;
			    });
			});
	});
	
	function grantRoleToUser(){
		var ids = "";
		$("[name='roleId']").each(function(i){
			var checked = $(this).is(':checked');
			if(checked){
				ids += $(this).val() + ",";
			}
		});
		
		if(ids.indexOf(",")>=0){
			ids = ids.substring(0,ids.length-1);
		}
		$("#roleIds").val(ids);
		
		$form.ajaxSubmit("roleForm",function(data){
			if(data.success){
				$msg.showInfo("保存成功");
				$portal.closePage();
			}else{
				$msg.showWarning(data.info);
			}
		});
	}
	
</script>
</head>

<body>

	      
	  <div class="$toolBar">
    <item name="save" action="javascript:grantRoleToUser()"></item>
    <item name="back" action="javascript:$portal.closePage();"></item>
  </div>
      <!-- 表格 -->
      <table id="gridTable1"   class="$gridTable">
       <caption>分配角色</caption>
        <tr>
        	
        	<th  class="check_col"><input type="checkbox" id="selectAll"/></th>
          	<th class="index_col"><fmt:message key="platform.common.ordernumber" bundle="${i18n_common}" /></th>
            <th width="200px"><fmt:message key="platform.role.roleName" bundle="${i18n_role}" /></th>
        </tr>

        <c:forEach items="${roleList}" var="role" varStatus="current">
	    	<c:set var="isChecked" value="false"></c:set>
	       	<c:forEach items="${userRoles}" var="userRole">
	       		<c:if test="${role.id == userRole.id}">
	       			<c:set var="isChecked" value="true"></c:set>
	       		</c:if>
	       	</c:forEach>
	        <tr>
	       		<td id="checkBoxTd"><input type="checkbox" name="roleId" value="${role.id}" <c:if test="${isChecked}">checked="checked"</c:if>/></td>
	          	<td>${current.count}</td>
	          	<td id="roleNameTd">${role.name}</td>
	        </tr>
        </c:forEach>        
      </table>
      

  <form action="${path}/role/saveGrantRoleToUser.do" name="roleForm" id="roleForm" method="post" target="data">
      <input type="hidden" name="roleIds" id="roleIds"/>
      <input type="hidden" name="userId" value="${userId }"/>
   </form>
</body>
</html>