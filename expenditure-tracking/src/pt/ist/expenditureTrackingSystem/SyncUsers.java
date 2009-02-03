package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class SyncUsers {

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    syncData();
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    @Service
    public static void syncData() throws IOException, SQLException {
	final Map<String, Person> people = new HashMap<String, Person>();
	for (final Person person : ExpenditureTrackingSystem.getInstance().getPeopleSet()) {
	    people.put(person.getUsername(), person);
	}
	final String contents = FileUtils.readFile("users.tsv");
	for (final String line : contents.split("\n")) {
	    final String[] parts = line.split("\t");
	    final String username = parts[0];
	    final String name = parts[1];
	    final String email = parts.length > 2 ? parts[2] : null;

	    final Person person;
	    if (people.containsKey(username)) {
		person = people.get(username);
	    } else {
		person = new Person(username);
		person.setName(name);
	    }
	    person.setEmail(email);
	}
    }

}
