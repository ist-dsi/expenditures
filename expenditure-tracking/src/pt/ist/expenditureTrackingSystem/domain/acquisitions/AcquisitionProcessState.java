package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class AcquisitionProcessState extends AcquisitionProcessState_Base {

    protected AcquisitionProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AcquisitionProcessState(final AcquisitionProcess acquisitionProcess,
	    final AcquisitionProcessStateType acquisitionProcessStateType) {
	this();
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	checkArguments(acquisitionProcess, acquisitionProcessStateType, person);
	setProcess(acquisitionProcess);
	setAcquisitionProcessStateType(acquisitionProcessStateType);
	setWho(person);
	setWhenDateTime(new DateTime());
    }

    private void checkArguments(AcquisitionProcess acquisitionProcess, AcquisitionProcessStateType acquisitionProcessStateType,
	    Person person) {
	if (acquisitionProcess == null || acquisitionProcessStateType == null || person == null) {
	    throw new DomainException("error.wrong.AcquisitionProcessState.arguments");
	}
    }

    public String getLocalizedName() {
	return getAcquisitionProcessStateType().getLocalizedName();
    }
}
