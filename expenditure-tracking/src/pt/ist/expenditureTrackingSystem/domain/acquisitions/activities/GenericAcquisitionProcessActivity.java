package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.codehaus.xfire.XFireRuntimeException;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.EmailSender;
import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.ActivityException;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class GenericAcquisitionProcessActivity extends AbstractActivity<RegularAcquisitionProcess> {

    protected boolean isCurrentUserProcessOwner(RegularAcquisitionProcess process) {
	Person currentOwner = process.getCurrentOwner();
	User user = getUser();
	return currentOwner == null || (user != null && user.getPerson() == currentOwner);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return isCurrentUserProcessOwner(process);
    }

    @Override
    protected void logExecution(RegularAcquisitionProcess process, String operationName, User user) {
	new OperationLog(process, user.getPerson(), operationName, new DateTime(), process.getAcquisitionProcessStateType());
    }

    @Override
    public String getLocalizedName() {
	return RenderUtils.getResourceString("ACQUISITION_RESOURCES", "label." + getClass().getName());
    }

    protected void notifyAcquisitionRequester(RegularAcquisitionProcess process) {
	Person person = process.getRequestor();
	ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", Language.getLocale());
	if (person.getOptions().getReceiveNotificationsByEmail()) {
	    try {
//		EmailSender.sendEmail(person.getUsername(), bundle.getString("process.label.notifyTopic"), bundle
//			.getString("process.label.notifyMessage"));
	    } catch (XFireRuntimeException e) {
		e.printStackTrace();
		throw new ActivityException("activities.messages.exception.webserviceProblem", getLocalizedName());
	    }
	}
    }

    protected void notifyUnitsResponsibles(RegularAcquisitionProcess process) {
	List<Person> people = new ArrayList<Person>();
	for (Unit unit : process.getPayingUnits()) {
	    for (Authorization authorization : unit.getAuthorizations()) {
		Person person = authorization.getPerson();
		if (person.getOptions().getReceiveNotificationsByEmail()) {
		    people.add(authorization.getPerson());
		}
	    }
	}

	ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", Language.getLocale());

	try {
	    for (Person person : people) {
//		EmailSender.sendEmail(person.getUsername(), bundle.getString("process.label.notifyTopic"), bundle
//			.getString("process.label.notifyMessage"));
	    }
	} catch (XFireRuntimeException e) {
	    e.printStackTrace();
	    throw new ActivityException("activities.messages.exception.webserviceProblem", getLocalizedName());
	}
    }

    protected boolean isUserOwnerOfProcess(final RegularAcquisitionProcess process) {
	final User user = getUser();
	return user != null && process.getRequestor().equals(user.getPerson());
    }

}
