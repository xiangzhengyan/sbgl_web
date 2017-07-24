<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title></title>
</head>
<script type="text/javascript">
function del() {
	var roleId = $com("gridTable1").getSelectedRowId();
	if(roleId==null){
		 $msg.showWarning('<fmt:message key="platform.role.choosedelrole" bundle="${i18n_role}" />');
		 return;
	}
	var grids = $com("gridTable1");
	var status=grids.getCellValue(roleId,2);
	status = status.replace( /^\s+|\s+$/g, "" );
	if(status=='<fmt:message key="platform.role.unable" bundle="${i18n_role}" />'){
		var rolename=grids.getCellValue(roleId,1);
		$msg.showWarning(rolename+'<fmt:message key="platform.role.inunusablestate" bundle="${i18n_role}" />');
		return;
		
	}else{
	  
	if(confirm("<fmt:message key='platform.role.confirmdel'  bundle='${i18n_role}'/>")){
		  $ajax.post({
			  type: "post",
			  url: "${path}/role/deleteRole.do?roleId="+roleId,
			  success: function (data) {
				  if(data.success){
					  $msg.showInfo("已设置角色为不可用");
					  var id = data.data;
						var grid = $com("gridTable1");
						var isEnable = '<fmt:message key="platform.role.unable" bundle="${i18n_role}" />';
						grid.setCellValue(id,2,isEnable);
				  }else{
					  $msg.showError(data.info);
				  }
       }
		  });
    }  
	}
}

function editCallback(data){
	if(data){
	 
		var role = data.data;
		var grid = $com("gridTable1");
		var isEnable = role.enable?'<fmt:message key="platform.role.able" bundle="${i18n_role}" />':'<fmt:message key="platform.role.unable" bundle="${i18n_role}" />';
	 
		if(data.operation=="update"){
			grid.updateRow(role.id,[role.code,role.name,isEnable]);
		}else if(data.operation=="add"){
			grid.addRow(role.id,[role.code,role.name,isEnable]);
		}
	}
}
 
	function doSearch() {
		$("#name").val($("#keyWord").val());
		$form.submit("roleForm");
	}
	
	 function modifyCallBack(data){
		 if(data){
			 var roleId = $com("gridTable1").getSelectedRowId();
				var role = data.data;
				var grid = $com("gridTable1");
				var isEnable = role.enable?'<fmt:message key="platform.role.able" bundle="${i18n_role}" />':'<fmt:message key="platform.role.unable" bundle="${i18n_role}" />';
				if(data.operation=="update"){
					grid.updateRow(role.id,[role.code,role.name,isEnable]);
				}else if(data.operation=="add"){
					grid.addRow(role.id,[grid.getRowCount()+1,role.name,isEnable]);
				}
			}
	 }
	 
	 function modify() {
		var  roleId= $com("gridTable1").getSelectedRowId();
		 if(roleId==null){
			$msg.showWarning('<fmt:message key="platform.role.needmodifyrole" bundle="${i18n_role}" />');
			 return;
		 }
		 else{
			$portal.openPage('${path}/role/pageForward.do?roleId=' + roleId,
					modifyCallBack);
		 }
		}
	 function operateauthor()
	 {

		 var  roleId= $com("gridTable1").getSelectedRowId();
		 if(roleId==null){
			 $msg.showWarning('<fmt:message key="platform.role.needauthorrole" bundle="${i18n_role}" />');
			 return;
		 }
		 else{		 
			$portal.openPage('${path}/role/pageForward.do?roleId='+roleId+'&operate=author');
		 }
		 
	 }
</script>
<body style="padding: 0 5px 0 5px;">
  <div class="$borderLayout" style="width: 100%; height: 100%;">
    <div class="top" style="height: 62px">

      <div class="$toolBar" bind-grid="gridTable1" >
        <item  name="new" action="javascript:$portal.openPage('${path}/role/pageForward.do',editCallback)"></item>
        <item  name="edit" check-select="true" action="modify()"></item>
        <item  name="delete" check-select="true" action="del()"></item>
        <item  name="authMenu" check-select="true" text="<fmt:message key='platform.role.authmenu' bundle='${i18n_role}' />" img="auth-menu.gif" img-dis="dis-auth-menu.png" action="operateauthor()"></item>
      </div> 

      <!-- 查询表单 -->
      <form method="post" action="${path}/role/queryRoleList.do" name="roleForm" id="roleForm">
        <table cellpadding="0" cellspacing="0" border="0" class="search_table">
           <tr>
	          <th  ><fmt:message key="platform.role.keyWords" bundle="${i18n_role}" /></th>
	          <td  ><input class="$input" id="keyWord" name="name" value="${keyWord}" /> </td>
	          <td>
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
      <table id="gridTable1" width="500px" height="100%" class="$grid">
         <tr>
          <th width="200px"><fmt:message key="platform.role.code" bundle="${i18n_role}" /></th>
          <th  width="200px"><fmt:message key="platform.role.roleName" bundle="${i18n_role}" /></th>
          <th  width="100px"><fmt:message key="platform.role.isable" bundle="${i18n_role}" /></th>
       
        </tr>

        <c:forEach items="${roleList}" var="role" varStatus="current">
          <tr id="${role.id}">
            <td>${role.code}</td>
              <td>${role.name}</td>
              <td><c:choose>
                  <c:when test="${role.enable == 0}">
                    <fmt:message key="platform.role.unable" bundle="${i18n_role}" />
                  </c:when>
                  <c:otherwise>
                    <fmt:message key="platform.role.able" bundle="${i18n_role}" />
                  </c:otherwise>
                </c:choose>
              </td>
              
            </tr>
        </c:forEach>
      </table>
    </div>
    
  </div>
   
</body>
</html>