package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Options extends Options_Base {

    public Options(final Person person) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setPerson(person);
	setDisplayAuthorizationPending(Boolean.FALSE);
	setRecurseAuthorizationPendingUnits(Boolean.FALSE);
	setReceiveNotificationsByEmail(Boolean.FALSE);
    }

    public void delete() {
	removeExpenditureTrackingSystem();
	removePerson();
	deleteDomainObject();
    }

}
