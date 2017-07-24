/**
 * 上传插件
 * hudt
 */
zfy.component.Uploader = function(id){
	this.init(id);
	this.action = this.getProperty("action");
	this.look = this.getProperty("look-id","imagelook");
	this.urlId = this.getProperty("url-id","imageurl");
	this.folder = this.getProperty("folder");
	this.display = this.getProperty("display","");
	this.display = this.display?this.display:"";
	this.accept = this.getProperty("accept","image/*");
	this.objName = this.getProperty("objName","projProjectCard.image");
	this.changedListeners = new Array();
	this.paint();
};
zfy.component.Uploader.prototype = new zfy.component.BaseComponent();

zfy.component.Uploader.prototype.paint = function(){
	var self = this;
	self.ele.addClass("form_input"); 
	if(!self.ele.attr("accept")){
		self.ele.attr("accept",self.accept);
	}
	self.ele.after("<input id=\""+self.urlId+"\" type=\"hidden\" name=\""+self.objName+"\" value=\""+this.display+"\" />");
	if(this.display){
		//编辑页面：已经上传的
		//this.ele.hide();
	}else{
		//新建页面
		//this.ele.hide();
		//this.ele.after("<a id='_link_"+self.id+"' href=\"javascript:;;\" onclick=\"$(\'#"+self.id+"\').trigger(\'click\')\">上传图片</a>");
	}
	this.ele.addClass("form_input");
	$(document).on("change", "#"+this.id, function() {
		self.changeEvent();
	});
};

zfy.component.Uploader.prototype.changeEvent = function(){
	var self = this;
	 $("#"+self.look).html("");
	 $.ajaxFileUpload({
         url : self.action, 
         fileElementId : self.id,
         secureuri : false,
         dataType: 'json',
         success: function (data, status)
         {
             if(data.success){
            	$("#"+self.look).append("上传成功,<a href='#' style='font-size:14px' onclick='window.showModalDialog(\""+self.folder+"/"+data.data+"\")'>点击预览</a>");
            	$("#"+self.urlId).val(data.data);
             	$("#_link_"+self.id).hide();
             	//绑定change事件
            	self.fireChanged();
             }else{
             	$msg.showError(data.info);
             }
         },
         error: function (data, status, e)
         {
             $msg.showError(e);
         }
     });
};

/**
 * 监听事件数组
 */
zfy.component.Uploader.prototype.addChangedListener = function(fn){
		this.changedListeners.push(fn);
	
};

zfy.component.Uploader.prototype.fireChanged = function(data){
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i]();
	}
};