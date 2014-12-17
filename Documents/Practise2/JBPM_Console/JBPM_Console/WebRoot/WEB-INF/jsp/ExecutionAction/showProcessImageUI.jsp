<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Process Definition List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.activeNode{
			border: 2px solid red;
		}
	</style>
</head>

<body>
	<!-- <a href="javascript: history.go(-1)">返回</a><br />  -->
	<img style="position: absolute; left: 0px; top: 0px;" src="${basePath}/ProcessDefinitionAction.do?method=processImage&deploymentId=${deploymentId}" />
	
	<c:forEach items="${coordList}" var="c">
		${c.x},${c.y},${c.width},${c.height}
		<div class="activeNode" style="position: absolute; left:${c.x}px; top:${c.y}px; width:${c.width}px; height:${c.height}px"></div>
	</c:forEach>
</body>
</html>
