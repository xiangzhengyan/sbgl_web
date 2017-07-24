/**
 * xiangzy
 */
zfy.component.Tree = function(id){
	//还未判断是否是input
	this.init(id);
	this.multiSelection = this.getProperty("multi-selection")=="true";
	this.rootId = this.getProperty("root-id");
	this.cascade = this.getProperty("cascade")=="true";
	
	this.paint();
	
};
zfy.component.Tree.prototype = new zfy.component.BaseComponent();

zfy.component.Tree.prototype.paint = function(){
	this.tree = new dhtmlXTreeObject(this.id, "100%", "100%", this.rootId);
	var imgurl = $util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/imgs/dhxtree_skyblue/";
	this.tree.setImagePath(imgurl); 
	if(this.multiSelection){
		this.tree.enableCheckBoxes(true);
		if(this.cascade){
			this.tree.enableThreeStateCheckboxes(true);
		}
		
	}
	};

zfy.component.Tree.prototype.loadXML = function(url,afterCall){
	this.tree.loadXML(url,afterCall);
};

zfy.component.Tree.prototype.attachEvent = function(eventName,fun){
	this.tree.attachEvent(eventName,fun);
};
zfy.component.Tree.prototype.getAllSelectedIds = function(eventName,fun){
	var r = this.tree.getAllCheckedBranches();
	return r;
};

zfy.component.Tree.prototype.getAllUnSelectedIds = function(eventName,fun){
	var r = this.tree.getAllUnchecked();
	return r;
};

zfy.component.Tree.prototype.enableContextMenu = function(menu){
	if(menu)    this.tree.cMenu=menu;
};

zfy.component.Tree.prototype.openItem = function(itemId){
	 var temp = this.tree._globalIdStorageFind(itemId);
	    if (!temp)
	        return 0;
	    else
	        return this.tree._openItem(temp);
};

zfy.component.Tree.prototype.findItem = function(searchStr, direction, top, mod,noneCallBack){
	 var item = this.tree.findItem(searchStr, direction, top, mod);
	 if(!item && noneCallBack){
		 noneCallBack();
	 }
};

zfy.component.Tree.prototype.getUserData = function(itemId,name) {
	return this.tree.getUserData(itemId,name);
};

zfy.component.Tree.prototype.setItemStyle = function(itemId,style_string,resetCss) {
	this.tree.setItemStyle(itemId,style_string,resetCss);
};

zfy.component.Tree.prototype.setItemColor = function(a,b,c) {
	this.tree.setItemColor(a,b,c);
};

zfy.component.Tree.prototype.unSelectItem = function(itemId) {
	var temp = this.tree._globalIdStorageFind(itemId);
	this.tree._unselectItem(temp);
};

zfy.component.Tree.prototype.setOnCheckHandler = function(func) {
	this.tree.attachEvent("onCheck", func);
};

zfy.component.Tree.prototype.getItemText = function(itemId) {
	return this.tree.getItemText(itemId);
};

zfy.component.Tree.prototype.isChecked = function(itemId) {
	var sNode = this.tree._globalIdStorageFind(itemId);
    if (!sNode)
        return;
    return sNode.checkstate;
};

zfy.component.Tree.prototype.setOnClickHandler = function(func) {
	this.tree.attachEvent("onSelect",func);
};

zfy.component.Tree.prototype.selectItem = function(itemId,mode,preserve) {
	this.tree.selectItem(itemId, mode, preserve);
};

zfy.component.Tree.prototype.getSelectedItemId = function() {
	return this.tree.getSelectedItemId();
};

zfy.component.Tree.prototype.hasChildren = function(itemId) {
	return this.tree.hasChildren(itemId);
};

zfy.component.Tree.prototype.isSelectRoot = function() {
	var id = this.getSelectedItemId();
	if(id){
		var pid = this.tree.getParentId(id);
		if(!pid || pid=="0"){
			return true;
		}
	}
	
};

zfy.component.Tree.prototype.getSelectedItemText = function() {
	return this.tree.getSelectedItemText();
};

zfy.component.Tree.prototype.insertNewChild = function(parentId,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children) {
    return this.tree.insertNewItem(parentId,itemId,itemText,itemActionHandler,image1,image2,image3,optionStr,children);
 };
 
zfy.component.Tree.prototype.deleteItem = function(itemId,selectParent){
	return this.tree.deleteItem(itemId,selectParent);
};

zfy.component.Tree.prototype.setItemText = function(itemId,newLabel,newTooltip) {
	return this.tree.setItemText(itemId,newLabel,newTooltip);
};