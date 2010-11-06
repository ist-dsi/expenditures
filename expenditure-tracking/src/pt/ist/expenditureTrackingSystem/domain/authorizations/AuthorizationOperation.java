package pt.ist.expenditureTrackingSystem.domain.authorizations;

public enum AuthorizationOperation {

    CREATE, EDIT, DELETE;

    public void log(final Authorization authorization, final String justification) {
	new AuthorizationLog(this, authorization, justification);
    }

}
