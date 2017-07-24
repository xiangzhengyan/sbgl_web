<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link href="${path}/resource/platform/theme/${themename}/css/main.css" rel="stylesheet" type="text/css"></link>
<link href="${path}/resource/platform/components/borderLayout/borderLayout.css" rel="stylesheet" type="text/css"></link>
<link href="${path}/resource/platform/components/multipleSelect/multipleSelect.css" rel="stylesheet" type="text/css"></link>

<script type="text/javascript" src="${path}/resource/platform/js/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${path}/resource/platform/js/jquery/jquery-extend.js"></script>
<script type="text/javascript" src="${path}/resource/platform/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="${path}/resource/platform/js/jquery.form.js"></script>

<script type="text/javascript" src="${path}/resource/platform/js/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="${path}/resource/platform/js/base.js"></script>
<script type="text/javascript" src="${path}/resource/platform/js/lang/zh_Ch/nls.js"></script>


<%
boolean debug = true;

if(!debug){
%>
<script type="text/javascript" src="${path}/resource/platform/js/main-min.js"></script>
<%}else{ %>
<script type="text/javascript" src="${path}/resource/platform/components/componentConfig.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/input/input.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/button/button.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/borderLayout/borderLayout.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/pageBar/pageBar.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/gridTable/gridTable.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/titleBar/titleBar.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/grid/grid.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/tree/tree.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/toolBar/toolBar.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/uploader/uploader.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/multipleSelect/multipleSelect.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/multiImageUploader/multiImageUploader.js"></script>
<script type="text/javascript" src="${path}/resource/platform/components/select/select.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/components/textArea/textArea.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/components/administrativeAreaSelect/administrativeAreaSelect.js"></script> 

<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxCommon/codebase/dhtmlxcommon.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxCommon/codebase/dhtmlxcontainer.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxCommon/codebase/dhtmlxcore.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/dhtmlxgrid.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/ext/dhtmlxgrid_selection.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/ext/dhtmlxgrid_srnd.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/ext/dhtmlxgrid_start.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxMenu/codebase/dhtmlxmenu.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxMenu/codebase/ext/dhtmlxmenu_ext.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxMessage/codebase/dhtmlxmessage.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxToolbar/codebase/dhtmlxtoolbar.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxTree/codebase/dhtmlxtree.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxTree/codebase/ext/dhtmlxtree_json.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxWindows/codebase/dhtmlxwindows.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxGrid/codebase/ext/dhtmlxgrid_validation.js"></script>
<script type="text/javascript" src="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxWindows/codebase/dhtmlxwindows_deprecated.js"></script>
<%} %>
<script type="text/javascript" src="${path}/resource/platform/js/common.js"></script>
<link rel="stylesheet" type="text/css" href="${path}/resource/platform/js/dhtmlx4.0.3/dhtmlx.css"/>
<link rel="stylesheet" type="text/css" href="${path}/resource/platform/js/dhtmlx4.0.3/sources/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_web.css"/>
<script type="text/javascript" src="${path}/resource/platform/js/artDialog4.1.6/artDialog.js?skin=opera"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/artDialog4.1.6/plugins/iframeTools.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/mJqueryCustomScollbar/jquery.mousewheel.js"></script> 
<script type="text/javascript" src="${path}/resource/platform/js/mJqueryCustomScollbar/jquery.mCustomScrollbar.js"></script> 
<link rel="stylesheet" type="text/css" href="${path}/resource/platform/js/mJqueryCustomScollbar/jquery.mCustomScrollbar.css"/>
