
//if( typeof zfy == "undefined"){
var zfy={};
//}
zfy.component={}; 
zfy.util ={};
var dhxWins;
/** 表单工具类**/
function $com(id){
	var ele =  $("#"+id);
	if(ele && ele[0])return ele[0].comobj;
}

function isIE(){
	if (!!window.ActiveXObject || "ActiveXObject" in window)  
        return true;  
    else  
        return false;  
}
//对话框
/**
 * url
 * title: 标题
 * width: 宽度
 * height: 高度
 * callback: 回调函数
 * parentWindow: 父窗口
 * showMax：是否显示最大、最小化按钮
 * position: 显示方位
 * modal: 是否模态窗口
 * id: id
 * skin: 皮肤（黑色为dhx_web）
 * onClose: 关闭事件 
 * htmlString: dialog显示HTML内容。如果有这个参数，则url无效
 * offsetX: X轴偏差（为正数则往右移动）数字类型，不要加px
 * offsetY: Y轴偏差（为正数则往下移动）数字类型，不要加px
 * maximizable: 能否双击时最大化true/false（默认）
 */
zfy.util.Dialog = {
		open : function(_url,title,width,height,callback,parentWindow,
				showMax, position, modal,id,skin,onClose,htmlString,
				offsetX,offsetY,maximizable){
			var url = _url;
			if(typeof _url == "object"){
				title = _url.title;
				width = _url.width;
				height = _url.height;
				callback = _url.callback;
				parentWindow = _url.parentWindow;
				showMax = _url.showMax;
				position = _url.position;
				modal = _url.modal;
				id = _url.id;
				title = _url.title;
				skin = _url.skin;
				onClose = _url.onClose;
				htmlString = _url.htmlString;
				offsetX = _url.offsetX;
				offsetY = _url.offsetY;
				maximizable = _url.maximizable;
				url = _url.url;
			}
			if(typeof width == "string"){
				if(width.indexOf("%") >= 0){
					//百分比计算实际大小
					width = document.body.clientWidth * parseFloat(width.replace("%",""))/100;
				}else{
					width = parseInt(width.replace("px",""));
				}
			}
			if(typeof height == "string"){
				if((height+"").indexOf("%") >= 0){
					//百分比计算实际大小
					height = document.body.clientHeight * parseFloat(height.replace("%",""))/100;
				}else{
					height =  parseInt(height.replace("px",""));
				}
			}
			if(width == null || parseInt(width) <= 0){
				width = 600;
			}
			if(height == null || parseInt(height) <= 0){
				height = 400;
			}
			if(title == null){
				title = "";
			}
			if(!dhxWins){
				dhxWins = new dhtmlXWindows();
			}
			if(skin){
				dhxWins.setSkin(skin);
			}else{
				dhxWins.setSkin("dhx_skyblue");
			}
			dhxWins.setImagePath($util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/imgs/dhxtree_skyblue/");
			dhxWins.attachViewportTo(document.body);
			var left = 0;
			var top = 0;
			if (position == null) {
				position = "center";
				left = (parseInt(document.body.scrollLeft)+parseInt(document.body.clientWidth) - width)/2;
				top = (parseInt(document.body.scrollTop) + parseInt(document.body.clientHeight) - height)/2;
			} else if (position.indexOf('right-bottom')==0) {
				var wucha = position.substring(position.lastIndexOf("-") + 1);
				wucha = wucha == "bottom"?5:parseInt(wucha);
				// 右下角
				left = parseInt(document.body.scrollLeft)+parseInt(document.body.clientWidth) - width - wucha;
				top = parseInt(document.body.scrollTop) + parseInt(document.body.clientHeight) - height + wucha - 15;
			} else if (position.indexOf('right-top')==0) {
				// 右上角
				left = parseInt(document.body.scrollLeft)+parseInt(document.body.clientWidth) - width;
				top = 0;
			}
			//偏差值
			if(offsetX){
				left += offsetX;
			}
			if(offsetY){
				top += offsetY;
			}
			var newid = id || new Date().getTime();
			var w1 = dhxWins.createWindow(newid, left, top, width, height);
			if (modal == null) {
				modal = true;
			}
			dhxWins.window(newid).setModal(modal);
			w1.setText(title);
			w1.button("park").hide();
			w1.setIconCss('without_icon');
			//不允许最大化
			if(!maximizable){
				w1.attachEvent("onMaximize", function(win){
					w1.minimize();
				});
			}
			//关闭事件
			w1.attachEvent("onClose", function(win){
				if(onClose)
					onClose();
				return true;
			})
			if(!showMax){
				w1.button("minmax1").hide();
				w1.button("minmax").disable();
			}
			//关闭时，回调函数
			/*w1.button("close").attachEvent("onClick", function() {
		    	eval(callback);
		        w1.close();
		    });*/
			window._dialog = w1;
			window._callback = callback;
			if(parentWindow){
				window._parentWindow = parentWindow;
				window._parentWindow._callback = callback;
			}
			if(!htmlString){
				w1.attachURL(url);
			}else{
				w1.attachHTMLString(htmlString);
			}
		},
		/**
		 * data:传递给回调函数的参数
		 */
		close:function(data,id){
			var w1 = window.parent._dialog;  
			var callback = window.parent._callback;
			if(callback){
				if(window.parent._parentWindow){
					window.parent._parentWindow._callback(data);
				}else{
					callback(data);
				}
				
			}
			w1.close();
		},
		//父页面调用的关闭窗口：closeDialog（close是dialog页面自己调用的，用于关闭自己）
		closeDialog : function(id){
			if(id && dhxWins && dhxWins.window(id)){
				dhxWins.window(id).close();
			}
		},
		getDialog:function(id){
			if(id){
				return dhxWins.window(id).getFrame();
			}
			var w1 = window.parent._dialog; 
			return w1.getFrame();
		},
		hide:function(id){
			if(id){
				if(dhxWins.window(id)){
					dhxWins.window(id).hide();
				}				
				return ;
			}
			var w1 = window.parent._dialog; 
			w1.hide();
		},
		show:function(id){
			if(id){
				if(dhxWins.window(id)){
					dhxWins.window(id).show();
				}	
				return ;
			}
			var w1 = window.parent._dialog; 
			w1.show();
		},
		attachEvent: function(id, eventName, fn) {
			dhxWins.window(id).attachEvent(eventName, fn);
		},
		checkExist:function(id){
			if(dhxWins && dhxWins.window(id)){
				return true;
			}else{
				return false;
			}
		}
};
var $dialog = zfy.util.Dialog;
zfy.util.Form = {
		submit : function(id){
			$mask.show();
			setTimeout(function(){
				$("#" + id).submit();
			},1);
		},
		ajaxSubmit : function(id,callback){
			$("input:focus").blur();
			var form = $("#" + id);
//			var params ={};
//			form.find("*[name]").each(function(){
//			if(this.type=="radio"){
//			if(this.checked){
//			params[this.name] = $(this).val();
//			}
//			}else if(this.type=="checkbox"){
//			if(this.checked){
//			var checkArr = null;
//			if(params[this.name]==null){
//			checkArr = new Array();
//			}else{
//			checkArr = params[this.name];
//			}
//			checkArr.push($(this).val());
//			params[this.name] = checkArr;
//			}
//			}else{
//			params[this.name] = $(this).val();
//			}
//			});
//			for(var key in params){
//			params[key] = params[key] + "";
//			}
			setTimeout(function(){
				$ajax.post(form.attr("action"),form.serialize(), function(data){
					if(callback){
						if(typeof data == "string"){
							eval("data="+data);
						}
						callback(data);
					}
				});
			},1);

//			var options={
//			url:form.attr("action"),
//			type:'post',
//			success:function(data){
//			if(callback){
//			if(typeof data == "string"){
//			eval("data="+data);
//			}
//			callback(data);
//			}
//			}
//			};
//			form.ajaxSubmit(options);
		},
		validate : function(id,isRtnExt){//isRtnExt：是否返回更多信息
			var result = {};
			result.flag = true;
			result.errorComobj = [];
			$("input:focus").blur();

			var form = $("#" + id);
			var flag = true;
			form.find("*[name]").each(function(){
				var comobj = this.comobj;
				if(comobj && comobj.verify){
					if(!comobj.verify()){
						flag = false;
						if(isRtnExt){
							result.flag = false;
							result.errorComobj.push(comobj);
						}
					}
				}
			}); 
			if(isRtnExt){
				return result;
			}
			return flag;
		},
		setChildrenValue : function(targetId,data){
			var target = $("#"+targetId);
			if(target == null){
				$msg.showInfo("没有找到此id:"+targetId);
				return;
			}
			target.find("*[name]").each(function(){
				var dataValue = eval("data."+$(this).attr("name"));
				if(dataValue != null){
					$(this).html(dataValue);
				}
			});
		},
		resetForm : function(id,param){
			var url = $("#" + id).attr('action');
			if(param){
				url += "?"+param;
			}
			window.location.href=url;
		}
};
var $form = zfy.util.Form;

/**消息提示工具类**/
zfy.util.MsgBox = {
		showInfo : function(s){
			window.top.dhtmlx.message(s);
			$(".dhtmlx_message_area",window.top.document).css("top","65px");
		},
		showWarning:function(s){
			window.top.dhtmlx.message({type:'warning',text:s});
			$(".dhtmlx_message_area",window.top.document).css("top","65px");
		},
		showError:function(s,isInner,top){
			window.top.dhtmlx.message({type:'error',text:s});
			$(".dhtmlx_message_area",window.top.document).css("top","65px");
		},
		showConfirm:function(s){
			return confirm(s);
		},

		/**
		 * content:信息内容
		 * title:标题
		 * icon:内置图标：error、succeed、question、warning
		 * fixed:默认false。开启静止定位。静止定位是css2.1的一个属性，它静止在浏览器某个地方不动，也不受滚动条拖动影响。（artDialog支持IE6 fixed）
		 * left:X轴的坐标。
				1、如果开启了fixed参数则以浏览器视口为基准；
				2、可以使用'0%' ~ '100%'作为相对坐标，如果浏览器窗口大小被改变其也会进行相应的调整
		 * top:Y轴的坐标。
				1、如果开启了fixed参数则以浏览器视口为基准；
				2、可以使用'0%' ~ '100%'、''goldenRatio''作为相对坐标，如果浏览器窗口大小被改变其也会进行相应的调整，其中'goldenRatio'表示为黄金比例垂直居中，绝对居中请使用'50%'
		 * time:设置对话框显示时间。以秒为单位
		 * resize:默认true。是否可以拖动实现放大缩小
		 * drag:true 是否拖拽
		 */
		
		alertTip:function(_content,_title,_left,_top){
			art.dialog({ 
				content: _content,
				title:"提示",
				left:_left,
				top:_top,
				resize:false,
				lock:true
			});
		},
		alertError:function(_content,_left,_top){
			art.dialog({ 
				content: _content,
				title:"错误",
				icon:"error",
				left:_left,
				top:_top,
				resize:false,
				lock:true
			});
		},
		alertWarning:function(_content,_left,_top){
			art.dialog({ 
				content: _content,
				title:"警告",
				icon:"warning",
				left:_left,
				top:_top,
				resize:false,
				lock:true
			});
		},
		alertSuccess:function(_content,_left,_top){
			art.dialog({ 
				content: _content,
				icon:"succeed",
				left:_left,
				top:_top,
				resize:false,
				lock:true
			});
		},
		alertConfirm:function (content,yesFunc,noFunc) {
			if(noFunc == undefined || noFunc == null){
				noFunc = true;
			}
			art.dialog({
		        icon: 'question',
		        fixed: true,
		        lock: true,
		        opacity: .1,
		        content: content,
		        ok: yesFunc,
		        cancel:noFunc
		    });
		}


};
var $msg = zfy.util.MsgBox;

/** util **/
zfy.Util = {
		getAppName : function(){
			return $_path;//在base.jsp里面定义
		},
		getBaseAppName : function(){
			return $_basePath;//在base.jsp里面定义
		},
		toString : function(str,nullstring){
			//为null显示字符,默认""
			if(nullstring == null || str == undefined){
				nullstring = "";
			}
			if(str == null || str == undefined){
				return nullstring;
			}
			return str;
		},
		toHtml : function(str){
			//转成html转义，并把null转成""
			if(str == null || str == undefined){
				return "";
			}
			return html2Escape(str);
		},
		toDate : function (javaDate,rex){
			if(javaDate==null || javaDate==''){
				return "";
			}
			var srouceDate = new Date(javaDate);
			var year = srouceDate.getFullYear();
			var month = srouceDate.getMonth()+1;
			var day = srouceDate.getDate();
			if(month.toString().length==1){
				month='0'+month;
			}
			if(day.toString().length==1){
				day='0'+day;
			}
			if(rex == "yyyy"){
				return year;
			}
			if(rex == "yyyy-MM"){
				return year + "-" + month;
			}
			return year + "-" + month + "-" +day;
		},
		createTitleDiv : function(left,top,txt){
			$("body").append("<div class='_titleDiv' style='z-index:10000;position:absolute;left:"+(left+25)+"px;top:"+(top-15)+"px'>"+txt+"</div>");
		},
		cleanTitleDiv : function(left,top,txt){
			$("._titleDiv").remove();
		},
		//设置cookie
		setCookie : function(cookieName,value,expiredays){
			var exdate = new Date();
			exdate.setDate(exdate.getDate() + expiredays);
			document.cookie=cookieName+ "=" +escape(value)+
			((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
		},
		//获取cookie
		getCookie : function(cookieName){
			if(document.cookie.length>0){
				var start = document.cookie.indexOf(cookieName + "=");
				if (start!=-1){ 
					start += cookieName.length + 1;
					var end = document.cookie.indexOf(";",start);
					if (end == -1){
						end = document.cookie.length;
					} 
					return unescape(document.cookie.substring(start,end));
			    } 
			}
			return "";
		},
		//统计一个字符串中某个字符出现次数
		getCharCount : function(str,char){
			if(str == null || char == null){
				return 0; 
			}
			var re = new RegExp(char,"g");
			var arr = str.match(re);
			if(arr == null){
				return 0;
			}
			return arr.length;
		},
		queryZd : function(group,code){
			var label = "";
			$.ajax({
			    type:"POST",
			    async:false,
			    url: $util.getAppName() + "/zdcommon/queryLabel.do",
			    data: {group:group,code:code},
			    success:function(data){
			    	if(data.success){
			    		label = $util.toString(data.data);
		    		}
		    	}
			});
			return label;
		},
		/*消除数字后面多余的0*/
		removeZero : function(num){
			return parseFloat(num);
		},
		/*保留几位*/
		resetNumber : function(num, scale){
			if(num&&num!=null){
				var number = parseFloat(num);
				return $util.removeZero(number.toFixed(scale));
			}
			return num;
		},
		/*构造一个JSON数据传到java后台  构造JSON操作记录 
		 * inputName,后台接收对象
		 * fromId,提交表单Id
		 */
		constrJsonForOptRecord : function(inputName,fromId){
			
		},		
		isImage:function(imgName){
			if(!imgName){
				return false;
			}
			idx = imgName.lastIndexOf(".");   
		    if (idx != -1){   
		        ext = imgName.substr(idx+1).toUpperCase();   
		        ext = ext.toLowerCase();
		        if(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png" || ext == "bmp"){
		        	return true;
		        }else{
		        	return false;
		        }
		    }else{
		    	return false;
		    }
		}
		
};
var $util = zfy.Util;

/** ajax 未做完**/
zfy.util.Ajax = {
		post : function(data,params,callbackFn){
			$mask.show();
			if(typeof data == "object"){
				//JSON格式提交，相当于$.ajax
				$.ajax({ 
					type: data.type,
					url: data.url, 
					data:data.data,
					success: function(d){
						$mask.hide();
						data.success(d);
					},
					error:function(){
						$mask.hide();
						data.error("后台出错");
					}
				}); 
			}else{
				//相当于$.post
				$.ajax({ 
					type: "post",
					url: data, 
					data:params,
					success: function(d){
						if(d=="sessionMiss"){
							$mask.hide();
							window.top.location.href='../';
							return;
						}
						$mask.hide();
						callbackFn(d);
					},
					error:function(){
						$mask.hide();
						$msg.showError("后台出错");
					}
				}); 
			}

		}

};
var $ajax = zfy.util.Ajax;

/** portal工具类相关封装 未做完**/
zfy.util.Portal = {

		getPortalWindow :function(){
			var p = window;
			while(p){
				if(p.isPortalWindow){
					return p;
				}
				if(p == p.parent){
					return null;
				}
				p = p.parent;
			}
			return null;

		},

		/**
		 * data.url :打卡的url
		 * data.tabId :页签id
		 * data.title :页签标题
		 * data.index :插入位置，对左侧页签适用，如果没有，就最近到末尾
		 */
		openTabPage: function(url, title, tabId, index) {
			var portal = this.getPortalWindow();
			portal.openPortalPage(url, title, tabId, index);
		},

		/**
		 * 关闭页签
		 */
		closeTabPage: function() {
			var portal = this.getPortalWindow();
			portal.closePortalPage();
		},
		/**
		 * data.url :打卡的url
		 * data.callback :回调函数
		 */
		openPage: function(url, callback){
			$("input:focus").blur();
			var dlg = $("<div/>").appendTo("body");
			dlg.callback = callback;
			dlg.css({float:"left",zIndex:9000,width:'100%',height:'100%',position:"absolute",top:0,left:0});
			dlg.append('<iframe src="'+url+'"width="100%" height="100%" border="0" marginWidth="0" marginHeight="0" frameBorder="no" scrolling="auto"/>');
			window._pageDialog = dlg;
		},

		/**
		 * data.param json格式（name(参数名称)：value参数值）
		 * data.callback :回调函数
		 */
		openPage2: function(url,param, callback){
			$("input:focus").blur();
			var dlg = $("<div/>").appendTo("body");
			dlg.callback = callback;
			dlg.css({float:"left",zIndex:9000,width:'100%',height:'100%',position:"absolute",top:0,left:0});
			for(var i in param){  
				if(i== param.length-1){
					url+= param[i].name+"="+encodeURIComponent(param[i].value);  
				}else{
					url+= param[i].name+"="+encodeURIComponent(param[i].value)+"&";  
				}
			}  
			dlg.append('<iframe src="'+url+'"width="100%" height="100%" border="0" marginWidth="0" marginHeight="0" frameBorder="no" scrolling="auto"/>');
			window._pageDialog = dlg;
		},
		
		/**
		 * data:传递给回调函数的参数
		 */
		closePage:function(data){
			try{
				window.top.document.getElementById('tobfocusforIE').focus();
			}catch(e){
			}
			var dlg = window.parent._pageDialog; 
			var callback = dlg.callback;
			if(callback){
				callback(data);
			}

			//TODO ie10 从编辑页返回列表页后无法获得焦点的问题
			var input = $(window.parent.document).find("input[data-type!='date']:visible:first"); 
			if(input && input[0]){
				input[0].focus();
			}
			dlg.remove();
		},
		
		/**
		 * data:传递给回调函数的参数
		 */
		closePageWithConfirm:function(formid){
			var checkdoms = null;
			if(formid){
				checkdoms = $("#"+formid).find("*[name]");
			}else{
				checkdoms = $("*[name]");
			}
			var needconfirm = false;
			checkdoms.each(function(){
				var comobj = this.comobj;
				if(comobj){
					if($(this).val() != comobj.initValue){
						needconfirm = true;
						return;
					}
				}
			});
			if(needconfirm){
				if($msg.showConfirm("您编辑的内容还没有进行保存\n确定返回吗？")){
					$portal.closePage();
				}
			}else{
				$portal.closePage();
			}
		}
		
};
var $portal = zfy.util.Portal;

/**下载连接**/
zfy.util.Download = {
		//url:请求地址,data:参数,callback执行回调函数
		open:function (url, data ,name,callback){	
		     var tempForm = document.createElement("form");  
		     tempForm.id="tempForm";  
		     tempForm.method="post";  
		     tempForm.action=url;  
		     tempForm.target=name;  
		       
		     for(var i in data){
		    	 var hideInput = document.createElement("input");  
			     hideInput.type="hidden";  
			     hideInput.name= i;
			     hideInput.value= data[i];
			     tempForm.appendChild(hideInput);  
		     }
		    
		     document.body.appendChild(tempForm);
		     //监听事件的方法        打开页面window.open(name);
		     tempForm.addEventListener("onsubmit",function(){  window.open(name,'_self'); }); 
		     /* if(_isIE){
		    	//触发事件
		        tempForm.fireEvent("onsubmit");
		     }  */
		     tempForm.submit();
		     document.body.removeChild(tempForm);
		     //执行回调函数
		     if(callback!="undefined"&&callback!=null){
		    	 callback();
		     }
		}
};
var $download = zfy.util.Download;

/**绑定描述组件**/
$(document).ready(function(){
	initComponent();
});

function initComponent(tag){
	tag = tag || tag + " ";
	if(tag){
		tag = "";
	}
	zfy.component.idIndex = 1000;
	var focus = false;
	$(tag+"*[class*='$']").each(function(i){
		//TODO 解决ie10下打开页面无法获得焦点的问题
		if(!focus && this.tagName=="INPUT" && !this.type=="hidden"){
			this.focus();
			focus=true;
		}
		var cls = $(this).attr("class").split(" ")[0];
		var component = zfy.component.Config.mapping[cls];
		if(component){
			if(!this.id){
				this.id="_genid_"+(zfy.component.idIndex++);
			}
			if(component.indexOf(".")==-1){
				component = "zfy.component."+component;
			}
			eval("new "+component+"('"+this.id+"')");
		}else{
			alert(cls+"不存在");
		};
	});
}

/** 基础组件**/
zfy.component.BaseComponent = function(){
};

zfy.component.BaseComponent.prototype.init = function(id){

	this.id = id;
	this.ele = $("#"+id);
	this.element = this.ele[0];
	this.element.comobj = this;
};

/**
 * 获得属性值
 * @param name
 * @returns
 */
zfy.component.BaseComponent.prototype.getProperty = function(name,defaultValue){
	var value =  this.ele.attr(name);
	return (defaultValue && (value==null||value==""))?defaultValue+"":value;
};


zfy.component.Dialog = function(data){
	this.url = data.url;
	this.callback = data.callback;
};

zfy.component.Dialog.prototype.openPortalPage = function(obj){

}; 

zfy.component.Dialog.closePortalPage = function(data){

};

zfy.util.Mask = {
		//当浏览器的大小发生改变时，改变遮罩层大小
		calculateSize : function(){
			var b = document.documentElement.clientHeight ? document.documentElement : document.body;
			var height = b.scrollHeight > b.clientHeight ? b.scrollHeight : b.clientHeight;
			var width = b.scrollWidth > b.clientWidth ? b.scrollWidth : b.clientWidth;
			$("#bgDiv").css({"height":height + "px","width":width + "px"});
			$("#loadingDiv").css({"top":height / 2 - 70 + "px","left":width / 2 -100 + "px"});
			$("#loadingLabel").css({"top":height / 2 - 25 + "px","left":width / 2  + "px"});
				
		},
		/**
		 * 添加遮罩
		 */
		show : function (val,centerText){
			centerText = centerText?centerText:"数据处理中，请稍后..";
			var txtWidth = centerText.length * 12
			var bodyWidth = 0;
			var bodyHeight = 0;
			if(val=="1"){
				var portalWindow = $portal.getPortalWindow();
				bodyWidth = portalWindow.innerWidth;
				bodyHeight = portalWindow.innerHeight;
			}else{
				bodyWidth = document.body.clientWidth;
				bodyHeight = Math.max(document.body.clientHeight, document.body.scrollHeight);				
			}
			//}
			//绘制遮罩
			var bgObj = document.createElement("div" );
			bgObj.setAttribute( 'id', 'bgDiv' );
			//添加遮罩
			document.body.appendChild(bgObj); 
			$("#bgDiv").css({
				position:"absolute",
				top:"0",
				background:"aliceblue",
				filter:"progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=50,finishOpacity=50",
				opacity:"0.5",
				left:"0",
				width:bodyWidth + "px",
				height:bodyHeight + "px",
				zIndex: "10000"
			});		
			//添加loading动画
			var loadingObj = document.createElement("div");
			loadingObj.setAttribute( 'id', 'loadingDiv' );
			document.body.appendChild(loadingObj); 
			$("#loadingDiv").css({
				background:"url("+$util.getAppName()+"/resource/platform/theme/blue/images/portal/loading.gif) no-repeat center center",
				position:"absolute",
				top:bodyHeight / 2 - 65 + "px",
				left:bodyWidth / 2 - txtWidth/2 - 75 + "px",
				width:"100px",
				height:"100px",
				zIndex:"10000"
			});
			//添加等待字样
			var loadingLabel = document.createElement("div");
			loadingLabel.setAttribute( 'id', 'loadingLabel' );
			document.body.appendChild(loadingLabel); 
			$("#loadingLabel").css({
				position:"absolute",
				top:bodyHeight / 2 - 25  + "px",
				left:bodyWidth / 2 - txtWidth/2 + "px",
				zIndex:"10000"
			});
			$("#loadingLabel").html("<label style='font-weight:bold'><font size=\"3\">"+centerText+"</font></label>");
			//监听窗口大小的改变
			window.onresize = function(){
				zfy.util.Mask.calculateSize();
			};
		},
		hide : function(){
			if($("#loadingDiv").length>0){
				$('#loadingDiv').remove();
				$('#bgDiv').remove();
				$('#loadingLabel').remove();
			}else{
				$('#loadingDiv', parent.document).remove();
				$('#bgDiv', parent.document).remove();
				$('#loadingLabel', parent.document).remove();				
			}
		}
};
var $mask = zfy.util.Mask;


function accAdd(arg1,arg2){ 
    var r1,r2,m; 
    try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;} 
    try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;} 
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
} 
//给Number类型增加一个add方法，调用起来更加方便。 
Number.prototype.add = function (arg){ 
    return accAdd(arg,this); 
};

//如果complete为true，则查询完整行政区划名
function getXzqhMc(dwdm,complete){
	var mc = "";
	var url = $util.getAppName()+"/xzqh/getXzqhMc.do?dwdm="+dwdm;
	if(complete){
		url = $util.getAppName()+"/xzqh/getXzqhMc.do?dwdm="+dwdm+"&complete=1";
	}
	$.ajax({
		type : "post",
		dataType : "json",
		async : false,
		url : url,
		success : function(data) {
			if(data.success){
				mc = data.data;
			}else{
				$msg.showError("获取行政区划名称时后台出错");
				$msg.showError(data.info);
			}
		},
		error: function(data){
			$msg.showError("获取行政区划名称出错");
		}
	});
	return mc;
}
//根据单位代码获取行政区划
function getXzqh(dwdm){
	var xzqh = {};
	var url = $util.getAppName()+"/xzqh/getXzqh.do?dwdm="+dwdm;
	$.ajax({
		type : "post",
		dataType : "json",
		async : false,
		url : url,
		success : function(data) {
			if(data.success){
				xzqh = data.data;
			}else{
				$msg.showError("获取行政区划时后台出错");
				$msg.showError(data.info);
			}
		},
		error: function(data){
			$msg.showError("获取行政区划出错");
		}
	});
	return xzqh;
}


//这段代码是为了：由于最开始的时候$ajax此工具方法没有写好，导致不好用，所以后来也就是现在，为了实现这样一个功能：
//原生jquery的$.ajax方法拓展一下，使得能够在session超时的时候跳转到登录界面
(function($){  
    //备份jquery的ajax方法  
    var _ajax=$.ajax;  
    //重写jquery的ajax方法  
    $.ajax=function(opt){  
        //备份opt中error和success方法  
        var fn = {  
            error:function(XMLHttpRequest, textStatus, errorThrown){},  
            success:function(data, textStatus){}  
        }  
        if(opt.error){  
            fn.error=opt.error;  
        }  
        if(opt.success){  
            fn.success=opt.success;  
        }  
          
        //扩展增强处理  
        var _opt = $.extend(opt,{  
            error:function(XMLHttpRequest, textStatus, errorThrown){  
                //错误方法增强处理  
            	//your code
                fn.error(XMLHttpRequest, textStatus, errorThrown);  
            },  
            success:function(data, textStatus){  
                //成功回调方法增强处理  
            	if(data=="sessionMiss"){
            		$mask.hide();
					window.top.location.href='../';
				}
                fn.success(data, textStatus);  
            }  
        });  
        _ajax(_opt);  
    };  
})(jQuery);  

/**
 * 解决登录系统后点击键盘上backspace键，系统返回登录页面的问题
 * chenhm 2014-08-22
 */
window.onload = function() {  
    document.getElementsByTagName("body")[0].onkeydown = function() {  
        //获取事件对象  
        var elem = event.relatedTarget || event.srcElement || event.target ||event.currentTarget;   
        if(event.keyCode==8){//判断按键为backSpace键  
          
                //获取按键按下时光标做指向的element  
                var elem = event.srcElement || event.currentTarget;   
                  
                //判断是否需要阻止按下键盘的事件默认传递  
                var name = elem.nodeName;  
                if(name != 'INPUT' && name != 'TEXTAREA'){  
                    return _stopIt(event);  
                }  
                var type_e = elem.type.toUpperCase();  
                if (name == 'INPUT' && (type_e != 'TEXT' && type_e != 'TEXTAREA' && type_e != 'PASSWORD' && type_e != 'FILE')){  
                        return _stopIt(event);  
                }  
                if(name == 'INPUT' && (elem.readOnly == true || elem.disabled == true)){  
                        return _stopIt(event);  
                }  
            }  
        }  
    }  
function _stopIt(e) {  
        if(e.returnValue){  
            e.returnValue = false ;  
        }  
        if(e.preventDefault){  
            e.preventDefault();  
        }                 
        return false;  
} 

//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
// new Date().format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
Date.prototype.format = function(fmt){
	var o = {
	 "M+" : this.getMonth()+1,                 //月份   
	 "d+" : this.getDate(),                    //日   
	 "h+" : this.getHours(),                   //小时   
	 "m+" : this.getMinutes(),                 //分   
	 "s+" : this.getSeconds(),                 //秒   
	 "q+" : Math.floor((this.getMonth()+3)/3), //季度   
	 "S"  : this.getMilliseconds()             //毫秒   
	};
	if(/(y+)/.test(fmt)){
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	}
	for(var k in o){
		if(new RegExp("("+ k +")").test(fmt)){
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		}
	}  
	return fmt;   
};

//获得字符串长度。中文2个长度，英文1个长度
function getLength(str) {
	  ///<summary>获得字符串实际长度，中文2，英文1</summary>
	  ///<param name="str">要获得长度的字符串</param>
	  var realLength = 0, len = str.length, charCode = -1;
	  for (var i = 0; i < len; i++) {
	    charCode = str.charCodeAt(i);
	    if (charCode >= 0 && charCode <= 128) realLength += 1;
	    else realLength += 2;
	  }
	  return realLength;
};

/**
 * Jquey判断一个元素是否是另一个元素的子元素或其本身
 */
jQuery.fn.isChildAndSelfOf = function(b){ 
	return (this.closest(b).length > 0); 
}; 


function getLayerId(layerUrl,name){
	var layerId = "";
	 $.ajax({
		 type:"post",
		 url:layerUrl+"?f=pjson",
		 async:false,
		 success:
			 function (data) {
			 var json = eval("("+data+")");
			 var layerArray = json.layers;
			  for(var i=0;i<layerArray.length;i++){
				if(layerArray[i].name==name){
					 if(layerArray[i].subLayerIds!=null){
					    	for ( var j = 0; j < layerArray[i].subLayerIds.length; j++) {
					    		if(layerArray[i].subLayerIds[j].name==name){
					    			layerId = layerArray[i].subLayerIds[j].id;
					    		}
							}
					    }else{
					    	layerId = layerArray[i].id;
					    }
				}  
			  
			  }
		 }
	 });
	return layerId;
}

//将HTML字符转成转移符
function html2Escape(sHtml) {
	 return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
}