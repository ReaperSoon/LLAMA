package fr.llama.dao;

import fr.llama.database.DatabaseUtil;

public abstract class AbstractDao {

	protected DatabaseUtil databaseUtil = DatabaseUtil.getInstance();
	
}
