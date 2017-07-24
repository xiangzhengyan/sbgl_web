/*
 * 通用输入框， 支持日期，数字等
 * 参数列表
 * id
 * data-type 数据类型(date, dateTime)
 * format 格式化
 * value 初始值
 * max-value 最大可选值
 * min-value 最小可选值
 * required 是否不能为空
 */
zfy.component.Input = function(id){
	//还未判断是否是input
	this.init(id);
	this.value = this.getProperty("value");
	this.format= this.getProperty("format");
	this.params= this.getProperty("params");
	this.dataType= this.getProperty("data-type");
	this.maxValue = this.getProperty("max-value")=="this"?getDate():this.getProperty("max-value");
	this.minValue = this.getProperty("min-value")=="this"?getDate():this.getProperty("min-value");	
	this.maxValueId = this.getProperty("max-value-id");
	this.minValueId = this.getProperty("min-value-id");	
	this.maxLength= this.getProperty("max-length",50);	
	if(this.dataType && (this.dataType == "number-pos" || this.dataType == "integer-pos") && this.maxLength > 32){
		this.maxLength = 32;
	}
	this.minLength= this.getProperty("min-length");	
	this.required= this.getProperty("required",false);	
	this.readonlyColor= this.getProperty("readonly-color","#EEEEEE");
	this.scale = this.getProperty("scale", 8);
	this.minus = this.getProperty("minus");//标记是否能小于零
	this.illegals = this.getProperty("illegals");
	if(!this.illegals){
		this.illegals = ["'","\"","\\"];
	}else{
		this.illegals = eval(this.illegals);
	}
	this.errorMsg = this.getProperty("error-msg");
	this.changedListeners = new Array();
	this.paint();
	this.initValue = this.ele.val();
};
zfy.component.Input.prototype = new zfy.component.BaseComponent();

