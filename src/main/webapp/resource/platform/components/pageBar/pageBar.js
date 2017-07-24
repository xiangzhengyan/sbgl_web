//分页组件  hudt 2013-07-05

/* 参数列表
 * id
 * formId 需要提交的表单ID
 * totalRows 总记录数
 * pageSize 每页显示记录数
 * currentPage 当前页数	
 */
zfy.component.PageBar = function(id){
	this.init(id);
	this.formId = this.getProperty("form-id");//需要提交的表单ID
	this.totalRows = parseInt(this.getProperty("total-rows","0"));
	this.pageSize = parseInt(this.getProperty("page-size","25"));
	this.currentPage = parseInt(this.getProperty("current-page","1"));
	
	//计算总页数
	this.totalPage = parseInt((parseInt(this.totalRows)  +  parseInt(this.pageSize)  - 1) / this.pageSize) ;
	this.totalPage = this.totalPage==0?1:this.totalPage;
	//计算前一页
	this.prePage = (this.currentPage-1) > 0 ? (this.currentPage-1) : 1;
	
	//计算后一页
	this.nextPage = (this.currentPage+1) <= this.totalPage ? (this.currentPage+1) : this.totalPage;//计算后一页
	
	//计算图片是否显示彩色背景图片，false为灰色图背景
	this.firstStatus = (this.currentPage > 1) ? true : false;
	this.preStatus = (this.currentPage > 1) ? true : false;
	this.nextStatus = (this.currentPage < this.totalPage) ? true : false;
	this.lastStatus = (this.currentPage < this.totalPage) ? true : false;
	this.paint();
};
zfy.component.PageBar.prototype = new zfy.component.BaseComponent();

zfy.component.PageBar.prototype.paint = function(){
	var firstImage = this.firstStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/first-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_first-page.gif";
	var preImage = this.preStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/pre-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_pre-page.gif";
	var nextImage = this.nextStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/next-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_next-page.gif";
	var lastImage = this.lastStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/last-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_last-page.gif";
	var self = this;
	var txt = "<div style='float:left;height:20px;padding:1px 5px'>共<span id='"+this.id+"totalRowsSpan'>"+this.totalRows+"</span>条记录 每页"
     + "<select class='pageSize1' style='height:18px'>"
     + "<option value='10'>10</option>"
     + "<option value='25'>25</option>"
     + "<option value='50'>50</option>"
     + "<option value='100'>100</option>"
     + "</select>行 第"+ this.currentPage +"/"+this.totalPage+"页&nbsp;&nbsp;</div>"
     + "<div class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('1','first','"+this.formId+"')\"><img style='padding-top:1px;' src='"+firstImage+"' /><span class='_pageBarWd'>首页</span></div>"
     + "<div class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.prePage+"','pre','"+this.formId+"')\"><img style='padding-top:1px;' src='"+preImage+"' /><span  class='_pageBarWd'>前页</span></div>" 
     + "<div class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.nextPage+"','next','"+this.formId+"')\"><img style='padding-top:1px;' src='"+nextImage+"' /><span  class='_pageBarWd'>后页</span></div>"
     + "<div class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.totalPage+"','last','"+this.formId+"')\"><img style='padding-top:1px;' src='"+lastImage+"' /><span  class='_pageBarWd'>尾页</span></div>";
//     + "<input name=\"toPage\" id=\"toPage\" type=\"text\" size=\"3\" />页&nbsp;&nbsp; "
//     + "<a href=\"javascript:;;\" onclick=\"$com('"+this.id+"').toPage('"+this.formId+"');\">GO</a>"
//	var txt2= "<input type='hidden' id='pageNum' name='pageNum' value='"+this.currentPage+"'/>"
     var txt2 = "<input type='hidden' id='"+this.id+"_pageSize' name='pageSize' value='"+this.pageSize+"'/>";
//     + "<input type='hidden' id='totalPage'  value='"+this.totalPage+"'/>"
     
	this.ele.html(txt);
	$("#"+this.formId).append(txt2);
	$("._pageBarSpan").css("cursor","pointer");
	$("._pageBarSpan").css("float","left");
	$("._pageBarSpan").css("height","20px");
	$("._pageBarSpan").css("line-height","18px");
	$("._pageBarSpan").css("padding","1px 5px 1px 5px");
	$("._pageBarSpan").css("border-radius","2px");
	$("._pageBarSpan").on("mouseover",function(){
		$(this).css("background-color","#ACDAF0");
	});
	$("._pageBarSpan").on("mouseout",function(){
		$(this).css("background-color","");
	});
	$("#"+this.id+" select").val(this.pageSize);
	$("#"+this.id+" select").on("change",function(){
		self.go(1, $(this).val());
	});
};

