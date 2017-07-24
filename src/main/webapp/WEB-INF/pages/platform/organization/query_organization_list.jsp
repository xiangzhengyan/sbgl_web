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
	var tr;
	var grantTable;
	$(document).ready(function() {
		tr = $("#users_table").html();
		grantTable = $("#grant_user_table_div").html();
	});
	var menu;
	var selectedAppId = "";
	var tree;
	var permissionButtons;
	// 页面加载完的处理
	$(function() {
		permissionButtons = eval('${buttons}');
		// 初始化树
		initTreeByAppId("${appId}");
	});
	//绑定全选
	$(function() {
		$("#selectAll").click(function(){
		    var checked = $(this).is(':checked');
		    $("input[name='grantUserId']").each(function(){
		    	 this.checked = checked;
		    });
		});
		
		$("#delSelectAll").click(function(){
		    var checked = $(this).is(':checked');
		    $("input[name='delId']").each(function(){
		    	 this.checked = checked;
		    });
		});
		
		$("#selectAll3").click(function(){
		    var checked = $(this).is(':checked');
		    $("input[name='delId2']").each(function(){
		    	 this.checked = checked;
		    });
		});
		
	}); 
	 
	// 根据子系统的ID初始化菜单树
	function initTreeByAppId(appId) {
		selectedAppId = appId;
		$("#treeObject").empty();
		//menu.addNewChild("addOrga", 0, "delOrga1", "删除1", false, "del.png", "del.png");

		document.getElementById("treeObject").comobj.paint(); 
	  tree = $("#treeObject")[0].comobj;
	  
		// 树加载完后的处理
		tree.attachEvent("onXLE", function(tree, id) {
		});
		// 添加点击事件
		//tree.attachEvent("onClick", doClick);
		tree.setOnClickHandler(doClick);
		tree.attachEvent("onRightClick", function(nodeId) {
			tree.selectItem(nodeId);
			// 检查该节点的类型，以便控制各个按钮的状态
			checkNodeType(nodeId, tree);
			//doClick(nodeId);
		});

		// 初始化树
		tree.loadXML("${path}/organization/queryOrgaTree.do");
	}

	//切换右侧页面类型
	function changeRightDiv(divId) {
		$("#saveOrgaDiv").hide();
		$("#queryOrgaDiv").hide();
		$("#grantUserDiv").hide();
		$("#saveUserDiv").hide();
		$("#funTreeDiv").hide();
		$("#grantButtonDiv").hide();
		
		$("#" + divId).show();
		setTimeout(function(){
			$("#right").resize();
			if(divId == "queryOrgaDiv"){
				$com("queryOrgaDiv").resize();
			}else if(divId == "grantUserDiv"){
				$com("grantUserDiv").resize();
			}else if(divId == "funTreeDiv"){
				$com("funTreeDiv").resize();
			}else if(divId == "saveUserDiv"){
				$com("saveUserDiv").resize();
			}else if(divId == "saveOrgaDiv"){
				$com("saveOrgaDiv").resize();
			}else if(divId == "grantButtonDiv"){
				$("#grantButtonDiv").resize();
			}
		},1)
		
	}

	// 树点击事件
	function doClick(nodeId) {
		if (nodeId == "org_root") {
			changeRightDiv("right");
			return;
		}
	
		if (tree != null && nodeId != null) {
			// 展开该节点，
			tree.openItem(nodeId);
			// 检查该节点的类型，以便控制各个按钮的状态
			//checkNodeType(nodeId);
			// 在页面右边显示该节点的详细信息(只读)
			//$("#rightChildren").attr("src", '${path}/function/viewFunction.do?id=' + nodeId);
			$.post("${path}/organization/queryById.do", {
				id : nodeId,
				from: "menu"
			}, function(data) {
				pageCallBack2(data,"menu");
			});
		}
	}
	//分页转页面之前
	function beforeSubmit2(page){
		var selectIds = "";
		var cancelIds = "";
		$("[name='delId']").each(function(i){
			var checked = $(this).is(":checked");
			if(checked){
				selectIds += $(this).val() + ",";
			}else{
				cancelIds += $(this).val() + ",";
			}
		});
		if(selectIds.indexOf(",")>=0){
			selectIds = selectIds.substring(0,selectIds.length-1);
		}
		if(cancelIds.indexOf(",")>=0){
			cancelIds = cancelIds.substring(0,cancelIds.length-1);
		}
		if(page == null){
			return {selectIds : selectIds , cancelIds : cancelIds};
		}else{
			page.addParams({selectIds : selectIds , cancelIds : cancelIds});
		}
		
	}
	//查询转页码之后
	function pageCallBack2(data,from) {
		//点击查询自己，以及属于自己的与员工
		$("#td_query_name").html(data[0].name);
		$("#td_query_type").html(data[3]);
		$("#td_query_belong").html(data[1]);
		//var txt = tr;
		//用户列表 
		if(!data[7]){
			$("#queryOrgaDiv_UserList").hide();
			$(".queryOrgaDiv_UserList").hide();
		}else{
			$("#queryOrgaDiv_UserList").show();
			$(".queryOrgaDiv_UserList").show();
			var userTableObj = $com("users_table");
			userTableObj.deleteAllRows();
			$.each(data[2], function(i, user) {
				var realname = user.realName == null ? "" : user.realName;
				var telphone = user.telphone == null ? "" : user.telphone;
				var email = user.email == null ? "" : user.email;
				var sex = user.sex == null ? "" :  user.sex=="1"?"男":"女";
				var loginName = user.loginName == null ? "" : user.loginName;
				var chbox ="";
				for(var j=0;j<data[6].length;j++){
					if(data[6][j]==user.id){ 
						chbox = "<input type='checkbox' checked='checked' name='delId' value='"+user.id+"'/>";
					}
				}
				if(chbox==""){
					chbox = "<input type='checkbox' name='delId' value='"+user.id+"'/>";
				}
				var array = new Array();
				//array[0]= chbox;
				array[0]= loginName;
				array[1]= realname;
				array[2]= email;
				array[3]= telphone;
				array[4]= sex;
				userTableObj.addRow(i,array);
			});
			if(from!=null && from=="menu"){
			//	$("#delSelectAll").removeAttr("checked");
			//	$("#delSelectAll")[0].checked = false; 
			}
			//未分配的用户分页条
			/* $("#queryUserPageBar").attr("total-rows", data[4].totalRows);
			$("#queryUserPageBar").attr("page-size", data[4].pageSize);
			$("#queryUserPageBar").attr("current-page", data[4].pageNum);
			//把所有ids加载到grantUserSelectIds,判断
			$("#queryUserPageBar").attr("params", "{id:" + data[5] + "}");
			new zfy.component.AjaxPageBar("queryUserPageBar"); */
		}
		changeRightDiv("queryOrgaDiv");
	}
	
	// 根据选中节点的不同，新建不同的类型
	function checkNodeType(nodeId, tree) {

		if(menu != null){
			menu.hide(); 
		}
		 
		menu = new dhtmlXMenuObject();
		if(tree.isSelectRoot()){
			menu.renderAsContextMenu();
			//menu.attachEvent("onClick",onMenuClick);

			menu.setIconsPath("${path}/resource/platform/js/dhtmlx4.0.3/imgs/dhxmenu_skyblue/");
			menu.addNewChild(menu.topId, 0, "addOrga", "新建", false, "new.png",
					"new.png");
		}else{
			menu.renderAsContextMenu();
			//menu.attachEvent("onClick",onMenuClick);

			menu.setIconsPath("${path}/resource/platform/js/dhtmlx4.0.3/imgs/dhxmenu_skyblue/");
			menu.addNewChild(menu.topId, 0, "addOrga", "新建", false, "new.png",
					"new.png");
			menu.addNewChild(menu.topId, 1, "modifyOrga", "修改", false,
					"modify.png", "modify.png");
			menu.addNewChild(menu.topId, 2, "delOrga", "删除", false, "del.png",
					"del.png");
			//menu.addNewChild(menu.topId, 3, "grantUser", "分配员工", false, "new.png",
			//		"new.png");
			//for(var index in permissionButtons){
				//if(permissionButtons[index].url == "organization/queryFunTree.do"){
				//	menu.addNewChild(menu.topId, 4, "grantFunction", "授权菜单", false, "new.png",
				//	"new.png");
					//break;
				//}
			//}
		}
		
		$.ajax({
			url : "${path}/organization/querySubTypes.do?id=" + nodeId,
			async : false,//async:false 同步请求
			contentType : 'application/x-www-form-urlencoded; charset=utf-8',
			success : function(list) {
				var hasChild = false;
				$.each(list, function(i, str) {
					if(str[1]!="employee"){
						menu.addNewChild("addOrga", 4 + i, "" + str[1], "新建"
								+ str[0], false, str[2] ,str[2]);
						hasChild = true;
					}
				});
				if(!hasChild){
					menu.removeItem("addOrga");
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				//alert(this.type + ' url:' + this.url + '; error:' + textStatus + ';' + xhr.status);
				return false;
			}
		});
		menu.showItem("addFunc");
		menu.showItem("modifyFunc");
		menu.hideItem("delFunc");
		menu.attachEvent("onClick", onMenuClick);
		tree.enableContextMenu(menu);
	}

	//菜单事件处理函数(菜单点击增删改后)
	function onMenuClick(menuId, zoom) {
		var selectId = tree.getSelectedItemId();
		var text = tree.getSelectedItemText();
		if (menuId == "grantFunction"){
			$ajax.post("${path}/organization/queryFunTree.do", {
				orgaId : selectId
			}, function(data) {
				var dat = data.data;
				$("#tree_title_orga_name").html(dat.orga.name);
				var options1 = "";
				for ( var i = 0; i < dat.apps.length; i++) {
					if(dat.apps[i].id==dat.appId){
						options1 += "<option value='"+dat.apps[i].id+"' selected='selected'>"+dat.apps[i].label+"</option>";
					}else{
						options1 += "<option value='"+dat.apps[i].id+"'>"+dat.apps[i].label+"</option>";
					}
				}
				$("#application").html(options1);
				changeRightDiv("funTreeDiv");
				initFunTree(dat.appId);
			});
		}else if (menuId == "modifyOrga") {
			$ajax.post("${path}/organization/viewUpdateOrga.do", {
				id : selectId
			}, function(data) {
				$("#td_title").html("修改" + data[3]);
				//$form.setChildrenValue("right_table",data[0]);
				$("#updateId").val(data[0]);
				$("#td_orga_name").html(data[1]);
				$("#right_title_save_orga").html(data[2] + data[3]);
				$("#td_orga_saveType").html(data[2] + data[6]);
				$("#td_orga_type").html(data[3]);
				$("#type").val(data[4]);
				$("#name1").val(data[5]);
				$("#saveOrUpdateBtn").unbind('click');
				$("#saveOrUpdateBtn").click(function() {
					updateOrga();
				});
				changeRightDiv("saveOrgaDiv");
			});
		} else if (menuId == "delOrga") {
			if (confirm("您确实要删除[" + text + "]吗？")) {
				$ajax.post("${path}/organization/delete.do", {
					id : selectId
				}, function(data) {
					$msg.showInfo("删除成功");
					tree.deleteItem(selectId,true);
				});
			}
		}  else if(menuId == "grantUser") {
			//分配用户给组织机构
			//点击查询自己，以及属于自己的与员工
			$ajax.post("${path}/organization/queryUserListNotGrantByOrgaId.do", {
				id : selectId,
				from : "menu"
			}, function(data) {
				pageCallBack(data,"menu");
				changeRightDiv("grantUserDiv");
			});
			
		}else if(menuId == "employee"){
			
			$ajax.post("${path}/organization/queryOrgaById.do", {
				id : selectId
			}, function(data) {
				$("#td_query_name3").html(data[0].name);
				$("#td_query_type3").html(data[2]);
				$("#td_query_belong3").html(data[1]);
				changeRightDiv("saveUserDiv");
			});
		}else {
			cleanInput();
			$ajax.post("${path}/organization/viewSaveOrga.do", {
				type : menuId,
				pid : selectId
			}, function(data) {
				$("#td_title").html("新建" + data[3]);
				//$form.setChildrenValue("right_table",data[0]);
				$("#pid").val(data[0]);
				$("#td_orga_name").html(data[1]);
				$("#right_title_save_orga").html(data[2] + data[3]);
				$("#td_orga_type").html(data[3]);
				$("#type").val(data[4]);
				$("#saveOrUpdateBtn").unbind('click');
				$("#saveOrUpdateBtn").click(function() {
					saveOrga();
				});
				changeRightDiv("saveOrgaDiv");
			});
		}

	}
	
	//分页转页面之前
	function beforeSubmit(page){
		var selectIds = "";
		var cancelIds = "";
		var usernameFilter = $("#usernameFilter").val();
		$("[name='grantUserId']").each(function(i){
			var checked = $(this).is(":checked");
			if(checked){
				selectIds += $(this).val() + ",";
			}else{
				cancelIds += $(this).val() + ",";
			}
		});
		if(selectIds.indexOf(",")>=0){
			selectIds = selectIds.substring(0,selectIds.length-1);
		}
		if(cancelIds.indexOf(",")>=0){
			cancelIds = cancelIds.substring(0,cancelIds.length-1);
		}
		if(page == null){
			return {selectIds : selectIds , cancelIds : cancelIds ,usernameFilter:usernameFilter};
		}else{
			page.addParams({selectIds : selectIds , cancelIds : cancelIds ,usernameFilter:usernameFilter});
		}
		
	}

	//分配转页之后
	function pageCallBack(data,from) {
		$("#td_query_name2").html(data[0].name);
		$("#td_query_type2").html(data[3]);
		$("#td_query_belong2").html(data[1]);
		var grantTableObj = $("#grant_user_table")[0].comobj;
		grantTableObj.deleteAllRows();
		//未分配的用户列表
		$.each(data[2],function(i, user) {
							var loginName = user.loginName == null ? ""
									: user.loginName;
							var realname = user.realName == null ? ""
									: user.realName;
							var telphone = user.telphone == null ? ""
									: user.telphone;
							var email = user.email == null ? "" : user.email;
							var sex = user.sex == null ? "" : user.sex=="1"?"男":"女";
							/* txt += "<tr><td style='text-align:center'><input type='checkbox' name='grantUserId' value='"+user.id+"'/></td>"
								+"<td>"+loginName+"</td>"
								+"<td>"+realname+"</td>"
								+"<td>"+email+"</td>"
								+"<td>"+telphone+"</td>"
								+"<td>"+sex+"</td></tr>"; */
							var chbox ="";
							for(var j=0;j<data[6].length;j++){
								if(data[6][j]==user.id){ 
									chbox = "<input type='checkbox' checked='checked' name='grantUserId' value='"+user.id+"'/>";
								}
							}
							if(chbox==""){
								chbox = "<input type='checkbox' name='grantUserId' value='"+user.id+"'/>";
							}
							var array = new Array();
							array[0] = chbox;
							array[1] = loginName;
							array[2] = realname;
							array[3] = email;
							array[4] = telphone;
							array[5] = sex;
							grantTableObj.addRow(i, array);
						});
		if(from!=null && from=="menu"){
			$("#selectAll").removeAttr("checked");
			$("#selectAll")[0].checked = false;
		}
		//未分配的用户分页条
		$("#grantUserPageBar").attr("total-rows", data[4].totalRows);
		$("#grantUserPageBar").attr("page-size", data[4].pageSize);
		$("#grantUserPageBar").attr("current-page", data[4].pageNum);
		//把所有ids加载到grantUserSelectIds,判断
		$("#grantUserPageBar").attr("params", "{id:" + data[5] + "}");
		new zfy.component.AjaxPageBar("grantUserPageBar");
		changeRightDiv("grantUserDiv");
		//$("#grant_user_table").html(txt); 
	}

	function saveOrUpdateOrga(){
		var name = $("#name1").val();
		if(name==null || name==""){
			$msg.showWarning("名称不能为空");
			$("#name1").focus();
			return;
		}
		var updateId = $("#updateId").val();
		if(updateId == null || updateId ==""){
			//新增
			saveOrga();
		}else{
			//修改
			updateOrga();
		}
	}
	
	//保存新建
	function saveOrga() {
		$("#name").val($("#name1").val());
		$form.ajaxSubmit("orgaForm", function(data) {
			if (data.success) {
				
				var orga = data.data[0];
				var url = data.data[1];
				cleanInput();
				var selectId = tree.getSelectedItemId();
				item = tree.insertNewChild(selectId, orga.id, orga.name, 0,
						url, url, url);
				tree.selectItem(orga.id, true, false);
				$msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_organization}" />');
			} else {
				$msg.showInfo(data.info);
				cleanInput();
			}
		});
		//initTreeByAppId();
	}

	//修改名称
	function updateOrga() {
		$("#updateName").val($("#name1").val());
		$form.ajaxSubmit("orgaUpdateForm", function(data) {
			if (data.success) {
				var sid = tree.getSelectedItemId();
				cleanInput();
				initTreeByAppId();
				doClick(sid);
				$msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_organization}" />');
			} else {
				$msg.showInfo(data.info);
				//cleanInput();
			}
		});
		
	}
	function cleanInput() {
		$("#name").val("");
		$("#name1").val("");
		$("#updateName").val("");
		$("#updateId").val("");
	}
	
	//保存分配选中的人到组织机构
	function saveGrant(){
		var params = beforeSubmit();
		params["orgaId"] = tree.getSelectedItemId();
		$ajax.post("${path}/organization/saveGrantUserList.do",params,function(data){
			if(data.success){   
				 $msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_organization}" />');
				doClick(tree.getSelectedItemId());
			}
		});
	}
	
	//删除组织和员工对应关系
	function del_orga_emp(){
		var length = $("input[name='delId']:checked").length; 
		if(length==0){
			$msg.showWarning("请勾选要移出的员工");
			return;
		}
		var params = beforeSubmit2();
		params["orgaId"] = tree.getSelectedItemId();
		$ajax.post("${path}/organization/deleteUserFromOrga.do",params,function(data){
			if(data.success){   
				 $msg.showInfo("<fmt:message key='platform.common.deleteSuccess' bundle='${i18n_common}' />");
				doClick(tree.getSelectedItemId());
			}
		});
	}
	
	//在组织下新建用户点保存
	function saveUser(){
		var params = {};
		params["orgaId"] = tree.getSelectedItemId();
		var loginName = $("#loginName").val();
		if(loginName == ""){
			$msg.showWarning("<fmt:message key='platform.user.validate_loginName' bundle='${i18n_user}' />");
			return;
		}
		var realName = $("#realName").val();
		if(realName == ""){
			$msg.showWarning("<fmt:message key='platform.user.validate_realName' bundle='${i18n_user}' />");
			return;
		}
		params["loginName"] = loginName;
		params["realName"] = $("#realName").val();
		params["telphone"] = $("#telphone").val();
		params["email"] = $("#email").val();
		params["sex"] = $("input:radio[name='sex']:checked").val()==null?"":$("input:radio[name='sex']:checked").val();
		$ajax.post("${path}/organization/saveUser.do",params,function(data){
			if(data.success){   
				 $msg.showInfo("<fmt:message key='platform.common.saveSuccess' bundle='${i18n_common}' />");
				doClick(tree.getSelectedItemId());
				$("#loginName").val("");
				$("#realName").val("");
				$("#telphone").val("");
				$("#email").val("");
				$("input[name='sex']").each(function(){
			    	 this.checked = false;;
			    });
				$msg.showInfo('<fmt:message key="platform.function.saveSuccess" bundle="${i18n_organization}" />');
			}else{
				$msg.showWarning(data.info);
			}
		}); 
	}
	
	
	
	///以下是授权菜单的
	var selectedAppId2 = "";
	var tree2;
	var preIds = "";
	var btnOnckilck = false;
	var n = 0;
	var functionId;
	// 页面加载完的处理
	function initFunTree(appId) {
		$("#treeObject2").empty();
		document.getElementById("treeObject2").comobj.paint(); 
		// 初始化树q
		initFunTreeByAppId(appId);  
		// 绑定子系统下拉框改变的事件
		$("#application").bind("change", function() {
			$("#treeObject2").empty();
			document.getElementById("treeObject2").comobj.paint(); 
			initFunTreeByAppId($(this).val());
		});
	};
	// 根据子系统的ID初始化菜单树
	function initFunTreeByAppId(appId) {
		tree2 = $("#treeObject2")[0].comobj;
		$("#saveBtn").attr("disabled", "true");
		selectedAppId2 = appId;
		
		// 树加载完后的处理
		tree2.attachEvent("onXLE", function(tree2, id) {
			$("#saveBtn").removeAttr("disabled");
		});
		//绑定单击事件
		tree2.attachEvent("onClick", doClick2);
		// 初始化树
		$mask.show();
		tree2.loadXML("${path}/organization/functionTree.do?appId=" + appId
				+ "&orgaId="+tree.getSelectedItemId()); 
		$mask.hide();
	}
	
	