zfy.component.Input.prototype.paint = function(){
	var self = this;
	self.element.setAttribute("autocomplete", "off");
	// 绑定class onfocus onblur 样式
	this.ele.addClass("form_input"); 
	//绑定change事件
	this.ele.on("change",function(){
		self.fireChanged();
	});
	//如果是readOnly 设置背景颜色、设置不能选中
	if(this.ele.attr("readonly")){
		this.ele.css("background-color",this.readonlyColor);
		this.ele.on("focus",function(){this.blur()});
	}
	this.ele.blur(
		function () {
			//去掉前后空格
			self.ele.val(self.ele.val().replace(/^(\s|\u00A0)+/,'').replace(/(\s|\u00A0)+$/,''));  
			//去掉左右的0
			if(self.dataType && self.dataType=="number-pos" && self.ele.val() && !isNaN(self.ele.val())){
				if(parseFloat(self.ele.val())==0){
					self.ele.val(0);
				}else{
					self.ele.val(self.ele.val().replace(/^(0)+/,''));
					if(self.ele.val().indexOf(".")>=0){
						self.ele.val(self.ele.val().replace(/(0)+$/,''));
					}
				}
				//第一位是正号 +,去掉 +号
				if(self.ele.val().indexOf("+") == 0){
					self.ele.val(self.ele.val().substr(1,self.ele.val().length));
				}
				//最后一位是小数点，去掉小数点
				if(self.ele.val().indexOf(".") == self.ele.val().length - 1){
					self.ele.val(self.ele.val().substr(0,self.ele.val().length-1));
				}
				//第一位是小数点，添加0
				if(self.ele.val().indexOf(".") == 0){
					self.ele.val("0"+self.ele.val());
				}
			}else if(self.dataType && self.dataType=="integer-pos" && self.ele.val() && !isNaN(self.ele.val())){
				if(parseFloat(self.ele.val())==0){
					self.ele.val(0);
				}else{
					self.ele.val(self.ele.val().replace(/^(0)+/,''));
					if(self.ele.val().indexOf(".")>=0){
						self.ele.val(self.ele.val().replace(/(0)+$/,''));
					}
				}
				//第一位是正号 +,去掉 +号
				if(self.ele.val().indexOf("+") == 0){
					self.ele.val(self.ele.val().substr(1,self.ele.val().length));
				}
				//最后一位是小数点，去掉小数点
				if(self.ele.val().indexOf(".") == self.ele.val().length - 1){
					self.ele.val(self.ele.val().substr(0,self.ele.val().length-1));
				}
				//第一位是小数点，添加0
				if(self.ele.val().indexOf(".") == 0){
					self.ele.val("0"+self.ele.val());
				}
			}
			this.comobj.verify();
		}
	);

	//type为时间选择控件
	if(this.dataType=="date" ||this.dataType=="dateTime"){
		this.datePicker();
	}else if(this.dataType=="currency"){
		this.currencyOper();
	}

};
//金额类型数据的操作
zfy.component.Input.prototype.currencyOper = function(){
	$("#"+this.id).after("<input type=\"hidden\" id=\"temp_"+this.id+"\" name=\""+$("#"+this.id).attr("name")+"\" />");
	
	$("#"+this.id).removeAttr("name");
	$("#"+this.id).attr("name","_name"+this.id);
	$("#temp_" + this.id).val($("#"+this.id).val());
//	$("#"+this.id).on('change',function(){
//		
//	});
	$("#"+this.id).blur(function(){
		$("#temp_" + this.id).val($("#"+this.id).val());
		var val = $("#"+this.id).val();
		if(val!=null && val!=""){
			if(!isNaN(val)){//isNaN()是数字就返回false
				var n = 2;
				 n = n>0 && n<=20 ? n : 2;
			     var s = val; 
			     s = parseFloat((s+"").replace(/[^\d\.-]/g,"")).toFixed(n)+"";
			     var l = s.split(".")[0].split("").reverse();
			    var  r = s.split(".")[1]; 
			     var t = "";
			     var i ;
			     for(i = 0;i<l.length;i++){
			         t+=l[i]+((i+1)%3==0 && (i+1) != l.length ? "," : ""); 
			     }
				var currency = t.split("").reverse().join("")+"."+r;
				if (currency == 'NaN.undefined' || currency == undefined) {
					$("#"+this.id).val("");
				} else {
					$("#"+this.id).val(currency);
				}
			}
		}
	});
	$("#"+this.id).focus(function(){
		$("#"+this.id).val($("#temp_" + this.id).val());
	});
	
};
//绘制datePicker控件
zfy.component.Input.prototype.datePicker = function(){
	var self = this;
	this.ele.addClass("Wdate"); 
	if(!this.format){
		if(this.dataType=="date")
			this.format = "yyyy-MM-dd";
		else
			this.format = "yyyy-MM-dd HH:mm:ss";
	}
	//MY97拼装需要的参数
	var params = null;
	if(!this.params){
		params = new Object();
	}else{
		params = eval("("+this.params+")");
	}
	params.dateFmt = this.format;
	
	if(!(this.maxValue==null || this.maxValue == "")){
		params.maxDate = this.maxValue;
	}
	if(!(this.minValue==null || this.minValue == "")){
		params.minDate=this.minValue;
	}
	params.onpicking = function(){
		setTimeout(function(){
			if(self._oldValue != self.ele.val()){
				self.fireChanged();
				self._oldValue = self.ele.val();
			}
		},1);
		
	};
	
	params.onpicked = function(){
		self.verify();
	};
	
	self._oldValue = this.value;
	this.ele.blur(function(){
			if(self._oldValue != self.ele.val()){
				self.fireChanged();
				self._oldValue = self.ele.val();
			}
	});
	
	//class为Wdate才能显示图标
	this.ele.focus(function(){ WdatePicker(params);}); 
};
//验证电话号码的格式
zfy.component.Input.prototype.Trim = function(val) {  
	var m = val.match(/^\s*(\S+(\s+\S+)*)\s*$/);  
	return (m == null) ? "" : m[1];  
};
zfy.component.Input.prototype.isMobile = function(val) {  
	return (/^(?:13\d|15\d|14[57]|18\d|17[68])-?\d{5}(\d{3}|\*{3})$/.test(this.Trim(val)));  
} ;

