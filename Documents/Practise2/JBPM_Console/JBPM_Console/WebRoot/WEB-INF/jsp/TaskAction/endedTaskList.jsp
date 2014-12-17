<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Completed Task</title>
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
            <th width="100">End Time</th>
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
            <td>${task.endTime}</td>
            <td>NULL</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
	