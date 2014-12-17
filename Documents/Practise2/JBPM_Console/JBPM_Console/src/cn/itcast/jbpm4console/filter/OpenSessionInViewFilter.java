package cn.itcast.jbpm4console.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.itcast.jbpm4console.util.HibernateUtils;

public class OpenSessionInViewFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 一，设置编码
		request.setCharacterEncoding("utf-8");

		// 二，控制事务
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction(); // 开始事务
			chain.doFilter(request, response); // ActionServlet --> Action
			tx.commit(); // 提交事务
		} catch (Exception e) {
			tx.rollback(); // 回滚事务
			throw new RuntimeException(e);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