zfy.component.Input.prototype.isTel = function(val)
{
	//"兼容格式: 国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
	//return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/.test(this.Trim()));
	return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(this.Trim(val)));
};
zfy.component.Input.prototype.verify = function(){
	var isok = true;
	var error =null;
	var val = this.ele.val();
	if(this.required && $.trim(val)==""){
	    error="不能为空";
		isok = false;
	}
	if(val && val!=""){
			for(var i in this.illegals){
				if(val.indexOf(this.illegals[i]) >= 0){
					var illegalsrting = "";
					for(var j in this.illegals){
						illegalsrting += this.illegals[j] + " ";
					}
					error="请不要输入（英文状态下）非法字符 "+illegalsrting+"\n此处非法字符为 "+this.illegals[i];
					isok = false;
				}
			}
			if(this.maxLength){
				//如果是数字
				if(this.dataType && this.dataType=="number-pos" && (val+"").length>this.maxLength){
					error="最多输入"+this.maxLength+"个数字（包含小数点）";
					isok = false;
					//其它情况	
				}else if(this.dataType && this.dataType=="integer-pos" && (val+"").length>this.maxLength){
					error="最多输入"+this.maxLength+"个数字";
					isok = false;
					//其它情况	
				}else if(val.length + $util.getCharCount(val,"\n") >this.maxLength){
					error="最多输入"+this.maxLength+"个汉字或字符,目前输入了"+(val.length + $util.getCharCount(val,"\n"))+"个汉字或字符";
					isok = false;
				}
			}
			if(this.minLength){
				//如果是数字
				if(this.dataType && this.dataType=="number-pos" && (val+"").length<this.minLength){
					error="最少输入"+this.minLength+"个数字（包含小数点）";
					isok = false;
					//其它情况	
				}else if(this.dataType && this.dataType=="integer-pos" && (val+"").length<this.minLength){
					error="最少输入"+this.minLength+"个数字";
					isok = false;
					//其它情况	
				}else if(val.length + $util.getCharCount(val,"\n")<this.minLength){
					error="最少输入"+this.minLength+"个汉字或字符,目前输入了"+(val.length + $util.getCharCount(val,"\n"))+"个汉字或字符";
					isok = false;
				}
			}
			if(this.minValue && (this.dataType=="number-pos" || this.dataType=="integer-pos" || this.dataType=="currency")){
				if(parseFloat(val) < parseFloat(this.minValue)){
					error="最小输入值为"+this.minValue;
					isok = false;
				}
			}
			if(this.maxValue && (this.dataType=="number-pos" || this.dataType=="integer-pos" || this.dataType=="currency")){
				if(parseFloat(val) > parseFloat(this.maxValue)){
					error="最大输入值为"+this.maxValue;
					isok = false;
				}
			}
			
			if(this.maxValue && this.minValue && (this.dataType=="number-pos" || this.dataType=="integer-pos" || this.dataType=="currency")){
				if(parseFloat(val) > parseFloat(this.maxValue) || parseFloat(val) < parseFloat(this.minValue)){
					error="请输入合理范围内的值("+this.minValue+"—"+this.maxValue+")";
					isok = false;
				}
			}
			if(this.maxValueId && $("#"+this.maxValueId).val()
					&& !isNaN($("#"+this.maxValueId).val()) && !isNaN(val)){
				if(parseFloat(val) > parseFloat($("#"+this.maxValueId).val())){
					error="区间起始值不能大于结束值";
					isok = false;
				}
			}
	
			if(this.minValueId && $("#"+this.minValueId).val()
					&& !isNaN($("#"+this.minValueId).val()) && !isNaN(val)){
				if(parseFloat(val) < parseFloat($("#"+this.minValueId).val())){
					error="区间结束值不能小于起始值";
					isok = false;
				}
			}
			if( this.dataType=="integer-pos"){//整数
				if(val.replace("-","").split(".")[0].length > 12){
					error="最多输入12位整数位";
					isok = false;
				}
				if(this.minus){//允许为负数
					if (!/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(val)) {//验证数字，包括正数、负数
						error="请输入一个数字";
						isok = false;
					}
					
				}else{
					if(!/^(\+)?\d+$/.test(val)){
						error="请输入一个大于或等于0的整数";
						isok = false;
					}
				}				
				if(this.maxValue!=null && parseInt(val) > parseInt(this.maxValue)){
					error="最大值不能超过"+this.maxValue;
					isok = false;
				}
			}else if(this.dataType=="number-pos"){
				if(val.replace("-","").split(".")[0].length > 12){
					error="最多输入12位整数位";
					isok = false;
				}
				if(this.minus){//允许为负数
					if (!/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(val)) {//验证数字，包括正数、负数
						error="请输入一个数字";
						isok = false;
					}
					
				}else{
					//带有小数点的数
					if(!/^\d+(\.\d+)?$/.test(val)){
						error="请输入一个大于或等于0的数字";
						isok = false;
					}
				}
				if(this.maxValue!=null && parseFloat(val) > parseFloat(this.maxValue)){
					error="最大值不能超过"+this.maxValue;
					isok = false;
				}
				var tempArray = (val + "").split(".");
				if(tempArray.length == 2 && tempArray[1].length > this.scale){
					error="最多保留"+this.scale+"位小数";
					isok = false;
				}
				
			}
		if(this.dataType=="currency"){
			if(isNaN($("#temp_"+ this.id).val())){
				error="不能输入非数字";
				isok = false;
			}
		}else if(this.dataType=="ip"){
			var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g;
			if(re.test(val)){
				if(!(RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)){
					error="请输入合法的ip";
					isok = false;
				}
			}else{
				error="请输入合法的ip";
				isok = false;
			}
		}else if(this.dataType=="phone-num"){
			if (!this.isMobile(val)&&!this.isTel(val))  {  
				error="请输入正确的电话号码格式";
				isok = false;
			}     
		}else if(this.dataType=="post-code"){
			if (!/^[0-9]{6}$/.test(val)) {//验证数字，包括正数、负数
				error="请输入正确的邮政编码（6位数字）";
				isok = false;
			}    
		}else if(this.dataType=="id-card"){
			if (!/(^\d{17}(\d|X)$)/.test(val)) {//验证数字，包括正数、负数
				error="请输入正确的18位身份证号码";
				isok = false;
			}    
		}

		
	}
	this.showError(error);
	return isok;
};

zfy.component.Input.prototype.showError = function(error){
	if(error){
		if (this.dataType == "date" || this.dataType == "dateTime") 
			this.ele.addClass("date_input_invalid");
		else 
			this.ele.addClass("input_invalid");
		
		if(this.errorMsg){
			this.element.title=this.errorMsg;
		}else{
			this.element.title=error; 
		}
	}else{
		if (this.dataType == "date" || this.dataType == "dateTime") 
			this.ele.removeClass("date_input_invalid");
		else 
			this.ele.removeClass("input_invalid");
		this.element.title="";
	}
};


/**
 * 监听事件数组
 */
zfy.component.Input.prototype.addChangedListener = function(fn){
	this.changedListeners.push(fn);
};

zfy.component.Input.prototype.fireChanged = function(data){
	for(var i=0; i<this.changedListeners.length;i++){
		this.changedListeners[i]();
	}
};
//获得当前时间 格式：yyyy-MM-dd hh:mm:ss
var getDate = function(){
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	return year+"-"+month+"-"+day+" "+h+":"+m+":"+s;
};

