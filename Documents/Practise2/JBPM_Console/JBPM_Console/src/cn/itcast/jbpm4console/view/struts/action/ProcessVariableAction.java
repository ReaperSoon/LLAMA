package cn.itcast.jbpm4console.view.struts.action;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import cn.itcast.jbpm4console.base.BaseAction;
import cn.itcast.jbpm4console.util.ProcessVariableUtils;
import cn.itcast.jbpm4console.view.struts.formbean.ProcessVariableActionForm;

public class ProcessVariableAction extends BaseAction {

	/** 列表 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1，获取所有的流程变量名
		String executionId = getParameter(request, "executionId");
		Set<String> variableNames = processEngine.getExecutionService().getVariableNames(executionId);

		// 2，获取所有的流程变量
		Map<String, Object> variables = processEngine.getExecutionService().getVariables(executionId, variableNames);
		request.setAttribute("list", variables);

		return mapping.findForward("list");
	}

	/** 删除 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1，获取要删除的变量的信息
		String name = getParameter(request, "name");
		String executionId = getParameter(request, "executionId");

		// 2，删除流程变量（Servic中不能直接删除）
		ExecutionImpl executionImpl = (ExecutionImpl) processEngine.getExecutionService().findExecutionById(executionId);
		executionImpl.removeVariable(name);

		return mapping.findForward("toList");
	}

	/** 添加页面 */
	public ActionForward addUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("saveUI");
	}

	/** 修改页面 */
	public ActionForward editUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1，获取变量的信息
		ProcessVariableActionForm actionForm = (ProcessVariableActionForm) form;
		String executionId = actionForm.getExecutionId();
		String name = actionForm.getName();

		// 2，处理变量值显示
		Object value = processEngine.getExecutionService().getVariable(executionId, name);
		String stringValue = ProcessVariableUtils.objectToString(value);

		// 3，准备要回显的数据：actionForm
		actionForm.setName(name);
		actionForm.setStringValue(stringValue);
		actionForm.setType(value.getClass());

		return mapping.findForward("saveUI");
	}

	/** 保存（添加/修改） */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1, actionForm
		ProcessVariableActionForm actionForm = (ProcessVariableActionForm) form;
		String executionId = actionForm.getExecutionId();
		String name = actionForm.getName();
		Object value = ProcessVariableUtils.stringToObject(actionForm.getStringValue(), actionForm.getType());

		// 2, service
		// processEngine.getExecutionService().setVariable(executionId, name, value);
		// 要启动历史变量
		processEngine.getExecutionService().createVariable(executionId, name, value, true);
		
		// 3, return
		return mapping.findForward("toList");
	}

}
