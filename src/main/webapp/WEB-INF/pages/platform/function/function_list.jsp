<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow: hidden">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<fmt:setBundle basename="com/zfysoft/platform/local/base/i18n_function" var="i18n_function" />
<title><fmt:message key="platform.function.title" bundle="${i18n_function}" /></title>
<script type="text/javascript">
	var tree;
	// 判断是否已某个字符串开头
	String.prototype.startWith=function(str){     
    var reg = new RegExp("^" + str);     
    return reg.test(this);        
  };
  
	var selectedAppId = $("#application").val();
	
	// 页面加载完的处理
	$(function() {
		// 初始化树
		initTreeByAppId("${appId}");
		// 绑定子系统下拉框改变的事件
		$("#application").bind("change", function() {
			initTreeByAppId($(this).val());
		});
	});
	
	//绑定全选
	$(function() {
		$("#selectAll").click(function(){
		    var checked = $(this).is(':checked');
		    $("input[name='actionId']").each(function(){
		    	 this.checked = checked;
		    });
		});
	});
		
	// 根据子系统的ID初始化菜单树
	function initTreeByAppId(appId) {
		selectedAppId = appId;
		$("#treeObject").empty();
		//初始化menu
		menu = new dhtmlXMenuObject("treeObject");
		menu.renderAsContextMenu(true);
		menu.attachEvent("onClick",onMenuClick);

		menu.setIconsPath("${path}/resource/platform/js/dhtmlx4.0.3/imgs/dhxmenu_skyblue/");
    menu.addNewChild(menu.topId, 0, "addFunc", "<fmt:message key='platform.function.newFunction' bundle='${i18n_function}' />", false, "new.png", "new.png");
    menu.addNewChild(menu.topId, 1, "modifyFunc", "<fmt:message key='platform.function.modifyFunction' bundle='${i18n_function}' />", false, "modify.png", "modify.png");
    menu.addNewChild(menu.topId, 2, "delFunc", "<fmt:message key='platform.function.deleteFunction' bundle='${i18n_function}' />", false, "del.png", "del.png");
    menu.addNewChild(menu.topId, 3, "action", "<fmt:message key='platform.function.authorAction' bundle='${i18n_function}' />", false, "del.png", "del.png");
	
    document.getElementById("treeObject").comobj.paint(); 
    tree = $("#treeObject")[0].comobj;
	 	// 树加载完后的处理
		tree.attachEvent("onXLE", function(tree, id) {
			$("#treeObject .containerTableStyle").css("width","260px")
			$("#treeObject .containerTableStyle").css("height","100%");
		});
    
 		// 添加点击事件
    tree.attachEvent("onClick", doClick);
    tree.attachEvent("onRightClick", function(nodeId){
        tree.selectItem(nodeId);
        // 检查该节点的类型，以便控制各个按钮的状态
        checkNodeType(menu, nodeId);
        //doClick(nodeId);
    });
    
	  //绑定时间处理函数,注意一定要放在loadXML之前否则右键处理函数在ie下失效
		tree.enableContextMenu(menu);
		// 初始化树
		tree.loadXML("${path}/role/functionTree.do?appId=" + appId
				+ "&roleId=${role.id}");
	}
	
	//菜单事件处理函数
	function onMenuClick(menuId, type){
		var selectedTreeNodeId = tree.getSelectedItemId();
		if(selectedTreeNodeId==null || selectedTreeNodeId==""){
			$msg.showError("请选中");
			return ;
		}
    if (menuId == "addFunc") {
     
    	$ajax.post({
            type: "post",
            url: "${path}/function/pageForward.do?parentId="+selectedTreeNodeId,
            success: function(data) {
              if (data.success) {
            	  $("#fucid").html("新建菜单");
                $("#view").css("display", "none");
                $("#action").css("display", "none");
                $("#edit").css("display", "none");
                $("#modify").css("display", "block");
                $com("modify_layout").resize();
                var parentFunc = data.data;
                $("#menuIndex").val(1);
                $("#appName").val(parentFunc.app == null?"":parentFunc.app.label);
                $("#appId").val(parentFunc.app == null?"":parentFunc.app.id);
                $("#parentName").val(parentFunc.label==null?"":parentFunc.label);
                $("#parentId").val(parentFunc.id==null?"":parentFunc.id);
                $("#label").val("");
                $("#id").val("");
                $("#url").val("");
                $("input[name='enable'][value=1]").attr("checked",true); 
               
               } else {
                 $msg.showInfo(data.info);
               }
            }
        });
    } else if (menuId == "modifyFunc") {
    	 
    	$ajax.post({
            type: "post",
            url: "${path}/function/pageForward.do?functionId="+selectedTreeNodeId,
            success: function(data) {
              if (data.success) {
            	  $("#fucid").html("修改菜单");
            	$("#view").css("display", "none");
                $("#action").css("display", "none");
                $("#edit").css("display", "none");
                $("#modify").css("display", "block");
                $com("modify_layout").resize();
                var func = data.data;
                $("#label").val(func.label);
                $("#id").val(func.id);
                $("#icoName").val(func.icoName);
                $("#menuIndex").val(func.menuIndex);
                $("#appName").val(func.app == null?"":func.app.label);
                $("#appId").val(func.app == null?"":func.app.id);
                $("#parentName").val(func.parent == null?"":func.parent.label);
                $("#parentId").val(func.parent == null?"":func.parent.id);
                $("#url").val(func.url);
                $("input[name='enable'][value="+func.enable+"]").attr("checked",true); 
                
               } else {
                 $msg.showInfo(data.info);
               }
            }
        });
    } else if (menuId == "delFunc") {
    	if (confirm("<fmt:message key='platform.function.confirm' bundle='${i18n_function}'/>")) {
    		$ajax.post(
    				'${path}/function/delFunc.do',
 			{functionId: selectedTreeNodeId},
    				function (data){
    					if(data.success){
    						
    						var node = data.data;
    						deleteNode(node.nodeId);
    						
    					}else{
    						$msg.showInfo(data.info);
    					}
    				});
    	} else {
    		return;
    	}
    } else if (menuId == 'action') {
    	$("#view").css("display", "none");
        $("#modify").css("display", "none");
        $("#edit").css("display", "none");
        $("#action").css("display", "block");
        $com("action_layout").resize();
        $ajax.post(
			'${path}/function/getAction.do',
			{functionId: selectedTreeNodeId},
			function (data){
				if(data.success){
					var grid = $com("gridTable1");
					grid.deleteAllRows();
					var list = data.data.list;
					 $.each(list, function(i, action) {
		               var chbox = "<input type='checkbox' name='actionId' value='"+action.id+"'/>";
		               var array = new Array();
		               array[0] = chbox;
		               array[1] = action.label;
		               array[2] = action.code;
		               grid.addRow(action.id,array);            
		           });
				}else{
					$msg.showInfo(data.info);
				}
			});
    }
	}
	var currentNodeId = "";
	// 树点击事件
	function doClick(nodeId) {
		if(tree != null && nodeId != null){
			
			currentNodeId = nodeId;
       // 展开该节点，
       tree.openItem(nodeId);
       // 检查该节点的类型，以便控制各个按钮的状态
       //checkNodeType(menu, nodeId);
       // 在页面右边显示该节点的详细信息(只读)
       $ajax.post({
           type: "post",
           url: "${path}/function/viewFunction.do?functionId=" + nodeId,
           success: function(data) {
             if (data.success) {
               $("#modify").hide();
               $("#edit").hide();
               $("#action").hide();
               $("#view").show();
               $com("view_layout").resize();
               
               var func = data.data;
               $("#td_view_label").html(func.label);
               $("#td_view_menuIndex").html(func.menuIndex);
              
               var appLabel = "";
               if (func.app == null)
            	   appLabel = "";
               else 
            	   appLabel = func.app.label;
               $("#td_view_appLabel").html(appLabel);
               var parentLabel = "";
               if (func.parent == null)
            	   parentLabel = "";
               else 
            	   parentLabel = func.parent.label;
               $("#td_view_parentLabel").html(parentLabel);
               $("#td_view_url").html(func.url);
               var enable = "";
               if (func.enable == 1) {
                 enable = '<fmt:message key="platform.function.able" bundle="${i18n_function}" />';
               } else {
                 enable = '<fmt:message key="platform.function.unable" bundle="${i18n_function}" />';
               }
               $("#td_view_enable").html(enable);
              } else {
                $msg.showInfo(data.info);
              }
           }
       });
       
    }
	}
	// 根据选中节点的不同，改变各个按钮的状态。选中子系统节点时不能删除
	function checkNodeType(menu, nodeId) {
		 if(nodeId.startWith("app_")){
			 menu.showItem("addFunc");
		     menu.hideItem("modifyFunc");
		     menu.hideItem("delFunc");
		     menu.hideItem("action");
		 } else if (tree.hasChildren(nodeId)) {
			 menu.showItem("addFunc");
             menu.showItem("modifyFunc");
             menu.showItem("delFunc");
             menu.hideItem("action");
		 } else {
			 menu.showItem("addFunc");
             menu.showItem("modifyFunc");
             menu.showItem("delFunc");
             menu.showItem("action");
		 }
	}
	
	// 添加节点，参数(父节点的ID，新节点的ID，新节点的名字)
  function addNode(parentId, nodeId, nodeName){
    if(tree != null){
  	  // 添加节点
       tree.insertNewChild(parentId, nodeId, nodeName, 0, "func.png", "func.png", "func.png");
      // 选中新添加的节点， 并触发点击事件
      tree.selectItem(nodeId, true, false);
   
    }
  }
	// 更新节点，参数(节点ID，节点的新的名字)
  function updateNode(nodeId, nodeName){
    if(tree != null){
      // 更新节点信息
      tree.setItemText(nodeId, nodeName);
      // 选中该节点，并且触发点击事件
      tree.selectItem(nodeId, true, false);
      
    }
  }
	//删除节点，参数(节点ID)
  function deleteNode(nodeId){
    if(tree != null){
      // 删除节点
      tree.deleteItem(nodeId, true);
      $msg.showInfo("删除成功");
    }
  }
	
	// 保存
  function save(){
	    $form.ajaxSubmit("myform",function(data){
	      if(data.success){
	        var node = data.data;
	        if(data.operation=="update"){
	        	updateNode(node.nodeId, node.nodeName);
	          }else if(data.operation=="add"){
	        	  addNode(node.parentNodeId, node.nodeId, node.nodeName);
	          }
	        $msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_function}" />');
	      }else{
	        $msg.showWarning(data.info);
	      }
	    });
	  }
  
	// 返回
  function closePage() {
	  var selectedTreeNodeId = tree.getSelectedItemId();
	  tree.selectItem(selectedTreeNodeId, true, false);
	  $dialog.close();
  }
	
	// 新增action
	function addAction() {
		$("#view").css("display", "none");
        $("#modify").css("display", "none");
        $("#action").css("display", "none");
        $("#edit").css("display", "block");
        $com("edit_layout").resize();
        $("#action_label").val("");
        $("#action_id").val("");
        $("#action_url").val("");
        $("#actionid").html("新增action");
    	 
	}
	
	// 修改action
	function modifyAction() {
        var actionId = "";
        $('input[name="actionId"]:checked').each(function(){
        	if (actionId == "") 
        		actionId = $(this).val();
        	else 
        		actionId += "," + $(this).val();
        });
        if (actionId == "") {
        	$msg.showWarning("请勾选一条要修改的记录");
        	return;
        }
        if (actionId.indexOf(",") != -1) {
        	$msg.showWarning("只能勾选一条要修改的记录");
        	return;
        }
        $("#view").css("display", "none");
        $("#modify").css("display", "none");
        $("#action").css("display", "none");
        $("#edit").css("display", "block");
        $com("edit_layout").resize();
        $("#actionid").html("修改action");
        $ajax.post({
            type: "post",
            url: "${path}/function/getActionById.do?actionId=" + actionId,
            success: function(data) {
              if (data.success) {
            	  var action = data.data;
            	  $("#action_label").val(action.label);
                  $("#action_id").val(action.id);
                  $("#action_url").val(action.code);
               
            	  } else {
            	  $msg.showWarning(data.info);
             	}
              } 
            });
	}
	
	// 保存action 
	function saveAction() {
	  $ajax.post({
        type: "post",
        url: "${path}/function/saveAction.do",
        data: {label:  $("#action_label").val(), id:$("#action_id").val(),
        		url: $("#action_url").val(), parent_id: tree.getSelectedItemId()},
        success: function(data) {
          var grid = $com("gridTable1");
          if (data.success) {
        	  var action = data.data;
        	  var chbox = "<input type='checkbox' name='actionId' value='"+action.id+"'/>";
        	  if(data.operation=="update"){
  				grid.updateRow(action.id,[chbox,action.label,action.code]);
	  		  }else if(data.operation=="add"){
	  			grid.addRow(action.id,[chbox,action.label,action.code]);
	  		  }
        	  
        	  $("#view").css("display", "none");
              $("#modify").css("display", "none");
              $("#edit").css("display", "none");
              $("#action").css("display", "block");
              $com("action_layout").resize();
              $msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_function}" />');
        	} else {
        	  $msg.showWarning(data.info);
         	}
          } 
        });
	}
	
	// 删除action
	function deleteAction() {
		var actionId = "";
        $('input[name="actionId"]:checked').each(function(){
        	if (actionId == "") 
        		actionId = $(this).val();
        	else 
        		actionId += "," + $(this).val();
        });
        if (actionId == "") {
        	$msg.showWarning("请勾选一个要删除的记录");
        	return;
        }
        if(confirm("你确定要删除吗?")){
        $ajax.post({
            type: "post",
            url: "${path}/function/deleteAction.do?ids="+actionId,
            success: function(data) {
              var grid = $com("gridTable1");
              if (data.success) {
            	  $msg.showInfo("删除成功");
            	  var list = data.data.list;
            	  $.each(list, function(i, actionid) {
            	  grid.deleteRow(actionid);
            	  });
              }
           }
        });}
	}
	
	function goBack() {
		$("#view").css("display", "none");
        $("#modify").css("display", "none");
        $("#edit").css("display", "none");
        $("#action").css("display", "block");
        $com("action_layout").resize();
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
	<div id="left">
		<div id="treeObject" class="$tree" multi-selection="false" cascade="true" root-id="0" style="width: 100%; height: 98%; overflow-y: auto;"></div>
	</div>
	<div id="right">
		    <div id="view" style="width: 99%; height: 100%; margin-left:10px; display: none;">
		      <div id="view_layout" class="$borderLayout" style="width: 100%; height: 100% ;">
	            	<div class="top" style="height: 5px"></div>
	            	<div class="center">
	              		<table cellpadding="0" cellspacing="0" border="0" class="form_table">
			                <tr>
			                  <th><span class="label_requried">*</span><fmt:message key="platform.function.functionName" bundle="${i18n_function}" /></th>
			                  <td id="td_view_label"></td>
			                  <th><fmt:message key="platform.function.functionIndex" bundle="${i18n_function}" /></th>
			                  <td  id="td_view_menuIndex"></td>
			                </tr>
			                <tr>
			                  <th><span class="label_requried">*</span><fmt:message key="platform.function.functionApp" bundle="${i18n_function}" /></th>
			                  <td id="td_view_appLabel"></td>
			                  <th><span class="label_requried">*</span><fmt:message key="platform.function.parentName" bundle="${i18n_function}" /></th>
			                  <td  id="td_view_parentLabel"></td>
			                </tr>
			                <tr>
			                  <th><span class="label_requried">*</span>URL</th>
			                  <td colspan="3" id="td_view_url"></td>
			                </tr>
			                <tr>
			                  <th><fmt:message key="platform.function.enable" bundle="${i18n_function}" /></th>
			                  <td colspan="3" id="td_view_enable"></td>
			                </tr>
		               </table>
	            	</div>
          		</div>
		    </div>
		    <!-- 新建\修改 -->
		    <div id="modify" style="width: 99%; height: 100%; margin-left:10px; display: none;">
		      <div id="modify_layout" class="$borderLayout" style="width: 100%; height: 100% ;">
	            <div class="top" style="height: 30px">
		             <div class="$toolBar">
					    <item name="save" action="save()"></item>
					    <item name="back" action="closePage()"></item>
	  				</div>
	            </div>
            	<div class="center">
		            <form action="${path}/function/saveFunc.do" id="myform" method="post">
		              <input type="hidden" name="id" id="id" value="${function.id}" />
		              <input type="hidden" name="icoName" id="icoName" value="${function.icoName}" />
		               <table class="gridTitle" style="width:200px;margin-left:240px"><tr><td id="fucid"> </td></tr></table>
		                <table cellpadding="0" cellspacing="0" border="0" class="form_table"  >
		                <tr>
		                  <th><span class="label_requried">*</span><fmt:message key="platform.function.functionName" bundle="${i18n_function}" /></th>
		                  <td><input name="label" id="label" class="$input" type="text" value="${function.label}" required="true"/>
		                                 </td>
		                  <th><fmt:message key="platform.function.functionIndex" bundle="${i18n_function}" /></th>
		                  <td><input name="menuIndex" id="menuIndex" class="$input" type="text" value="${function.menuIndex }"/></td>
		                </tr>
		                <tr>
		                  <th><span class="label_requried">*</span><fmt:message key="platform.function.functionApp" bundle="${i18n_function}" /></th>
		                  <td><input name="appName" id="appName" class="$input" type="text" value="${function.app.label}" readonly="readonly" required="true"/>
		                                  <input type="hidden" name="appId" id="appId" value="${function.app.id}" /></td>
		                  <th><span class="label_requried">*</span><fmt:message key="platform.function.parentName" bundle="${i18n_function}" /></th>
		                  <td><input name="parentName" id="parentName" class="$input" type="text" readonly="readonly" value="${function.parent.label}"/>
		                                  <input type="hidden" name="parentId" id="parentId" value="${function.parent.id}" /></td>
		                </tr>
		                <tr>
		                  <th ><span class="label_requried">*</span>URL</th>
		                  <td colspan="3"   class="colspan3"><input name="url" id="url" class="$input" type="text"   class="$input"   style="width:100%"   value="${function.url }" required="true"/></td>
		                </tr>
		                <tr>
		                  <th><fmt:message key="platform.function.enable" bundle="${i18n_function}" /></th>
		                  <td colspan="3"><fmt:message key="platform.function.able" bundle="${i18n_function}" />：<input name="enable" id="enable" type="radio" value="1" <c:if test="${function.enable == 1}">checked</c:if>/> &nbsp;&nbsp;<fmt:message key="platform.function.unable" bundle="${i18n_function}" />：<input name="enable" id="enable" type="radio" value="0" <c:if test="${function.enable == 0}">checked</c:if>/></td>
		                </tr>
		              </table>
		              </form>
            		</div>
          		</div>
		    </div>
		    <!-- action -->
		      <div id="action" style="width: 99%; height: 100%; margin-left:5px; display: none;">
		      	<div id="action_layout" class="$borderLayout" style="width: 100%; height: 100% ;">
		      		<div class="top" style="height: 30px">
	                    <div class="$toolBar">
				       		 <item name="new" action="addAction()" text="<fmt:message key='platform.function.addAction' bundle='${i18n_function}' />"></item>
				         	 <item name="edit" action="modifyAction()" text="<fmt:message key='platform.function.modifyAction' bundle='${i18n_function}' />"></item>
				        	 <item name="delete" action="deleteAction()"  text="<fmt:message key='platform.function.deleteAction' bundle='${i18n_function}' />"></item>
	      				</div>
      				</div>
      				<div class="center">
		              	<table id="gridTable1" width="100%" height="auto" class="$gridTable">
					        <tr>
					          <th  style='text-align: center'><input type="checkbox" id="selectAll" onchange="grantSelectAll(this)"/></th>
					          <th><fmt:message key="platform.function.actionName" bundle="${i18n_function}" /></th>
					          <th><fmt:message key="platform.function.actionUrl" bundle="${i18n_function}" /></th>
					        </tr>
					     </table>
				     </div>
		      	</div>
		      </div>
		      <!-- 编辑action -->
		      <div id="edit" style="width: 99%; height: 100%; margin-left:5px; display: none;">
		      	<div id="edit_layout" class="$borderLayout" style="width: 100%; height: 100% ;">
		      		<div class="top" style="height: 30px">
			      	    <div class="$toolBar">
						    <item name="save" action="saveAction()"></item>
						    <item name="back" action="goBack()"></item>
						</div>
					</div>
					<div class="center">
		              	 <form action="${path}/function/saveFunc.do" id="myform" method="post">
		              	   <table class="gridTitle" style="width:200px;margin-left:240px"><tr><td id="actionid"> </td></tr></table>
				            <table cellpadding="0" cellspacing="0" border="0" class="form_table"  >
				               <input type="hidden" name="action_id" id="action_id" value="${action.id}" />
	             
	          
	           
				                <tr>
				                  <th><span class="label_requried">*</span><fmt:message key="platform.function.actionName" bundle="${i18n_function}" /></th>
				                  <td><input name="action_label" id="action_label" class="$input" type="text" value="${action.label}" required="true"/>
				                               </td>
				                  <th><span class="label_requried">*</span><fmt:message key="platform.function.actionUrl" bundle="${i18n_function}" /></th>
				                  <td><input name="action_url" id="action_url" class="$input" type="text" value="${action.url}"/></td>
				                </tr>
						     </table>
			      		</form>
		      		</div>
		      	</div>
		   </div>
	   </div>
</body>
</html>