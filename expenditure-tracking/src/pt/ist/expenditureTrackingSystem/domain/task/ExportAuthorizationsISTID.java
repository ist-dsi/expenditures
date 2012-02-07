package pt.ist.expenditureTrackingSystem.domain.task;

public class ExportAuthorizationsISTID extends ExportAuthorizationsISTID_Base {

    public ExportAuthorizationsISTID() {
	super();
    }

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist-id";
    }

    @Override
    protected String getVirtualHost() {
	return "dot.ist-id.ist.utl.pt";
    }

}
