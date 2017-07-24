<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>企业远程设备管理平台</title>
<link href="${path}/resource/login_res/reset.css" rel="stylesheet" type="text/css" media="screen">
	<link href="${path}/resource/login_res/login.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${path}/resource/platform/js/jquery/jquery-1.10.2.js"></script>

</head>

<body onkeydown="javascript:if(event.keyCode==13)loginSubmit();">
	<input id="idInput" type="hidden" value="company">
	<div class="container clearfix">
		<!-----------------页面左侧文字------------------>
		<div class="secL">
			<h2>企业远程设备管理平台</h2>
			<p>
				让一切近在咫尺。<br/>基于云计算SaaS平台架构，随时随地只要有<br/>网络的地方都可使用，安全稳定，值得信赖。<br/> 
			</p>
		</div>
		<!-----------------页面右侧表单------------------>
		<div class="secR">
			<!-----页面右侧透明背景----->
			<div class="box-bg"></div>
			<!--------表单内容------------>
			<form action="${path}/login/login.do" method="post" name="frmLogin" id="loginForm">
				<div class="form">
					<h1>登录平台</h1>
					<p>
         
						<span class="f9c442">${msg}</span>
					</p>
					<div class="item clearfix">
						<label for="userNameIpt"></label> 
						<input type="text" tabindex="1" id="username" name="username" notnull="true" info="用户名" placeholder="请输入用户名">
					</div>
					<div class="item itempass clearfix">
						<label for="password"></label> 
						<input type="password" tabindex="2" id="password" name="password" notnull="true" info="密码" autocomplete="off" placeholder="请输入密码">
					</div>
                    <div class="item validatepic clearfix">
                      <img id="img" src="${path}/login/image.do" width="75" height="30" style="display: inline; float: left;"> 
                      <input id="code" name="code" tabindex="3" class="ipt ipt-y f_l" type="text" style="margin-right: 5px; display: inline;" notnull="true" required="required"  info="验证码" autocomplete="off" disableautocomplete=""> 
                      <a tabindex="4" class="changepic" id="forGetPassword" href="javascript:selectCode();">换一张?</a>
                    </div>
					<div class="item">
					   <button type="submit" tabindex="5"  id="btnSubmit" onkeydown="javascript:if(event.keyCode==13){event.returnValue = false}">登&nbsp;&nbsp;录</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
 
        
    function selectCode() {
        $('#img').attr("src", "${path}/login/image.do?rand="+new Date().getTime());
	}
		    


    </script>



</body></html>