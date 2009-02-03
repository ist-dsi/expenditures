package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.codehaus.xfire.XFireRuntimeException;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.ActivityException;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.smtp.EmailSender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class GenericAcquisitionProcessActivity extends AbstractActivity<RegularAcquisitionProcess> {

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return isCurrentUserProcessOwner(process);
    }

    protected void notifyAcquisitionRequester(RegularAcquisitionProcess process) {
	Person person = process.getRequestor();
	ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", Language.getLocale());
	if (person.getOptions().getReceiveNotificationsByEmail()) {
	    EmailSender.send("Central Compras", "noreply@ist.utl.pt", new String[] { "noreply@ist.utl.pt" }, Collections
		    .singletonList(person.getEmail()), Collections.EMPTY_LIST, Collections.EMPTY_LIST, bundle
		    .getString("process.label.notifyTopic"), bundle.getString("process.label.notifyMessage"));
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
	for (Person person : people) {
	    EmailSender.send("Central Compras", "noreply@ist.utl.pt", new String[] { "noreply@ist.utl.pt" }, Collections
		    .singletonList(person.getEmail()), Collections.EMPTY_LIST, Collections.EMPTY_LIST, bundle
		    .getString("process.label.notifyTopic"), bundle.getString("process.label.notifyMessage"));
	}
    }

    protected boolean isUserOwnerOfProcess(final RegularAcquisitionProcess process) {
	final User user = getUser();
	return user != null && process.getRequestor().equals(user.getPerson());
    }

}
