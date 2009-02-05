package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import java.util.ArrayList;
import java.util.List;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.DeleteAfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.ReceiveAcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.services.Service;

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

    private static final ThreadLocal<AfterTheFactAcquisitionProcessBean> threadLocal = new ThreadLocal<AfterTheFactAcquisitionProcessBean>();

    @Service
    public static AfterTheFactAcquisitionProcess createNewAfterTheFactAcquisitionProcess(
	    AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	threadLocal.set(afterTheFactAcquisitionProcessBean);
	final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess = new AfterTheFactAcquisitionProcess();
	afterTheFactAcquisitionProcess.edit(afterTheFactAcquisitionProcessBean);
	final Person loggedPerson = Person.getLoggedPerson();
	new GenericLog(afterTheFactAcquisitionProcess, loggedPerson, afterTheFactAcquisitionProcess.getClass().getName()
		+ ".Create", new DateTime());
	return afterTheFactAcquisitionProcess;
    }

    protected int getYearForConstruction() {
	return threadLocal.get().getYear().intValue();
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
	final List<AbstractActivity<AfterTheFactAcquisitionProcess>> activities = new ArrayList<AbstractActivity<AfterTheFactAcquisitionProcess>>();
	for (final AbstractActivity<AfterTheFactAcquisitionProcess> activity : this.activities) {
	    if (activity.isActive(this)) {
		activities.add(activity);
	    }
	}
	return activities;
    }

    public void edit(final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.edit(afterTheFactAcquisitionProcessBean);
    }

    @Override
    public void delete() {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.delete();
    }

    @Override
    public boolean isAvailableForPerson(Person person) {
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL) || person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER);
    }

    public void cancel() {
	getAcquisitionAfterTheFact().setDeletedState(Boolean.TRUE);
    }

    public void renable() {
	getAcquisitionAfterTheFact().setDeletedState(Boolean.FALSE);
    }

    @Override
    public boolean hasAnyAvailableActivitity() {
	return !getActiveActivities().isEmpty();
    }
    
    @Override
    public void allocateFundsToUnit() {
	// do nothing
    }
    
        @Override
    public void submitForApproval() {
	// nothing to do here...
    }
    @Override
    public boolean isInAllocatedToUnitState() {
	return false;
    }

    @Override
    protected void authorize() {
	// nothing to do here...
    }

}
