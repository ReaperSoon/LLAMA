package cn.itcast.jbpm4console.view.struts.formbean;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

public class TaskActionForm extends ActionForm {
	private String taskId;
	private String executionId;
	private String outcome;

	private Map<String, String> variableNames = new LinkedHashMap<String, String>();
	private Map<String, String> variableValues = new LinkedHashMap<String, String>();

	public void setName(String key, String value) {
		if (StringUtils.isNotBlank(key)) {
			variableNames.put(key, value);
		}
	}

	public void setValue(String key, String value) {
		variableValues.put(key, value);
	}

	// ====================================================

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public Map<String, String> getVariableNames() {
		return variableNames;
	}

	public Map<String, String> getVariableValues() {
		return variableValues;
	}

}
