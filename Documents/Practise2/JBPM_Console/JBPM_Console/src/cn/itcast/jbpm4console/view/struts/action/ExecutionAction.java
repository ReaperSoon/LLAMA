package cn.itcast.jbpm4console.view.struts.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.ProcessInstanceQuery;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;

import cn.itcast.jbpm4console.base.BaseAction;
import cn.itcast.jbpm4console.domain.ExecutionView;
import cn.itcast.jbpm4console.domain.HistoryProcessInstanceView;
import cn.itcast.jbpm4console.view.struts.formbean.ExecutionActionForm;

public class ExecutionAction extends BaseAction {

	/**
	 * 正在执行的流程实例列表
	 */
	public ActionForward listRunning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExecutionActionForm actionForm = (ExecutionActionForm) form;

		// 1，流程实例列表
		ProcessInstanceQuery query = processEngine.getExecutionService().createProcessInstanceQuery();
		if (StringUtils.isNotBlank(actionForm.getProcessDefinitionId())) { // 可能有ProcessDefinitionId的过滤条件
			query.processDefinitionId(actionForm.getProcessDefinitionId());
		}

		request.setAttribute("list", wrap1(query.list())); // 放包装后的 ExecutionView 列表

		// 2，准备数据：processDefinitionList
		List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService()
				.createProcessDefinitionQuery()//
				.orderDesc(ProcessDefinitionQuery.PROPERTY_ID)//
				.list();
		request.setAttribute("processDefinitionList", processDefinitionList);

