package cn.itcast.jbpm4console.view.struts.action;

import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jbpm.api.Deployment;

import cn.itcast.jbpm4console.base.BaseAction;
import cn.itcast.jbpm4console.view.struts.formbean.DeploymentActionForm;

public class DeploymentAction extends BaseAction {

	/** 查询所有 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Deployment> deploymentList = processEngine.getRepositoryService()//
				.createDeploymentQuery()//
				.list();
		request.setAttribute("deploymentList", deploymentList);
		return mapping.findForward("list");
	}

	/** 删除，会级连删除相关的流程实例与历史信息。 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deploymentId = request.getParameter("id");
		processEngine.getRepositoryService().deleteDeploymentCascade(deploymentId);
		return mapping.findForward("toList");
	}

	/** 添加页面 */
	public ActionForward deployUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("deployUI");
	}

	/** 添加 */
	public ActionForward deploy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1, actionForm
		DeploymentActionForm actionForm = (DeploymentActionForm) form;
		ZipInputStream zipInputStream = new ZipInputStream(actionForm.getResource().getInputStream());

		// 2, service
		processEngine.getRepositoryService()//
				.createDeployment()//
				.addResourcesFromZipInputStream(zipInputStream)//
				.deploy();

		// 3, return
		return mapping.findForward("toList");
	}

	/** 查看所包含的文件 */
	public ActionForward showResources(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String deploymentId = request.getParameter("id");
		Set<String> resourceNames = processEngine.getRepositoryService().getResourceNames(deploymentId);
		request.setAttribute("resourceNames", resourceNames);
		return mapping.findForward("showFiles");
	}

}
