<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Group Task List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
</head>
<body>
<table class="list">
    <thead>
        <tr>
            <th width="50">ID</th>
            <th width="100">Name</th>
            <th width="100">Candidate</th>
            <th width="100">Creation Time</th>
            <th>Operation</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="task">
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>
				<c:forEach items="${task.participations}" var="participation">
					<html:link action="/TaskAction?method=assignTask&taskId=${task.id}" title="分配此任务给候选人 ${participation.userId}">${participation.userId}</html:link> 
					<br/>
				</c:forEach>
			</td>
            <td>${task.createTime}</td>
            <td><html:link action="/TaskAction?method=takeTask&taskId=${task.id}" onclick="return confirm('拾取任务后，请到个人任务列表中查看')">拾取</html:link></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
说明：任务被当前用户拾取后，就到当前用户的个人任务列表中去了。
</body>
</html>
