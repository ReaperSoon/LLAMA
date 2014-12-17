<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Top</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
</head>
<body style="margin-top: 15px; margin-left: 15px;">
	<div style="font-size: 24px; width: 200px; float:left;"> JBPM4 Console </div>
	<c:if test="${user eq 'admin'}">
		<div style="width: 300px; float:left;">Notice："admin" can check all tasks</div>
	</c:if>
	<div style="float: right">
		Current User：${user }<br/>
	 	<html:link action="/UserAction?method=logout">Logout</html:link>
	</div>
</body>
</html>
