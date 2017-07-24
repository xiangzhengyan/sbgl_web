<%@page import="com.zfysoft.platform.model.FunctionVo"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>企业远程设备管理平台</title>
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
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect.
    -->
    <link rel="stylesheet" href="${path}/resource/single/adminlte/dist/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    

    <script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxMessage/codebase/dhtmlxmessage.js"></script> 
    <link rel="stylesheet" type="text/css" href="${path}/resource/platform/js/dhtmlx4.0.3/dhtmlx.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_web.css"/>

  </head>
  <!--
  BODY TAG OPTIONS:
  =================
  Apply one or more of the following classes to get the
  desired effect
  |---------------------------------------------------------|
  | SKINS         | skin-blue                               |
  |               | skin-black                              |
  |               | skin-purple                             |
  |               | skin-yellow                             |
  |               | skin-red                                |
  |               | skin-green                              |
  |---------------------------------------------------------|
  |LAYOUT OPTIONS | fixed                                   |
  |               | layout-boxed                            |
  |               | layout-top-nav                          |
  |               | sidebar-collapse                        |
  |               | sidebar-mini                            |
  |---------------------------------------------------------|
  -->
  <body class="hold-transition skin-blue-light sidebar-mini fixed">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">

        <!-- Logo -->
        <a href="#" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini">Cloud</span>
          <!-- logo for regular state and mobile devices -->
          <span class="logo-lg"><b>远程设备管理</b></span>
        </a>

        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
              <!-- Messages: style can be found in dropdown.less-->
              
              <li class="dropdown messages-menu" style="display:none">
                <!-- Menu toggle button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <i class="fa fa-envelope-o"></i>
                  <span class="label label-success">4</span>
                </a>
                <ul class="dropdown-menu">
                  <li class="header">You have 4 messages</li>
                  <li>
                    <!-- inner menu: contains the messages -->
                    <ul class="menu">
                      <li><!-- start message -->
                        <a href="#">
                          <div class="pull-left">
                            <!-- User Image -->
                            <img src="${path}/resource/single/adminlte/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                          </div>
                          <!-- Message title and timestamp -->
                          <h4>
                            Support Team
                            <small><i class="fa fa-clock-o"></i> 5 mins</small>
                          </h4>
                          <!-- The message -->
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li><!-- end message -->
                    </ul><!-- /.menu -->
                  </li>
                  <li class="footer"><a href="#">See All Messages</a></li>
                </ul>
              </li><!-- /.messages-menu -->

              <!-- Notifications Menu -->
              
              <li class="dropdown notifications-menu" style="display:none">
                <!-- Menu toggle button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <i class="fa fa-bell-o"></i>
                  <span class="label label-warning">10</span>
                </a>
                <ul class="dropdown-menu">
                  <li class="header">You have 10 notifications</li>
                  <li>
                    <!-- Inner Menu: contains the notifications -->
                    <ul class="menu">
                      <li><!-- start notification -->
                        <a href="#">
                          <i class="fa fa-users text-aqua"></i> 5 new members joined today
                        </a>
                      </li><!-- end notification -->
                    </ul>
                  </li>
                  <li class="footer"><a href="#">View all</a></li>
                </ul>
              </li>
              <!-- Tasks Menu -->
              <li class="dropdown tasks-menu" style="display:none">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <i class="fa fa-flag-o"></i>
                  <span class="label label-danger">9</span>
                </a>
                <ul class="dropdown-menu">
                  <li class="header">You have 9 tasks</li>
                  <li>
                    <!-- Inner menu: contains the tasks -->
                    <ul class="menu">
                      <li><!-- Task item -->
                        <a href="#">
                          <!-- Task title and progress text -->
                          <h3>
                            Design some buttons
                            <small class="pull-right">20%</small>
                          </h3>
                          <!-- The progress bar -->
                          <div class="progress xs">
                            <!-- Change the css width attribute to simulate progress -->
                            <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span class="sr-only">20% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li><!-- end task item -->
                    </ul>
                  </li>
                  <li class="footer">
                    <a href="#">View all tasks</a>
                  </li>
                </ul>
              </li>
          
              <!-- User Account Menu -->
              <li class="dropdown user user-menu">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <!-- The user image in the navbar-->
                  <img src="${path}/resource/single/adminlte/dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                  <!-- hidden-xs hides the username on small devices so only the image appears. -->
                  <span class="hidden-xs">${loginUser.realName}</span>
                </a>
                <ul class="dropdown-menu">
                  <!-- The user image in the menu -->
                  <li class="user-header">
                    <img src="${path}/resource/single/adminlte/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                    <p>
                      ${loginUser.realName}
                     
                      <c:if test="${loginUser.email!=null}">
                        <small>${loginUser.email}</small>
                      </c:if>
                      <c:if test="${loginUser.telphone!=null}">
                        <small>${loginUser.telphone}</small>
                      </c:if>
                      
                     
                      
                    </p>
                  </li>
                  <!-- Menu Body -->
                  <!-- 显示用户角色  -->
                  <li class="user-body">
                  
                  <c:forEach items="${roles}" var="role">
                      <div class="col-xs-4 text-center">
                        <a href="#">${role.name}</a>
                      </div>
                  </c:forEach>
                  <!-- 为了好看，一排不够3个用#补充 -->
                  <c:forEach var="i" begin="1" end="${3-roles.size()%3}">
                     <div class="col-xs-4 text-center">
                      <a href="#">#</a>
                    </div>
                  </c:forEach>
             
                    
                  </li>
                  <!-- Menu Footer-->
                  <li class="user-footer">
                    <div class="pull-left">
                      <a href="javascript:openPage('user/updatemyselfpwd.do','修改密码','修改密码');" class="btn btn-default btn-flat">修改密码</a>
                    </div>
                    <div class="pull-right">
                      <a href="${path}/login/logout.do" class="btn btn-default btn-flat">退&nbsp;&nbsp;出</a>
                    </div>
                  </li>
                </ul>
              </li>
              <!-- Control Sidebar Toggle Button -->
              <!-- 右侧边栏 -->
              <!-- 
              <li>
                <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
              </li>
               -->
            </ul>
          </div>
        </nav>
      </header>
      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">

        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">

          <!-- Sidebar user panel (optional) -->
          <div class="user-panel">
            <div class="pull-left image">
              <img src="${path}/resource/single/adminlte/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
              <p>${loginUser.realName}</p>
              <!-- Status -->
              <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
          </div>

          <!-- search form (Optional) -->
          <!-- 
          <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
              </span>
            </div>
          </form>
           -->
          <!-- /.search form -->

          <!-- Sidebar Menu -->
          <ul class="sidebar-menu">
            <!-- 
            <li class="header">HEADER</li>
          
             
            <li class="active"><a href="#"><i class="fa fa-link"></i> <span>Link</span></a></li>
            <li><a href="#"><i class="fa fa-link"></i> <span>Another Link</span></a></li>
            <li class="treeview">
            
              <a href="#"><i class="fa fa-link"></i> <span>Multilevel</span> <i class="fa fa-angle-left pull-right"></i></a>
              <ul class="treeview-menu">
                <li><a href="#">Link in level 2</a></li>
                <li><a href="#">Link in level 2</a></li>
              </ul>
            </li>
             -->
             
             <%!
                public void buildMenu(StringBuffer sbf, FunctionVo fun,String labels){

                  String url = fun.getUrl();
                  String label =fun.getLabel();
                  String icon = fun.getIcoName();
                  if(url!=null && !url.equals("#")){
                    sbf.append("<li><a href=\"javascript:openPage('"+fun.getUrl()+"','"+fun.getLabel()+"','"+labels+"')\"><i class=\"fa fa-user\"></i> <span>"+label+" </span></a></li>");
                  }else{
                    sbf.append("<li class=\"treeview\">");
                    sbf.append("<a href=\"#\"><i class=\"fa fa-folder\"></i> <span>"+label+"</span> <i class=\"fa fa-angle-left pull-right\"></i></a>");
                    List<FunctionVo> children = fun.getChildren();
                    boolean hasChild = (children!=null && children.size()>0);
                    if(hasChild){
                      sbf.append("<ul class=\"treeview-menu\">");
                    }
                    for(FunctionVo c:children){
                    	buildMenu(sbf,c,labels+","+c.getLabel());
                    }
                    if(hasChild){
                    	sbf.append("</ul>");
                    }
                    sbf.append("</li>");
                  }
               
                }
             
             %>

             <%
                List<FunctionVo> list = (List<FunctionVo>)request.getAttribute("firstLevel");
                
             StringBuffer sbf = new StringBuffer(); 
               if(list!=null){
                for(FunctionVo fun:list){
                  buildMenu(sbf, fun,fun.getLabel());
                }
               }
             %>
              
             <%=sbf.toString() %>
             
            
          </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1 id="pageTitle">
                                设备管理
            <small></small>
          </h1>
          <ol id="titlePath" class="breadcrumb">
            <li><i class="fa fa-dashboard"></i>&nbsp;设备管理</li>
          
          </ol>
        </section>

        <!-- Main content -->
        <section class="content">

          <iframe id="pageIframe" style="width:100%;height:100px;border:1px solid white" ></iframe>

        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <!-- 
      <footer class="main-footer">
        <div class="pull-right hidden-xs">
          Anything you want
        </div>
        <strong>Copyright &copy; 2015 <a href="#">Company</a>.</strong> All rights reserved.
      </footer>
       -->

      <!-- Control Sidebar -->
      <aside class="control-sidebar control-sidebar-dark">
        <!-- Create the tabs -->
        <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
          <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
          <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
          <!-- Home tab content -->
          <div class="tab-pane active" id="control-sidebar-home-tab">
            <h3 class="control-sidebar-heading">Recent Activity</h3>
            <ul class="control-sidebar-menu">
              <li>
                <a href="javascript::;">
                  <i class="menu-icon fa fa-birthday-cake bg-red"></i>
                  <div class="menu-info">
                    <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>
                    <p>Will be 23 on April 24th</p>
                  </div>
                </a>
              </li>
            </ul><!-- /.control-sidebar-menu -->

            <h3 class="control-sidebar-heading">Tasks Progress</h3>
            <ul class="control-sidebar-menu">
              <li>
                <a href="javascript::;">
                  <h4 class="control-sidebar-subheading">
                    Custom Template Design
                    <span class="label label-danger pull-right">70%</span>
                  </h4>
                  <div class="progress progress-xxs">
                    <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                  </div>
                </a>
              </li>
            </ul><!-- /.control-sidebar-menu -->

          </div><!-- /.tab-pane -->
          <!-- Stats tab content -->
          <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div><!-- /.tab-pane -->
          <!-- Settings tab content -->
          <div class="tab-pane" id="control-sidebar-settings-tab">
            <form method="post">
              <h3 class="control-sidebar-heading">General Settings</h3>
              <div class="form-group">
                <label class="control-sidebar-subheading">
                  Report panel usage
                  <input type="checkbox" class="pull-right" checked>
                </label>
                <p>
                  Some information about this general settings option
                </p>
              </div><!-- /.form-group -->
            </form>
          </div><!-- /.tab-pane -->
        </div>
      </aside><!-- /.control-sidebar -->
      <!-- Add the sidebar's background. This div must be placed
           immediately after the control sidebar -->
           <!--  -->
      <div class="control-sidebar-bg"></div>
    </div><!-- ./wrapper -->

    <!-- REQUIRED JS SCRIPTS -->

    <!-- jQuery 2.1.4 -->
    <script src="${path}/resource/single/adminlte/plugins/jQuery/jQuery-2.1.4.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${path}/resource/single/adminlte/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${path}/resource/single/adminlte/dist/js/app.min.js"></script>

    <!-- Optionally, you can add Slimscroll and FastClick plugins.
         Both of these plugins are recommended to enhance the
         user experience. Slimscroll is required when using the
         fixed layout. -->
        <script src="${path}/resource/single/adminlte/plugins/slimScroll/jquery.slimscroll.min.js"></script>
        
         
    <script>
    
      function openHomePage(){
    	  openPage("sbgl/sbgl.do","设备管理","设备管理");
      }
    
      //打开功能页面
      function openPage(url,label,labels){
    	  if("${sessionScope.loginUser.modifyPassword}" == "1"){
    		  url = "user/updatemyselfpwd.do";
    		  label = "第一次登录，请修改密码";
    		  labels = "修改密码";
    	  }
    	  
          // $("#pageIframe").attr("src","${path}/"+url);
          //防止iframe回退
           $("#pageIframe")[0].contentWindow.location.replace("${path}/"+url);
          
          
          $("#pageTitle").html(label);
          
          //设置右上角标签
          var labs = labels.split(",");
          var html ="";
          //titlePath
          for ( var i = 0; i < labs.length; i++) {
        	if(i==0){
        		html+=" <li class='active'><i class='fa fa-dashboard'></i>&nbsp;&nbsp;"+labs[i]+"</li>";
        	}else{
        		html+=" <li class='active'>"+labs[i]+"</li>";
        	}
			
		  }
          $("#titlePath").html(html);
          resizeIframe();
        }
    
      //调整iframe高度
      function resizeIframe(){
    	  setTimeout(function(){
  		  	var h = $(".content-wrapper").height()-82;
  	       	$("#pageIframe").height(h);
    	  },1);
      }
      $(window).resize(function(){ 
    		 resizeIframe();
  		});
      
      
      setTimeout(function(){
    	  openHomePage(); 
         },1);
         
     
       
       
    </script>
  </body>
</html>
