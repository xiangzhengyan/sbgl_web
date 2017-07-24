<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>尹通科技</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${path}/resource/single/adminlte/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${path}/resource/single/font-awesomer-4.4.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${path}/resource/single/ionicons-2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${path}/resource/single/adminlte/dist/css/AdminLTE.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="${path}/resource/single/adminlte/plugins/iCheck/square/blue.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="hold-transition login-page" style="background-image: url('${path}/resource/single/bg.jpg');">
    <div class="login-box">
      <div class="login-logo">
       <b>尹通云设备</b>
      </div><!-- /.login-logo -->
      <div class="login-box-body">
        <p class="login-box-msg" style="color: red">${msg}</p>
        <form action="${path}/login/login.do" method="post">
          <div class="form-group has-feedback">
            <input required="required" id="username" name="username" class="form-control" placeholder="用户名" value="${username}">
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input  required="required" id="password" name="password" type="password" class="form-control" placeholder="密码" value="">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          <div class="row">
            <div class="col-xs-8">
              <div class="checkbox icheck">
                <label>
                  <input id="rememberme" type="checkbox"> 记住我
                </label>
              </div>
            </div><!-- /.col -->
            <div class="col-xs-4">
              <button onclick="befSubmit()" class="btn btn-primary btn-block btn-flat">登&nbsp录</button>
            </div><!-- /.col -->
          </div>
        </form>

        <div class="social-auth-links text-center">
        
        </div><!-- /.social-auth-links -->

        <a href="#">忘记密码？</a><br>

      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->

    <!-- jQuery 2.1.4 -->
    <script src="${path}/resource/single/adminlte/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${path}/resource/single/adminlte/bootstrap/js/bootstrap.min.js"></script>
    <!-- iCheck -->
    <script src="${path}/resource/single/adminlte/plugins/iCheck/icheck.min.js"></script>
    <script>
      $(function () {
        $('input').iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue',
          increaseArea: '20%' // optional
        });
        
        $('#rememberme').on('ifChanged', function(event){
        	localStorage.rememberme=this.checked;
      	});
        
      
        if(localStorage.rememberme=="true"){
        	$('#rememberme').iCheck("check");
        	if($('#username').val()==''){
        		$('#username').val(localStorage.lastname);
        	}
        }else{
        	$('#username').val("");
        	$('#password').val("");
        }
       

      });
      
    
      function befSubmit(){
    	if(localStorage.rememberme){
    		localStorage.lastname = $('#username').val();
    	}
      }
 

 
    </script>
  </body>
</html>
