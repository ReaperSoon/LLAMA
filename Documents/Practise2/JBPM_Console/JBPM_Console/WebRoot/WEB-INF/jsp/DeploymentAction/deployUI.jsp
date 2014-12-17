<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Process Deployment</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
</head>

<body>

<html:form action="/DeploymentAction?method=deploy" enctype="multipart/form-data">
	Please upload the process deployment package(zip)：<html:file property="resource" style="width: 400px;"/><br /><br />
    <input type="submit" value="Deploy" />
</html:form>

</body>
</html>
