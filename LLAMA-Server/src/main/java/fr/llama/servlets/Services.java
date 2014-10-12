package fr.llama.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.llama.rpc.LlamaException;
import fr.llama.rpc.RpcObject;
import fr.llama.rpc.ServicesLoader;
import fr.llama.services.AbstractService;

/**
 * Servlet implementation class Services
 */
@WebServlet("/services/*")
public class Services extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServicesLoader servicesLoader = ServicesLoader.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Services() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>It work !</h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String[] args = request.getRequestURI().substring(request.getContextPath().length()+1).split("/");
		if (args.length >= 3) {
			String className = args[1];
			String methodeName = args[2];
			System.out.println(getClientIpAddr(request) + " ask for service " + className + "." + methodeName);
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
				JSONArray obj = new JSONArray(jb.toString());
				Object[] serviceArgs = new Object[1];
				serviceArgs[0] = obj;
				RpcObject serviceRpc = servicesLoader.getServices().get((className + "." + methodeName).toLowerCase());
				if (serviceRpc == null)
					throw new LlamaException("Service not found : " + className + "." + methodeName);
				AbstractService service = serviceRpc.getService();
				Method method = service.getClass().getMethod(methodeName, JSONArray.class);
				JSONObject invokeResult = (JSONObject) method.invoke(service, serviceArgs);
				JSONObject ret = new JSONObject();
				if (invokeResult != null) {
					if (!invokeResult.has("error")) {
						ret.put("success", invokeResult);
						out.print(ret.toString());
					}else {
						out.print(invokeResult);
					}
				}else {
					ret.put("success", "success");
					out.print(ret.toString());
				}
				out.flush();
			} catch (Exception e) {
				//				e.printStackTrace();
				out.println("{\"error\":{\"message\":\"" + e.getMessage() + "\"}}");
			}
		}
	}
	public String getClientIpAddr(HttpServletRequest request) {  
		String ip = request.getHeader("X-Forwarded-For");  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;  
	} 
}
