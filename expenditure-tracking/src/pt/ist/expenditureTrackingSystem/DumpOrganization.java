package pt.ist.expenditureTrackingSystem;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;

public class DumpOrganization {

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	// TODO : reimplmenent as scheduled script
	//FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    public static void main(String[] args) {
	init();
	try {
	    dumpData();
	} catch (final Exception e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    private static final Set<Unit> processedUnits = new HashSet<Unit>();

    @Service
    private static void dumpData() throws FileNotFoundException {
	final PrintWriter printWriter = new PrintWriter("organization.csv");
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	dumpPeople(printWriter, expenditureTrackingSystem.getPeopleSet());
	dump(printWriter, expenditureTrackingSystem.getTopLevelUnitsSet());
	printWriter.close();
    }

    private static void dumpPeople(final PrintWriter printWriter, final Set<Person> peopleSet) {
	for (final Person person : peopleSet) {
	    dump(printWriter, person);
	}
    }

    private static void dump(final PrintWriter printWriter, final Person person) {
	printWriter.write("Person");
	printWriter.write("\t");
	printWriter.write(Long.toString(person.getOID()));
	printWriter.write("\t");
	printWriter.write(person.getUsername());
	printWriter.write("\t");
	printWriter.write(person.getName());
	printWriter.write("\n");
    }

    private static void dump(final PrintWriter printWriter, final Set<Unit> units) {
	for (final Unit unit : units) {
	    dump(printWriter, unit);
	}
    }

    private static void dump(final PrintWriter printWriter, final Unit unit) {
	if (!processedUnits.contains(unit)) {
	    processedUnits.add(unit);

	    if (unit instanceof CostCenter) {
		dumpCostCenter(printWriter, (CostCenter) unit);
	    } else if (unit instanceof Project) {
		dumpProject(printWriter, (Project) unit);
	    } else if (unit instanceof SubProject) {
		dumpSubProject(printWriter, (SubProject) unit);
	    } else {
		dumpUnit(printWriter, unit);
	    }

	    dumpResponsibilities(printWriter, unit);
	    dump(printWriter, unit.getSubUnitsSet());
	}
    }

    private static void dumpCostCenter(final PrintWriter printWriter, final CostCenter costCenter) {
	dumpUnitInfo(printWriter, costCenter, "CostCenter");
	printWriter.write("\t");
	printWriter.write(costCenter.getCostCenter());
	printWriter.write("\n");
    }

    private static void dumpProject(final PrintWriter printWriter, final Project project) {
	dumpUnitInfo(printWriter, project, "Project");
	printWriter.write("\t");
	printWriter.write(project.getProjectCode());
	printWriter.write("\n");
    }

    private static void dumpSubProject(final PrintWriter printWriter, final SubProject subProject) {
	dumpUnitInfo(printWriter, subProject, "SubProject");
	printWriter.write("\n");
    }

    private static void dumpUnit(final PrintWriter printWriter, final Unit unit) {
	dumpUnitInfo(printWriter, unit, "Unit");
	printWriter.write("\n");
    }

    private static void dumpUnitInfo(final PrintWriter printWriter, final Unit unit, final String prefix) {
	printWriter.write(prefix);
	printWriter.write("\t");
	printWriter.write(Long.toString(unit.getOID()));
	printWriter.write("\t");
	printWriter.write(unit.getName());
	printWriter.write("\t");
	printWriter.write(getAccountingUnitName(unit));
	printWriter.write("\t");
	printWriter.write(getParentId(unit));	
    }

    private static String getAccountingUnitName(final Unit unit) {
	final AccountingUnit accountingUnit = unit.getAccountingUnit();
	return accountingUnit == null ? "null" : accountingUnit.getName();
    }

    private static String getParentId(final Unit unit) {
	final Unit parent = unit.getParentUnit();
	return parent == null ? "null" : Long.toString(parent.getOID());
    }

    private static void dumpResponsibilities(final PrintWriter printWriter, final Unit unit) {
	for (final Authorization authorization : unit.getAuthorizationsSet()) {
	    dumpResponsibilities(printWriter, unit, authorization);
	}
    }

    private static void dumpResponsibilities(final PrintWriter printWriter, final Unit unit, final Authorization authorization) {
	final Person person = authorization.getPerson();
	printWriter.write("Responsibility");
	printWriter.write("\t");
	printWriter.write(Long.toString(unit.getOID()));
	printWriter.write("\t");
	printWriter.write(Long.toString(person.getOID()));
	printWriter.write("\n");	
    }

}
