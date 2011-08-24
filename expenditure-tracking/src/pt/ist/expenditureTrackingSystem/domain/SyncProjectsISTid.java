package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;


public class SyncProjectsISTid extends SyncProjectsAux {

    private final static Money AUTHORIZED_VALUE = new Money("5000");

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist-id";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.ist-id.ist.utl.pt";
    }

    @Override
    protected Unit findCostCenter(final String costCenterString) {
	for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
	    if (unit instanceof CostCenter) {
		final CostCenter costCenter = (CostCenter) unit;
		if (Integer.parseInt(costCenterString) == Integer.parseInt(costCenter.getCostCenter())) {
		    return unit;
		}
	    }
	}

	final ExpenditureTrackingSystem ets = ExpenditureTrackingSystem.getInstance();
	if (ets.hasAnyTopLevelUnits()) {
	    return ets.getTopLevelUnitsIterator().next();
	}

	System.out.println("No top level unit configured for virtual host: " + getVirtualHost());
	return null;
    }

    @Override
    protected Money getAuthorizationValue() {
	return AUTHORIZED_VALUE;
    }


}
