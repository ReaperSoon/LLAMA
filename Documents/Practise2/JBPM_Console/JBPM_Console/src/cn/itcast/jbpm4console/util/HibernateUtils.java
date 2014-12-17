package cn.itcast.jbpm4console.util;

import org.hibernate.SessionFactory;
import org.jbpm.api.Configuration;

public class HibernateUtils {

	private static SessionFactory sessionFactory;
	
	static {
		sessionFactory = Configuration.getProcessEngine().get(SessionFactory.class);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils.sessionFactory = sessionFactory;
	}

}
