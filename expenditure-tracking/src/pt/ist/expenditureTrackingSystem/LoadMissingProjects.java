package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.nio.channels.NoConnectionPendingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class LoadMissingProjects {

    static int i = 0;
    static int noCostCenter = 0;
    private static Map<String, String> teachers = new HashMap<String, String>();
    private static Set<Integer> projectResponsibles = new HashSet<Integer>();

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    loadTeachers();
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

	System.out.println(i);
	System.out.println(noCostCenter);
    }

    private static void parse(String line) {
	String[] split = line.split("\t");

	if (split[4].equals("\"V\"")) {
	    Project project = Project.findProjectByCode(split[0]);
	    if (project == null) {
		createProject(split);
		i++;
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

    private static final Set<String> cdCostCenters = new HashSet<String>();
    static {
	cdCostCenters.add("9999");
	cdCostCenters.add("0003");
    }

    private static final Set<String> notFoundCostCenters = new HashSet<String>();

    private static Unit findCostCenter(final String costCenterString) {
	final String costCenter = cdCostCenters.contains(costCenterString) ? "0003" : costCenterString;
	final Integer cc = Integer.valueOf(costCenter);
	final String ccString = cc.toString();
	// final Unit unit = Unit.findUnitByCostCenter(cc.toString());
	Unit unit = null;
	for (final Unit ounit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
	    if (ounit instanceof CostCenter) {
		final CostCenter ccUnit = (CostCenter) ounit;
		if (Integer.parseInt(ccString) == Integer.parseInt(ccUnit.getCostCenter())) {
		    unit = ounit;
		}
	    }
	}
	if (unit == null) {
	    notFoundCostCenters.add(costCenterString);
	}
	return unit;
    }
    
    private static void createProject(String[] arguments) {
	String projectCodeString = arguments[0];
	String costCenterString = arguments[1].replace("\"", "");
	String responsibleString = arguments[2];
	String acronym = arguments[3].replace("\"", "");
	String accountingUnitString = arguments[5].replace("\"", "");

	final Unit costCenter = findCostCenter(costCenterString);
	if (costCenter != null) {
	    final Person responsible = findPerson(responsibleString);

	    final CreateUnitBean createUnitBean = new CreateUnitBean(costCenter);
	    createUnitBean.setProjectCode(projectCodeString);
	    createUnitBean.setName(acronym);
	    final Unit unit = Unit.createNewUnit(createUnitBean);

	    final AccountingUnit accountingUnit = AccountingUnit.readAccountingUnitByUnitName(accountingUnitString);
	    if (accountingUnit != null) {
		unit.setAccountingUnit(accountingUnit);
	    } else {
		System.out.println("No accounting unit found for project: " + projectCodeString);
	    }

	    if (responsible != null) {
		if (projectResponsibles.contains(Integer.valueOf(responsibleString))) {
		    final Authorization authorization = new Authorization(responsible, unit);
		    authorization.setMaxAmount(new Money("12470"));
		} else {
		    System.out.println("[" + responsibleString + "] for project [" + acronym
			    + "] is not in project responsibles list");
		}
	    }
	}
	else {
	    System.out.println(costCenterString);
	    noCostCenter++;
	}
    }

}
