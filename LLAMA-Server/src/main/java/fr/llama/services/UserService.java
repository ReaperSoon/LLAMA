package fr.llama.services;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.llama.dao.UserDao;
import fr.llama.dto.UserDto;
import fr.llama.rpc.LlamaService;

public class UserService extends AbstractService {
	
	private UserDao userDao = UserDao.getInstance();
	private SecurityService securityService = SecurityService.getInstance();
	
	@LlamaService
	public JSONObject register(JSONArray array) throws JSONException, SQLException, NoSuchAlgorithmException {
		UserDto user = new UserDto(array.getJSONObject(0));
		String salt = securityService.generateSalt();
		String password = securityService.md5(user.getPassword() + salt);
		
		UserDto newUser = userDao.create(user, password, salt);
		newUser.setPassword(password);
		return new JSONObject(newUser);
	}
}
