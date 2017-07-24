<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_function" var="i18n_function" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_organization" var="i18n_organization" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user" />
<title><fmt:message key="platform.function.title" bundle="${i18n_function}" /></title>
<script type="text/javascript">
	var selectedAppId = "";
	var tree;
	// 页面加载完的处理
	$(function() {
		// 初始化树
		initTreeByAppId("${appId}");
	});

	function initTreeByAppId() {
		$("#treeObject").empty();

		document.getElementById("treeObject").comobj.paint();
		tree = $("#treeObject")[0].comobj;

		// 树加载完后的处理
//		tree.attachEvent("onXLE", function(tree, id) {
//		});
		// 添加点击事件
//		tree.setOnClickHandler(doClick);

		// 初始化树
		tree.loadXML("${path}/organization/queryOrgaTree.do");
	}

	// 树点击事件
//	function doClick(nodeId) {
//		return;
//		if (nodeId == "org_root") {
//			return;
//		}
//	}
	
	function done(){
		var nodeId = tree.getSelectedItemId();
		if(!nodeId){
			$msg.showWarning("请选择一个机构");  
		}else{
			$dialog.close({id:nodeId,name:tree.getSelectedItemText()});
		}
	}
	function cancel(){
		$dialog.close(); 
	}

	$(function() {
		$(window).resize();
	})
</script>
</head>
<body>

  <div class="$borderLayout" style="width: 100%; height: 100%;">
    <div class="top" style="height: 32px">
      <input name="key" id="key" class="$input" value="" />
      <button class="search_button" onclick="tree.findItem(document.getElementById('key').value, null, null, false);">
        <fmt:message key='platform.common.query' bundle='${i18n_common}' />
      </button>
      <button onclick="done()">确定</button>
       <button onclick="cancel()">取消</button>
    </div>
    <div class="center">
      <div style="height: 100%; overflow: auto">
        <div id="treeObject" class="$tree" multi-selection="false" cascade="true" root-id="0" style="width: 100%; height: 100%; overflow-y: auto;"></div>
      </div>
    </div>
</body>
</html>
