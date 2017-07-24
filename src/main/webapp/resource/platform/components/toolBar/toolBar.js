
/**
 * 自定义item的图标：img
 * 自定义item的不可选图标：img-dis
 * 自定义item文字：text
 * 若必须先选中一行数据才能点击按钮，则加check-select="true"（配合toolbar的bind-grid）
 * toobar上的属性bind-grid为绑定的grid
 */
zfy.component.ToolBar = function(id){
	this.init(id);
	this.formId = this.getProperty("form-id");
	this.bindGrid = this.getProperty("bind-grid");
	this.userAuth = {};
	this.allowAuth = this.getProperty("auth",false);
	if(this.allowAuth){
		this.initAuth();
	}
	this.items = new Array();
	this.paint();
	this.initEvent();
};
zfy.component.ToolBar.prototype = new zfy.component.BaseComponent();
zfy.component.ToolBar.prototype.initAuth = function(){
	var fullUrl = window.location.href + "";
	var functionUrl = fullUrl.substring($_basePath.length,fullUrl.length);
	if(functionUrl.indexOf("?") >= 0){
		functionUrl = functionUrl.substring(0,functionUrl.indexOf("?"));
	}
	var self = this;
	$.ajax({
	    type:"POST",
	    async:false,
	    url: $util.getAppName() + "/toolbarAction/authorize.do",
	    data:{url:functionUrl},
	    success:function(data){
	    	if(data.success){
	    		self.userAuth = data.data;
    		}else{
    			$msg.showError(data.info);
    		}
    	},
    	error:function(){
    		$msg.showError("初始化工具栏权限出错");
    	}
	});
}
zfy.component.ToolBar.prototype.paint = function(){
	var self = this;
	this.ele.append("<div id="+(this.id+1)+"></div>");
	this.toolbar = new dhtmlXToolbarObject((this.id+1));
	var imgurl = $util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/";
	//windowf.setImagePath(imgurl);
	//this.toolbar.setSkin("dhx_terrace");
	this.toolbar.setIconsPath(imgurl+"ico/");
	$("#"+this.id+" item").each(function(i){
		var index = self.items.length;
		var itemName =  $(this).attr("name");
		switch(itemName){
			case "new" : self.createItem(index,this,"new","新&nbsp;&nbsp;建"/**,"new.gif","new_dis.png"**/);  break;
			case "save" : self.createItem(index,this,"save","保&nbsp;&nbsp;存"/**,"save.gif","save.gif"**/); break;
			case "edit" : self.createItem(index,this,"edit","编&nbsp;&nbsp;辑"/**,"edit.gif","edit_dis.png"**/); break;
			case "view" : self.createItem(index,this,"view","查&nbsp;&nbsp;看"/**","view.gif","view.gif **/); break;
			case "delete" : self.createItem(index,this,"delete","删&nbsp;&nbsp;除"/**,"remove.gif","remove_dis.png"**/); break;
			case "back" : self.createItem(index,this,"back","返&nbsp;&nbsp;回"/**,"back.gif","back.gif"**/); break;
			case "downloadExcel" : self.createItem(index,this,"downloadExcel","下载模板","downloadExcel.gif","downloadExcel.gif"); break;
			case "importExcel" : self.createItem(index,this,"importExcel","导入Excel","import_excel.gif","import_excel.gif"); break;
			case "foldExcel" : self.createItem(index,this,"foldExcel","上传数据","folder_excel.gif","folder_excel.gif"); break;
			case "backMain" : self.createItem(index,this,"backMain","返回主资产页面","back.gif","back.gif"); break;
			case "exportExcel" : self.createItem(index,this,"exportExcel","导出Excel","export.gif","export.gif"); break;
			case "validate" : self.createItem(index,this,"validateExcel","数据验证","validate.gif","validate.gif"); break;
			case "map" : self.createItem(index,this,"map","地图","ztt_map.png","ztt_map.png"); break;
			case "gis" : self.createItem(index,this,"gis","在地图中绘制","gis.jpg","gis.jpg"); break;
			case "operlog" : self.createItem(index,this,"operlog","操作日志","log.jpg","log.jpg"); break;
			case "statuslog" : self.createItem(index,this,"statuslog","状态日志","log.jpg","log.jpg"); break;
			case "showDelCamera" : self.createItem(index,this,"showDelCamera","查看已删除的摄像头","view.gif","view.gif"); break;
			case "viewCyd" : self.createItem(index,this,"viewCyd","查看采样点","view.gif","view.gif"); break;
			case "histogram" : self.createItem(index,this,"histogram","查看柱状图","chart.gif","chart.gif"); break;
			case "chooseColShow" : self.createItem(index,this,"chooseColShow","选择显示列","grid.gif","grid.gif"); break;
			default : self.createDefItem(index,this); break;
		}
	});
};

