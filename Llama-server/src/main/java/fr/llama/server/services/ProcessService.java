package fr.llama.server.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.llama.server.rpc.RemoteService;
import fr.llama.server.rpc.ServiceException;
import fr.llama.server.util.Logger;

public class ProcessService extends AbstractService {

	private static ProcessService instance = null;

	private ProcessEngine processEngine = new Configuration().buildProcessEngine();

	private RepositoryService 		repositoryService;
	private ExecutionService 		executionService;
	private TaskService				taskService;
	private HistoryService			historyService;
	private ManagementService		managementService;

	public ProcessService() {
		super(new ProcessServiceMan());
		instance = this;
		repositoryService = processEngine.getRepositoryService();
		executionService = processEngine.getExecutionService();
		taskService = processEngine.getTaskService();
		historyService = processEngine.getHistoryService();
		managementService = processEngine.getManagementService();
	}

	public static ProcessService getInstance() {
		if (instance == null)
			instance = new ProcessService();
		return instance;
	}

	/**
	 * 
	 * @param array : a JSONArray containing JSONObject. You need to give the "task_id", "user_id", "participation_type"
	 * @throws ServiceException
	 * @throws Exception
	 */
	@RemoteService
	public Object addUserToTask(JSONArray array) throws ServiceException, Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).addUserToTaskMan();
			}
		}
		JSONObject obj = array.getJSONObject(0);
		String taskId = obj.getString("task_id");
		String userId = obj.getString("user_id");
		String participationType = obj.getString("participation_type");
		if (taskService.getTask(taskId) == null) {
			throw new ServiceException("Task '" + taskId + "' not found");
		}
		taskService.addTaskParticipatingUser(taskId, userId, participationType);
		return null;
	}

	/**
	 * 
	 * @param nothing: no argument is necessary
	 * @return an array containing all deployed processes as JSONObject
	 * @throws Exception
	 */
	@RemoteService
	public Object getDeployedProcesses(JSONArray array) throws Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage"))
				return ((ProcessServiceMan)serviceMan).getDeployedProcessesMan();
		}
		JSONArray processes = new JSONArray();
		for (ProcessDefinition processDef : repositoryService.createProcessDefinitionQuery().list()) {
			JSONObject process = new JSONObject();
			process.put("id", processDef.getId());
			process.put("name", processDef.getName());
			process.put("version", processDef.getVersion());
			process.put("deployment_id", processDef.getDeploymentId());
			process.put("description", processDef.getDescription());
			process.put("image_resource_name", processDef.getImageResourceName());
			process.put("key", processDef.getKey());
			processes.put(process);
		}
		return processes;
	}

	/**
	 * 
	 * @param array : the process id you want to deploy (e.g. <id>.jpdl.xml && <id>.png)
	 * @return the deployement id in database
	 * @throws Exception 
	 */
	@RemoteService
	public JSONObject deployProcess(JSONArray array) throws Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).deployProcessMan();
			}
		}
		String processId = array.getString(0);
		String deploymentDbid = repositoryService.createDeployment()
				.addResourceFromClasspath("fr/llama/server/process/" + processId + ".jpdl.xml")
				.addResourceFromClasspath("fr/llama/server/process/" + processId + ".png")
				.deploy();
		Logger.log("Deployed on DB with id : " + deploymentDbid);
		JSONObject returnObj = new JSONObject();
		returnObj.put("deployment_db_id", deploymentDbid);
		return returnObj;
	}

	/**
	 * 
	 * @param array : the process id you want to delete
	 * @throws Exception 
	 */
	@RemoteService
	public Object deleteProcess(JSONArray array) throws Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).deleteProcessMan();
			}
		}
		repositoryService.deleteDeploymentCascade(array.getString(0));
		return null;
	}

	/**
	 * 
	 * @param array an array representing the process, containing "id": <your_process_key> and optional variables as JSONObject : "variables": { key:value, key2:value2 }
	 * @throws Exception
	 * @throws ServiceException
	 */
	@RemoteService
	public Object startProcess(JSONArray array) throws ServiceException, Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).startProcessMan();
			}
		}
		JSONObject process = array.getJSONObject(0);
		String processId = process.getString("id");
		//		if (executionService.findProcessInstanceById(processId) == null) {
		//			throw new ServiceException("Process '" + processId + "' not found");
		//		}
		if (process.has("variables")) {
			JSONObject processVariables = process.getJSONObject("variables");
			@SuppressWarnings("unchecked")
			Iterator<String> it = processVariables.keys();
			Map<String, Object> variables = new HashMap<>();
			while (it.hasNext()) {
				String key = it.next();
				variables.put(key, processVariables.get(key));
			}
			executionService.startProcessInstanceById(processId, variables);
		}else {
			executionService.startProcessInstanceById(processId);
		}
		Logger.log("Process '" + processId + "' started successfully");
		return null;
	}

	/**
	 * 
	 * @param array : the user personal ID
	 * @return an array containing a JSONObject for each task witch contains all informations about the task
	 * @throws ServiceException
	 * @throws Exception
	 */
	@RemoteService
	public Object getPersonnalTasks(JSONArray array) throws ServiceException, Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).getPersonnalTasks();
			}
		}
		TaskQuery taskQueryForUser = taskService.createTaskQuery().assignee(array.getString(0));
		if (taskQueryForUser == null) {
			throw new ServiceException("No task found for " + array.getString(0));
		}
		JSONArray tasks = new JSONArray();
		for (Task task : taskQueryForUser.list()) {
			JSONObject jsonTask = new JSONObject();
			jsonTask.put("task_id", task.getId());
			jsonTask.put("name", task.getName());
			jsonTask.put("assignee", task.getAssignee());
			jsonTask.put("date", task.getCreateTime().toString());
			jsonTask.put("execution_id", task.getExecutionId());
			tasks.put(jsonTask);
		}
		return tasks;
	}

	/**
	 * 
	 * @param array an array representing the task, containing "key": <your_task_key> and optional variables as JSONObject : "variables": { key:value, key2:value2 }
	 * @throws Exception
	 * @throws ServiceException
	 */
	@RemoteService
	public Object completeTask(JSONArray array) throws ServiceException, Exception {
		if (!array.isNull(0)) {
			if (array.get(0).equals("help") || array.get(0).equals("man") || array.get(0).equals("usage")) {
				return ((ProcessServiceMan)serviceMan).completeTask();
			}
		}
		JSONObject task = array.getJSONObject(0);
		String taskId = task.getString("id");
		if (taskService.getTask(taskId) == null) {
			throw new ServiceException("Task '" + taskId + "' not found");
		}
		if (task.has("variables")) {
			JSONObject taskVariables = task.getJSONObject("variables");
			@SuppressWarnings("unchecked")
			Iterator<String> it = taskVariables.keys();
			Map<String, Object> variables = new HashMap<>();
			while (it.hasNext()) {
				String key = it.next();
				variables.put(key, taskVariables.get(key));
			}
			taskService.setVariables(taskId, variables);
			taskService.completeTask(taskId);
		}else {
			taskService.completeTask(taskId);
		}
		Logger.log("Task '" + taskId + "' completed");
		return null;
	}
}
