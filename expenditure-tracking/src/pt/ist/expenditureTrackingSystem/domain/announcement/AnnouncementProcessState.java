package pt.ist.expenditureTrackingSystem.domain.announcement;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AnnouncementProcessState extends AnnouncementProcessState_Base {

    protected AnnouncementProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AnnouncementProcessState(final AnnouncementProcess process, final AnnouncementProcessStateType processStateType) {
	this();
	final Person person = getPerson();
	checkArguments(process, processStateType, person);
	super.initFields(process, person);
	setAnnouncementProcessStateType(processStateType);
    }

    public AnnouncementProcessState(final AnnouncementProcess process, final AnnouncementProcessStateType processStateType,
	    String justification) {
	this(process, processStateType);
	setJustification(justification);
    }

    protected Person getPerson() {
	return Person.getLoggedPerson();
    }

    private void checkArguments(AnnouncementProcess announcementProcess,
	    AnnouncementProcessStateType announcementProcessStateType, Person person) {
	if (announcementProcessStateType == null) {
	    throw new DomainException("error.wrong.AnnouncementProcessState.arguments");
	}
	super.checkArguments(announcementProcess, person);
    }

    public String getLocalizedName() {
	return getAnnouncementProcessStateType().getLocalizedName();
    }

}