function doClick2(nodeId) {
		if (n >= 1) {
			var ids = "";
			$('input[name="delId2"]:checked').each(function(){
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
		if(tree2 != null && nodeId != null){
       // 展开该节点，
       tree2.openItem(nodeId);
       // 在页面右边显示该菜单下的按钮、超链接
       $ajax.post({
    	   type: "post",
    	   url: "${path}/organization/querybtn.do?functionId="+nodeId+"&orgaId="+tree.getSelectedItemId(),
    	   success: function(data) {
    		   if (data.success) {
    			   	var btnTableObj = $com("gridTable1");
    			   	btnTableObj.deleteAllRows();
    			   	var all = data.data.btnList;
    			   	var exist = data.data.existList;
							$.each(all, function(i, btn) {
								var chbox = "<input type='checkbox' name='delId2' value='"+btn.id+"'/>";
								$.each(exist, function(i, exist) {
									if (btn.id == exist.id) {
									chbox = "<input type='checkbox' checked='checked' name='delId2' value='"+btn.id+"'/>";
									}
								});
							var array = new Array();
							array[0]= chbox;
							array[1]= btn.label;
							btnTableObj.addRow(i,array);						
						});
							
							$('input[name="delId2"]:checked').each(function(){
								preIds += $(this).val();
							});
    		   }else{
					  $msg.showInfo(data.info);
			   }
    		   $("#grantButtonDiv").show();
    		   $("#grantButtonDiv").resize();
    	   }
		});
    }
		n = n+1;
		functionId = nodeId;
	}

	function save() {
		btnOnckilck = true;
		tree2 = $("#treeObject2")[0].comobj;
		var id_array=new Array();
		$('input[name="delId2"]:checked').each(function(){
		    id_array.push($(this).val());//向数组中添加元素
		});
		$("#saveBtn").attr("disabled", "true");
		$ajax.post("${path}/organization/authorize.do", {
			orgaId : tree.getSelectedItemId(),
			selectedIds : tree2.getAllSelectedIds(),
			appId : selectedAppId2,
			functionId : functionId,
			unSelectedIds : tree2.getAllUnSelectedIds(),
			btnIds : id_array + ""
		}, function(data) {
			if (data == '1') {
				$msg.showInfo("<fmt:message key='platform.role.modifysuccess' bundle='${i18n_role}'/>");
				$("#saveBtn").removeAttr("disabled");
			}
		});
		
	}
	
	
function userToolBarChange(type){
	var selectId = tree.getSelectedItemId();
	if(type == "grantUserDiv"){
    	//分配用户给组织机构
    	//点击查询自己，以及属于自己的与员工
    	$ajax.post("${path}/organization/queryUserListNotGrantByOrgaId.do", {
    		id : selectId,
    		from : "menu"
    	}, function(data) {
    		pageCallBack(data,"menu");
    		changeRightDiv("grantUserDiv");
    	});
  	}
	else if(type == "saveUserDiv"){
  		$ajax.post("${path}/organization/queryOrgaById.do", {
  			id : selectId
  		}, function(data) {
  			$("#td_query_name3").html(data[0].name);
  			$("#td_query_type3").html(data[2]);
  			$("#td_query_belong3").html(data[1]);
  			changeRightDiv("saveUserDiv");
  		});
  	}
	else if(type == 'queryOrgaDiv'){
		$ajax.post("${path}/organization/queryById.do", {
			id : selectId,
			from: "menu"
		}, function(data) {
			pageCallBack2(data,"menu");
		});
	}
}

function usernameFilter(){
	var selectId = tree.getSelectedItemId();
	$ajax.post("${path}/organization/queryUserListNotGrantByOrgaId.do", {
		id : selectId,
		usernameFilter : $("#usernameFilter").val(),
		from : "menu"
	}, function(data) {
		pageCallBack(data,"menu");
	});
}
$(function(){
	$(window).resize();
})
</script>
</head>
<body>
  <table border="0" cellspacing="0" cellpadding="0" style="height: 100%; width: 99%;">
    <tr>
      <td colspan="2" height="1"></td>
    </tr>
    <tr>
      <td width="280" valign="top" style="border-right: 1px solid #A4BED4">
      <div class="$borderLayout" style="width: 100%; height: 100%;">
    <div class="top" style="height: 32px">
    <input name="key" id="key" class="$input" value=""/>
	<button class="search_button" onclick="tree.findItem(document.getElementById('key').value, null, null, false);">
		<fmt:message key='platform.common.query' bundle='${i18n_common}' />
	</button>
    </div>
	<div class="center">
	<div style="height: 100%;overflow: auto">
            	<div id="treeObject" class="$tree"  multi-selection="false" cascade="true" root-id="0" style="width: 100%; height: 100%; overflow-y: auto;"></div>
            </div>
	</div>	  	
            
      </td>
      <td>
        <!-- 右侧页面 -->
        <div id="right" style="width: 100%; height: 100%; margin-left: 10px;">
          <!-- 新建/修改组织开始 -->
          <div id="saveOrgaDiv" class="$borderLayout" style="display: none; width: 100%; height: 100%;">
             
            <div class="orga_table top" style="height:28px;">
	              <div class="$toolBar"  style="height:28px">
	               <item name="save" action="javascript:saveOrUpdateOrga()"> </item>
	               <item name="back" action="javascript:userToolBarChange('queryOrgaDiv')"></item> 
	              </div>
	        </div>
            <div class="center">
              <table class="gridTitle" style="width:200px;margin-left:240px"><tr><td id="td_title"  > </td></tr></table>
              <table cellpadding="0" cellspacing="0" border="0" class="form_table" id="right_table"   >
                <tr>
                  <th  id="td_orga_belong"><fmt:message key="platform.organization.belong" bundle="${i18n_organization}" /></th>
                  <td  colspan="3" id="td_orga_name" class="colspan3"></td>
                  </tr>
                  <tr>
                  <th   id="td_orga_saveType"><fmt:message key="platform.organization.saveType" bundle="${i18n_organization}" /></th>
                  <td   id="td_orga_type"></td>
                  <th   id="td_orga_new_name"><span  class="label_requried">*</span><fmt:message key="platform.organization.name" bundle="${i18n_organization}" /></th>
                  <td   id="td_orga_input_name"><input id="name1" class="$input" type="text" required="true" /></td>
                </tr>
                
              </table>
              <form action="${path}/organization/save.do" name="orgaForm" id="orgaForm" method="post">
                <input type="hidden" id="type" name="type" value="" /> <input type="hidden" id="pid" name="pid" value="" /> <input type="hidden" name="name" id="name" class="$input" type="text"
                  required="true" />
              </form>
              <form action="${path}/organization/updateOrgaName.do" name="orgaUpdateForm" id="orgaUpdateForm" method="post">
                <input type="hidden" id="updateName" name="name" value="" /> <input type="hidden" id="updateId" name="id" value="" />
              </form>
            </div>
          </div>
          <!-- 新建/修改组织结束 -->
          
          
          
          
          <!-- 新建用户开始 -->
          <div id="saveUserDiv" class="$borderLayout" style="display: none; width: 100%; height: 100%;">
              <!-- 修改后的样式 -->
              <div class="top" style="height:28px;"></div>
                  <div class="$toolBar">
               		<item name="save" action="javascript:saveUser()"> </item>
               		<item name="back" action="javascript:userToolBarChange('queryOrgaDiv')"></item> 
             		</div>  
             <div class="center save_user_table">
                   <table class="gridTitle" style="width:200px;margin-left:240px"><tr><td   > 新增员工</td></tr></table>
                   <table cellpadding="0" cellspacing="0" border="0" class="form_table"  >
   
                
                  
        		  <tr>
        		    <th><span class="label_requried">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.loginName"  bundle="${i18n_user}"/></th>
        		    <td><input name="loginName" id="loginName" class="$input" type="text" value="" required="true"/></td>
        		    <th><span class="label_requried">*</span>&nbsp;&nbsp;<fmt:message key="platform.user.real_name" bundle="${i18n_user}"/></th>
        		    <td>
        		      <input name="realName" id="realName" class="$input" value="" required="true"/>
        		    </td>
        		  </tr>
        		  <tr>
        		    <th ><fmt:message key="platform.user.email" bundle="${i18n_user}"/></th>
        		    <td><input name="email" id="email" class="$input" value=""	required="true"/></td>
        		    <th ><fmt:message key="platform.user.telphone" bundle="${i18n_user}"/></th>
        		    <td><input name="telphone" id="telphone" class="$input" value=""/></td>
        		  </tr>
        		  <tr>
        		    <th><fmt:message key="platform.user.sex" bundle="${i18n_user}"/></th>
        		    <td colspan="3">
                  <fmt:message key="platform.user.male" bundle="${i18n_user}"/>
                  :<input name="sex" type="radio" value="1" /> &nbsp;&nbsp;
                  <fmt:message key="platform.user.female" bundle="${i18n_user}"/>
                  :<input name="sex" type="radio" value="0"/>
                    </td>
        		  </tr>
    		 	<!--tr>
    		         <td colspan="4" style="text-align: right;">
    		         	<input type="button" value="<fmt:message key='platform.common.save'  bundle="${i18n_common}"/>" class="$button" onclick="saveUser()"/>&nbsp;&nbsp; 
    		         </td>
    		   </tr-->
		</table>
              
            </div>
          </div>
          <!-- 新建用户结束 -->





          <!-- 查询组织开始 -->
          <div id="queryOrgaDiv" class="$borderLayout" style="display: none; width: 100%; height: 100%;">
          <div class="sp" ></div>
            <div class="top" style="height: 75px;">
              <div class="orga_table">
                <table cellpadding="0" cellspacing="0" border="0" class="form_table" id="right_table" >
                  <tr>
                    <th  id="td_query_belong_label"><fmt:message key="platform.organization.belong" bundle="${i18n_organization}" /></th>
                    <td   id="td_query_belong" colspan="3" class="colspan3"></td>
                    </tr><tr>
                    <th  id="td_query_name_label"><fmt:message key="platform.organization.name" bundle="${i18n_organization}" /></th>
                    <td  id="td_query_name"></td>
                    <th   id="td_query_type_label"><fmt:message key="platform.organization.type" bundle="${i18n_organization}" /></th>
                    <td  id="td_query_type"></td>
                  </tr>
                </table>
              </div>
              
            
             
              <!-- 
                <div id="queryOrgaDiv_UserList" class="$toolBar"  style="height: 44px;margin-top:4px">
                
                 <div class="$toolBar">
                  <item name="new" action="userToolBarChange('saveUserDiv')" text="<fmt:message key='platform.user.addEmployee' bundle='${i18n_user}' />"></item>
                  <item text="<fmt:message key='platform.user.allotEmployee' bundle='${i18n_user}' />" img="role.gif" action="userToolBarChange('grantUserDiv')"></item>
                  <item name="delete" action="del_orga_emp()"  text="<fmt:message key='platform.user.deleteCheckedUser' bundle='${i18n_user}' />"></item>
                           
                </div>

              </div> 
               -->
            </div>
            <div class="center queryOrgaDiv_UserList">
              <div style="height: 20px;margin-top:4px;font-weight: bold;"> 员工列表 </div>
              <div class="orga_table">
                <table width="100%" height="auto" class="$gridTable"  id="users_table"> 
                  <tr>
                  <!--   
                    <th  style="display:none; text-align: center"><input type="checkbox" id="delSelectAll" _onchange="delSelectAll(this)"/></th>
                  -->
                    
                    <th  id="td_orga_emp_loginName_label"><fmt:message key="platform.user.loginName" bundle="${i18n_user}" /></th>
                    <th id="td_orga_emp_name_label"><fmt:message key="platform.user.real_name" bundle="${i18n_user}" /></th>
                    <th  id="td_orga_emp_email_label"><fmt:message key="platform.user.email" bundle="${i18n_user}" /></th>
                    <th  id="td_orga_emp_phone_label"><fmt:message key="platform.user.telphone" bundle="${i18n_user}" /></th>
                    <th  id="td_orga_emp_sex_label"><fmt:message key="platform.user.sex" bundle="${i18n_user}" /></th>
                  </tr>
                </table>
              </div>
            </div>
            <!-- 分页条 -->
        
          </div><!-- borderlayout -->
          <!-- 查询组织结束 -->




          <!-- 分配用户给组织机构开始 -->
          <div id="grantUserDiv" class="$borderLayout" style=" height: 100%; width: 100%; display: none">
            <div class="top"  >
              <!-- 修改后的样式 -->
                <div class="$toolBar">
               <item name="save" action="javascript:saveGrant()"> </item>
               <item name="back" action="javascript:userToolBarChange('queryOrgaDiv')"></item>  
             </div> 
            </div>
            <!-- top end -->
            <div class="center"  style="margin-top:0px">
              <div class="orga_table" id="grant_user_table_div">
              <table cellpadding="0" cellspacing="0" border="0" class="search_table">
            <tr>
          <th>用户名或用户姓名</th>
          <td>
          <input class="$input" id="usernameFilter" name="usernameFilter" value="${userInfo.keyWords}" />&nbsp;&nbsp;
          </td>
          <td>
          	<button class="search_button" type="button" onclick="usernameFilter()">
                <fmt:message key='platform.common.query' bundle='${i18n_common}' />
              </button>
          </td>
        </tr>

        </table>
                 <table class="gridTitle" style="width:200px;margin-left:220px"><tr><td   > 分配员工</td></tr></table>
                <table height="auto" class="$gridTable" id="grant_user_table"    >
                
              
   
                
                  <tr>
                    <th  style='text-align: center'><input type="checkbox" id="selectAll" onchange="grantSelectAll(this)"/></th>
                    <th width="150px"><fmt:message key="platform.user.loginName" bundle="${i18n_user}" /></th>
                    <th width="150px"><fmt:message key="platform.user.real_name" bundle="${i18n_user}" /></th>
                    <th width="100px"><fmt:message key="platform.user.email" bundle="${i18n_user}" /></th>
                    <th width="100px"><fmt:message key="platform.user.telphone" bundle="${i18n_user}" /></th>
                    <th width="30px"><fmt:message key="platform.user.sex" bundle="${i18n_user}" /></th>
                  </tr>
                </table>
              </div>
            </div>
            <!-- 分页条 -->
            <div class="bottom" style="height: 30px" size="30">
              <div id="grantUserPageBar" class="$ajaxPageBar"
               url="${path}/organization/queryUserListNotGrantByOrgaId.do"
                callback="pageCallBack" beforeSubmit="beforeSubmit(this)"
                total-rows="" page-size="" current-page=""></div>
            </div>
      
    
          </div>
          <!-- 分配用户给组织机构开始结束 -->
          
          
          
           <!-- 查询授权菜单开始 -->
          <div id="funTreeDiv" class="$borderLayout" style="display:none;width: 100%; height: 100%">
              <div class="top"  style="height: 55px">
	              <div class="$toolBar"   >
          
         <item name="save" action="javascript:save()"></item>
          <item name="back" action="javascript:changeRightDiv()"></item>
         
               
      </div>
      <div  style="font-weight: bold;color:grey;margin-left:258px;height:20px;margin-bottom:5px"> 
         
           组织机构授权菜单 
           
       </div>
              </div>
              <div class="center">
                <table   height="500px">
            		<tr>
            			<td style="border-right: 1px solid #A4BED4; width: 300px;">
            				<div style="height: 498px;width:298px;overflow: auto">
            					<div id="treeObject2" class="$tree"  multi-selection="true" cascade="true" root-id="0" style="height: 98%;"></div>
            				</div>
            			</td>
            			<td valign="top" id="grantButtonDiv" style="display:none">
            				<!-- <div class="$titleBar">
        			        <table>
        			          
        			        </table>
        				  	</div> -->
        				  	 
            				<table id="gridTable1"  height="auto" class="$gridTable">
            				 
        			        <tr>
        			          <th class="check_col"><input type="checkbox" id="selectAll3"/></th>
        			          <th style="width:300px">菜单页面按钮链接授权</th>
        			        </tr>
        			      </table>
            			</td>
            		</tr>
    	        </table>
              </div>
          </div>
          <!-- 查询授权菜单结束 -->
          
          
        </div> <!-- 右侧结束 -->
      </td>
    </tr>
  </table>
</body>
</html>
  