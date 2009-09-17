package pt.ist.expenditureTrackingSystem.domain.authorizations;

public enum AuthorizationOperation {

    CREATE, EDIT, DELETE;

    public void log(final Authorization authorization) {
	new AuthorizationLog(this, authorization);
    }

}
