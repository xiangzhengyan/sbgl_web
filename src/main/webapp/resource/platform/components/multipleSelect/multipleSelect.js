// 多选下拉

/**
 * 多选下拉框。目前覆盖村委会用到
 * @author chenhm
 * @param id
 * @param unitCode 上级镇单位代码
 *
 */
var id = "";
zfy.component.MultipleSelect = function(id){
	this.init(id);
	this.parentId = this.getProperty("parent_id");
	this.value = this.getProperty("value");
	this.tp = this.getProperty("tp");
	this.label = this.getProperty("label", "");
	this.required = this.getProperty("required",false);
	this.changedListeners = new Array();
	this.url = this.getProperty("url",$util.getAppName()+"/server/queryVillage.do");
	this.paint();
};
zfy.component.MultipleSelect.prototype = new zfy.component.BaseComponent();

zfy.component.MultipleSelect.prototype.paint = function(id) {
	var self = this;
	id = this.id;
	this.ele.addClass("form_input");
	var div = document.getElementById('village');
	if (div == '' || div == null) {
		div = document.createElement("DIV");
		div.id = "village";
		div.style.position="absolute";
		div.style.display = "none";
		document.body.appendChild(div);
	}
	
	var url = "";
	var villageCode = this.value;
	if (this.parentId == "" || this.parentId == null) {
		url = this.url;
	} else {
		var	unitCode = $("#" + this.parentId).val();//
		//判断是否大于八位，若大于八位 说明是村的代码，得街区前八位获得乡镇代码
		if(unitCode.length>8){
			unitCode = unitCode.substr(0,8);
		}
		if(this.url.indexOf("?")>=0){
			url = this.url+"&unitCode=" + unitCode+"&tp="+self.tp;
		}else{
			url = this.url+"?unitCode=" + unitCode+"&tp="+self.tp;
		}
	}
	var html = '<iframe id="iframe_village" name="iframe_village" src="'+url+'"width="100%" height="100%" border="0" marginWidth="0" marginHeight="0" frameBorder="no" />';
	$("#village").html(html);
	$("#village").css("border", "1px solid #999999");
	$("#village").css({"width":"198px", "height":"148px"});
	$("#village").css("margin-top", "-1px");
	$("#village").hide();
	$("#" + this.id).css("display", "none");
	$("#" + this.id).val(this.value);
	var txt = document.getElementById(this.id);
	var obj = document.getElementById('label');
	if (obj == null || obj == '') {
		obj = document.createElement('input'); 
		obj.id = "label";
		obj.name = "label";
		if (this.label == null || this.label == undefined)
			this.label = "";
		obj.value = this.label;
		if (this.required) {
			obj.required = true;
		}
		if (this.disabled == true) {
			obj.disabled = 'disabled';
		}
		obj.className = "input";
		txt.parentNode.insertBefore(obj,txt);
	}
	if (this.length == null || this.length == '') {
		this.length = '200px';
	}
	$("#label").css("width", this.length);
	$("#label").addClass("multipleLabel");
	if("\v"=="v") {  //判断是否IE浏览器
		obj.onpropertychange = myfun;   //IE的话添加onpropertychange 事件
	}else{
		obj.addEventListener("input",myfun,false);  //非IE的话用 addEventListener 添加监听事件
	}
	function myfun(){
		if(obj.value == '') {
			$("#"+this.id).val("");
		}
		self.fireChanged();
	}
	
	var iframe = document.getElementById("iframe_village");
	if (iframe.attachEvent){
	    iframe.attachEvent("onload", function(){
	    	checked(villageCode);
	    });
	} else {
	    iframe.onload = function(){
	    	checked(villageCode);
	    };
	}


jQuery.fn.selectArea = function(targetId) {
	var _seft = this;
	var targetId = $(targetId);

	this.click(function(){
		var A_top = $(this).offset().top + $(this).outerHeight(true);
		var A_left =  $(this).offset().left;
		var error = "";
		error = $("#iframe_village" ).contents().find("#error").val();
//		if(_isIE){
//			error = document.frames("iframe_village").document.getElementById('error').value;
//		}else{
//			error = document.getElementById("iframe_village").contentDocument.getElementById("error").value;
//		}
		if (error != null && error != '') {
			$msg.showWarning(error);
			return;
		}
		targetId.show().css({"position":"absolute","top":A_top+"px" ,"left":A_left+"px"});
	});

	//	点击事件
	$(document).click(function(event){
		if(event.target.id != _seft.selector.substring(1)){
			self.selectValue();
		}
	});
	
	targetId.click(function(e){
		e.stopPropagation();
	});

    return this;
};
$("#label").selectArea("#village");
};

zfy.component.MultipleSelect.prototype.selectValue = function(){
	var self = this;
	var targetId = $("#village");
	targetId.hide();
	var coverVillage = "";
	var label = "";
	var box ="";
	box = $("#iframe_village" ).contents().find("input[name='unitCode']");
	if(box.length <=0){
		return;
	}
	for (var i = 0; i < box.length; i++) {
		if(box[i].checked){
			coverVillage = coverVillage + box[i].value + ",";
			label = label + $("#iframe_village" ).contents().find("#"+box[i].value).html() + ",";
		}
	}
	coverVillage = coverVillage.substring(0, coverVillage.length-1);
	label = label.substring(0, label.length-1);
	self.ele.val(coverVillage);
	$("#label").val(label);
};

zfy.component.MultipleSelect.prototype.verify = function(){
	this.selectValue();
	this.isok = true;
	this.error =null;
	var val = this.ele.val();
	if(this.required && $.trim(val)==""){
		this.error="不能为空";
	    this.isok = false;
	}
	if(val!=""){
		if( this.dataType=="integer-pos" && !/^(\+)?\d+$/.test(val)){
			this.error="请输入一个大于0的整数";
		    this.isok = false;
		}else if(this.dataType=="number-pos" && !/^\d+(\.\d+)?$/.test(val)){
			this.error="请输入一个大于0的数字";
			this.isok = false;
		}
	}
	if(this.verifyArray){
		for(var i in this.verifyArray){
			this.verifyArray[i]();
		}
	}
	this.showError(this.error);
	return this.isok;
};
zfy.component.MultipleSelect.prototype.addVerify = function(fn){
	if(!this.verifyArray){
		this.verifyArray = [];
	}
	this.verifyArray.push(fn);
};
zfy.component.MultipleSelect.prototype.showError = function(error){
	if(error){
		$("#label").addClass("label_invalid");
		$("#label")[0].title=error; 
	}else{
		$("#label").removeClass("label_invalid");
		$("#label")[0].title="";
	}
};

function checked(villageCode) {
	if (villageCode != null && villageCode != undefined && villageCode != '') {
		var box ="";
		box = $("#iframe_village" ).contents().find("input[name='unitCode']");
//		if(_isIE){
//			box = $(document.frames("iframe_village").document).find("input[name='unitCode']");
//		}else{
//			box = $(document.getElementById("iframe_village").contentDocument).find("input[name='unitCode']");
//		}
		for (var i = 0; i < box.length; i++) {
			if (villageCode.indexOf(box[i].value) >= 0) {
				box[i].checked = true;
			}
		}
	}
}


/**
 * 监听事件数组
 */
zfy.component.MultipleSelect.prototype.addChangedListener = function(fn){
		this.changedListeners.push(fn);
};

zfy.component.MultipleSelect.prototype.fireChanged = function(data){
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i]();
	}
	this.verify();
};