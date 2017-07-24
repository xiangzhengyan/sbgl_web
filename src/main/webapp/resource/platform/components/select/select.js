//下拉组件  xiangzy 2013-11-18

zfy.component.Select = function(id){
	this.init(id);
	this.required= this.getProperty("required",false);	
	this.groupCode= this.getProperty("group-code");//有group-code，就是字典表的select,没有的话，就是普通的空间，下拉内容在页面上设置	
	this.noneOption=this.getProperty("none-option", false);
	this.noneOptionLabel=this.getProperty("none-option-label", "全部");//空白的时候，值为“ ”，默认是全部
	this.value= this.getProperty("val");
	this.parent= this.getProperty("parent");
	this.paint();
	this.changedListeners = new Array();
	this.initValue = this.ele.val();
};
zfy.component.Select.prototype = new zfy.component.BaseComponent();
zfy.component.Select.prototype.paint = function(){
	var self = this;
	if(this.groupCode){
		if(this.parent){
			$(document).on("change","#"+this.parent,function(){
				self.resetOptions();
			});
		}
		this.resetOptions();
	}
	this.ele.addClass("form_select"); 
	this.ele.change(function(){
		self.fireChanged();
	});
	this.ele.blur(
			function () { 
				this.comobj.verify();
			}
		);
};

zfy.component.Select.prototype.resetOptions = function(){
	var self = this;
	self.ele.find("option").remove();
	if(this.noneOption && this.value==""){
		self.ele.append("<option value='' selected='selected'>"+this.noneOptionLabel+"</option>");
	}else if(this.noneOption && this.value!=""){
		self.ele.append("<option value='' >"+this.noneOptionLabel+"</option>");
	}
	
	var url = $util.getAppName()+"/zdcommon/query.do?groupCode="+self.groupCode+"&parentCode=";
	if(this.parent){
		url += $("#"+this.parent).val();
	}
	//默认选项
	$.ajax({
		    type:"POST",
		    async:false,
		    url: url,
		    success:function(msg){
		    	if(msg.success){
		    		var data = msg.data;
		    		for(var key in data){
		    			if(key == self.value){
		    				self.ele.append("<option value='"+key+"' selected='true'>"+data[key]+"</option>");	    				
		    			}else{
		    				self.ele.append("<option value='"+key+"'>"+data[key]+"</option>");
		    			}
		    		}
		    	}else{
		    		$msg.showError(msg.info);
		    	}
		    },
		    error:function(e){
		    	$msg.showError(e);
		    }
	});	
}

zfy.component.Select.prototype.addChangedListener = function(fn){
	this.changedListeners.push(fn);
};

zfy.component.Select.prototype.fireChanged = function(data){
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i](); 
	}
};

zfy.component.Select.prototype.verify = function(){
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

zfy.component.Select.prototype.showError = function(error){
	if(error){
		this.ele.addClass("input_invalid");
		this.element.title=error; 
	}else{
		this.ele.removeClass("input_invalid");
		this.element.title="";
	}
};