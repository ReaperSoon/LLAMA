package fr.llama.server.services;

public abstract class AbstractService {
	
	protected AbstractServiceMan serviceMan;
	
	protected AbstractService(AbstractServiceMan serviceMan) {
		this.serviceMan = serviceMan;
	}

}
