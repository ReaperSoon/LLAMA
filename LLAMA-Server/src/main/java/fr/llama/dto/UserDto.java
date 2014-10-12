package fr.llama.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDto extends AbstractDto {

	private int	id;
	private String pseudo;
	private String password;
	
	public UserDto() {
		
	}
	
	public UserDto(JSONObject obj) throws JSONException {
		setPseudo(obj.getString("pseudo"));
		setPassword(obj.getString("password"));
		if (obj.has("id"))
			setId(obj.getInt("id"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}