		return mapping.findForward("listRunning");
	}

	/**
	 * 已执行完的流程实例列表
	 */
	public ActionForward listEnded(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExecutionActionForm actionForm = (ExecutionActionForm) form;

		// 1，流程实例列表
		HistoryProcessInstanceQuery query = processEngine.getHistoryService()
				.createHistoryProcessInstanceQuery()//
				.ended() // 查询结束了的
		// .state()
		// TODO 按结束时间降序排列，这里有BUG，如果指定这个，则会在order by之前没有空格，就有了语法错误
		// .orderDesc(HistoryProcessInstanceQuery.PROPERTY_ID)
		;

		if (StringUtils.isNotBlank(actionForm.getProcessDefinitionId())) { // 可能有ProcessDefinitionId的过滤条件
			query.processDefinitionId(actionForm.getProcessDefinitionId());
		}
		request.setAttribute("list", wrap2(query.list())); // 放包装后的 ExecutionView 列表

		// 2，准备数据：processDefinitionList
		List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService()
				.createProcessDefinitionQuery()//
				.orderAsc(ProcessDefinitionQuery.PROPERTY_NAME)//
				.orderDesc(ProcessDefinitionQuery.PROPERTY_VERSION)//
				.list();
		request.setAttribute("processDefinitionList", processDefinitionList);

		return mapping.findForward("listEnded");
	}

	/**
	 * 启动流程实例
	 */
	public ActionForward startProcessInstance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String processDefinitionId = request.getParameter("processDefinitionId");
		processDefinitionId = new String(processDefinitionId.getBytes("iso8859-1"), "utf-8"); // 处理中文乱码

		processEngine.getExecutionService().startProcessInstanceById(processDefinitionId);
		return mapping.findForward("toListRunning");
	}

	/**
	 * 向后执行一步
	 */
	public ActionForward signal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 1，获取Execution与要使用的outcome
		String executionId = getParameter(request, "executionId");
		String outcome = getParameter(request, "outcome"); // 要使用的outcome

		// 2，执行Signal
		if (StringUtils.isBlank(outcome)) {
			// 如果没有指定outcome，就使用默认的（第1个）outcome
			// a, 查出Execution与ProcessDefinition
			Execution execution = processEngine.getExecutionService().findExecutionById(executionId);
			ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processEngine.getRepositoryService()//
					.createProcessDefinitionQuery()//
					.processDefinitionId(execution.getProcessDefinitionId())//
					.uniqueResult();
			// b, 找出当前正在执行的活动
			String activeActivityName = execution.findActiveActivityNames().iterator().next(); // 当前应只有一个正在执行的活动
			Activity activity = pdImpl.findActivity(activeActivityName);
			// c, 找出这个活动的第1个outcome
			if (activity.getOutgoingTransitions().size() > 1) {
				throw new IllegalArgumentException("有多个outcome，却没有指定要使用哪个outcome");
				// processEngine.getExecutionService().signalExecutionById(executionId);
			} else {
				outcome = activity.getOutgoingTransitions().get(0).getName();
				processEngine.getExecutionService().signalExecutionById(executionId, outcome);
			}
		} else {
			processEngine.getExecutionService().signalExecutionById(executionId, outcome);
		}

		return mapping.findForward("toListRunning");
	}

	/**
	 * 查看流程图，高亮当前正在执行的节点
	 */
	public ActionForward showProcessImageUI(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isEnded = "true".equals(request.getParameter("isEnded")); // 是否已执行完
		String processInstanceId = getParameter(request, "processInstanceId");

		// 1，获取当前正在执行的活动名称
		String deploymentId = null;
		String processDefinitionId = null;
		Set<String> activityNames = null;
		if (isEnded) {
			// 已执行完的，就查询历史
			HistoryProcessInstance hpi = processEngine.getHistoryService()//
					.createHistoryProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//
					.uniqueResult();

			processDefinitionId = hpi.getProcessDefinitionId();
			activityNames = new HashSet<String>();
			activityNames.add(hpi.getEndActivityName()); // 结束的活动名称
		} else {
			// 正在执行的，就使用ExecutionService查询
			ProcessInstance pi = processEngine.getExecutionService()//
					.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//
					.uniqueResult();

			processDefinitionId = pi.getProcessDefinitionId();
			activityNames = new HashSet<String>(pi.findActiveActivityNames()); // 当前正在执行的活动的名称
		}

		// 2，找出他们的坐标
		Set<ActivityCoordinates> coordList = new HashSet<ActivityCoordinates>();
		for (String activityName : activityNames) {
			ActivityCoordinates coord = processEngine.getRepositoryService().getActivityCoordinates(
					processDefinitionId, activityName);
			coordList.add(coord);
		}
		request.setAttribute("coordList", coordList);

		// 3，获取 deploymentId
		deploymentId = processEngine.getRepositoryService().createProcessDefinitionQuery()//
				.processDefinitionId(processDefinitionId)//
				.uniqueResult()//
				.getDeploymentId();
		request.setAttribute("deploymentId", deploymentId);

		return mapping.findForward("showProcessImageUI");
	}

	/**
	 * 直接结束流程实例
	 */
	public ActionForward end(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processInstanceId = getParameter(request, "processInstanceId");
		processEngine.getExecutionService()
				.endProcessInstance(processInstanceId, ProcessInstance.STATE_ENDED);
		return mapping.findForward("toListRunning");
	}

	/**
	 * 删除流程实例正在执行的流程实例（已执行完的流程实例不能删除）
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String processInstanceId = getParameter(request, "processInstanceId");
		processEngine.getExecutionService().deleteProcessInstance(processInstanceId);
		return mapping.findForward("toListRunning");
	}

	/**
	 * 包装 ProcessInstance 列表为 ExecutionView 列表
	 * 
	 * @param executionList
	 * @return
	 */
	private List<ExecutionView> wrap1(List<ProcessInstance> executionList) {
		List<ExecutionView> viewList = new ArrayList<ExecutionView>();
		for (Execution execution : executionList) {
			Set<String> variableNames = processEngine.getExecutionService()//
					.getVariableNames(execution.getId());
			Map<String, Object> variables = processEngine.getExecutionService()//
					.getVariables(execution.getId(), variableNames);
			viewList.add(new ExecutionView(execution, variables));
		}

		return viewList;
	}

	/**
	 * 包装 HistoryProcessInstance 列表为 ExecutionView 列表
	 * 
	 * @param executionList
	 * @return
	 */
	private List<HistoryProcessInstanceView> wrap2(List<HistoryProcessInstance> historyProcessInstanceList) {
		List<HistoryProcessInstanceView> viewList = new ArrayList<HistoryProcessInstanceView>();
		for (HistoryProcessInstance hpi : historyProcessInstanceList) {
			Set<String> variableNames = processEngine.getHistoryService()//
					.getVariableNames(hpi.getProcessInstanceId());
			Map<String, ?> variables = processEngine.getHistoryService()//
					.getVariables(hpi.getProcessInstanceId(), variableNames);
			viewList.add(new HistoryProcessInstanceView(hpi, variables));
		}
		return viewList;
	}
}
