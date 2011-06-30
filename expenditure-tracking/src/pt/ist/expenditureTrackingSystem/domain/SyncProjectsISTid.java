package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;


public class SyncProjectsISTid extends SyncProjectsAux {

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


}
