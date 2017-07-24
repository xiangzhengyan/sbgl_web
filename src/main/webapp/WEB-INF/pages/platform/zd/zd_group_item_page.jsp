<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>字典维护管理</title>
<script type="text/javascript">
//页面加载完毕后执行 加载左侧树 和 右侧列表(树节点的第一个孩子)
$(function(){
	/*左侧树*/
	tree = new dhtmlXTreeObject("treeObject", "100%", "100%", 0);
	/*图片路径*/
	tree.setImagePath("${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxTree/codebase/imgs/dhxtree_skyblue/");
	/*点击树绑定事件*/
	tree.attachEvent("onClick", doClick);
	/*初始化树*/
	tree.loadXML($util.getAppName()+"/zd/getZdGroupTree.do",function(){
		/*默认字典展开*/
		tree.openItem("zd");
		/*右边定义宽度*/
		$(".containerTableStyle").css("overflow-x","hidden");
	});
});
/*tree节点点击事件*/
function doClick(id){
	if(id!='zd'){
		$("#rightIframe").show();
		var url = "${path}/zd/getZdItems.do?groupCode="+id;
		$("#rightIframe").attr("src",url);
	}else{
		$("#rightIframe").hide();
	}
};
/*查找树节点*/
function searchTree(){
	var value = $("#key").val();//搜索值
	var nodeId = tree.findItem(value, null, null, false);//对应节点ID
	if(nodeId){
		doClick(nodeId);
	}else{
		$msg.showWarning("树没有该节点!");
	}
};
</script>
<style>
html,body{
	width:100%;
	height:100%;
}
.wrapper{
	width:100%;
	height:100%;
	position: relative;
}
#left{
	height:100%;
	width:200px;
	border-right:1px solid #a4bed4;
}
#right{
	position: absolute;
	left:202px;
	right:0px;
	top:0px;
	bottom:2px;
}
#rightIframe{
	width:100%;
	height:100%;
	border:0;
}
</style>
</head>
<body>
<div class="wrapper">
	<div id="left">
	    <div class="top" style="height: 5%;">
    	   <input name="key" id="key" class="$input" style="width: 120px;"/>
		   <button class="search_button" type="button" onclick="searchTree()">查询</button>
     	</div>
		<div id="treeObject" style="width:200px;height: 95%;"></div>
	</div>
	<div id="right">
		<iframe id="rightIframe"></iframe>
	</div>
</div>
</body>
</html>