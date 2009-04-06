package pt.ist.expenditureTrackingSystem.domain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SyncUsers extends SyncUsers_Base {


    public SyncUsers() {
        super();
    }

    @Override
    public void executeTask() {
	try {
	    syncData();
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

    @Service
    public static void syncData() throws IOException, SQLException {
	final Connection connection = Transaction.getCurrentJdbcConnection();

	Statement statementQuery = null;
	ResultSet resultSetQuery = null;
	try {
	    statementQuery = connection.createStatement();
	    resultSetQuery = statementQuery.executeQuery("select fenix.USER.USER_U_ID, fenix.PARTY.PARTY_NAME from fenix.USER inner join fenix.PARTY on fenix.PARTY.ID_INTERNAL = fenix.USER.KEY_PERSON;");
	    int c = 0;
	    int u = 0;
	    while (resultSetQuery.next()) {
		c++;
		final String username = resultSetQuery.getString(1);
		final String mlname = resultSetQuery.getString(2);
		final User user = User.findByUsername(username);
		if (user != null) {
		    final Person person = user.getExpenditurePerson();
		    if (person != null) {
			final MultiLanguageString name = MultiLanguageString.importFromString(mlname);
			person.setName(name.getContent());
			u++;
		    }
		}
	    }
	    System.out.println("Processed: " + c + " users.");
	    System.out.println("Updated: " + u + " users.");
	} finally {
	    if (resultSetQuery != null) {
		resultSetQuery.close();
	    }
	    if (statementQuery != null) {
		statementQuery.close();
	    }
	}
    }

}
