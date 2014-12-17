package cn.itcast.jbpm4console.view.struts.formbean;

import org.apache.struts.action.ActionForm;

public class ExecutionActionForm extends ActionForm {
	private String processDefinitionId;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

}
