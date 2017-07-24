// JavaScript Document
//�Զ���һ��menu����;
function Menu(id,name,parentId,url){
	this.id = id;
	this.name = name;
	this.parentId = parentId;
	this.url = url;
	this.children = null;
}

Menu.prototype.getChildren = function () {
	if (this.children == 'undefined' || this.children == null){
		this.children = new Array();
	}
	return this.children;
};

Menu.prototype.addChild = function(menu){
   if (this.id == menu.parentId){
	    var children = this.getChildren();
		var len = children.length;
		children[len] = menu;
		this.children = children;
	}
};

/**
 * 解析json数据，生成menu
 * @param data
 * @param favJson
 */
function menu(data, favJson, appName) {
	var menuArr = new Array();
	var j = 0;
	if(data==null){
		return;
	}
	for (var i = 0; i < data.length; i++) {
        if (data[i].parentId == 0) {
        	var menu = new Menu(data[i].id, data[i].label, data[i].parentId, data[i].url);
        	remenu = reMenu(data, data[i].id, menu);
            menuArr[j] = remenu;
            j = j + 1;
        }
	}
	var menu_fav = new Menu(1, '收藏夹', null, null);
	if (favJson!= null && favJson != '') {
	     for (var n = 0; n < favJson.length; n++) {
	     	var menuChild_fav = new Menu(favJson[n].id, favJson[n].label, 1, favJson[n].url);
	     	menu_fav.addChild(menuChild_fav);
	     }
	     menuArr[j] = menu_fav;
	}
     $("#menu").empty();
     $("#menu").html(initMenu(menuArr, appName));
}

/**
 * 递归生成菜单
 * @param data
 * @param id
 * @param menu
 * @returns
 */
function reMenu(data, id, menu) {
	for (var k = 0; k < data.length; k++) {
        if (data[k].parentId == id) {
        	var menuChild = new Menu(data[k].id, data[k].label, data[k].parentId, data[k].url);
        	menu.addChild(menuChild);
        	reMenu(data, data[k].id, menuChild);
        }
    }
	return menu;
}

/**
 * 初始化菜单信息
 * @param menuArr
 * @returns {String}
 */
function initMenu(menuArr, parentName) {
	var _parentName = parentName;
	var html = "";
	if (menuArr == 'undefined') {
		//jQuery("#nav").html(html);
		return html;
	}
	var len = menuArr.length;
	for (var i = 0;i < len;i++){
		parentName = _parentName;
		var menu = menuArr[i];
		var url = menu.url;
		parentName = "$a$"+parentName+"$/a$$s$";
		if (menu.children != null && menu.children.length > 0) {
			parentName = parentName + "$a$" + menu.name + "$/a$$s$";
			html += "<li class='menu_sub'>";
			if(url == null || url == "" || url == "#"){
			    html += " <a title='' href='#' class='nav_sub'><span>" + menu.name + "</span></a>";
			} else {
			    html += " <a title='' target='data' onclick='showLink(\"" + childParentName + "\");showFav(\""+i+"\",\""+menu.id+"\");setCurrentFunction(\""+menu.id+"\",\""+menu.url+"\");'  href='" + url + "'><span>" + menu.name + "</span></a>";
			}
			html += recursionMenu(menu.children,parentName, menu.id);
		} else {
			parentName = parentName + "$SS$" + menu.name + "$/SS$";
			html += "<li>";
			if(url == null || url == "" || url == "#"){
			   html += " <a title='' href='#'><span>" + menu.name + "</span></a>";
			} else {
			   html += " <a title='' target='data' onclick='showLink(\"" + childParentName + "\");showFav(\""+i+"\",\""+menu.id+"\");setCurrentFunction(\""+menu.id+"\",\""+menu.url+"\");' href='" + url + "'><span>" + menu.name + "</span></a>";
			}
		}
		html += "</li>";
		if(i != len - 1) {
			html += "<li>| </li>";
	    }
	}
	return html;
}

/**
 * 无限极菜单初始化
 * @param children
 * @param parentName
 * @param i
 * @returns {String}
 */
