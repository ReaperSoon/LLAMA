<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Completed Process Instance List</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		function showImage( processInstanceId ){
			processInstanceId = encodeURI(processInstanceId);
			var url = "${basePath}/ExecutionAction.do?method=showProcessImageUI&isEnded=true&processInstanceId=" + processInstanceId + "&t=" + new Date();
			window.showModalDialog(url, null, "resizable:yes");
		}
	</script>
</head>
<body>

<html:form action="/ExecutionAction?method=listEnded">
    <fieldset>
	    Parent Process
	   <html:select property="processDefinitionId">
	    	<html:option value="">All</html:option>
	    	<html:optionsCollection name="processDefinitionList" label="id" value="id"/>
	    </html:select>
	    <input type="submit" value="Search" />
    </fieldset>
</html:form>

<table class="list">
    <thead>
        <tr>
            <th width="50">ID</th>
            <!-- <th width="100">Key</th> -->
            <th width="100">Start Time</th>
            <th width="100">End Time</th>
            <th width="150">Process Variables</th>
            <th>Operation</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="execution">
        <tr>
            <td>${execution.processInstanceId}</td>
            <!-- <td>${execution.key}</td> -->
            <td>${execution.startTime}</td>
            <td>${execution.endTime}</td>
            <td>
				<c:forEach items="${execution.variables}" var="entry" varStatus="status">
					${entry.key} = ${entry.value } <br/>
            	</c:forEach> 
			</td>
            <td><a href="javascript:showImage('${execution.processInstanceId}')">Check Process Chart</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
	