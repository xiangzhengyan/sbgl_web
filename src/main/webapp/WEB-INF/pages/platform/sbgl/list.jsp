<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>设备管理</title>
<style type="text/css">

.normal_btn{
	color:white;
	border:none;
	border-radius: 6px; 
	height:23px;
	line-height:23px;
	padding: 0 15 0 15;
	cursor:pointer; 
    background-color:#3c8dbc;
}


.normal_btn:HOVER,.normal_btn:HOVER{
	background-color:#367fa9;
}


</style>
<script type="text/javascript">
	/* 查询    */
	function doSearch() {
		if (!$form.validate("seachform")) {
			$msg.showError("请确认搜索条件符合规范");
			return;
		}
		$form.submit("seachform");
	};
	
	<c:if test="${sessionScope.isAdmin}">
	/*新增*/
	function add() {
		$portal.openPage('${path}/sbgl/detail.do?oper=new&orgId=${orgId}');
	};
	/*修改*/
	function edit() {
		var id = $com("gridTable1").getSelectedRowId();
		$portal.openPage('${path}/sbgl/detail.do?oper=edit&orgId=${orgId}&id='+id);
	};
	/*删除*/
	function del() {
		var id = $com("gridTable1").getSelectedRowId();
		if (!$msg.showConfirm("您确定要删除选中的设备吗？")) {
			return;
		} else {
			var str=prompt("请输入要删除的设备编码","");
			if(str==null){
				return;
			}
			var code = $com("gridTable1").getCellValue(id,0);

			var imgIndex = code.lastIndexOf(">");
			if(imgIndex!=-1){
				code = $.trim(code.substring(imgIndex+1));
				
			}
			if (str!=code) {
				$msg.showWarning("输入的编码不正确");
				return;
			}
			$ajax.post("${path}/sbgl/del.do", {
				sbId : id
			}, function(data) {
				if (data.success) {
					$com("gridTable1").deleteRow(id, "totalRowsSpan");
					$msg.showInfo(data.info);
				} else {
					$msg.showError(data.info);
				}
			});
		}
	};
	
	
	function mgrSb(id,code){
		var id = $com("gridTable1").getSelectedRowId();
		$portal.openPage('${path}/sbgl/mgrSb.do?id='+id);
	}
	</c:if>
	
	function viewSb(id,code){
		$portal.openPage('${path}/sbgl/viewSb.do?id='+id+"&code="+code);
	}
	
</script>
</head>
<body>
  <div class="$borderLayout" style="width: 100%; height: 100%;">
    
    <div class=" top" style="height: ${sessionScope.isAdmin?65:38}px;">
      <div class="title2">设备列表</div>
     <c:if test="${sessionScope.isAdmin}">
      <div class="$toolBar" style="height: 28px" bind-grid="gridTable1">
        <item name="new" text="新增设备" action="add()"></item>
        <item name="edit" text="修改设备信息" check-select="true" action="edit()"></item>
        <item name="view" text="管理设备" check-select="true" action="mgrSb()"></item>
        <item name="delete" text="删除设备" check-select="true" action="del()"></item>
      </div>
      </c:if>
    </div>
    <div class="center">

      <table id="gridTable1" width="100%" height="100%"  class="$grid">
        <tr>
          <th width="243">编码</th>
          <th width="150">名称</th>
          <th width="150">最后通信时间</th>
          <!-- 
          <th width="150">安装时间</th>
           -->
   
          <th width="100">负责人</th>
          <th width="150">联系电话</th>
          
          <th width="80">设备数据</th>
        </tr>
        <c:forEach items="${list}" var="sb" varStatus="current">
          <tr id="${sb.id}">
            <td style="line-height: 18px">
           
            <c:if test="${sb.alarm}">
              <img width="16" height="16" src="${path}/resource/platform/theme/blue/images/main/error.jpg"></img>
            </c:if>
            ${sb.code}
            </td>
            <td>${sb.name}</td>
            <td style="line-height: 18px">            
              <fmt:formatDate value="${sb.lastTime}" pattern="yyyy-MM-dd HH:mm:ss" />
              </td>
            <!-- 
            <td><fmt:formatDate value="${sb.installTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
             -->
            <td>${sb.mgrPersion}</td>
            <td>${sb.phone}</td>
            <td><button class="normal_btn" onclick="viewSb('${sb.id}','${sb.code}')">查看</button></td>
          </tr>
        </c:forEach>
      </table>
    </div>
  </div>
</body>
</html>