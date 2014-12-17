<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Process Definition List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		if(parent != window){
			parent.location.href = window.location.href;
		}
	</script>
</head>
<body style="margin-top: 150px;text-align:center;">
	<form action="${basePath}/UserAction.do?method=login" method="post">
	    User name:<input type="text" name="username" style="width:150px" id="username"/>
	    <br />
	    Password:<input type="password" name="password" style="width:150px"/>
	    <br />
	    <input type="submit" value="Login" />
	</form>
	<script type="text/javascript">
		document.getElementById("username").focus();
	</script>
</body>
</html>
