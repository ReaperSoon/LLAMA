package cn.itcast.jbpm4console.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.itcast.jbpm4console.domain.User;


public class CheckUserFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 一，检查是否已登录
		HttpServletRequest req = (HttpServletRequest) request;
		String user = (String) req.getSession().getAttribute("user"); // 当前登录用户

		// 1，如果没有登录
		if (user == null) {
			// 获取当前访问的URL
			String url = req.getRequestURI().substring(req.getContextPath().length()); // 
			url = url + "?method=" + req.getParameter("method");
			url = url.replaceAll("//+", "/");

			if (url.startsWith("/UserAction.do?method=login")) { // login, loginUI
				// 如果正在登录，就放行
				chain.doFilter(request, response);
			} else {
				// 如果不是使用登录，就转到登录页面
				request.getRequestDispatcher("/UserAction.do?method=loginUI").forward(request, response);
			}
		}
		// 2，如果已登录，就放行
		else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
