package pt.ist.expenditureTrackingSystem.domain;

import java.util.Collection;

import module.organization.domain.PartyType;
import module.organizationIst.domain.IstPartyType;
import myorg.domain.MyOrg;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ConnectUnitsToOrganization extends ConnectUnitsToOrganization_Base {
    
    public ConnectUnitsToOrganization() {
        super();
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

    @Override
    @Service
    public void executeTask() {
	for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
	    connect(unit);
	}
    }

    private void connect(final Unit unit) {
	if (!unit.hasUnit()) {
	    final module.organization.domain.Unit organizationUnit = findUnit(unit);
	    if (organizationUnit != null) {
		final PartyType partyType = getPartyType(unit);
		organizationUnit.addPartyTypes(partyType);
		unit.setUnit(organizationUnit);
	    }
	}
    }

    private PartyType getPartyType(final Unit unit) {
	final IstPartyType istPartyType = getIstPartyType(unit);
	return PartyType.readBy(istPartyType.getType());
    }

    private IstPartyType getIstPartyType(final Unit unit) {
	if (unit instanceof SubProject) {
	    return IstPartyType.SUB_PROJECT;
	} else if (unit instanceof Project) {
	    return IstPartyType.PROJECT;
	} else if (unit instanceof CostCenter) {
	    return IstPartyType.COST_CENTER;
	} else {
	    return IstPartyType.UNIT;	    
	}
    }

    private module.organization.domain.Unit findUnit(final Unit unit) {
	if (unit instanceof SubProject) {
	    return null;
	} else if (unit instanceof Project) {
	    return null;
	} else if (unit instanceof CostCenter) {
	    final module.organization.domain.Unit organizationUnit = findUnit(unit, MyOrg.getInstance().getTopUnitsSet());
	    if (organizationUnit != null) {
		return organizationUnit;
	    }
	} else {
	    final module.organization.domain.Unit organizationUnit = findUnit(unit, MyOrg.getInstance().getTopUnitsSet());
	    if (organizationUnit != null) {
		return organizationUnit;
	    }
	}
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("Unable to find org unit for: ");
	stringBuilder.append(unit.getClass().getSimpleName());
	stringBuilder.append(" ");
	stringBuilder.append(unit.getPresentationName());
	stringBuilder.append(".");
	logInfo(stringBuilder.toString());
	return null;
    }

    private module.organization.domain.Unit findUnit(final Unit unit, final Collection<module.organization.domain.Unit> units) {
	for (final module.organization.domain.Unit organizationUnit : units) {
	    if (match(organizationUnit.getPartyName(), unit.getName())) {
		logInfo("Matched: " + unit.getPresentationName() + " with: " + organizationUnit.getPresentationName());
		return organizationUnit;
	    }
	    final module.organization.domain.Unit result = findUnit(unit, organizationUnit.getChildUnits());
	    if (result != null) {
		return result;
	    }
	}
	return null;
    }

    private boolean match(final MultiLanguageString multiLanguageString, final String string) {
	return multiLanguageString.getContent().equalsIgnoreCase(string);
    }
    
}
