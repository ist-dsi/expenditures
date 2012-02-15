package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;


public class SyncProjectsADIST extends SyncProjectsAux {

    private final static Money AUTHORIZED_VALUE = new Money("0");

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.adist";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.adist.ist.utl.pt";
    }

    @Override
    protected Money getAuthorizationValue() {
	return AUTHORIZED_VALUE;
    }

    @Override
    protected Unit findCostCenter(final String costCenterString) {
	final ExpenditureTrackingSystem ets = ExpenditureTrackingSystem.getInstance();
	if (ets.hasAnyTopLevelUnits()) {
	    return ets.getTopLevelUnitsIterator().next();
	}

	System.out.println("No top level unit configured for virtual host: " + getVirtualHost());
	return null;
    }

    @Override
    protected Boolean isDefaultRegeimIsCCP(final String type) {
	return Boolean.FALSE;
    }

}
