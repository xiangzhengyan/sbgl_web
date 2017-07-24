<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>配置管理</title>
</head>
<script>
	function save(){
		if($form.validate("cfgForm")){
			$msg.showWarning('请确认输入都符合规范');
			return;
		}
		$form.ajaxSubmit("cfgForm",function(data){
			if(data.success){
				$msg.showInfo('保存成功！');
				$portal.closePage(data);
			}else{
				$msg.showWarning("保存失败！");
			}
		});
	}
</script>
<body style="padding: 0 5px 0 5px;">
	<div class="$borderLayout" style="width: 100%; height: 100%;" >
		<div class="top" style="height: 30px;">
			<div class="$toolBar">
		        <item name="back" action="javaScript:$portal.closePage();"></item>
			</div>
		</div>
		<div class="center">
			<div class="title" style="width:540px">			
				查看配置项
			</div>
				<table cellpadding="0" cellspacing="0" border="0" class="form_table">
					<tr>
						<th>参数名称</th>
						<td>${cfg.name }</td>
					</tr> 
					<tr>
						<th>参数值</th>
						<td style="width: 400px; height: 50px;">
							<textArea style="width: 100%; height: 100%" readonly="readonly" class="$input" required="true" id="value" name="value" max-length="256">${cfg.value }</textArea>
						</td>
					</tr>
					<tr>
						<th>参数描述</th>
						<td style="width: 400px; height: 50px;">
							<textArea style="width: 100%; height: 100%" readonly="readonly" class="$input" id="description" name="description" max-length="256">${cfg.description }</textArea>
						</td>
					</tr> 
				</table>
		</div>
	</div>
</body>
</html>