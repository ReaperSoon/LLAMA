package cn.itcast.jbpm4console.base;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;

import cn.itcast.jbpm4console.util.HibernateUtils;

@SuppressWarnings("unchecked")
public class BaseServiceImpl<T> implements BaseService<T> {

	protected Class<T> clazz;

	public BaseServiceImpl() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class) pt.getActualTypeArguments()[0];
	}

	public void save(T entity) {
		getSession().save(entity);
	}

	public void delete(Long id) {
		Object obj = getSession().get(clazz, id);
		getSession().delete(obj);
	}

	public void update(T entity) {
		getSession().update(entity);
	}

	public T getById(Long id) {
		return (T) getSession().get(clazz, id);
	}

	public List<T> getByIds(Long[] ids) {
		if (ids == null || ids.length == 0) {
			return Collections.EMPTY_LIST;
		}

		return getSession().createQuery(//
				"FROM " + clazz.getName() + " WHERE id in (:ids)")//
				.setParameterList("ids", ids)//
				.list();
	}

	public List<T> findAll() {
		return getSession().createQuery("FROM " + clazz.getName()).list();
	}

	/**
	 * 获取当前可用的Session
	 * 
	 * @return
	 */
	protected Session getSession() {
		return HibernateUtils.getSessionFactory().getCurrentSession();
	}

	/**
	 * 获取ProcessEngine
	 * @return
	 */
	protected ProcessEngine getProcessEngine() {
		return Configuration.getProcessEngine();
	}
}
