<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_role" var="i18n_role" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common" />
<script type="text/javascript">
	var selectedAppId = "";
	var tree;
	var n = 0;
	var functionId;
	
	// 判断是否已某个字符串开头
	String.prototype.startWith=function(str){     
    var reg = new RegExp("^" + str);     
    return reg.test(this);        
  };

	// 页面加载完的处理
	$(function() {
		// 初始化树q
		initTreeByAppId("${appId}");
		// 绑定子系统下拉框改变的事件
		$("#application").bind("change", function() {
			$("#treeObject").empty();
			document.getElementById("treeObject").comobj.paint(); 
			initTreeByAppId($(this).val());
		});
		
		//绑定全选
		$("#selectAll").click(function(){
		    var checked = $(this).is(':checked');
		    $("input[name='delId']").each(function(){
		    	 this.checked = checked;
		    });
		});
	});
	// 根据子系统的ID初始化菜单树
	function initTreeByAppId(appId) {
	  tree = $("#treeObject")[0].comobj;
		$("#saveBtn").attr("disabled", "true");
		selectedAppId = appId;
		
		// 树加载完后的处理
		tree.attachEvent("onXLE", function(tree, id) {
			$("#saveBtn").removeAttr("disabled");
		});
		
		 tree.attachEvent("onClick", doClick);
		 tree.setOnCheckHandler(doCheck);
		// 初始化树
		tree.loadXML("${path}/role/functionTree.do?appId=" + appId
				+ "&roleId=${role.id}");
	}
	
	// 树checkbox单击事件
	function doCheck(id, state) {
		if (state == 1) {
			// checked
			// 展开该节点，
      tree.openItem(id);
      // 在页面右边显示该菜单下的按钮、超链接
      $ajax.post({
        type: "post",
        url: "${path}/role/querybtn.do?functionId="+id+"&roleId="+'${role.id}',
        success: function(data) {
          if (data.success) {
             var btnTableObj = $com("gridTable1");
             btnTableObj.deleteAllRows();
             var all = data.data.btnList;
             var exist = data.data.existList;
             $.each(all, function(i, btn) {
               var chbox = "<input type='checkbox' name='delId' value='"+btn.id+"'/>";
               $.each(exist, function(i, exist) {
                 if (btn.id == exist.id) {
                 chbox = "<input type='checkbox' checked='checked' name='delId' value='"+btn.id+"'/>";
                 }
               });
             var array = new Array();
             array[0]= chbox;
             array[1]= btn.label;
             btnTableObj.addRow(i,array);            
           });
          }else{
           $msg.showInfo(data.info);
         }
        }
       });
		} else {
			// unchecked
			$("[name='delId']").removeAttr("checked");//全不选
		}
	}
	
	var preIds = "";
	var btnOnckilck = false;
	// 树单击事件
	function doClick(nodeId) {
		if (n >= 1) {
			var ids = "";
			$('input[name="delId"]:checked').each(function(){
			    ids += ($(this).val());
			});
			if (ids != preIds && btnOnckilck == false) {
				if (confirm("<fmt:message key='platform.role.needSave' bundle='${i18n_role}'/>")) {
					save();
				}
			}
			preIds = "";
			btnOnckilck = false;
		}
		if(tree != null && nodeId != null){
       // 展开该节点，
       tree.openItem(nodeId);
       // 在页面右边显示该菜单下的按钮、超链接
       if (nodeId.startWith("app_")) {
    	   nodeId = nodeId.substring(4, nodeId.length);
       }
       
       $ajax.post({
    	   type: "post",
    	   url: "${path}/role/querybtn.do?functionId="+nodeId+"&roleId="+'${role.id}',
    	   success: function(data) {
    		   if (data.success) {
    			   	var btnTableObj = $com("gridTable1");
    			   	btnTableObj.deleteAllRows();
    			   	var all = data.data.btnList;
    			   	var exist = data.data.existList;
							$.each(all, function(i, btn) {
								var chbox = "<input type='checkbox' name='delId' value='"+btn.id+"'/>";
								$.each(exist, function(i, exist) {
									if (btn.id == exist.id && tree.isChecked(nodeId) == 1) {
									chbox = "<input type='checkbox' checked='checked' name='delId' value='"+btn.id+"'/>";
									}
								});
							var array = new Array();
							array[0]= chbox;
							array[1]= btn.label;
							btnTableObj.addRow(i,array);						
						});
							$('input[name="delId"]:checked').each(function(){
								preIds += $(this).val();
							});
    		   }else{
					  $msg.showInfo(data.info);
				  }
    	   },
    	   error : function(data) {
    		   $msg.showInfo(data);
    	   }
				});
    }
		n = n+1;
		functionId = nodeId;
	}
	
	function save() {
		btnOnckilck = true;
		var tree = $("#treeObject")[0].comobj;
		var id_array=new Array();
		$('input[name="delId"]:checked').each(function(){
		    id_array.push($(this).val());//向数组中添加元素
		});
		$("#saveBtn").attr("disabled", "true");
		$ajax.post("${path}/role/authorize.do", {
			roleId : '${role.id}',
			functionId : functionId,
			selectedIds : tree.getAllSelectedIds(),
			unSelectedIds : tree.getAllUnSelectedIds(),
			btnIds : id_array + "",
			appId : selectedAppId
		}, function(data) {
			if (data == '1') {
				$msg.showInfo("<fmt:message key='platform.role.modifysuccess' bundle='${i18n_role}'/>");
				$("#saveBtn").removeAttr("disabled");
			}
		});
	}
	
	
</script>
<style>
#left{
	position: absolute;
	left:0px;
	top:0px;
	bottom: 0px;
	width:260px;
	border-right: 1px solid #A4BED4;
}
#right{
	position: absolute;
	left: 262px;
	right: 0px;
	top:0px;
	bottom: 0px;
}
</style>
</head>
<body>
  <div class="$borderLayout" style="width: 100%; height: 100%">
    <div class="top" style="height: 55px">
     <div class="$toolBar"   >
          
         <item name="save" action="javascript:save()"></item>
          <item name="back" action="javascript:$portal.closePage();"></item>
         
               
      </div>
      <div class="$toolBar" style="font-weight: bold;color:grey;margin-left:260px;height:20px;"> 
         
           角色授权菜单 
           
       </div>
       
    </div>
    <div class="center">
		<div id="left">
			<div id="treeObject" class="$tree"  multi-selection="true" cascade="true" root-id="0" style="width: 100%; height: 98%"></div>
		</div>
		<div id="right">
			<table id="gridTable1" height="auto" class="$gridTable"  style="border-top:0px ;">
		        <tr>
		          <th  class="check_col"><input type="checkbox" id="selectAll"/></th>
		          <th  style="width:300px">菜单页面按钮链接授权</th>
		        </tr>
      		</table>
		</div>
    </div>
  </div>
</body>
</html>