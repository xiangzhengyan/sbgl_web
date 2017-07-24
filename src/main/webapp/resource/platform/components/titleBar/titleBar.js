//标题头组件  hudt 2013-07-10
zfy.component.TitleBar = function(id){
	var titleBar = $("#"+id);
	//添加class属性
	titleBar.addClass("tit");
	//得到标题头中的table
	var innerTable = titleBar[0].innerHTML;
	//添加圆角
	titleBar[0].innerHTML = "<div class=\"tit_bg\">"
					+ "<div class=\"tit_right\"></div>"
					+ "<div class=\"tit_left\"></div>"
					+ innerTable 
					+ "</div>";
	
};