zfy.component.PageBar.prototype.gotoPage = function(pageNo,action,formId) {
//	var currPageNo = $("#pageNum").val();
//	var totalPage = $("#totalPage").val();
	pageNo = parseInt(pageNo);
	var tipMsg = "";
	var flag = true;
	if (action == "first"){
		if (!this.firstStatus) {
			tipMsg = "当前已是第一页！";
			flag = false;
		}
	} else if (action == "pre"){
		if (!this.preStatus){
			flag = false;
			tipMsg = "当前已是第一页!";
		}
	} else if (action == "next"){
		if (!this.nextStatus){
			flag = false;
			tipMsg = "当前已是最后一页！";
		}
	} else {
		if (!this.lastStatus){
			flag = false;
			tipMsg = "当前已是最后一页！";
		}
	}
	if (!flag) {
		$msg.showWarning(tipMsg);
		return;
	}
	//提交表单
	this.go(pageNo, this.pageSize);
};

zfy.component.PageBar.prototype.go = function(pageNo,pageSize){
	var url;
	if($("#"+this.formId).attr("action").indexOf("?")>=0){
		url = $("#"+this.formId).attr("action")+"&pageSize="+pageSize+"&pageNum="+pageNo;
	}else{
		url = $("#"+this.formId).attr("action")+"?pageSize="+pageSize+"&pageNum="+pageNo;
	}
    $("#"+this.formId).attr("action",url);
    $mask.show();
    $("#"+this.formId).submit();
};

//AJAX分页组件  hudt 2013-07-05

/* 参数列表
 * id
 * url 需要提交的URL
 * totalRows 总记录数
 * pageSize 每页显示记录数
 * currentPage 当前页数	
 * params 参数
 * beforeSubmit 转页之前的function
 */
zfy.component.AjaxPageBar = function(id){
	this.init(id);
	this.url = this.getProperty("url");//需要提交的URL
	this.params = this.getProperty("params","{}");//需要提交的参数
	this.callback = this.getProperty("callback","callback");//需要提交的表单ID
	this.beforeSubmit = this.getProperty("beforeSubmit");//转页之前的function
	this.totalRows = parseInt(this.getProperty("total-rows","0"));
	this.pageSize = parseInt(this.getProperty("page-size","25"));
	this.currentPage = parseInt(this.getProperty("current-page","1"));
	
	//计算总页数
	this.totalPage = parseInt((parseInt(this.totalRows)  +  parseInt(this.pageSize)  - 1) / this.pageSize) ;
	this.totalPage = this.totalPage==0?1:this.totalPage;
	//计算前一页
	this.prePage = (this.currentPage-1) > 0 ? (this.currentPage-1) : 1;
	
	//计算后一页
	this.nextPage = (this.currentPage+1) <= this.totalPage ? (this.currentPage+1) : this.totalPage;//计算后一页
	
	//计算图片是否显示彩色背景图片，false为灰色图背景
	this.firstStatus = (this.currentPage > 1) ? true : false;
	this.preStatus = (this.currentPage > 1) ? true : false;
	this.nextStatus = (this.currentPage < this.totalPage) ? true : false;
	this.lastStatus = (this.currentPage < this.totalPage) ? true : false;
	
	eval("this.params="+this.params);
	this.draw();
	
};
zfy.component.AjaxPageBar.prototype = new zfy.component.BaseComponent();

