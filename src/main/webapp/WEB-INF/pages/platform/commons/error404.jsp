<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" import="java.io.*" %>
<%@ include file="/WEB-INF/pages/platform/commons/base.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-x: hidden;">
<head>
<%@ include file="/WEB-INF/pages/platform/commons/resource.jsp"%>
<title>404错误页面</title>
<script type="text/javascript">
	function closePage() {
		$portal.closeTabPage();
	}
</script>
</head>
<body>
	<div>
		<table>
			<tr>
				<td>
					<p>提示：</p>
					<p>errorCode:404。</p>
					<p>您正在浏览的页面可能已经被删除、重命名或者暂时不可用。</p>
					<p>请复制上面的错误信息，然后单击下面的“返回”按钮，抓图后，发给维护人员。
						<input type="button" id="queryBtn" value="返  回" class="$button" onClick="closePage();" />   
					</p>
					<hr>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>