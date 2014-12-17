package cn.itcast.jbpm4console.view.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cn.itcast.jbpm4console.base.BaseAction;
import cn.itcast.jbpm4console.domain.User;
import cn.itcast.jbpm4console.view.struts.formbean.UserActionForm;

/**
 * 用户的CURD与登录注销
 * 
 * @author tyg
 */
public class UserAction extends BaseAction {

	/** 列表 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<User> userList = userService.findAll();
		request.setAttribute("userList", userList);
		return mapping.findForward("list");
	}

	/** 删除 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = Long.parseLong(request.getParameter("id"));
		userService.delete(id);
		return mapping.findForward("toList");
	}

	/** 添加页面 */
	public ActionForward addUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("saveUI");
	}

	/** 添加 */
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1, actionForm --> new User()
		UserActionForm actionForm = (UserActionForm) form;
		User user = new User();
		BeanUtils.copyProperties(user, actionForm);

		// 2, service.save(user)
		userService.save(user);

		// 3, return
		return mapping.findForward("toList");
	}

	/** 修改页面 */
	public ActionForward editUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 准备要回显的数据：actionForm
		UserActionForm actionForm = (UserActionForm) form;
		User user = userService.getById(actionForm.getId());
		BeanUtils.copyProperties(actionForm, user);

		return mapping.findForward("saveUI");
	}

	/** 修改 */
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1, actionForm --> getById()
		UserActionForm actionForm = (UserActionForm) form;
		User user = userService.getById(actionForm.getId());
		BeanUtils.copyProperties(user, actionForm);

		// 2, service.update(user)
		userService.update(user);

		// 3, return
		return mapping.findForward("toList");
	}

	// =====================================================

	/**
	 * 登录页面
	 */
	public ActionForward loginUI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("loginUI");
	}

	/**
	 * 登录
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		request.getSession().setAttribute("user", username);
		return mapping.findForward("toIndex");
	}

	/**
	 * 注销
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("user");
		return mapping.findForward("toLoginUI");
	}
}
