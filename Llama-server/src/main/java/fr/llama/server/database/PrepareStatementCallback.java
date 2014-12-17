package fr.llama.server.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementCallback {
	void prepareStatement(PreparedStatement preparedStatement) throws SQLException;
}
