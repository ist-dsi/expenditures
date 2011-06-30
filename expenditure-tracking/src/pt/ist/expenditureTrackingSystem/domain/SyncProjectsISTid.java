package pt.ist.expenditureTrackingSystem.domain;


public class SyncProjectsISTid extends SyncProjectsAux {

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist-id";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.ist-id.ist.utl.pt";
    }


}
