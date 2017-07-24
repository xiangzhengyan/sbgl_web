<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>配置管理</title>
</head>
<script>
	function save(){
		if(!$form.validate("cfgForm")){
			$msg.showWarning(NLS.VALIDATE_ERROR);
			return;
		}
		$form.ajaxSubmit("cfgForm",function(data){
			if(data.success){
				$msg.showInfo(NLS.SAVE_SUCCESS);
				$portal.closePage(data);
			}else{
				$msg.showWarning(data.info);
			}
		});
	}
</script>
<body style="padding: 0 5px 0 5px;">
	<div class="$borderLayout" style="width: 100%; height: 100%;" >
		<div class="top" style="height: 30px;">
			<div class="$toolBar">
				<item name="save" action="save()"></item>
		        <item name="back" action="javaScript:$portal.closePage();"></item>
			</div>
		</div>
		<div class="center">
			<div class="title" style="width:790px">			
				<c:if test="${empty  cfg.id}">新建配置项</c:if>
				<c:if test="${not empty cfg.id}">编辑配置项</c:if>
			</div>
			<form action="${path }/cfg/save.do" id="cfgForm" name="cfgForm">
			<input type="hidden" id="id" name="id" value="${cfg.id }"/>
				<table cellpadding="0" cellspacing="0" border="0" class="form_table">
					<tr>
						<th><span style="color: red;">*</span>参数名称</th>
						<td style="width: 650px;"><input style="width: 100%; height: 100%" class="$input" required="true" max-length="256" id="name" name="name" value="${cfg.name }"/></td>
					</tr> 
					<tr>
						<th><span style="color: red;">*</span>参数值</th>
						<td style="width: 650px; height: 50px;">
							<textArea style="width: 100%; height: 100%" class="$input" required="true" id="value" name="value" max-length="256">${cfg.value }</textArea>
						</td>
					</tr>
					<tr>
						<th>参数描述</th>
						<td style="width: 650px; height: 50px;">
							<textArea style="width: 100%; height: 100%" class="$input" id="description" name="description" max-length="256">${cfg.description }</textArea>
						</td>
					</tr> 
				</table>
			</form>
		</div>
	</div>
</body>
</html>