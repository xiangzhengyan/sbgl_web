/**
 * 参数列表
 * @author chenhm
 * levels 可被选中的区域类型1,2,3,4,5
 */
zfy.component.AdministrativeAreaSelect = function(id){
	this.init(id);
	this.root = this.getProperty("root","53");//根节点，默认为53（云南省）
	this.required = this.getProperty("required",false);
	this.canSelectLevels = this.getProperty("can-select-levels", "1,2,3,4,5");//可选级别，默认全部
	this.showLevels = this.getProperty("show-levels", "1,2,3,4,5");//可见级别，默认全部
	this.complete = this.getProperty("complete", "1");//是否显示完整行政区划名称，默认为1（是）
	this.url = this.getProperty("url", $util.getAppName()+"/xzqh/getSubItem.do");//动态请求地址
	this.changedListeners = new Array();
	this.paint();
	this.initValue = this.ele.val();
};
zfy.component.AdministrativeAreaSelect.prototype = new zfy.component.BaseComponent();

zfy.component.AdministrativeAreaSelect.prototype.paint = function(id) {
	var self = this;
	this.value = this.ele.val();
	if(!this.value){
		this.value = "";
	}
	//select为用于显示的input框
	this.selectId = new Date().getTime() + "_select_" + this.id;
	this.ele.after('<input id="'+this.selectId+'" readonly="readonly" autocomplete="off" class="administrativeArea" style="width:'+this.element.style.width+'"></input>');
	this.select = $("#"+this.selectId);
	//设置显示的名字
	if(this.value){
		if(this.complete == "1"){
			this.select.val(getXzqhMc(this.value,true));
	    }else{
	    	this.select.val(getXzqhMc(this.value,false));
	    }
	}
	this.select.on("click",function(){
		self.box.show();
	});
	$(document).on("click",function(event){
		if(!$(event.target).isChildAndSelfOf ("#"+self.box[0].id) 
				&& !$(event.target).isChildAndSelfOf ("#"+self.selectId)){
			if(self.box.is(":visible")){
				self.box.hide();
				self.verify();
			}
		}
	});
	
	// 创建div
	var box = document.createElement("div");
	box.id = "box_" + this.id;
	box.style.position="absolute";
	box.style.display = "none";
	box.style.overflow = "hidden";
	box.style.zIndex = 1;
	box.style.border = "1px solid #999999";
	box.style.width = "300px";
	box.style.height = "350px";
	box.style.backgroundColor = "#ffffff";
	box.style.left = $(document).scrollLeft() + this.ele.offset().left;
	box.style.top = $(document).scrollTop() + this.ele.offset().top + this.ele.height() + 4;
	document.body.appendChild(box);
	this.box = $(box);
	this.ele.hide();
	// 创建查询框
	var key = document.createElement("input");
	key.id = "key_" + this.id;
	key.style.width = "100px";
	key.setAttribute("class", "$input");
	key.style.display = "none";//(暂时去掉搜索功能)
	box.appendChild(key);
	eval("new zfy.component.Input('" + key.id+"')");
	// 创建树
	var tree = document.createElement("div");
	tree.id = "treeObject" + this.id;
	tree.setAttribute("class", "$tree");
	tree.setAttribute("multi-selection", "false");
	tree.setAttribute("cascade", "true");
	tree.setAttribute("root-id", "0");
	tree.style.width = "300px";
	tree.style.height = "100%";
	tree.style.overflow = "auto";
	box.appendChild(tree);
	// 创建查询按钮(暂时去掉此功能)
	$(key).after('<button style="display:none" class="search_button" onclick="$com(\''+tree.id+'\').findItem(document.getElementById(\''+key.id+'\').value, null, null, false,function(){$msg.showWarning(\'没有找到相关数据\')});">查询</button>');
	this.initTree(tree.id);
	$("#"+tree.id + " .containerTableStyle").css("overflow-x","hidden");
};

zfy.component.AdministrativeAreaSelect.prototype.initTree = function(id){
	var self = this;
	this.tree = eval("new zfy.component.Tree('"+id+"','100%','100%',0)");
	//树首项
	this.tree.tree.setDataMode("json");
	//动态加载请求
	this.tree.tree.setXMLAutoLoading(this.url+"?canSelectLevels="+this.canSelectLevels+"&showLevels="+this.showLevels);
	//ajax获取根行政区划
	var rootXzqh = getXzqh(this.root);
	var rootXzqhLevel = this.getLevel(this.root);
	//判断是否有选中权限，否则置为灰色
	if(this.canSelectLevels.indexOf(rootXzqhLevel + "") >= 0){
		this.tree.tree.loadJSONObject({id:'0', item:[ {id:this.root, text:rootXzqh.mc, child:rootXzqh.child ,im0:"area.gif",im1:"area.gif",im2:"area.gif"} ]});
	}else{
		this.tree.tree.loadJSONObject({id:'0', item:[ {id:this.root, text:rootXzqh.mc, child:rootXzqh.child ,im0:"areagrey.gif",im1:"areagrey.gif",im2:"areagrey.gif"} ]});
	}
	this.tree.attachEvent("onClick", function(dwdm) {
		// 如果单击节点区域类型不在可以选择节点区域类型中，则不选中节点，不做任何事情
		if (self.canSelectLevels.indexOf(self.getLevel(dwdm)) == -1) {
			var temp = this._globalIdStorageFind(dwdm);
			this._unselectItem(temp);
			return;
		}
		if(dwdm != self.ele.val()){
			if(self.complete == "1"){
				self.select.val(getXzqhMc(dwdm,1));
			}else{
				self.select.val(getXzqhMc(dwdm));
			}
			self.ele.val(dwdm);
			self.fireChanged();
		}
		self.box.hide();
		self.verify();
	});
	this.tree.tree.openItem("53");
}
	
zfy.component.AdministrativeAreaSelect.prototype.verify = function(){
	var isok = true;
	var error =null;
	var val = this.ele.val();
	if(this.required && $.trim(val)==""){
	    error="不能为空";
		isok = false;
	}
	this.showError(error);
	return isok;
};

zfy.component.AdministrativeAreaSelect.prototype.showError = function(error){
	if(error){
		$("#" + this.selectId).addClass("administrativeArea_invalid");
		$("#" + this.selectId).attr({title: error});
	}else{
		$("#" + this.selectId).removeClass("administrativeArea_invalid");
		$("#" + this.selectId).attr({title: ""});
	}
};

/**
 * 监听事件数组
 */
zfy.component.AdministrativeAreaSelect.prototype.addChangedListener = function(fn){
	this.changedListeners.push(fn);
};

zfy.component.AdministrativeAreaSelect.prototype.fireChanged = function(data){
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i]();
	}
};

zfy.component.AdministrativeAreaSelect.prototype.getLevel = function(dwdm){
	if(dwdm == null || dwdm == ""){
		return 100;
	}
	var len = dwdm.length;
	
	//单位代码长度为2 代表 level=1为省的
	if(len == 2){
		return 1;
	}
	//单位代码长度为4 代表 level=2为市州的
	if(len == 4){
		return 2;
	}
	//单位代码长度为6 代表 level=3为区县市的
	if(len == 6){
		return 3;
	}
	//单位代码长度为9 代表 level=4为乡镇的
	if(len == 9){
		return 4;
	}
	//单位代码长度为12 代表 level=5为村的
	if(len == 12){
		return 5;
	}
	return 100;
}