package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.AccountingUnitBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreatePersonBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class LoadTestData {

    private static Set<Integer> projectResponsibles = new HashSet<Integer>();
    private static Map<String, String> teachers = new HashMap<String, String>();
    private static Map<String, AccountingUnit> accountingUnitProjectMap = new HashMap<String, AccountingUnit>();
    private static Map<String, AccountingUnit> accountingUnitCostCenterMap = new HashMap<String, AccountingUnit>();
    private static Map<String, Set<String>> personAccountingUnitMap = new HashMap<String, Set<String>>();

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	// TODO : reimplmenent as scheduled script
	// FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	// ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());
    }

    private static Set<String> createSetWithElement(final String string) {
	final Set<String> strings = new HashSet<String>();
	strings.add(string);
	return strings;
    }

    public static void main(String[] args) {
	init();
	try {
	    loadData();
	} catch (final IOException e) {
	    throw new Error(e);
	}
	System.out.println("Done.");
    }

    private static class FenixUnit {
	private final String oid;
	private final String parent;
	private final String costCenterCode;
	private final String name;
	private Unit unit;

	private FenixUnit(final String line) {
	    final int iTab1 = line.indexOf('\t');
	    final int iTab2 = line.indexOf('\t', iTab1 + 1);
	    final int iTab3 = line.indexOf('\t', iTab2 + 1);

	    oid = line.substring(0, iTab1);

	    final String parentString = line.substring(iTab1 + 1, iTab2);
	    parent = parentString.equals("null") ? null : parentString;

	    final String costCenterString = line.substring(iTab2 + 1, iTab3);
	    costCenterCode = costCenterString.equals("null") ? null : costCenterString;

	    name = line.substring(iTab3 + 1);
	}

	public Unit getParentUnit(final FenixUnitMap fenixUnitMap) {
	    final FenixUnit fenixUnit = parent == null ? null : fenixUnitMap.get(parent);
	    return fenixUnit == null ? null : fenixUnit.unit;
	}

    }

    private static class FenixUnitMap extends ArrayList<FenixUnit> {

	private FenixUnitMap(final String contents) {
	    for (final String line : contents.split("\n")) {
		add(line);
	    }
	}

	public FenixUnit get(final String oid) {
	    if (oid == null) {
		return null;
	    }
	    for (final FenixUnit fenixUnit : this) {
		if (oid.equals(fenixUnit.oid)) {
		    return fenixUnit;
		}
	    }
	    return null;
	}

	private void add(final String line) {
	    final FenixUnit fenixUnit = new FenixUnit(line);
	    add(fenixUnit);
	}

    }

    private static class CdUnit {
	private CdUnit parentUnit;
	private final String costCenterCode;
	private final String name;
	private Unit unit;

	private CdUnit(final CdUnit parent, final String line) {
	    final String[] parts = line.split("\t");
	    final String ccPart = parts[0].trim();
	    costCenterCode = ccPart.isEmpty() ? null : ccPart;
	    name = parts[1].trim();
	    parentUnit = parent;
	}
    }

    private static class CdUnitMap extends ArrayList<CdUnit> {

	private CdUnitMap(final String contents) {
	    final Stack<CdUnit> stack = new Stack<CdUnit>();
	    stack.push(add(null, "\tInstituto Superior Técnico"));

	    boolean previousIsCC = false;
	    for (final String line : contents.split("\n")) {
		final CdUnit cdUnit = add(stack.peek(), line);
		if (cdUnit.costCenterCode == null) {
		    if (previousIsCC && !stack.peek().name.equals("Taguspark")) {
			stack.pop();
			if (cdUnit.name.startsWith("COORDENA") && cdUnit.name.endsWith("O DE LICENCIATURA")) {
			    stack.pop();
			}
		    }
		    cdUnit.parentUnit = stack.peek();
		    stack.push(cdUnit);
		    previousIsCC = false;
		} else {
		    previousIsCC = true;
		}
	    }
	}

	private CdUnit add(final CdUnit parent, final String line) {
	    final CdUnit cdUnit = new CdUnit(parent, line);
	    add(cdUnit);
	    return cdUnit;
	}

    }

    private static class FenixPerson {

	private final String username;
	private final String name;
	private final Set<String> unitOids = new HashSet<String>();
	private Person person;

	public FenixPerson(final String line) {
	    final String[] parts = line.split("\t");
	    username = parts[0];
	    name = parts[1];
	    for (int i = 2; i < parts.length; i++) {
		final String unitOid = parts[i];
		unitOids.add(unitOid);
	    }
	}

    }

    private static class FenixPeopleSet extends ArrayList<FenixPerson> {

	public FenixPeopleSet(final String contents) {
	    for (final String line : contents.split("\n")) {
		final FenixPerson fenixPerson = new FenixPerson(line);
		add(fenixPerson);
	    }
	}

    }

    private static void loadData() throws IOException {
	// final String unitContents = FileUtils.readFile("units.txt");
	// final FenixUnitMap fenixUnitMap = new FenixUnitMap(unitContents);
	// createUnits(fenixUnitMap);

	final String personAccountingUnitsContent = FileUtils.readFile("accountingUsers.txt");
	loadPersonAccountingUnits(personAccountingUnitsContent);

	final String projectAccountingUnits = FileUtils.readFile("projectAccountingUnits.txt");
	final Map<String, AccountingUnit> accountingUnits = createProjectAccountingUnits(projectAccountingUnits);

	final String costCenterAccountingUnits = FileUtils.readFile("costCenterAccountingUnits.txt");
	createcostCenterAccountingUnits(costCenterAccountingUnits, accountingUnits);

	loadTeachers();
	final String activeCostCenterContents = FileUtils.readFile("activeCostCenters.csv");
	final CdUnitMap cdUnitMap = new CdUnitMap(activeCostCenterContents);
	createUnits(cdUnitMap);

	final String peopleContents = FileUtils.readFile("people.txt");
	final FenixPeopleSet fenixPeopleSet = new FenixPeopleSet(peopleContents);
	createPeople(fenixPeopleSet, accountingUnits /* , fenixUnitMap */);

	final String projectContents = FileUtils.readFile("projects.txt");
	loadProjectResponsiblesSet();
	createProjects(projectContents, fenixPeopleSet);

	LoadSuppliersData.loadData();

	final String subProjectContents = FileUtils.readFile("subProjects.txt");
	createSubProjects(subProjectContents);

	createUnidadesInvestigacaoAuthorizations();
	createDepartamentosAuthorizations();

	final String cpvReferences = FileUtils.readFile("cpv.csv");
	createCPVCodes(cpvReferences);
    }

    private static void loadPersonAccountingUnits(final String personAccountingUnitsContent) {
	for (final String line : personAccountingUnitsContent.split("\n")) {
	    final String[] parts = line.split("\t");
	    final String username = parts[0];
	    final String accountingUnit = parts[1];

	    Set<String> accountingUnits = personAccountingUnitMap.get(username);
	    if (accountingUnits == null) {
		accountingUnits = new HashSet<String>();
		personAccountingUnitMap.put(username, accountingUnits);
	    }
	    accountingUnits.add(accountingUnit);
	}
    }

    @Service
    private static Map<String, AccountingUnit> createProjectAccountingUnits(final String projectAccountingUnits) {
	final Map<String, AccountingUnit> accountingUnits = new HashMap<String, AccountingUnit>();

	for (final String line : projectAccountingUnits.split("\n")) {
	    final String parts[] = line.split("\t");
	    final String projectCode = parts[0].trim();
	    final String accountingUnitCode = parts[1].trim();

	    final AccountingUnit accountingUnit;
	    if (accountingUnits.containsKey(accountingUnitCode)) {
		accountingUnit = accountingUnits.get(accountingUnitCode);
	    } else {
		final AccountingUnitBean accountingUnitBean = new AccountingUnitBean();
		accountingUnitBean.setName(accountingUnitCode);
		accountingUnit = AccountingUnit.createNewAccountingUnit(accountingUnitBean);
		accountingUnits.put(accountingUnitCode, accountingUnit);
	    }
	    accountingUnitProjectMap.put(projectCode, accountingUnit);
	}

	return accountingUnits;
    }

    @Service
    private static void createcostCenterAccountingUnits(final String costCenterAccountingUnits,
	    final Map<String, AccountingUnit> accountingUnits) {

	for (final String line : costCenterAccountingUnits.split("\n")) {
	    final String parts[] = line.split("\t");
	    final String costCenterCode = parts[0].trim();
	    final String accountingUnitCode = parts.length == 2 ? parts[1].trim() : null;

	    if (accountingUnitCode != null) {
		final AccountingUnit accountingUnit;
		if (accountingUnits.containsKey(accountingUnitCode)) {
		    accountingUnit = accountingUnits.get(accountingUnitCode);
		} else {
		    final AccountingUnitBean accountingUnitBean = new AccountingUnitBean();
		    accountingUnitBean.setName(accountingUnitCode);
		    accountingUnit = AccountingUnit.createNewAccountingUnit(accountingUnitBean);
		    accountingUnits.put(accountingUnitCode, accountingUnit);
		}
		accountingUnitCostCenterMap.put(costCenterCode, accountingUnit);
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

    @Service
    private static void createDepartamentosAuthorizations() throws IOException {
	System.out.println("Responsaveis Departamento");
	String contents = FileUtils.readFile("responsaveisDepartamento.csv");
	for (String line : contents.split("\n")) {
	    String[] split = line.split("\t");
	    Unit unit = findUnitByName(split[0]);
	    String nMec = split[2];
	    String user = findISTUsername(nMec);
	    if (unit != null) {
		Person person = Person.findByUsername(user);
		if (person != null) {
		    Authorization authorization = new Authorization(person, unit);
		    authorization.setMaxAmount(new Money("12470"));
		} else {
		    System.out.println("Unable to find person with userName: [" + user + "]");
		}
	    } else {
		System.out.println("Unable to find unit with name: [" + split[0] + "]");
	    }
	}
	System.out.println("*******************");
    }

    @Service
    private static void createUnidadesInvestigacaoAuthorizations() throws IOException {
	System.out.println("Unidades Investigacao");
	String contents = FileUtils.readFile("responsaveisUnidadesInvestigacao.csv");
	for (String line : contents.split("\n")) {
	    String[] split = line.split("\t");
	    String nMec = split[3];
	    String user = findISTUsername(nMec);
	    Person person = Person.findByUsername(user);
	    if (person != null) {
		String costCenterNumber = split[0];
		if (costCenterNumber != null && !costCenterNumber.isEmpty()) {
		    Unit costCenter = Unit.findUnitByCostCenter(costCenterNumber);
		    if (costCenter != null) {
			Authorization authorization = new Authorization(person, costCenter);
			authorization.setMaxAmount(new Money("12470"));
		    } else {
			System.out.println("Unable to find unit with cost center: [" + costCenterNumber + "]");
		    }
		}
	    } else {
		System.out.println("Unable to find person with userName: [" + user + "]");
	    }
	}
	System.out.println("*******************");
    }

    private static void loadProjectResponsiblesSet() throws IOException {
	String contents = FileUtils.readFile("responsaveisProjectos.csv");
	for (String line : contents.split("\n")) {
	    String[] split = line.split("\t");
	    projectResponsibles.add(Integer.valueOf(split[0]));
	}
    }

    @Service
    private static void createCPVCodes(String cpvReferences) {
	for (final String line : cpvReferences.split("\n")) {
	    String[] lineArgs = line.split("\\|");
	    try {
		new CPVReference(lineArgs[0].replace("\"", ""), lineArgs[1].replace("\"", ""));
	    } catch (DomainException e) {
		// do not worry
	    }
	}
    }

    @Service
    private static void createUnits(final FenixUnitMap fenixUnitMap) {
	for (final FenixUnit fenixUnit : fenixUnitMap) {
	    createUnit(fenixUnit, fenixUnitMap);
	}
    }

    @Service
    private static void createUnits(final CdUnitMap cdUnitMap) {
	for (final CdUnit cdUnit : cdUnitMap) {
	    createUnit(cdUnit);
	}
    }

    @Service
    private static void createPeople(final FenixPeopleSet fenixPeopleSet, Map<String, AccountingUnit> accountingUnits
    /* , final FenixUnitMap fenixUnitMap */) {
	for (final FenixPerson fenixPerson : fenixPeopleSet) {
	    final CreatePersonBean createPersonBean = new CreatePersonBean();
	    createPersonBean.setUsername(fenixPerson.username);
	    createPersonBean.setName(fenixPerson.name);
	    final Person person = Person.createPerson(createPersonBean);
	    if (!fenixPerson.unitOids.isEmpty()) {
		person.getOptions().setDisplayAuthorizationPending(Boolean.TRUE);
		person.getOptions().setRecurseAuthorizationPendingUnits(Boolean.TRUE);
	    }
	    fenixPerson.person = person;
	    for (final String oid : fenixPerson.unitOids) {
		// final FenixUnit fenixUnit = fenixUnitMap.get(oid);
		// final Authorization authorization = new Authorization(person,
		// fenixUnit.unit);
		// authorization.setCanDelegate(Boolean.TRUE);
	    }
	    final Set<String> personAccountingUnits = personAccountingUnitMap.get(fenixPerson.username);
	    if (personAccountingUnits != null) {
		for (final String aus : personAccountingUnits) {
		    final AccountingUnit accountingUnit = accountingUnits.get(aus);
		    if (accountingUnit == null) {
			System.out.println("Did not find accounting unit: " + accountingUnit + "!!!");
		    } else {
			accountingUnit.addPeople(person);
		    }
		}
	    }
	}
    }

    private static void createUnit(final FenixUnit fenixUnit, final FenixUnitMap fenixUnitMap) {
	final CreateUnitBean createUnitBean = new CreateUnitBean(fenixUnit.getParentUnit(fenixUnitMap));
	createUnitBean.setCostCenter(fenixUnit.costCenterCode);
	createUnitBean.setName(fenixUnit.name);
	fenixUnit.unit = Unit.createNewUnit(createUnitBean);
    }

    private static void createUnit(final CdUnit cdUnit) {
	final Unit parentUnit = cdUnit.parentUnit == null ? null : cdUnit.parentUnit.unit;
	final CreateUnitBean createUnitBean = new CreateUnitBean(parentUnit);
	createUnitBean.setCostCenter(cdUnit.costCenterCode);
	createUnitBean.setName(cdUnit.name);
	cdUnit.unit = Unit.createNewUnit(createUnitBean);

	if (cdUnit.unit instanceof CostCenter) {
	    final CostCenter costCenter = (CostCenter) cdUnit.unit;
	    if (costCenter.getCostCenter() != null && !costCenter.getCostCenter().isEmpty()) {
		final AccountingUnit accountingUnit = accountingUnitCostCenterMap.get(costCenter.getCostCenter());
		if (accountingUnit != null) {
		    costCenter.setAccountingUnit(accountingUnit);
		} else {
		    System.out.println("No accounting unit found for cost center: " + costCenter.getCostCenter());
		}
	    }
	}
    }

    @Service
    private static void createProjects(final String contents, final FenixPeopleSet fenixPeopleSet) {
	for (final String line : contents.split("\n")) {
	    final String[] parts = line.split("\t");
	    final String projectCodeString = parts[0];
	    final String costCenterString = parts[1];
	    final String responsibleString = parts[2].trim();
	    final String acronimo = parts[3].trim();

	    final Unit costCenter = findCostCenter(costCenterString);
	    if (costCenter != null) {
		final Person responsible = findPerson(responsibleString, fenixPeopleSet);

		final CreateUnitBean createUnitBean = new CreateUnitBean(costCenter);
		createUnitBean.setProjectCode(projectCodeString);
		createUnitBean.setName(acronimo);
		final Unit unit = Unit.createNewUnit(createUnitBean);
		final AccountingUnit accountingUnit = accountingUnitProjectMap.get(projectCodeString);
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
			System.out.println("[" + responsibleString + "] for project [" + acronimo
				+ "] is not in project responsibles list");
		    }
		}
	    }
	}
	for (final String costCenterString : notFoundCostCenters) {
	    System.out.println("Unable to find unit with cost center: [" + costCenterString + "]");
	}
    }

    @Service
    private static void createSubProjects(final String contents) {
	for (final String line : contents.split("\n")) {
	    final String[] parts = line.split("\t");
	    final String projectCode = parts[0];
	    final String institution = parts[1].trim();
	    final String description = parts[2].trim();

	    final Project project = findProject(projectCode);
	    if (project == null) {
		System.out.println("No project found for: " + projectCode);
	    } else {
		new SubProject(project, institution + " - " + description);
	    }
	}
    }

    private static Project findProject(final String projectCode) {
	for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
	    if (unit instanceof Project) {
		final Project project = (Project) unit;
		if (projectCode.equals(project.getProjectCode())) {
		    return project;
		}
	    }
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

    private static String findISTUsername(String nMec) {
	String username = teachers.get(nMec);
	if (username == null) {
	    System.out.println("Can't find username for " + nMec);
	    throw new RuntimeException();
	}
	return username;
    }

    private static Person findPerson(final String responsibleString, final FenixPeopleSet fenixPeopleSet) {
	if (!responsibleString.isEmpty()) {
	    String user = findISTUsername(responsibleString);
	    for (final FenixPerson fenixPerson : fenixPeopleSet) {
		final String username = fenixPerson.username;
		if (username.startsWith("ist") && user.equals(username)) {
		    return fenixPerson.person;
		}
	    }
	}
	return null;
    }

    private static Unit findUnitByName(final Unit unit, final String name) {
	if (unit.getName().equals(name)) {
	    return unit;
	}

	for (Unit unit2 : unit.getSubUnitsSet()) {
	    Unit unit3 = findUnitByName(unit2, name);
	    if (unit3 != null) {
		return unit3;
	    }
	}

	return null;
    }

    private static Unit findUnitByName(final String name) {
	for (Unit topLevelUnit : ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet()) {
	    Unit unit = findUnitByName(topLevelUnit, name);
	    if (unit != null) {
		return unit;
	    }
	}
	return null;
    }

}
