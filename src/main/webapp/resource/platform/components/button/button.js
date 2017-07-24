//按钮组件  hudt 2013-07-09

/* 参数列表
 * id
 */
zfy.component.Button = function(id){
	this.init(id);
	this.paint();
};
zfy.component.Button.prototype = new zfy.component.BaseComponent();
zfy.component.Button.prototype.paint = function(){
	this.ele.addClass("form_button");
	this.ele.hover(
		  function () { 
			  //mouseover
			  $(this).removeClass("form_button");
			  $(this).addClass("form_button_on");
		  },
		  function () {
			  //mouseout
			  $(this).removeClass("form_button_on");
			  $(this).addClass("form_button");
		  }
	);
};
