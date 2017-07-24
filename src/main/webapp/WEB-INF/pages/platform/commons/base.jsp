<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="zfy" uri="/WEB-INF/tag/code.tld"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="mode" value="${session.mode}"></c:set>
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName }:${pageContext.request.serverPort}${path }"></c:set>
<c:set var="imgPath" value="${applicationScope.systemConfig.imageUrl}"></c:set>
<!-- 皮肤获取 -->
<c:set var="themename" value="${sessionScope.themename}" />
<c:if test="${empty themename}">
<c:set var="themename" value="blue" />
</c:if>
<!-- 语言获取 -->
<c:set var="locale" value="${sessionScope.locale}"></c:set>
<c:if test="${empty locale}">
<c:set var="locale" value="" />
</c:if>
<fmt:setLocale value="zh_CN" scope="session" />
<fmt:setBundle basename="com/zfysoft/platform/local/common/i18n_common" var="i18n_common"/>
<script type="text/javascript">
var $_path = "${pageContext.request.contextPath}";
var $_projectIpPort = "${pageContext.request.serverName }:${pageContext.request.serverPort}";
var $_basePath = "${basePath}";
</script>