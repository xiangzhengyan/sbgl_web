<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>系统参数</title>
</head>
<script type="text/javascript">

	function doSearch() {
		$form.submit("cfgForm");
	}
	function doReset() {
		$form.resetForm("cfgForm");
	}

	function add() {
		$portal.openPage('${path}/cfg/pageForward.do?opt=new', modifyCallBack);
	}
	function modifyCallBack(data) {
		if (data) {
			var cfg = data.data;
			var grid = $com("gridTable1");
			if (data.operation == "update") {
				grid.updateRow(cfg.id, [ cfg.name, cfg.value, cfg.description ]);
			} else if (data.operation == "add") {
				grid.addRow(cfg.id, [ cfg.name, cfg.value, cfg.description ],'myPageBartotalRowsSpan');
			}
		}
	}

	function modify() {
		var cfgId = $com("gridTable1").getSelectedRowId();
		$portal.openPage('${path}/cfg/pageForward.do?opt=edit&id=' + cfgId,
				modifyCallBack);
	}
	function del() {
		var cfgId = $com("gridTable1").getSelectedRowId();
		if(confirm("确定删除该参数?")){
			$.ajax({
				type : "post",
				url : "${path}/cfg/delete.do?id=" + cfgId,
				success : function(data) {
					if (data.success) {
						var grid = $com("gridTable1");
						grid.deleteRow(cfgId,'myPageBartotalRowsSpan');
						$msg.showInfo(data.info);
					} else {
						$msg.showError(data.info);
					}
				}
			});
		}
	
	}
	function view() {
		var id = $com("gridTable1").getSelectedRowId();
		$portal.openPage('${path}/cfg/pageForward.do?opt=view&id=' + id);
	}

</script>
<body style="padding: 0 5px 0 5px;">
  <div class="$borderLayout" style="width: 100%; height: 100%;">
    <div class="top" style="height: 62px">

      <div class="$toolBar" bind-grid="gridTable1">
        <item name="new" action="add()"></item>
        <item name="edit" check-select="true" action="modify();"></item>
        <item name="view" check-select="true" action="view()"></item>
        <item name="delete" check-select="true" action="del()"></item>
      </div>

      <!-- 查询表单 -->
      <form action="${path}/cfg/getAll.do" name="cfgForm" id="cfgForm" method="post">
	    <table cellpadding="0" cellspacing="0" border="0" class="search_table" sort-col="${page.sortCol }" sort-order="${page.sortOrder }">
            <tr>
            	<th>参数名称</th>
          		<td>
          	  		<input class="$input" id="name" name="name" value="${name }" /> 	
	          	</td>
         		<%-- <th>参数描述</th>
          		<td>
          	  		<input class="$input" id="description" name="description" value="${description }" />  	
	          	</td> --%>
	          	<td>
              <button class="search_button" type="button" onclick="doSearch();">
                <fmt:message key='platform.common.query' bundle='${i18n_common}' />
              </button>
               <button class="reset_button" onclick="doReset();" type="button">
	                <fmt:message key='platform.common.reset' bundle='${i18n_common}' />
	            </button>
            </td>
	        	</tr>
        </table>
	  </form>
    </div>
    <div class="center">
      <!-- 表格 -->
      <table id="gridTable1" width="945px" height="100%" class="$grid" form-id="cfgForm" sort-col="${page.sortCol }" sort-order="${page.sortOrder }">
         <tr>
	         <th  width="120px" sort-name="name">参数名称</th>
	         <th width="525px">参数值</th>
	         <th width="300px">描述</th>
         </tr> 
        <c:forEach items="${cfgList}" var="cfg">
          <tr id="${cfg.id}">
            <td align="center">${fn:escapeXml(cfg.name)}</td>
            <td align="left">${fn:escapeXml(cfg.value)}</td>
            <td align="left">${fn:escapeXml(cfg.description)}</td>
          </tr>
        </c:forEach>
      </table>
    </div>
    <div class="bottom" style="height: 23px">
      <!-- 分页条 -->
      <div class="$pageBar" id="myPageBar" form-id="cfgForm" total-rows="${page.totalRows}" page-size="${page.pageSize}" current-page="${page.pageNum}"></div>
    </div>
  </div>
</body>
</html>