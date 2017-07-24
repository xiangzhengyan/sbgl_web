<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_function" var="i18n_function" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_organization" var="i18n_organization" />
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_user" var="i18n_user" />
<title><fmt:message key="platform.function.title" bundle="${i18n_function}" /></title>
<style>
html,body {
	width: 100%;
	height: 100%;
}



#rightIframe {
	width: 100%;
	height: 100%;
	border: 0;
}
</style>
<script type="text/javascript">
	var selectedAppId = "";
	var tree;
	$(function() {
		initTreeByAppId();
	});

	function initTreeByAppId() {
		$("#treeObject").empty();

		document.getElementById("treeObject").comobj.paint();
		tree = $("#treeObject")[0].comobj;

		// 树加载完后的处理
		//		tree.attachEvent("onXLE", function(tree, id) {
		//		});
		// 添加点击事件
		tree.setOnClickHandler(doClick);

		tree.attachEvent("onXLE", function() {
			tree.findItem("", null, null, false);
		});

		// 初始化树
		tree.loadXML("${path}/organization/queryOrgaTree.do");

	}

	// 树点击事件
	function doClick(nodeId) {
		$("#rightIframe").show();
		$("#rightIframe").attr("src",
				"${path}/sbgl/sbListByOrg.do?orgId=" + nodeId)
	}
</script>
</head>
<body>
  <table width="100%" height="100%">
    <tr>

      <td width="282" valign="top">
        <div class="$borderLayout" style="width: 100%; height: 100%;">
          <div class="top" style="height: 32px">
          <input name="key" id="key" class="$input" style="margin-left: 5; _margin-top: 10; width: 100px" />
            <button class="search_button" onclick="tree.findItem(document.getElementById('key').value, null, null, false);">查询</button>
          </div>

          <div class="center">
            <div id="treeObject" class="$tree" cascade="true" root-id="0" style="width: 280px; height: 100%;"></div>
          </div>
        </div></td>
      <td valign="top" height="100%"><iframe id="rightIframe"></iframe></td>
    </tr>

  </table>






</body>
</html>
