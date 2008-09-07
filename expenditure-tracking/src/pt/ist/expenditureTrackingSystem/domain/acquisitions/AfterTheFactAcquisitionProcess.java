package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.DeleteAfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.EditAfterTheFactAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.ReceiveAcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AfterTheFactAcquisitionProcess extends AfterTheFactAcquisitionProcess_Base {

    private static List<AbstractActivity> activities = new ArrayList<AbstractActivity>();

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

    public List<AbstractActivity> getActiveActivities() {
	return new ArrayList(activities);
    }

    public void edit(final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.edit(afterTheFactAcquisitionProcessBean);
    }

    public void delete() {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
	acquisitionAfterTheFact.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
