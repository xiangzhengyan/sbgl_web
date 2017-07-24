<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user" />
<title><fmt:message key="platform.user.userlist" bundle="${i18n_user}" /></title>
</head>
<script type="text/javascript">
 function del(){
		 var  userId= $com("gridTable1").getSelectedRowId();
		 if(userId==null){
			 $msg.showWarning("<fmt:message key='platform.user.choose_need_del_user' bundle='${i18n_user}' />");
			 return; 
		 }
			var grids = $com("gridTable1");
			var status=grids.getCellValue(userId,4);
			status = status.replace( /^\s+|\s+$/g, "" );
			if(status=="<fmt:message key='platform.user.status_invalid' bundle='${i18n_user}' />"){
				
				var loginname=grids.getCellValue(userId,0);
				 $msg.showWarning(loginname+"<fmt:message key='platform.user.in_status_invalid' bundle='${i18n_user}' />");
				
				return;
				
			}else
				{
		       if(confirm("<fmt:message key='platform.user.confirm_info' bundle='${i18n_user}' />")) {
		           jQuery("#delId").val(userId);
		           $form.ajaxSubmit("delForm",function(data){
		  		     if(data.success){
		  		          $msg.showWarning("用户已处于不可用状态");
		  		 	    // modifyCallBack(data);
		  		        var id = data.data.id;
						var grid = $com("gridTable1");
						var isEnable = '<fmt:message key="platform.user.status_invalid" bundle="${i18n_user}" />';
						grid.setCellValue(id,4,isEnable);
		  		}else{
		  			$msg.showInfo(data.info);
		  		}
		  	});
		    }
				}
		  }
 

 
    function doSearch() {
	    $("#pageNum").val("1");
	    $form.submit("userForm");
     }
	
	 function modifyCallBack(data){
		 if(data){
				var user = data.data;
				var grid = $com("gridTable1");
				var status = $util.queryZd("USER_STATUS",user.status);
				 
	      		if(data.operation=="update"){
					grid.updateRow(user.id,[user.loginName,user.realName,user.telphone,user.email,status]);
				}else if(data.operation=="add"){
					grid.addRow(user.id,[user.loginName,user.realName,user.telphone,user.email,status]);
				}
			}
	 }
	 
	 function modify() {
		 var  userId= $com("gridTable1").getSelectedRowId();
		 $portal.openPage('${path}/user/createUserForward.do?id='+userId+'',
					modifyCallBack);
		}
	 function operateauthor()
	 {

		 var  userId= $com("gridTable1").getSelectedRowId();
		 if(userId){		 
			$portal.openPage('${path}/user/forwardToRoleToUser.do?id='+userId+'');
		 }
		 
	 }
	 function view() {
			id = $com("gridTable1").getSelectedRowId();
			$portal.openPage('${path}/user/viewUser.do?id='+id);
		}
</script>
<body style="padding: 0 5px 0 5px;">
  <div class="$borderLayout" style="width: 100%; height: 100%;">
    <div class="top" style="height: 62px">

      <div class="$toolBar" bind-grid="gridTable1">
        <item name="new"   action="javascript:$portal.openPage('${path}/user/createUserForward.do',modifyCallBack)"></item>
        <item name="edit"   check-select="true" action="modify();"></item>
        <item name="view"   check-select="true" action="view()"></item>
        <item name="delete"   check-select="true" action="del()"></item>
        <!-- 
        <item name="authRole"   check-select="true" text="<fmt:message key='platform.user.role_to_user_title' bundle='${i18n_user}' />" img="role.gif" img-dis="dis_role.png" action="operateauthor()"></item>
         -->
      </div>

      <!-- 查询表单 -->
      <form action="${path}/user/queryUserList.do" name="userForm" id="userForm" method="post">
	    <table cellpadding="0" cellspacing="0" border="0" class="search_table">
            <tr>
         		<th><fmt:message key="platform.user.keyWords" bundle="${i18n_user}" /></th>
          		<td>
          	  		<input class="$input" id="keyWords" name="keyWords" value="${userInfo.keyWords}" />&nbsp;&nbsp;
	          	  	<button class="search_button" onclick="doSearch();">
	                	<fmt:message key='platform.common.query' bundle='${i18n_common}' />
	              	</button>
	          	</td>
	        	</tr>
        </table>
	  </form>
    </div>
    <div class="center">
      <!-- 表格 -->
      <table id="gridTable1" width="750px" height="100%" class="$grid" form-id="userForm" sort-col="${page.sortCol }" sort-order="${page.sortOrder }">
         <tr>
	         <th width="150px"><fmt:message key="platform.user.loginName" bundle="${i18n_user}" /></th>
	         <th width="150px" sort-name="realName"><fmt:message key="platform.user.real_name" bundle="${i18n_user}" /></th>
	         <th width="150px" sort-name="telphone"><fmt:message key="platform.user.telphone" bundle="${i18n_user}"/></th>
	         <th width="150px"><fmt:message key="platform.user.email" bundle="${i18n_user}"/></th>
	         <th width="150px"><fmt:message key="platform.user.status" bundle="${i18n_user}"/></th>
         </tr>
        <c:forEach items="${page.pageList}" var="user">
          <tr id="${user.id}">
            <td>${fn:escapeXml(user.loginName)}</td>
            <td>${fn:escapeXml(user.realName)}</td>
            <td>${fn:escapeXml(user.telphone)}</td>
            <td>${fn:escapeXml(user.email)}</td>
            <td>
            	<zfy:zd code="${user.status}" group="USER_STATUS"></zfy:zd>
            </td>
          </tr>
        </c:forEach>
      </table>
    </div>
    <div class="bottom" style="height: 23px">
      <!-- 分页条 -->
      <div class="$pageBar" form-id="userForm" total-rows="${page.totalRows}" page-size="${page.pageSize}" current-page="${page.pageNum}"></div>
    </div>
  </div>
  <form style="display: none" action="${path}/user/del.do" method="post" id="delForm">
    <input type="hidden" id="delId" name="id" />
  </form>
</body>
</html>