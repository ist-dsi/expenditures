package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.DeleteAfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.ReceiveAcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AfterTheFactAcquisitionProcess extends AfterTheFactAcquisitionProcess_Base {

    private static List<AbstractActivity<AfterTheFactAcquisitionProcess>> activities = new ArrayList<AbstractActivity<AfterTheFactAcquisitionProcess>>();

    static {
	activities.add(new EditAfterTheFactAcquisition());
	activities.add(new ReceiveAcquisitionInvoice());
	activities.add(new DeleteAfterTheFactAcquisitionProcess());
    }

    protected AfterTheFactAcquisitionProcess() {
	super();
	new AcquisitionAfterTheFact(this);
    }

    @Service
    public static AfterTheFactAcquisitionProcess createNewAfterTheFactAcquisitionProcess(
	    AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess = new AfterTheFactAcquisitionProcess();
	afterTheFactAcquisitionProcess.edit(afterTheFactAcquisitionProcessBean);
	final User user = UserView.getUser();
	new GenericLog(afterTheFactAcquisitionProcess, user.getPerson(), afterTheFactAcquisitionProcess.getClass().getName() + ".Create", new DateTime());
	return afterTheFactAcquisitionProcess;
    }

    @Override
    public <T extends GenericProcess> AbstractActivity<T> getActivityByName(final String activityName) {
	for (AbstractActivity activity : activities) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}
	return null;
    }

    public List<AbstractActivity<AfterTheFactAcquisitionProcess>> getActiveActivities() {
	return new ArrayList<AbstractActivity<AfterTheFactAcquisitionProcess>>(activities);
    }

    public void edit(final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.edit(afterTheFactAcquisitionProcessBean);
    }

    @Override
    public void delete() {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    @Override
    public boolean isAvailableForPerson(Person person) {
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL) || person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER);
    }

}
