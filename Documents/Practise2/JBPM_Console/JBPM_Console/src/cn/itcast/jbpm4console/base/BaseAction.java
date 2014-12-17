package cn.itcast.jbpm4console.base;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.actions.DispatchAction;
import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;

import cn.itcast.jbpm4console.service.UserService;
import cn.itcast.jbpm4console.service.UserServiceImpl;

public class BaseAction extends DispatchAction {

	protected UserService userService = new UserServiceImpl();
	protected ProcessEngine processEngine = Configuration.getProcessEngine();

	/**
	 * 获取当前登录用户
	 * 
	 * @param request
	 * @return
	 */

	protected String getCurrentUser(HttpServletRequest request) {
		return (String) request.getSession().getAttribute("user");
	}

	/**
	 * 获取当前登录用户的actorId（就是name）<br>
	 * 
	 * @param request
	 * @return
	 */
	protected String getCurrentActorId(HttpServletRequest request) {
		String actorId = getCurrentUser(request);
		return actorId;
	}

	/**
	 * 获取要显示的页码参数，默认为1
	 * 
	 * @param request
	 * @return
	 */
	protected int getPageNum(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("pageNum"));
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	/**
	 * 获取参数（会处理中文乱码问题）
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	protected String getParameter(HttpServletRequest request, String name) {
		try {
			String value = request.getParameter(name);
			return (value == null) ? null : new String(value.getBytes("iso8859-1"), "utf-8"); // 处理中文乱码
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取参数（会处理中文乱码问题）
	 * 
	 * @param request
	 * @param name
	 * @param defaultValue 没有value时的默认值
	 * @return
	 */
	protected String getParameter(HttpServletRequest request, String name, String defaultValue) {
		try {
			String value = request.getParameter(name);
			return new String(value.getBytes("iso8859-1"), "utf-8"); // 处理中文乱码
		} catch (UnsupportedEncodingException e) {
			return defaultValue;
		}
	}
}
