package pt.ist.expenditureTrackingSystem.persistenceTier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ExternalDbQuery {

    protected abstract String getQueryString();

    protected abstract void processResultSet(final ResultSet resultSet) throws SQLException;

    public void execute(final Connection connection) throws SQLException {
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	try {
	    preparedStatement = connection.prepareStatement(getQueryString());
	    resultSet = preparedStatement.executeQuery();
	    processResultSet(resultSet);
	} finally {
	    if (resultSet != null) {
		try {
		    resultSet.close();
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	    if (preparedStatement != null) {
		try {
		    preparedStatement.close();
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

}
