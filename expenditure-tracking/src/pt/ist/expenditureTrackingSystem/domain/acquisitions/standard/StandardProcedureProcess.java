package pt.ist.expenditureTrackingSystem.domain.acquisitions.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcessOperationLog;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ActivityScope;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class StandardProcedureProcess extends StandardProcedureProcess_Base {

    public StandardProcedureProcess() {
	super();
    }

    private static Map<ActivityScope, List<GenericAcquisitionProcessActivity>> activities = new HashMap<ActivityScope, List<GenericAcquisitionProcessActivity>>();

    @Override
    public GenericAcquisitionProcessActivity getActivityByName(String activityName) {

	for (ActivityScope scope : activities.keySet()) {
	    for (GenericAcquisitionProcessActivity activity : activities.get(scope)) {
		if (activity.getName().equals(activityName)) {
		    return activity;
		}
	    }
	}
	return null;
    }

    @Override
    public void acquisitionPayed() {
	// TODO Auto-generated method stub

    }

    @Override
    public void allocateFundsPermanently() {
	// TODO Auto-generated method stub

    }

    @Override
    public void allocateFundsToSupplier() {
	// TODO Auto-generated method stub

    }

    @Override
    public void allocateFundsToUnit() {
	// TODO Auto-generated method stub

    }

    @Override
    public void approveBy(Person person) {
	// TODO Auto-generated method stub

    }

    @Override
    public void cancel() {
	// TODO Auto-generated method stub

    }

    @Override
    public void confirmInvoiceBy(Person person) {
	// TODO Auto-generated method stub

    }

    @Override
    public void inGenesis() {
	// TODO Auto-generated method stub

    }

    @Override
    public void invoiceReceived() {
	// TODO Auto-generated method stub

    }

    @Override
    public void processAcquisition() {
	// TODO Auto-generated method stub

    }

    @Override
    public void reject() {
	// TODO Auto-generated method stub

    }

    @Override
    public void submitForApproval() {
	// TODO Auto-generated method stub

    }

    @Override
    public void submitForFundAllocation() {
	// TODO Auto-generated method stub

    }

    @Override
    public void submitedForInvoiceConfirmation() {
	// TODO Auto-generated method stub

    }

    @Override
    public void resetEffectiveFundAllocationId() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isStandardAcquisitionProcess() {
	return true;
    }

    @Override
    protected void approve() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void confirmInvoice() {
	// TODO Auto-generated method stub
	
    }
    
}
