package fr.llama.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import fr.llama.services.AbstractService;

public class ServicesLoader implements javax.servlet.ServletContextListener {
	
	private HashMap<String, RpcObject> services;
	private static ServicesLoader instance;
	
	public ServicesLoader() throws Exception {
		if (instance != null)
			throw (new Exception("ServiceLoader is already instanciated, please use ServiceLoader.getInstance()"));
	}
	
	public static ServicesLoader getInstance() {
		if (instance == null)
			try {
				instance = new ServicesLoader();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return instance;
	}
	
	public void loadServices() {
		services = new HashMap<String, RpcObject>();
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		    .setScanners(new SubTypesScanner(false), new ResourcesScanner())
		    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
		    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("fr.stevecohen.services"))));
		
		Set<Class<? extends AbstractService>> allClasses = reflections.getSubTypesOf(AbstractService.class);
		System.out.println("Loading services : ");
		Long start = System.currentTimeMillis();
		try {
			for (Class<? extends AbstractService> klass : allClasses) {
				Method[] methodes = klass.getMethods();
				AbstractService instance = klass.newInstance();
				for (Method method : methodes) {
					if (method.isAnnotationPresent(LlamaService.class)) {
						services.put((klass.getSimpleName() + "." + method.getName()).toLowerCase(), new RpcObject(instance, method));
						System.out.println(klass.getSimpleName() + "." + method.getName());
					}
				}
			}
			Long elapsed = System.currentTimeMillis() - start;
			System.out.println("Loaded " + services.size() + " service in " + elapsed + "ms");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, RpcObject> getServices() {
		return services;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		instance = this;
		this.loadServices();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
}
