package fr.llama.server.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.llama.server.rpc.ServiceException;

public class ProcessServiceMan extends AbstractServiceMan {

	public JSONObject addUserToTaskMan() throws JSONException {
		JSONObject arg = new JSONObject();
		arg.put("task_id", "<your_task_id>");
		arg.put("user_id", "<the_user_id>");
		arg.put("participation_type", "<candidate | owner | client | viewer | replaced-assignee>");
		JSONArray array = new JSONArray();
		array.put(arg);

		JSONObject man = generateManFrom("This service add a new user to a task", arg.toString(INDENT), null);

		return man;
	}

	public JSONObject getDeployedProcessesMan() throws Exception {

		JSONObject process = new JSONObject();
		process.put("id", "<process_id>");
		process.put("name", "<process_name>");
		process.put("version", "<process_version>");
		process.put("deployment_id", "<process_deployment_id>");
		process.put("description", "<process_description>");
		process.put("image_resource_name", "<process_image>");
		process.put("key", "<process_key>");

		JSONArray array = new JSONArray();
		array.put(process);
		array.put(process);

		JSONObject man = generateManFrom("This service return an array containing all deployed processes as Json Objects.", null, array.toString(INDENT));
		return man;
	}

	public JSONObject deployProcessMan() throws Exception {
		JSONObject man = generateManFrom("This service deploy an existing Process with given id. You need to have <id>.jpdl.xml and <id>.png on your process package",
				new JSONArray().put("<process_id>").toString(2), null);
		return man;
	}

	public JSONObject deleteProcessMan() throws Exception {
		JSONObject man = generateManFrom("This service delete an existing Process by given id",
				new JSONArray().put("<process_id>").toString(2), null);
		return man;
	}

	public JSONObject startProcessMan() throws ServiceException, Exception {
		JSONObject process = new JSONObject();
		process.put("id", "<process_id>");
		JSONObject vars = new JSONObject();
		vars.put("key1", "value1");
		vars.put("key2", "value2");
		process.put("[optional]variables", vars);

		JSONObject man = generateManFrom("This service start an existing Process by given id. You can add optional variables",
				process.toString(INDENT), null);
		return man;
	}

	public JSONObject getPersonnalTasks() throws ServiceException, Exception {
		JSONArray tasks = new JSONArray();
		JSONObject jsonTask = new JSONObject();
		jsonTask.put("task_id", "<task_id>");
		jsonTask.put("name", "<task_name>");
		jsonTask.put("assignee","<task_assignee>");
		jsonTask.put("date", "<task_creation_date>");
		jsonTask.put("execution_id", "<task_execution_id>");
		
		tasks.put(jsonTask);
		tasks.put(jsonTask);
		
		JSONObject man = generateManFrom("This service return an array containing all task linked to a username. All task are Json Objects",
				new JSONArray().put("<username>").toString(INDENT), tasks.toString(INDENT));
		return man;
	}

	public JSONObject completeTask() throws ServiceException, Exception {
		JSONObject task = new JSONObject();
		task.put("id", "<task_id>");
		JSONObject vars = new JSONObject();
		vars.put("key1", "value1");
		vars.put("key2", "value2");
		task.put("[optional]variables", vars);

		JSONObject man = generateManFrom("This service complete an existing task by given id. You can add optional variables",
				task.toString(INDENT), null);
		return man;
	}
}
