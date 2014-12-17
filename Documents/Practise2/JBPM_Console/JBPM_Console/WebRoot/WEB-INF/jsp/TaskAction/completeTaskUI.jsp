<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Task Management</title>
	<%@ include file="/WEB-INF/jsp/Public/commons.jspf" %>
	<link href="${basePath}/css/public.css" rel="stylesheet" type="text/css" />
	<style>
		.variableItem{
			 margin-top: 10px;
		}
	</style>
	<script type="text/javascript">
		var index = 1;
		function addItem(){
			var template = '<div class="variableItem" id="item_' + index + '">';
			template += 'Name:<input type="text" name="name(' + index + ')" />，';
			template += 'Value:<input type="text" name="value(' + index + ')" />';
			template += '<input type="button" value="Delete" onclick="deleteItem(' + index + ')" />';
			template += '</div>';
			$("#variableItems").append(template);
			index ++;
		}
		function deleteItem(index){
			$("#item_" + index).remove();
		}
		
		$(function(){
			addItem();
			addItem();
		});
	</script>
</head>

<body>
	<html:form action="/TaskAction?method=completeTask">
		<html:hidden property="taskId"/>
		<html:hidden property="executionId"/>
	
         Click to add a new Process Variable:<input type="button" value="Add Variables" onclick="addItem()"/>。（Variable Name is required and only support String type.）<br />
        <div id="variableItems">
        </div>
        <br />

		<c:if test="${fn:length(outcomes) gt 1}">
			Next Step：
			<html:select property="outcome">
				<c:forEach items="${outcomes}" var="outcome">
					<html:option value="${outcome}">${outcome}</html:option>
				</c:forEach>
			</html:select>
	        <br />
        </c:if>
        <input type="submit" value="Finish" />
    </html:form>
</body>
</html>
	