zfy.component.AjaxPageBar.prototype.draw = function(){
	var firstImage = this.firstStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/first-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_first-page.gif";
	var preImage = this.preStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/pre-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_pre-page.gif";
	var nextImage = this.nextStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/next-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_next-page.gif";
	var lastImage = this.lastStatus?$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/last-page.gif":$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/un_last-page.gif";
	var txt = "共"+this.totalRows+"条记录 每页"
     + "<select class='pageSize1' onchange=\"this.parentNode.comobj.changePageSize(this,'"+this.formId+"')\">"
     + "<option value='10'>10</option>"
     + "<option value='25'>25</option>"
     + "<option value='50'>50</option>"
     + "<option value='100'>100</option>"
     + "</select>"
     + "行 第"+ this.currentPage +"/"+this.totalPage+"页"
     + "&nbsp;&nbsp;<span class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('1','first','"+this.url+"')\"><img style='padding-top:3px;' src='"+firstImage+"' /><span class='_pageBarWd'>首页</span></span>&nbsp;&nbsp;"
     + "<span class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.prePage+"','pre','"+this.url+"')\"><img style='padding-top:3px;' src='"+preImage+"' /><span  class='_pageBarWd'>前页</span></span>&nbsp;&nbsp;" 
     + "<span class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.nextPage+"','next','"+this.url+"')\"><img style='padding-top:3px;' src='"+nextImage+"' /><span  class='_pageBarWd'>后页</span></span>&nbsp;&nbsp; "
     + "<span class='_pageBarSpan' onclick=\"$com('"+this.id+"').gotoPage('"+this.totalPage+"','last','"+this.url+"')\"><img style='padding-top:3px;' src='"+lastImage+"' /><span  class='_pageBarWd'>尾页</span></span>&nbsp;&nbsp;";
     + "<input type='hidden' id='pageNum' name='pageNum' value='"+this.currentPage+"'/>"
     + "<input type='hidden' id='pageSize' name='pageSize' value='"+this.pageSize+"'/>"
     + "<input type='hidden' id='totalPage'  value='"+this.totalPage+"'/>";
	this.ele.html(txt);
	$("._pageBarSpan").css("cursor","pointer");
	$("._pageBarSpan").on("mouseover",function(){
		$(this).css("background-color","#ACDAF0");
	});
	$("._pageBarSpan").on("mouseout",function(){
		$(this).css("background-color","");
	});
	$("#"+this.id+" select").val(this.pageSize);
};

zfy.component.AjaxPageBar.prototype.gotoPage = function(pageNo,action,url) {
	
	pageNo = parseInt(pageNo);
	var tipMsg = "";
	var flag = true;
	if (action == "first"){
		if (!this.firstStatus) {
			tipMsg = "当前已是第一页！";
			flag = false;
		}
	} else if (action == "pre"){
		if (!this.preStatus){
			flag = false;
			tipMsg = "当前已是第一页!";
		}
	} else if (action == "next"){
		if (!this.nextStatus){
			flag = false;
			tipMsg = "当前已是最后一页！";
		}
	} else {
		if (!this.lastStatus){
			flag = false;
			tipMsg = "当前已是最后一页！";
		}
	}
	if (!flag) {
		$msg.showWarning(tipMsg);
		return;
	}
	//提交表单
	this.submit(this.url, pageNo,$("#"+this.id+" [id='pageSize']").val());
};

zfy.component.AjaxPageBar.prototype.changePageSize = function(slt,formid){
	//提交表单
	this.submit(this.url, 1,$(slt).val());
};

zfy.component.AjaxPageBar.prototype.addParams = function(params){
	for(var key in params){ 
      this.params[key] = params[key];
    } 
};

zfy.component.AjaxPageBar.prototype.submit = function(url,pageNo,pageSize){
	this.params["pageNum"] = pageNo;
	this.params["pageSize"] = pageSize;
	if(this.beforeSubmit){
		eval(this.beforeSubmit);
	}
	$ajax.post(url,this.params,eval(this.callback));
};



