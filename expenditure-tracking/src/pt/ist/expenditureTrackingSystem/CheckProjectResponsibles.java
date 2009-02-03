package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import myorg._development.PropertiesManager;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class CheckProjectResponsibles {

    private static Map<String, String> teachers = new HashMap<String, String>();
    private static Set<Integer> projectResponsibles = new HashSet<Integer>();

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	// TODO : reimplmenent as scheduled script
	//FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    loadTeachers();
	    loadProjectResponsiblesSet();
	    loadData();
	} catch (final IOException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    @Service
    private static void loadData() throws IOException {
	final String projectContents = FileUtils.readFile("projects2.txt");
	createProjects(projectContents);
    }

    private static void createProjects(String projectContents) {
	for (String line : projectContents.split("\n")) {
	    parse(line);
	}
    }

    private static void parse(String line) {
	String[] split = line.split("\t");

	if (split[4].equals("\"V\"")) {
	    Project project = Project.findProjectByCode(split[0]);
	    if (project != null) {
		checkResponsibles(project, split[2]);
	    } else {
		System.out.println("Project not found" + split[0]);
	    }
	}
    }

    @Service
    private static void checkResponsibles(Project project, String nMec) {
	Integer nMecInt = Integer.valueOf(nMec);
	Person responsible = findPerson(nMec);
	if (projectResponsibles.contains(nMecInt) && responsible != null && !project.hasAuthorizationsFor(responsible)) {
	    System.out.println(responsible.getName() + " was not responsible of" + project.getName());
	    final Authorization authorization = new Authorization(responsible, project);
	    authorization.setMaxAmount(new Money("12470"));
	} else {
	    if (responsible != null && !project.hasAuthorizationsFor(responsible)) {
		System.out.println("Could not add " + responsible.getName() + " (" + nMec + ") to" + project.getName());
	    }
	}
    }

    private static void loadTeachers() throws IOException {
	final String contents = FileUtils.readFile("teacher.csv");
	for (String line : contents.split("\n")) {
	    String[] split = line.split("\t");
	    if (split.length == 2 && split[1] != null && !split[1].isEmpty()) {
		teachers.put(split[0], split[1]);
	    }
	}
    }

    private static String findISTUsername(String nMec) {
	String username = teachers.get(nMec);
	if (username == null) {
	    System.out.println("Can't find username for " + nMec);
	}
	return username;
    }

    private static void loadProjectResponsiblesSet() throws IOException {
	String contents = FileUtils.readFile("responsaveisProjectos.csv");
	for (String line : contents.split("\n")) {
	    String[] split = line.split("\t");
	    projectResponsibles.add(Integer.valueOf(split[0]));
	}
    }

    private static Person findPerson(final String responsibleString) {
	if (!responsibleString.isEmpty()) {
	    String user = findISTUsername(responsibleString);
	    return user == null ? null : Person.findByUsername(user);
	}
	return null;
    }

}
