package pt.ist.expenditureTrackingSystem.persistenceTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;

public abstract class ExternalDbOperation {

    private Connection connection = null;

    protected String getDatabaseUrl() {
	StringBuilder stringBuffer = new StringBuilder();
	stringBuffer.append("jdbc:oracle:thin:");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".user"));
	stringBuffer.append("/");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".pass"));
	stringBuffer.append("@");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".alias"));
	return stringBuffer.toString();
    }

    protected abstract String getDbPropertyPrefix();

    protected abstract void doOperation() throws SQLException;

    public void execute() throws SQLException {
	try {
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	    connection = DriverManager.getConnection(getDatabaseUrl());
	    connection.setAutoCommit(false);
	    doOperation();
	} finally {
	    if (connection != null) {
		try {
		    connection.close();
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public void executeQuery(final ExternalDbQuery externalDbQuery) throws SQLException {
	externalDbQuery.execute(connection);
    }

}
