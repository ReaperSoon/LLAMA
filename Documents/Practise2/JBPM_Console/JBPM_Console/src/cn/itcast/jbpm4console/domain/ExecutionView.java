package cn.itcast.jbpm4console.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.api.Configuration;
import org.jbpm.api.Execution;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.model.Activity;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;

public class ExecutionView {

	// TODO 在 ExecutionView 中使用了 ProcessEngine
	private ProcessEngine processEngine = Configuration.getProcessEngine();

	private Execution execution;
	private Map<String, ?> variables;

	public ExecutionView(Execution execution, Map<String, ?> variables) {
		this.execution = execution;
		this.variables = variables;
	}

	/**
	 * @return
	 */
	public Set<String> getActiveActivityNames() {
		return execution.findActiveActivityNames();
	}

	/**
	 * 获取当前正在执行的活动中的所有outcomes
	 * 
	 * @return
	 */
	public List<String> getActiveActivityOutcomes() {
		// a, 查出ProcessDefinition
		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.processDefinitionId(execution.getProcessDefinitionId())//
				.uniqueResult();
		// b, 找出当前正在执行的活动
		if (execution.findActiveActivityNames().size() > 0) {
			String activeActivityName = execution.findActiveActivityNames().iterator().next(); // 当前应只有一个正在执行的活动
			Activity activity = pdImpl.findActivity(activeActivityName);
			// c, 找出这个活动的outcomes
			List<? extends Transition> transitionList = activity.getOutgoingTransitions();

			List<String> outcomes = new ArrayList<String>();
			for (Transition t : transitionList) {
				outcomes.add(t.getName());
			}
			return outcomes;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 获取子 Execution 列表
	 * 
	 * @return
	 */
	public Collection<ExecutionView> getExecutions() {
		List<ExecutionView> viewList = new ArrayList<ExecutionView>();
		Collection<? extends Execution> executionChildren = execution.getExecutions();

		for (Execution execution : executionChildren) {
			Set<String> variableNames = processEngine.getExecutionService()//
					.getVariableNames(execution.getId());
			Map<String, Object> variables = processEngine.getExecutionService()//
					.getVariables(execution.getId(), variableNames);
			viewList.add(new ExecutionView(execution, variables));
		}

		return viewList;
	}

	public String getId() {
		return execution.getId();
	}

	public String getKey() {
		return execution.getKey();
	}

	public String getName() {
		return execution.getName();
	}

	public Map<String, ?> getVariables() {
		return variables;
	}

	// ---

	public boolean getIsProcessInstance() {
		return execution.getIsProcessInstance();
	}

	public int getPriority() {
		return execution.getPriority();
	}

	public boolean isEnded() {
		return execution.isEnded();
	}

	public String getState() {
		return execution.getState();
	}

}