function recursionMenu (children,parentName, i) {
	var html = "<ul>";
	var childParentName = "";
	$.each(children, function(index, menu) {
		var url = menu.url;
		if (i == 1) {
			childParentName = getFavParentName(menu.id);
		} else {
			childParentName = parentName;
		}
		if (menu.children != null && menu.children.length > 0) {
			childParentName += "$a$" + menu.name + "$/a$$s$";
			html += "<li class=''>";
			if(url == null || url == "" || url == "#"){
				html += "<a  href='#' class='nav_sub'>"+ menu.name + "</a>";
			} else {
				html += "<a title='' target='data' onclick='showLink(\"" + childParentName + "\");showFav(\""+i+"\",\""+menu.id+"\");setCurrentFunction(\""+menu.id+"\",\""+menu.url+"\");' href='" + url + "'>" + menu.name + "</a>";
			}
			html += recursionMenu(menu.children,childParentName);
		} else {
			childParentName +=  "$SS$" + menu.name + "$/SS$"; 
			html += "<li>";
			if(url == null || url == "" || url == "#"){
				html += "<a  href='#'>"+ menu.name + "</a>";
			} else {
				if (i == 1) {
					html += "<a title='' target='data' onclick='form_Submit(\""+childParentName+"\",\""+menu.url+"\",\""+menu.id+"\");showLink(\"" + childParentName + "\");showFav(\""+i+"\",\""+menu.id+"\");setCurrentFunction(\""+menu.id+"\",\""+menu.url+"\");' href='" + url + "'>" + menu.name + "</a>";
				} else {
					html += "<a title='' target='data' onclick='showLink(\"" + childParentName + "\");showFav(\""+i+"\",\""+menu.id+"\");setCurrentFunction(\""+menu.id+"\",\""+menu.url+"\");' href='" + url + "'>" + menu.name + "</a>";
				}
			}
		}
		childParentName = "";
        html += "</li>";
	});
	html += "</ul>";
	return html;
}

/**
 * 设置当前菜单id和菜单url
 * @param functionId
 * @param url
 */
function setCurrentFunction(functionId, url) {
	currentId = functionId;
	currentUrl = url;
}

/**
 * 菜单链接显示
 * @param str
 */
function showLink(str){
	if (str == "" || str == undefined)
		str = jQuery("#title_path_td_bak").html();
	while(str.indexOf("$a$") >= 0){str = str.replace("$a$","<span style='color: #af2d25'>");}
	while(str.indexOf("$/a$") >= 0){str = str.replace("$/a$","</span>");}
	while(str.indexOf("$s$") >= 0){str = str.replace("$s$","<span >&gt;</span>");}
	while(str.indexOf("$SS$") >= 0){str = str.replace("$SS$","<span>");}
	while(str.indexOf("$/SS$") >= 0){str = str.replace("$/SS$","</span>");}	
	jQuery("#title_path_td").html(str);
}

/**
 * 新打开页面
 * @param title 页面标题
 * @param url 页面url
 */
function openPortalPage(url, title, tabId, index) {
	var str = $("#title_path_td").html();
	while(str.indexOf('<span style="color: #af2d25">') >= 0){str = str.replace('<span style="color: #af2d25">',"$a$");}
	while(str.indexOf("</span><span>&gt;</span>") >= 0){str = str.replace("</span><span>&gt;</span>","$/a$$$s$");}
	while(str.indexOf("<span>") >= 0){str = str.replace("<span>","$SS$");}
	while(str.indexOf("</span>") >= 0){str = str.replace("</span>","$/SS$");}
	while(str.indexOf('<SPAN style="COLOR: #af2d25">') >= 0){str = str.replace('<SPAN style="COLOR: #af2d25">',"$a$");}
	while(str.indexOf("</SPAN><SPAN>&gt;</SPAN>") >= 0){str = str.replace("</SPAN><SPAN>&gt;</SPAN>","$/a$$s$");}
	while(str.indexOf("<SPAN>") >= 0){str = str.replace("<SPAN>","$SS$");}
	while(str.indexOf("</SPAN>") >= 0){str = str.replace("</SPAN>","$/SS$");}
	str = str.replace("$SS$","$a$").replace("$/SS$","$/a$$$s$");
	str = str + "$SS$" + title + "$/SS$";
	while(str.indexOf("$a$") >= 0){str = str.replace("$a$",'<span style="color: #af2d25">');}
	while(str.indexOf("$/a$") >= 0){str = str.replace("$/a$","</span>");}
	while(str.indexOf("$s$") >= 0){str = str.replace("$s$","<span >&gt;</span>");}
	while(str.indexOf("$SS$") >= 0){str = str.replace("$SS$","<span>");}
	while(str.indexOf("$/SS$") >= 0){str = str.replace("$/SS$","</span>");}	
	while(str.indexOf("$") >= 0){str = str.replace("$","");}
	$("#title_path_td").html(str);
	$("#data").attr("src", url);
}

// 关闭当前页面，返回首页
function closePortalPage() {
	$("#title_path_td").html("首页");
	$("#data").attr("src", url);
}