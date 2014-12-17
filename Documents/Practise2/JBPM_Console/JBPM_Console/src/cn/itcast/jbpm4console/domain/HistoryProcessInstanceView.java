package cn.itcast.jbpm4console.domain;

import java.util.Date;
import java.util.Map;

import org.jbpm.api.history.HistoryProcessInstance;

public class HistoryProcessInstanceView {

	private HistoryProcessInstance historyProcessInstance;
	private Map<String, ?> variables;

	public HistoryProcessInstanceView(HistoryProcessInstance historyProcessInstance, Map<String, ?> variables) {
		this.historyProcessInstance = historyProcessInstance;
		this.variables = variables;
	}

	/**
	 * 流程变量
	 * 
	 * @return
	 */
	public Map<String, ?> getVariables() {
		return variables;
	}

	public String getKey() {
		return historyProcessInstance.getKey();
	}

	public String getState() {
		return historyProcessInstance.getState();
	}

	public Date getStartTime() {
		return historyProcessInstance.getStartTime();
	}

	public Date getEndTime() {
		return historyProcessInstance.getEndTime();
	}

	public Long getDuration() {
		return historyProcessInstance.getDuration();
	}

	public String getEndActivityName() {
		return historyProcessInstance.getEndActivityName();
	}

	public String getProcessDefinitionId() {
		return historyProcessInstance.getProcessDefinitionId();
	}

	public String getProcessInstanceId() {
		return historyProcessInstance.getProcessInstanceId();
	}

}
