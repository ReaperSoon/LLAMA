package fr.llama.server.services;

import org.json.JSONException;
import org.json.JSONObject;

public class AbstractServiceMan {
	
	protected static final String voidReturn = "{ success: success } or { error : { message: error_message } }";
	protected static final String voidArg = "[]";
	protected static final int INDENT = 2;
	
	public JSONObject generateManFrom(String description, String argValue, String returnValue) throws JSONException {
		JSONObject man = new JSONObject();
		man.put("Description", description);
		man.put("Argument(s)", argValue != null ? argValue : voidArg);
		man.put("Return", returnValue != null ? new JSONObject().put("success", returnValue) : voidReturn);
		return man;
	}
}
