package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.Money;



public class SyncProjectsIST extends SyncProjectsAux {

    private final static Money AUTHORIZED_VALUE = new Money("75000");

    @Override
    public String getVirtualHost() {
	return "dot.ist.utl.pt";
    }

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist";
    }

    @Override
    protected boolean isResponsabilityAllowed(final String responsibleString) {
	return projectResponsibles.contains(Integer.valueOf(responsibleString));
    }

    @Override
    protected Money getAuthorizationValue() {
	return AUTHORIZED_VALUE;
    }

}
