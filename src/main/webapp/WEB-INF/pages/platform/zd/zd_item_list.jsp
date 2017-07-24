<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>字典维护管理</title>
<script type="text/javascript">
/* 查询    */
function doSearch(){
	if(!$form.validate("seachform")){
		$msg.showError("请确认搜索条件符合规范");
		return;
	}
	$form.submit("seachform");
};
/*新增*/
function add(){
	$portal.openPage('${path}/zd/pageForwordItem.do?operation=add&groupCode=${groupCode}');
};
/*修改*/
function edit(){
	var id = $com("gridTable1").getSelectedRowId();
	$portal.openPage('${path}/zd/pageForwordItem.do?operation=update&groupCode=${groupCode}&id='+id);
};
/*删除*/
function del(){
	var id = $com("gridTable1").getSelectedRowId();
	if(!$msg.showConfirm("您确定要删除选中的行吗？")){
		return;
	}else{
		$ajax.post("${path}/zd/delZdItem.do", {
			id : id
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
/*重新访问该页面*/
function doReset(){
	$form.resetForm('seachform','groupCode=${groupCode}');
}

</script>
</head>
<body>
<div class="$borderLayout" style="width: 100%; height: 100%;">
		<div class="top" style="height: 60px">
				<div class="$toolBar" id="toolBar" bind-grid="gridTable1">
						<item name="new"  action="add()"></item>
						<item name="edit" check-select="true" action="edit()"></item>	
						<item name="delete" check-select="true" action="del()"></item>		
				</div>
				<form action="${path}/zd/getZdItems.do" id="seachform" method="post">
						<input type="hidden" id="groupCode" name="groupCode" value="${groupCode }"/>
						<table cellpadding="0" cellspacing="0" border="0" class="search_table">
							<tr>
								<th>名称</th>
								<td><input class="$input" style="width: 100px" id="label" name="label" value="${zdItem.label}" /></td>
								<td>
								     <button class="search_button" type="button" onclick="doSearch();">
								          <fmt:message key='platform.common.query' bundle='${i18n_common}' />
								     </button>
							    </td>
							</tr>
						</table>
				</form>
		</div>
		<div class="center">
			<table id="gridTable1" width="100%" height="100%" class="$grid">
				<tr>
					<!-- 字典label-->
					<th align="center" width="200px">名称</th>
					<th align="center" width="120px">是否可删</th>
					<th align="center" width="120px">排序号</th>
					<c:if test="${!empty parentZdGroup&&!empty parentZdItems}">
						<th align="center" width="200px">从属${parentZdGroup.groupLabel }</th>
					</c:if>
				</tr>
				<c:forEach items="${zdItems}" var="base" varStatus="current">
					<tr id="${base.id}">
						<td>${base.label}</td>
						<c:if test="${base.allowdel == '1' }">
							<td>是</td>
						</c:if>
						<c:if test="${base.allowdel == '0' }">
							<td>否</td>
						</c:if>
						<td>${base.index}</td>
						<c:if test="${!empty parentZdGroup&&!empty parentZdItems}">
							<c:forEach items="${parentZdItems }" var="parentbase" varStatus="current">
								<c:if test="${parentbase.code == base.parentCode }">
									<td>${parentbase.label }</td>
								</c:if>
							</c:forEach>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</div>	
		<div class="bottom" style="height: 1px">
    	</div>
</div>
</body>
</html>