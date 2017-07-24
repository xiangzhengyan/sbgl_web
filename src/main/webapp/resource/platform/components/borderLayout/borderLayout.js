//borderLayout组件  xiangzy 2013-07-05

zfy.component.BorderLayout = function(id){
	this.init(id);
	
	 $(this.element).addClass("borderLayout");
   	this.topEl = $("#" + id + " .top");
   	this.centerEl = $("#" + id + " .center");
 	this.bottomEl = $("#" + id + " .bottom");

 	var self = this;
 		self.resize(); 
  	var onResize = function(){
//  		if(self.onResizeFlag){
//  			return;
//  		}
//  		self.onResizeFlag = true;
  		self.resize();
//  		window.setTimeout(function(){
//  			self.onResizeFlag = false;
//  		}, 10);
  		
   	};  
   	

 	$(window).resize(onResize);

 	
   	

};

zfy.component.BorderLayout.prototype = new zfy.component.BaseComponent();


zfy.component.BorderLayout.prototype.resize= function(){

	if(!this.centerEl){
		alert("错误信息：borderLayout必须有center节点");
		return;
	} 
	
	var totalW = $(this.element).width();
	var totalH = $(this.element).height();
	if(totalW<0)totalW=0;
	if(totalH<0)totalH=0;

	var top =  0; //对应center用过的height,width,top,left
	var left  = 0; //对应center用过的height,width,top,left
	var height  = 0; //对应center用过的height,width,top,left
	var width = 0; //对应center用过的height,width,top,left

	if(this.topEl){
		var topHeight = this.topEl.height();    
		top+=topHeight;
		height+=topHeight;
		this.topEl.css({width:totalW});
	}
	this.centerEl.css({top:top});
	
	if(this.bottomEl){
		var bottomHeight = this.bottomEl.height();
		if(bottomHeight==0){
			var bootomHeightTmp = this.bottomEl.css("height");
			if(bootomHeightTmp.indexOf("px")>0){
				bottomHeight = parseInt(bootomHeightTmp.substring(0,bootomHeightTmp.length-2));
			}
		} 
		height+=bottomHeight;
		this.bottomEl.css({top:totalH-bottomHeight,height:bottomHeight,width:totalW});
	}
	
	
	height = totalH - height - 1;

	if(height<0)height = 0;
	
	this.centerEl.css({height:height,width:totalW});
	
};





