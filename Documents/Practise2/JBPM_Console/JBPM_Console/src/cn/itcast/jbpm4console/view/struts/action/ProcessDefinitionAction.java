package cn.itcast.jbpm4console.view.struts.action;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;

import cn.itcast.jbpm4console.base.BaseAction;

public class ProcessDefinitionAction extends BaseAction {

	/**
	 * 列表显示所有
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.orderDesc(ProcessDefinitionQuery.PROPERTY_NAME)//
				.orderDesc(ProcessDefinitionQuery.PROPERTY_VERSION)//
				.list();
		request.setAttribute("list", processDefinitionList);
		return mapping.findForward("list");
	}

	/**
	 * 查看流程图
	 */
	public ActionForward showProcessImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deploymentId = getParameter(request, "deploymentId");
		ProcessDefinition pd = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.deploymentId(deploymentId)//
				.uniqueResult();
		request.setAttribute("pd", pd);
		return mapping.findForward("showProcessImage");
	}

	/**
	 * 获取流程图
	 */
	public ActionForward processImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deploymentId = request.getParameter("deploymentId");

		String processImageName = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery()//
				.deploymentId(deploymentId)//
				.uniqueResult()//
				.getImageResourceName();

		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId,
				processImageName);
		IOUtils.copy(in, response.getOutputStream());
		in.close();

		return null;
	}

}
