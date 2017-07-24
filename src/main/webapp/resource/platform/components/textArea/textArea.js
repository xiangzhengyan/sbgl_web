/**
 * required 是否不能为空
 * max-len 最大长度（区分中英文）
 * min-len 最小长度（区分中英文）
 * @param id
 * @returns {zfy.component.TextArea}
 */

zfy.component.TextArea = function(id){
	this.init(id);
	this.value = this.getProperty("value");
	this.format = this.getProperty("format");
	this.dataType = this.getProperty("data-type");
	this.required = this.getProperty("required",false);
	this.maxValue = this.getProperty("max-value");//长度校验
	this.minValue = this.getProperty("min-value");//长度校验
	this.maxLen = this.getProperty("max-len",255);//长度校验区分汉字和其他字符 汉字2 其他字符1 
	this.minLen = this.getProperty("min-len");//长度校验区分汉字和其他字符 汉字2 其他字符1
	this.changedListeners = new Array();
	this.paint();
	this.initValue = this.ele.val();
};

zfy.component.TextArea.prototype = new zfy.component.BaseComponent();

zfy.component.TextArea.prototype.paint = function(){
	this.ele.addClass("form_textarea"); 
	this.ele.blur(
		function () { 
			this.comobj.verify();
		}
	);
};

zfy.component.TextArea.prototype.verify = function(data){
	var isok = true;
	var error =null;
	var val = this.ele.val();
	if(this.required && $.trim(val)==""){
	    error="不能为空";
		isok = false;
	}
	if(this.minLen && ecLen(val)<this.minLen){
		error="长度过短,最小长度应该为:"+this.minLen+"个字符";
		isok = false;
	}
	if(this.maxLen && ecLen(val)>this.maxLen){
		error="超过最大长度,最大长度应该为:"+this.maxLen+"个字符";
		isok = false;
	}
	if(this.minValue && val.length<this.minValue){
		error="长度过短,最小长度应该为:"+this.minLen+"个字符";
		isok = false;
	}
	if(this.maxValue && val.length>this.maxValue){
		error="超过最大长度,最大长度应该为:"+this.maxValue+"个汉字";
		isok = false;
	}
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i]();
	}
	this.showError(error);
	return isok;
};
/**返回字符串的长度。区分中英文，英文占1个长度，中文占2个长度。*/
var ecLen = function(entryVal){
	entryLen=entryVal.length;
	cnChar=entryVal.match(/[^\x00-\x80]/g);//利用match方法检索出中文字符并返回一个存放中文的数组
	if(cnChar!=null){
		entryLen+=cnChar.length;//算出实际的字符长度
	}
	return entryLen;
};
zfy.component.TextArea.prototype.showError = function(error){
	if(error){
		this.ele.addClass("input_invalid");
		this.element.title=error; 
	}else{
		this.ele.removeClass("input_invalid");		
		this.element.title="";
	}
};