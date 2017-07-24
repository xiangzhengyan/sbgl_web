<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<style>
#left{
	position: absolute;
	width: 280px;
	height: 100%;
	border-right: 1px solid #A4BED4;
}
#right{
	position: absolute;
	left: 290px;
	right: 0px;
	height: 100%;
}
</style>
<script>
$(function(){
	var tree = $com("xzqhTree");
	tree.tree.setDataMode("json");
	tree.tree.setXMLAutoLoading($util.getAppName()+"/xzqh/getSubItem.do?canSelectLevels=1,2,3,4,5&showLevels=1,2,3,4,5");
	tree.tree.loadJSONObject({id:'0', item:[ {id:"1", text:"中华人民共和国", child:"20" ,im0:"area.gif",im1:"area.gif",im2:"area.gif"} ]});
	tree.selectItem("1");
	tree.attachEvent("onClick", function(dwdm) {
		$("#xzqhDm").html(dwdm);
		$("#xzqhMc").html(tree.getItemText(dwdm));
	});
});
</script>
</head>
<body style="padding: 0 5px 0 5px;">
  <div id="left">
  	<div id="xzqhTree" class="$tree" root-id="0" cascade="true"></div>
  </div>
  <div id="right">
  	<br/>
  	<div class="title" style="width: 700px">行政区划信息</div>
  	<table cellpadding="0" cellspacing="0" border="0" class="form_table">
	  <tr>
	    <th width="150px">行政区划名称</th>
	    <td width="250px" id="xzqhMc">中华人民共和国</td>
	    <th width="150px">代码</th>
	    <td width="250px" id="xzqhDm">1</td>
	  </tr>
	</table>
  </div>
</body>
</html>