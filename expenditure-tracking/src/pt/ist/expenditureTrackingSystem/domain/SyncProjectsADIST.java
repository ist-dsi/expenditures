package pt.ist.expenditureTrackingSystem.domain;


public class SyncProjectsADIST extends SyncProjectsAux {

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.adist";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.adist.ist.utl.pt";
    }


}
