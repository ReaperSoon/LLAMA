package cn.itcast.jbpm4console.view.struts.formbean;

import org.apache.struts.action.ActionForm;

public class ProcessVariableActionForm extends ActionForm {

	private String executionId;
	private String name;
	private String stringValue;
	private Class type;

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

}
