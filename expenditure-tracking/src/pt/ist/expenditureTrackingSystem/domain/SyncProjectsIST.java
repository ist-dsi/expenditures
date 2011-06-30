package pt.ist.expenditureTrackingSystem.domain;


public class SyncProjectsIST extends SyncProjectsAux {

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

}
