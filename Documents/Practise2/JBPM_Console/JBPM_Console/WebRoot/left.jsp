<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Menu</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		 ul{
		 	margin-left: 15px;
		 }
	</style>
</head>
<body>
<ul id="menuList">
    <li><html:link action="/ProcessDefinitionAction?method=list" target="right">Flow Definition Management</html:link></li>
    <li>Task Management
    	<ul>
        	<li><html:link action="/TaskAction?method=personalTaskList" target="right">My Personal Task List</html:link></li>
            <li><html:link action="/TaskAction?method=groupTaskList" target="right">My Group Task List</html:link></li>
            <li><html:link action="/TaskAction?method=endedTaskList" target="right">My Completed Task List</html:link></li>
        </ul>
    </li>
    <li>Process Monitor
    	<ul>
    		<li><html:link action="/ExecutionAction?method=listRunning" target="right">Executing</html:link></li>
    		<li><html:link action="/ExecutionAction?method=listEnded" target="right">Completed</html:link></li>
    	</ul>
    </li>
</ul>
</body>
</html>	