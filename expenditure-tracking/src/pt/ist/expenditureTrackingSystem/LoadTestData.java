package pt.ist.expenditureTrackingSystem;

import java.io.IOException;
import java.util.ArrayList;

import pt.ist.expenditureTrackingSystem._development.PropertiesManager;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class LoadTestData {

    public static void init() {
	String domainModelPath = "web/WEB-INF/classes/domain_model.dml";
	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(domainModelPath));
	ExpenditureTrackingSystem.initialize(FenixWebFramework.getConfig());	
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
	private Long oid;
	private Long parent;
	private String costCenterCode;
	private String name;
	private Unit unit;

	private FenixUnit(final String line) {
	    final int iTab1 = line.indexOf('\t');
	    final int iTab2 = line.indexOf('\t', iTab1 + 1);
	    final int iTab3 = line.indexOf('\t', iTab2 + 1);

	    oid = Long.valueOf(line.substring(0, iTab1));

	    final String parentString = line.substring(iTab1 + 1, iTab2);
	    parent = parentString.equals("null") ? null : Long.valueOf(parentString);

	    final String costCenterString = line.substring(iTab2 + 1, iTab3);
	    costCenterCode = costCenterString.equals("null") ? null : costCenterString;

	    name = line.substring(iTab3 + 1);

	    System.out.println(oid);
	    System.out.println(parent);
	    System.out.println(costCenterCode);
	    System.out.println(name);
	    System.out.println();
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

	public FenixUnit get(final Long oid) {
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

    private static void loadData() throws IOException {
	final String contents = FileUtils.readFile("units.txt");
	final FenixUnitMap fenixUnitMap = new FenixUnitMap(contents);
	createUnits(fenixUnitMap);
    }

    @Service
    private static void createUnits(final FenixUnitMap fenixUnitMap) {
	for (final FenixUnit fenixUnit : fenixUnitMap) {
	    createUnit(fenixUnit, fenixUnitMap);
	}
    }

    private static void createUnit(final FenixUnit fenixUnit, final FenixUnitMap fenixUnitMap) {
	final Unit unit = new Unit(fenixUnit.getParentUnit(fenixUnitMap));
	unit.setCostCenter(fenixUnit.costCenterCode);
	unit.setName(fenixUnit.name);
	fenixUnit.unit = unit;
    }

    private static void connectUnit(final FenixUnit fenixUnit, final FenixUnitMap fenixUnitMap) {
	final Unit unit = fenixUnit.unit;
	final Unit parentUnit = fenixUnit.getParentUnit(fenixUnitMap);
	unit.setParentUnit(parentUnit);
	if (parentUnit == null) {
	    unit.setExpenditureTrackingSystemFromTopLevelUnit(null);    
	} else {
	    unit.setExpenditureTrackingSystemFromTopLevelUnit(unit.getExpenditureTrackingSystem());
	}
    }

}
