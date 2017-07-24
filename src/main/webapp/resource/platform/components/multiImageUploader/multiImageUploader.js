//上传多个图片 
/*
 * input-name ： 图片原名的输入框name，后台根据此name接收
 * input-bz:图片备注名称的name，后台根据此name接收备注名称，默认为input-name加Bz
 * type ： view或不填默认edit，代表是查看图片或是编辑新增时
 * json：后台将图片们用json输出List<UploadImages>
 * UploadImages 应有id、imageName、imageBz字段。后台Model参考UploadImages
 * 例：编辑时
 * <div id="multi" class="$multiImageUploader" style="width:100%;height:80%" input-name="images" input-bz="imageBzs" json='${images}'></div>
 * 例：查看时
 * <div id="multi" class="$multiImageUploader" type="view" style="width: 100%; height: 100%" json='${images}'></div>
 */
zfy.component.MultiImageUploader = function(id){
	this.init(id);
	this.inputName= this.getProperty("input-name");	
	this.fileBz= this.getProperty("input-bz",this.inputName+"Bz");	
	this.json= this.getProperty("json");
	this.type= this.getProperty("type","edit");
	this.paint();
};
zfy.component.MultiImageUploader.prototype = new zfy.component.BaseComponent();
zfy.component.MultiImageUploader.prototype.paint = function(){
	var self = this;
	$(document).on("click",".lst",function(){
		self.amplifyImg(this);
	});
	if(self.type == "view"){
		if(this.json){
			var imageArray = eval("("+self.json+")");
			for(var index in imageArray){
				var imageName = imageArray[index].imageName;
				var imageBz = imageArray[index].imageBz;
				self.addViewBox(imageName,imageBz);
			}
			if(imageArray.length == 0){
				this.ele.append("<div style='margin-top:20px;text-align:center;color:#6c6c6c'>没有上传扫描件</div>");
			}
		}else{
			this.ele.append("<div style='margin-top:20px;text-align:center;color:#6c6c6c'>没有上传扫描件</div>");
		}
		return;
	}
	if(!this.json){
		//新增
		self.addNewBox();
	}else{
		//编辑
		var editImageArray = eval("("+self.json+")");
		for(var index in editImageArray){
			var imageName = editImageArray[index].imageName;
			var imageBz = editImageArray[index].imageBz;
			self.addEditedBox(imageName,imageBz);
		}
		self.addNewBox();
	}
};
zfy.component.MultiImageUploader.prototype.addNewBox = function(){
	var newid = new Date().getTime();
	this.ele.append("<label for='"+newid+"'><div class=\"upload-item\"><input id='"+newid+"' name=\"imgFile\" type=\"file\" onchange=\"$com('"+this.id+"').seletedImage(this)\"/><div class=\"upload-tip\">添加图片</div></div>");
	this.resetCss();
};
zfy.component.MultiImageUploader.prototype.addEditedBox = function(imageName,imageBz){
	this.ele.append("<div class=\"upload-item\"><img title='"+imageBz+"' class='lst' src='/upload/img/"+imageName+"'></img><input type='hidden' name='"+this.inputName+"' value='"+imageName+"' /><div class='upload-fileBz'><div class='upload-fileBz-word'>"+imageBz+"</div><img title='修改图片备注' onclick=\"$com('"+this.id+"').editFileBz(event,this)\" src='"+$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/edit.gif'/><input type='text' style='display:none' name='"+this.fileBz+"' value='"+imageBz+"' maxlength='50'/></div><div class=\"upload-del\" onclick=\"$com('"+this.id+"').removeUploadDiv(event,this)\"></div></div>");
	this.resetCss();
};
zfy.component.MultiImageUploader.prototype.addViewBox = function(imageName,imageBz){
	this.ele.append("<div class=\"upload-item\"><img title='"+imageBz+"' class='lst' src='/upload/img/"+imageName+"'></img><input type='hidden' name='"+this.inputName+"' value='"+imageName+"' /><div class='upload-fileBz'><div class='upload-fileBz-word'>"+imageBz+"</div></div></div>");
	this.resetCss();
};
/*图片上传（选择）*/
zfy.component.MultiImageUploader.prototype.seletedImage = function(obj){
	if(!this.checkImageSuffix(obj.value)){
		obj.value = null;
		obj.outerHTML=obj.outerHTML; 
		return;
	}
	if(!this.checkImageSize(obj,10*1024*1024)){
		obj.value = null;
		obj.outerHTML=obj.outerHTML; 
		return;
	}
	
	var self = this;
	var id = new Date().getTime();
	$(obj).attr("id",id);
	$mask.show(null,"正在上传图片..");
	$.ajaxFileUpload({
		url:$util.getAppName()+'/common/uploadFile2.do',
		secureuri:false,
        fileElementId:id,
        dataType: 'json',
        success: function (data, status){
        	$mask.hide();
            if(data){
            	if(data.exNotMatch){
            		$msg.showError("格式不正确");
            		return ;
            	}
            	if(data.success){
            		$msg.showInfo("上传成功");
            		var parnetBox = $("#"+id).parent();
            		//增加隐藏域记住图片名称
            		parnetBox.append("<input type='hidden' name='"+self.inputName+"' value='"+data.filePath+"' />");
            		parnetBox.append("<div class='upload-fileBz'><div class='upload-fileBz-word'>"+data.originalFileName+"</div><img title='修改图片备注' onclick=\"$com('"+self.id+"').editFileBz(event,this)\" src='"+$util.getAppName()+"/resource/platform/js/dhtmlx4.0.3/ico/edit.gif'/><input type='text' style='display:none' name='"+self.fileBz+"' value='"+data.originalFileName+"' maxlength='50'/></div>");
            		//添加移除按钮
            		parnetBox.append("<div class=\"upload-del\" onclick=\"$com('"+self.id+"').removeUploadDiv(event,this)\"></div>");
            		//图片预览
            		parnetBox.find(".upload-tip").html("<img  onclick='$com(\""+this.id+"\").amplifyImg(this)' class='lst' src='/upload/img/"+data.filePath+"'></img>");
            		//删除自己，不再上传
            		$("#"+id).remove();
            		//添加下一张待添加图片位置
            		self.addNewBox();
            	}else{
            		$msg.showError("上传失败");
            	}
            }
        },
        error: function (data, status, e){
        	$mask.hide();
        	$msg.showError("上传失败"+e);
        }
	});
};
/*图片上传（删除）*/
zfy.component.MultiImageUploader.prototype.removeUploadDiv = function(e,obj){
	event.cancelBubble=true;
	obj.parentNode.parentNode.removeChild(obj.parentNode);
};
/*修改文件备注*/
zfy.component.MultiImageUploader.prototype.editFileBz = function(e,obj){
	event.cancelBubble=true;
	$(obj).hide();
	$(obj).parent().find(".upload-fileBz-word").hide();
	var input = $(obj).parent().find("input");
	input.show();
	input.focus();
	input.keydown(function(event){
		if(event.keyCode==13){
			$(this).blur();
		}
	});
	input.on("blur",function(){
		input.hide();
		$(obj).show();
		$(obj).parent().find(".upload-fileBz-word").html(input.val());
		$(obj).parent().find(".upload-fileBz-word").show();
	});
};
zfy.component.MultiImageUploader.prototype.resetCss = function(){
	$(".upload-item").css("height",(this.ele.width()/6 - 14)+"px");
	$(".upload-item").css("width", (this.ele.width()/6 - 14)+"px");
	$(".upload-item input[type='file']").css("width", $(".upload-item").width()+"px");
	$(".upload-item input[type='file']").css("height", $(".upload-item").width()+"px");
	$(".upload-tip").css("width", $(".upload-item").width()+"px");
	$(".upload-tip").css("height", $(".upload-item").width()+"px");
	$(".upload-tip").css("line-height", $(".upload-item").width()+"px");
	$(".lst").css("width", $(".upload-item").width()+"px");
	$(".lst").css("height", $(".upload-item").width()+"px");
	$(".upload-fileBz-word").css("width", (this.ele.width()/6 - 12 - 20)+"px");
};
//检查图片文件后缀名
zfy.component.MultiImageUploader.prototype.checkImageSuffix = function(imgName){
	if(!imgName){
		/*$msg.showError("请选择一张图片文件");*/
		return false;
	}
	idx = imgName.lastIndexOf(".");   
    if (idx != -1){   
        ext = imgName.substr(idx+1).toUpperCase();   
        ext = ext.toLowerCase();
        if(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png"){
        	return true;
        }else{
        	$msg.showError("只能上传后缀名为gif、jpg、png、jpeg的图片文件");
        }
    }else{
    	$msg.showError("只能上传后缀名为gif、jpg、png、jpeg的图片文件");
    	return false;
    }
};
//检查上传图片大小
zfy.component.MultiImageUploader.prototype.checkImageSize = function(target,topSize){
	//检测上传文件的大小        
    var fileSize = 0;       
    if (isIE() && !target.files){       
        var filePath = target.value;       
        var fileSystem = new ActiveXObject("Scripting.FileSystemObject");          
        var file = fileSystem.GetFile (filePath);       
        fileSize = file.Size;      
    } else {      
        fileSize = target.files[0].size;       
    }
    
    if(fileSize > topSize){
    	$msg.showError("上传文件最大不能超过"+ (topSize/1024/1024).toFixed(2) +"M，当前文件"+ (fileSize/1024/1024).toFixed(2) +"M");
    	return false;
    }else{
    	return true;
    }
};

//放大图片
zfy.component.MultiImageUploader.prototype.amplifyImg = function(imgobj){
	var obj = imgobj;
	var htmlString = "<a target='_blank' href='"+obj.src+"'><img src='"+obj.src+"' style='width:100%;height:100%'></img></a>"
	window.top.$dialog.open({
		title : obj.title,
		htmlString : htmlString,
		width : "80%",
		height : "80%",
		maximizable : true,
		modal : true
	})
}