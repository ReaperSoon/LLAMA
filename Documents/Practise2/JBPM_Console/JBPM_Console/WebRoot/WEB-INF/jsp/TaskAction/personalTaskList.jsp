<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Personal Task List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
</head>
<body>

<table class="list">
    <thead>
        <tr>
            <th width="50">ID</th>
            <th width="100">Name</th>
            <th width="100">Assignee</th>
            <th width="100">Creation Time</th>
            <th>Process Variables</th>
            <th>Operation</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="task">
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>${task.assignee}</td>
            <td>${task.createTime}</td>
            <td>
            	<c:forEach items="${task.variables}" var="entry" varStatus="status">
					${entry.key} = ${entry.value } <br/>
            	</c:forEach> 
            </td>
            <td><html:link action="/TaskAction?method=completeTaskUI&taskId=${task.id}">
            		GO<html:param name="executionId">${task.executionId}</html:param>
            	</html:link>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
	