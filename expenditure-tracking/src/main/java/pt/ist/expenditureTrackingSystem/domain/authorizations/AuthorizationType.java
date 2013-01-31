package pt.ist.expenditureTrackingSystem.domain.authorizations;

public enum AuthorizationType {

	CAN_AUTHORIZE_AQUISITION_REQUEST(true);

	private final boolean canBeDelegated;

	private AuthorizationType(final boolean canBeDelegated) {
		this.canBeDelegated = canBeDelegated;
	}

	public boolean canBeDelegated() {
		return canBeDelegated;
	}

}
