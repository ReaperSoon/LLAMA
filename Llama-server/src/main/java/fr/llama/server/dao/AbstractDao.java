package fr.llama.server.dao;

import fr.llama.server.database.DatabaseUtil;

public abstract class AbstractDao {

	protected DatabaseUtil databaseUtil = DatabaseUtil.getInstance();
	
}