zfy.component.ToolBar.prototype.initEvent = function(){
	var self = this;
	this.toolbar.attachEvent("onClick", function(id) {
		for(var i=0;i<self.items.length;i++){
			if(self.items[i].id==id){
				$("input:focus").blur();
				setTimeout(function(){
					self.items[i].onclick();
				},1);
				break;
			};
		};
    });
	var form = $("#"+this.formId);
	setTimeout(function(){
		if(self.bindGrid){
			//初始化时，让必须先选中行的按钮默认为灰色不可选状态
			for(var i in self.items){
				if(self.items[i].jHtmlItem && self.items[i].jHtmlItem.attr("check-select") == "true"){
					self.disableItem(self.items[i].id);
				}
			}
			//当选中行时，让必须先选中行的按钮变为可选状态
			$com(self.bindGrid).attachEvent("onRowSelect",function(id){
				if(id == -1){
					return;
				}
				for(var i in self.items){
					if(self.items[i].jHtmlItem && self.items[i].jHtmlItem.attr("check-select") == "true"){
						self.enableItem(self.items[i].id);
					}
				}
			});
			//当删除行时，让必须先选中行的按钮变为不可选状态
			$com(self.bindGrid).attachEvent("onBeforeRowDeleted",function(){
				for(var i in self.items){
					if(self.items[i].jHtmlItem && self.items[i].jHtmlItem.attr("check-select") == "true"){
						self.disableItem(self.items[i].id);
					}
				}
			});
			//当删除行时，让必须先选中行的按钮变为不可选状态
			$com(self.bindGrid).attachEvent("onSelectionCleared",function(){
				for(var i in self.items){
					if(self.items[i].jHtmlItem && self.items[i].jHtmlItem.attr("check-select") == "true"){
						self.disableItem(self.items[i].id);
					}
				}
			});
			
		}
		if(self.formId){
			form.find("*[name]").each(function(){
				$(this).change(function(){
					self.changed = true;
				});
				if(this.comobj && this.comobj.addChangedListener){
					this.comobj.addChangedListener(function(){
						self.changed = true;
					});
				}
				//TODO xiangzy 日期选择和清除没有监听
			}); 
		}
	},1);
	
	//解决点击保存后控件还没有验证的问题（移上去就失去焦点开始验证）
	$(document).on("mouseover",".dhx_toolbar_btn",function(){
		$("input:focus").blur();
		$("textarea:focus").blur();
	});
	
	//点击灰色按钮事件
	$(document).on("click",".dhxtoolbar_btn_dis",function(){
		$msg.showWarning("请在表格中选择要操作的行");
	});
};

/**
 * 创建定义过的常用的item（如增删改查导入等）
 * @param index
 * @param htmlItem
 * @param name
 * @param defText
 * @param defImg
 * @param defImgDis
 */
zfy.component.ToolBar.prototype.createItem = function(index,htmlItem,name,defText,defImg,defImgDis){
	var itemAuth = $(htmlItem).attr("auth");
	if(this.userAuth && !this.auth(name) && (itemAuth!="false")){
		return;
	}
	var self = this;
	var text = $(htmlItem).attr("text")==null?defText:$(htmlItem).attr("text");
	var img = $(htmlItem).attr("img")==null?defImg:$(htmlItem).attr("img");
	this.toolbar.addButton(name, index, text, img, defImgDis);
	this.items[index] = new Object();
	this.items[index].id = name;
	this.items[index].jHtmlItem = $(htmlItem);
	this.items[index].onclickString = $(htmlItem).attr("action");
	this.items[index].onclick = function(){
		if($(htmlItem).attr("check-select") == "true" && self.bindGrid){
			var selectedId = $com(self.bindGrid).getSelectedRowId();
			if(selectedId == null){
				$msg.showWarning("请选择需要"+defText+"的行!");
				return;
			}
		}
		eval(self.items[index].onclickString); 
	};
	
};

/**
 * 创建自定义的Item
 * @param index
 * @param htmlItem
 * @param defImg
 * @param defImgDis
 */
zfy.component.ToolBar.prototype.createDefItem = function(index,htmlItem,defImg,defImgDis){
	var text = $(htmlItem).attr("text");
	var name = $(htmlItem).attr("name");
	var id = $(htmlItem).attr("id");
	var itemAuth = $("htmlItem").attr("auth");
	if(this.userAuth && (itemAuth!="false")){
		if(text==null){
			$msg.showError("自定义item标签必须指定text属性");
			return;
		}
		if(this.allowAuth && name==null){
			$msg.showError("自定义item标签必须指定name属性,name属性用于配置权限");
			return;
		}
		if(this.userAuth && !this.auth(name)){
			return;
		}
	}
	var self = this;
	if(!id){
		id = "_genid_"+htmlItem.id+"_"+index;
	}
	this.toolbar.addButton(id, index, $(htmlItem).attr("text"),
			$(htmlItem).attr("img")==null?defImg:$(htmlItem).attr("img"),
			$(htmlItem).attr("img-dis")==null?defImgDis:$(htmlItem).attr("img-dis"));
	this.items[index] = new Object();
	this.items[index].jHtmlItem = $(htmlItem);
	this.items[index].id = id;
	this.items[index].onclickString = $(htmlItem).attr("action");
	this.items[index].onclick = function(){
		eval(self.items[index].onclickString); 
	};
};

zfy.component.ToolBar.prototype.auth = function(name){
	if(!this.allowAuth){
		return true;
	}
	for(var key in this.userAuth){
		if(name.toLowerCase()==key.toLowerCase()){
			return true;
		}
	}
	return false;
};

//使某个按钮变为不可用
zfy.component.ToolBar.prototype.disableItem = function(name){
	this.toolbar.disableItem(name);
};
//使某个按钮变为可用
zfy.component.ToolBar.prototype.enableItem = function(name){
	this.toolbar.enableItem(name);
};
