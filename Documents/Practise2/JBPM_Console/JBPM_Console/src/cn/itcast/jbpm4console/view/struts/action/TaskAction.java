package cn.itcast.jbpm4console.view.struts.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;

import cn.itcast.jbpm4console.base.BaseAction;
import cn.itcast.jbpm4console.domain.HistoryTaskView;
import cn.itcast.jbpm4console.util.HibernateUtils;
import cn.itcast.jbpm4console.view.struts.formbean.TaskActionForm;

/**
 * TODO 缺少任务退回或重新分配的功能
 * @author tyg
 *
 */
public class TaskAction extends BaseAction {

	/**
	 * 个人任务列表
	 */
	public ActionForward personalTaskList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// TODO 如果是admin，则使用null，因为想实现admin可以查看所有任务的功能
		String userId = getCurrentActorId(request);
		if ("admin".equals(userId)) {
			userId = null;
		}

		List<Task> taskList = processEngine.getTaskService().findPersonalTasks(userId);
		request.setAttribute("list", taskList);
		return mapping.findForward("personalTaskList");
	}

	/**
	 * 组任务列表
	 */
	public ActionForward groupTaskList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO 如果是admin，则使用null，因为想实现admin可以查看所有任务的功能
		String userId = getCurrentActorId(request);
		if ("admin".equals(userId)) {
			userId = null;
		}

		List<Task> taskList = processEngine.getTaskService().findGroupTasks(userId);
		request.setAttribute("list", taskList);
		return mapping.findForward("groupTaskList");
	}

	/**
	 * 办理完的任务
	 */
	public ActionForward endedTaskList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 1， 查询列表
		List<HistoryTask> historyTaskList = processEngine.getHistoryService().createHistoryTaskQuery()//
				.assignee(getCurrentActorId(request))//
				.state(Task.STATE_COMPLETED)//
				.list();

		// 2，处理数据
		List<HistoryTaskView> viewList = new ArrayList<HistoryTaskView>();
		for (HistoryTask historyTask : historyTaskList) {
			HistoryTaskInstanceImpl historyTaskInstanceImpl = (HistoryTaskInstanceImpl) HibernateUtils
					.getSessionFactory()//
					.getCurrentSession()//
					.createCriteria(HistoryTaskInstanceImpl.class)//
					.add(Restrictions.eq("historyTask", historyTask))//
					.uniqueResult();
			viewList.add(new HistoryTaskView(historyTask, historyTaskInstanceImpl));
		}

		request.setAttribute("list", viewList);
		return mapping.findForward("endedTaskList");
	}

	/**
	 * 拾取任务
	 */
	public ActionForward takeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String taskId = request.getParameter("taskId");
		processEngine.getTaskService().takeTask(taskId, getCurrentActorId(request));
		return mapping.findForward("toPersonalTaskList");
	}

	/**
	 * 分配任务
	 */
	public ActionForward assignTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String taskId = request.getParameter("taskId");
		processEngine.getTaskService().assignTask(taskId, getCurrentActorId(request));
		return mapping.findForward("toPersonalTaskList");
	}

	/**
	 * 办理任务页面
	 */
	public ActionForward completeTaskUI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String taskId = request.getParameter("taskId");
		Set<String> outcomes = processEngine.getTaskService().getOutcomes(taskId);
		request.setAttribute("outcomes", outcomes);
		return mapping.findForward("completeTaskUI");
	}

	/**
	 * 完成任务
	 */
	public ActionForward completeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 1，获取信息
		TaskActionForm actionForm = (TaskActionForm) form;
		String taskId = actionForm.getTaskId();
		String outcome = actionForm.getOutcome();

		// 处理变量
		Map<String, Object> variables = new HashMap<String, Object>();
		for (String key : actionForm.getVariableNames().keySet()) {
			String name = actionForm.getVariableNames().get(key);
			if (StringUtils.isNotBlank(name)) {
				String value = actionForm.getVariableValues().get(key);
				variables.put(name, value);
			}
		}

		// 2，保存流程变量，并指定要保存变量的历史信息
		String executionId = processEngine.getTaskService().getTask(taskId).getExecutionId();
		processEngine.getExecutionService().createVariables(executionId, variables, true); // 要保存历史变量

		// 3，完成任务，并使用指定的outcome离开
		if (StringUtils.isNotBlank(outcome)) {
			processEngine.getTaskService().completeTask(taskId, outcome);
		} else {
			// 如果没有指定outcome，就使用默认的（第1个）outcome
			Set<String> outcomes = processEngine.getTaskService().getOutcomes(taskId);
			if (outcomes.size() > 1) {
				throw new IllegalArgumentException("有多个outcome，却没有指定要使用哪个outcome");
			}
			outcome = outcomes.iterator().next();
			processEngine.getTaskService().completeTask(actionForm.getTaskId(), outcome);
		}
		return mapping.findForward("toPersonalTaskList");
	}
}
