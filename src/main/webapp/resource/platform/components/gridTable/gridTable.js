/*
 * 
 */
zfy.component.GridTable = function(id) {

	this.init(id);
	// this.headerRowCount = parseInt(this.getProperty("header-count","1"));
	this.headerRowCount = 1;
	this.selectedId = null;
	this.paint();

};
zfy.component.GridTable.prototype = new zfy.component.BaseComponent();

zfy.component.GridTable.prototype.paint = function() {
	
	this.ele.attr("cellpadding", 0);
	this.ele.attr("cellspacing", 0);
	this.ele.attr("border", 0);
	this.ele.attr("class", "grid_table");
	var self = this;
	this.ele.find("tr").each(function(i) {
		//$msg.showInfo(self.headerRowCount);
		if (i > self.headerRowCount - 1) {
			var tr = $(this);
//			tr.hover(function(event) {
//				tr.addClass("row_on");
//			}, function(event) {
//				tr.removeClass("row_on");
//			});
			tr.addClass(i % 2 == 0 ? "row_odd" : "row_even");
		}
	});
	var trs = document.getElementById(this.id).getElementsByTagName('tr');
	window.onload = function() {
		for (var i = 0; i < trs.length; i++) {
			trs[i].onmousedown = function() {
				tronmousedown(this);
			};
		}
	};
	function tronmousedown(obj) {
		for ( var o = 0; o < trs.length; o++) {
			if (trs[o] == obj) {
				trs[o].style.backgroundColor = '#FEFBBD';
				self.selectedId = $(trs[o]).attr("id");
			} else {
				trs[o].style.backgroundColor = '';
			}
		}
	}  
};


zfy.component.GridTable.prototype.updateRow = function(id,data) {
	var tr = this.ele.find("#"+id);
	if(tr){
		tr.children("td").each(function(i){
			if(i>=data.length)return;
			if(data[i]!=null){
				this.innerHTML = data[i];
			}
		});
	}
};

zfy.component.GridTable.prototype.deleteRow = function(id) {
	var tr = this.ele.find("#"+id);
	if(tr){
		tr.remove();
	}
};

zfy.component.GridTable.prototype.deleteSelectedRow = function() {
	var tr = this.ele.find("#"+this.selectedId);
	if(tr){
		tr.remove();
	}
};


zfy.component.GridTable.prototype.getSelectedRowId = function() {
	 return this.selectedId;
};

zfy.component.GridTable.prototype.addRow = function(id,data) {
//	var str = "<tr id=\""+id+"\">";
//	
//	for ( var i = 0; i < data.length; i++) {
//		str+="<td>"+data[i]+"</td>";
//	}
//	str += "</tr>";
//	this.ele.append(str);
	
	var tr = this.element.insertRow(this.element.rows.length);
	tr.id = id;
	for ( var i = 0; i < data.length; i++) {
	  var td = tr.insertCell(i);
	  td.innerHTML = data[i];
	}
	
	this.paint();
};

zfy.component.GridTable.prototype.deleteAllRows = function(id) {
	var trs = this.ele.find("tr");
	trs.each(function(index){
		//index==0为th表头，不删 
		if(index>0)
		$(this).remove();
	});
};

zfy.component.GridTable.prototype.getRowCount = function() {
	return this.ele.find("tr").length - this.headerRowCount;
};

zfy.component.GridTable.prototype.fixTable = function(row,col,color) {
	this.ele.fixTable(
			row, //可滚动区域第一行的行号
			col, //可滚动区域第一列的列号
			color //(可选)固定区域与滚动区域的分隔线颜色
		);
};
