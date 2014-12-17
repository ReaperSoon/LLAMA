<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Running Process Instance</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		function showImage( processInstanceId ){
			processInstanceId = encodeURI(processInstanceId);
			var url = "${basePath}/ExecutionAction.do?method=showProcessImageUI&isEnded=false&processInstanceId=" + processInstanceId + "&t=" + new Date();
			window.showModalDialog(url);
		}
		
		/**
		var needSelectOutcome = ${fn:length(outcomes) gt 1};
		function signal(){
			if(needSelectOutcome){
				url = "";
				window.showModalDialog(url, window);
			}
		}
		*/
	</script>
</head>
<body>

<html:form action="/ExecutionAction?method=listRunning">
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
          <!--   <th width="100">Key</th>  -->
            <th width="150">Current Step</th>
            <th width="150">Process Variables</th>
            <th>Operation</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="execution">
        <tr>
            <td>${execution.id}</td>
           <!--  <td>${execution.key}</td> -->
            <td>${execution.activeActivityNames}</td>
            <td>
            	<c:forEach items="${execution.variables}" var="entry" varStatus="status">
					${entry.key} = ${entry.value } <br/>
            	</c:forEach> 
            </td>
            <td><a href="javascript: showImage( '${execution.id}' )">Check Process Chart</a><br>
            	<c:forEach items="${execution.activeActivityOutcomes}" var="outcome">
	             	<html:link action="/ExecutionAction?method=signal" onclick="return confirm('Are you sure')">
	             		<html:param name="executionId" value="${execution.id}"></html:param>
	             		<html:param name="outcome" value="${outcome}"></html:param>
	             		向后执行一步：${outcome}
	             	</html:link><br>
             	</c:forEach>
             	
             	<!-- TODO 显示子 Execution，只支持显示两级 -->
             	<c:forEach items="${execution.executions}" var="child">
             		<c:forEach items="${child.activeActivityOutcomes}" var="outcome2">
		             	<html:link action="/ExecutionAction?method=signal" onclick="return confirm('Are you sure')">
		             		<html:param name="executionId" value="${child.id}"></html:param>
		             		<html:param name="outcome" value="${outcome2}"></html:param>
		             		子Execution（${child.name}）向后执行一步：${outcome2}
		             	</html:link><br>
	             	</c:forEach>
             	</c:forEach>
             	
            	<html:link action="/ExecutionAction?method=end" onclick="return confirm('Are you sure?')">
            		Finish<html:param name="processInstanceId">${execution.id}</html:param>
            	</html:link><br>
                <html:link action="/ExecutionAction?method=delete" title="只能删除正在执行的流程实例" onclick="return confirm('Are you sure?')">
                	Delete<html:param name="processInstanceId">${execution.id}</html:param>
                </html:link>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
	