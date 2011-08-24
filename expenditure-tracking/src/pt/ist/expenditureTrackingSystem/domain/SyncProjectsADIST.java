package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.Money;


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


}
