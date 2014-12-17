package cn.itcast.jbpm4console.view.struts.formbean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ProcessDefinitionActionForm extends ActionForm {
	private FormFile resource;

	public FormFile getResource() {
		return resource;
	}

	public void setResource(FormFile resource) {
		this.resource = resource;
	}

}
