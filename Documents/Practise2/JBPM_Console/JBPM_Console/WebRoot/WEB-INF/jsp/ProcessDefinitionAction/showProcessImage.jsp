<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Process Flow Char(${pd.id})</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<!-- <a href="javascript: history.go(-1)">返回</a><br /> -->
	<img src="${basePath}/ProcessDefinitionAction.do?method=processImage&deploymentId=${param.deploymentId}" />
</body>
</html>
