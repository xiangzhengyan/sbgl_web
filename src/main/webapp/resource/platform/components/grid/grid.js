/*
 * 
 */
zfy.component.Grid = function(id) {
	this.init(id);
	this.width = this.getProperty("width");
	this.height = this.getProperty("height");
	this.formId = this.getProperty("form-id");
	this.sortCol = this.getProperty("sort-col");
	this.sortOrder = this.getProperty("sort-order");
	/*定义全局for了排序*/
	this.sortNameArr = [];
	if(this.width){
		var cwidth = "";
		if((this.width+"").indexOf("%") >0){
			cwidth = (parseFloat((this.width+"").substring(0,(this.width+"").length-1)) + 0) + "%";
		}else{
			cwidth = parseFloat((this.width+"").replace("px","")) + 20;
			if(cwidth > $(window).width()){
				cwidth = "99.5%";
			}
		}
		this.element.setAttribute("gridWidth",cwidth);
	}
	if(this.height){
		this.element.setAttribute("gridHeight",this.height);
	}
	$("#"+this.id+" th").each(function(i){
		if($(this).attr("ext-align")=="number"){
			$(this).attr("align","right");
		}
	});
	this.paint();  
	this.ele=$("#"+id);
	this.element=$("#"+id)[0];
	
};
zfy.component.Grid.prototype = new zfy.component.BaseComponent();

zfy.component.Grid.prototype.paint = function() {
	var self = this;
	/*针对数字列 th 给了format属性值时 自动绑定type 满足dhtmlXGridFromTable API*/
	$("#"+this.id+" th").each(function(i){
		if($(this).attr("format")){
			$(this).attr("type","edn");
		}
	});
	
	/*
	 * 针对排序的操作
	 */
	/*初始化排序工作*/
	if(this.sortCol!=null&&this.sortOrder!=null){
		var html = "";
		var form = $("#"+this.formId);
		if($("#sortCol").length>0&&$("#sortOrder").length>0){
			$("#sortCol").val(this.sortCol);
			$("#sortOrder").val(this.sortOrder);
		}else if($("#sortCol").length>0){
			$("#sortCol").val(this.sortCol);
			html = "<input type='hidden' id='sortOrder' name='sortOrder' value='"+this.sortOrder+"'/>";
		}else if($("#sortOrder").length>0){
			html = "<input type='hidden' id='sortCol' name='sortCol' value='"+this.sortCol+"'/>";
			$("#sortOrder").val(this.sortOrder);
		}else{
			html = "<input type='hidden' id='sortCol' name='sortCol' value='"+this.sortCol+"'/>"
    		 	 + "<input type='hidden' id='sortOrder' name='sortOrder' value='"+this.sortOrder+"'/>";
		}
		form.append(html);
	}
	/*记录排序的列*/
	var sortNameArr = [];
	$("#"+this.id+" th").each(function(i){
		var sortName = $(this).attr("sort-name");
		if(sortName&&sortName!=null){
			sortNameArr.push(sortName);	//该列需要排序列名放入
		}else{
			sortNameArr.push(null);		//不用排序的用null占位
		}
	});
	this.sortNameArr = sortNameArr;
	
	this.grid = new dhtmlXGridFromTable(this.element);
	
	/**
	 * 设置是否显示排序状态
	 */
	if(this.sortCol!=null&&this.sortOrder!=null){
		/*找到排序的列号*/
		var index = this.sortNameArr.indexOf(this.sortCol);
		if(index>=0){
			this.setSortImgState(true, index, this.sortOrder);
		}
	}
	
	/*注册Grid表格的排序事件 */ 
	this.grid.attachEvent("onBeforeSorting", function(ind){
		self.sortGridOnServer(ind);
	});  
	
	//this.grid.enableAutoWidth();
	this.grid.enableAutoHeight();
	this.grid.setSkin("dhx_skyblue");
	//dhtmlx grid拆分原始表格后，原来的element\ele重新生成
	this.grid.entBox.id = this.id;
	this.init(this.id);
	if(this.grid.getRowsNum() == 0){
		this.grid.addRow(-1,"无相关数据"); 
		var _row = this.grid.getRowById(-1);
		var _cellCount = this.grid.getColumnsNum();//列数
		var _cell =_row.childNodes[0];
		_cell.colSpan =_cellCount; 
		this.grid.setRowTextStyle(-1,'text-align:center');
		/*$("#"+this.id).after("<div id=\"noData\">无相关数据...</div>");
		$("#noData").css(
				{"position":"absolute",
						  "top":"50px",
						  "z-index":'1001',
						  "left":'150px'});*/
	}
	var self = this;
	this.grid.attachEvent("onRowSelect",function(rowId){
		if(rowId == -1){
			self.grid.clearSelection();
		}
	});
};


/***************************************/

zfy.component.Grid.prototype.updateRow = function(id,data) {
	//data转义
	for(i in data){
		data[i] = $util.toHtml(data[i]);
	}
	for (var i = 0;i<data.length;i++) {
		this.setCellValue(id,i,data[i]==null?"":data[i]);
	}
};

