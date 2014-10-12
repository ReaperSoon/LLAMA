package fr.llama.services;

public abstract class AbstractService {
	
	private static AbstractService 	instance = null;

	public AbstractService() {
		instance = this;
	}

	public static AbstractService getInstance() {
		if (instance == null)
			instance = new UserService();
		return instance;
	}
}
