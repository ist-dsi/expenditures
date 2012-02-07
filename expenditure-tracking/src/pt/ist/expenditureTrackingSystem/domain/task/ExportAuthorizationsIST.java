package pt.ist.expenditureTrackingSystem.domain.task;

public class ExportAuthorizationsIST extends ExportAuthorizations {

    @Override
    public String getVirtualHost() {
	return "dot.ist.utl.pt";
    }

    @Override
    protected String getDbPropertyPrefix() {
	return "db.mgp.ist";
    }
}