zfy.component.Grid.prototype.deleteRow = function(id,decreaseIds) {
	if(id==null || id == ""){
		this.grid.deleteSelectedRows();
	}else{
		var r = this.grid.getRowById(id);
		var rowIndex = this.grid.getRowIndex(id);
		var nextRow = this.grid._nextRow(rowIndex,1);
		//向下选择为空，则向上选择
		if(nextRow == null){
			nextRow = this.grid._nextRow(rowIndex,-1);
		}
		this.grid.deleteRow(id,r);
		this.grid.selectRow(nextRow,true);
	}
	//比如分页的总记录数，需要做-1操作
	if(typeof decreaseIds == "string"){
		var node = $("#"+decreaseIds);
		if(node != null){
			if(node.html()!=null){
				node.html(parseInt(node.html())-1);
			}
			if(node.val()!=null){
				node.val(parseInt(node.val())-1);
			}
		}
	}else if(typeof decreaseIds == "object"){
		for(var i=0 ; i<decreaseIds.length;i++){
			var node = $("#"+decreaseIds[i]);
			if(node.val()!=null){
				node.val(parseInt(node.val())-1);
			}
			if(node.html()!=null){
				node.html(parseInt(node.html())-1);
			}
		}
	}
};

zfy.component.Grid.prototype.addRow = function(id,data,increaseIds) {
	//data转义
	for(i in data){
		data[i] = $util.toHtml(data[i]);
	}
	
	//先检查是否有无数据行，有的话，先删除掉无数据行
	var allRowIds = this.grid.getAllRowIds(",").split(",");
	for(var i in allRowIds){
		if(allRowIds[i] == -1){
			this.deleteRow(allRowIds[i]);
		}
	}
	this.grid.addRow(id, data, -1);
	this.selectRowById(id);
	//比如分页的总记录数，需要做+1操作
	if(typeof increaseIds == "string"){
		var node = $("#"+increaseIds);
		if(node != null){
			if(node.val()!=null){
				node.val(parseInt(node.val())+1);
			}
			if(node.html()!=null){
				node.html(parseInt(node.html())+1);
			}
		}
	}else if(typeof increaseIds == "object"){
		for(var i=0 ; i<increaseIds.length;i++){
			var node = $("#"+increaseIds[i]);
			if(node != null){
				if(node.val()!=null){
					node.val(parseInt(node.val())+1);
				}
				if(node.html()!=null){
					node.html(parseInt(node.html())+1);
				}
			}
		}
	}
	
};

zfy.component.Grid.prototype.deleteAllRows = function(id) {
	this.grid.clearAll();
	this.addNoDataRow();
};

zfy.component.Grid.prototype.getRowCount = function() {
	
};

zfy.component.Grid.prototype.getSelectedRowId = function() {
	return this.grid.getSelectedId();
};

zfy.component.Grid.prototype.selectRowById = function(rowid) {
	this.grid.selectRowById(rowid,null,true,true);
};

zfy.component.Grid.prototype.setCellValue = function(rowId,colIndex,value) {
	var cell = this.grid.cells(rowId, colIndex);
	if(cell){
		cell.setValue(value);
	}
};

zfy.component.Grid.prototype.getCellValue = function(rowId,colIndex) {
	var cell = this.grid.cells(rowId, colIndex);
	if(cell){
		return cell.getValue();
	}
};

zfy.component.Grid.prototype.attachEvent = function(eventName,fun){
	this.grid.attachEvent(eventName,fun);
};

/*设置行样式*/
zfy.component.Grid.prototype.setRowTextStyle = function(rowId,rowStyleStr){
	this.grid.setRowTextStyle(rowId,rowStyleStr);
};

/*设置列样式*/
zfy.component.Grid.prototype.setCellTextStyle = function(rowId,cellIndex,cellStyleStr){
	this.grid.setCellTextStyle(rowId,cellIndex,cellStyleStr);
};

/*设置行样式*/
zfy.component.Grid.prototype.setRowColor = function(rowId,rowStyleStr){
	this.grid.setRowColor(rowId,rowStyleStr);
};

/*设置开启排序样式*/
zfy.component.Grid.prototype.setSortImgState = function(state, index, direct){
	this.grid.setSortImgState(state, index, direct);
};

/*排序服务 ind列、gridObj、direct排序[asc、desc]*/
zfy.component.Grid.prototype.sortGridOnServer = function(ind){
	var sortNameArr = this.sortNameArr;
	var col = sortNameArr[ind];		//找到触发的列的列名
	if(col&&col!=null){
		var order = $("#"+this.formId+" #sortOrder").val();	//找到原有的排序
		//改变排序方法
		if(order==null||order=='asc'){
			order = 'desc';
		}else{
			order = 'asc';
		}
		/*找到其form表单 创建对应的隐藏域*/
		$("#"+this.formId+" #sortCol").val(col);
		$("#"+this.formId+" #sortOrder").val(order);
		/*提交*/
		$form.submit(this.formId);
		//this.setSortImgState(true, ind, direct); //是否出现上下头
		//return true;
	}
};


/*ajax可以用*/
zfy.component.Grid.prototype.addNoDataRow = function(){
	if(this.grid.getRowsNum() == 0){
		this.grid.addRow(-1,"无相关数据"); 
		var _row = this.grid.getRowById(-1);
		var _cellCount = this.grid.getColumnsNum();//列数
		var _cell =_row.childNodes[0];
		_cell.colSpan =_cellCount; 
		this.grid.setColAlign("center");
	}
};
