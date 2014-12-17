<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Process Definition List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		function showImage( deploymentId ){
			var url = "${basePath}/ProcessDefinitionAction.do?method=showProcessImage&deploymentId=" + deploymentId + "&t=" + new Date();
			window.showModalDialog(url);
		}
	</script>
</head>
<body>
<table class="list">
    <thead>
        <tr>
            <th width="50">ID</th>
            <th width="100">Name</th>
            <th width="100">Key</th>
            <th width="50">Version</th>
            <th>Description</th>
            <th>Operation</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="pd">
        <tr>
            <td>${pd.id}</td>
            <td>${pd.name}</td>
            <td>${pd.key}</td>
            <td>${pd.version}</td>
            <td>${pd.description}</td>
            <td><a href="javascript:showImage(${pd.deploymentId})">Check Process Chart</a> 
            	<html:link action="/ExecutionAction?method=startProcessInstance">
            		Start Process Instance
            		<html:param name="processDefinitionId">${pd.id}</html:param>
            	</html:link>                
                <html:link action="/DeploymentAction?method=delete&id=${pd.deploymentId}" 
                	onclick="return confirm('The related runtime information will be also deleted. Are you sure?')">Delete</html:link>
            </td>
        </tr>   
    </c:forEach>
    </tbody>
    <tfoot>
        <tr>
            <td colspan="6"><html:link action="/DeploymentAction?method=deployUI">Process Deployment Definition </html:link></td>
        </tr>
    </tfoot>
</table>
Notice: The related runtime information will be also deleted.
</body>
</html>
