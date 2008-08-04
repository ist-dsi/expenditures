package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;

public class FundAllocation extends AbstractActivity<AcquisitionProcess> {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACCOUNTABILITY);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.APPROVED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	String fundAllocationId = (String) objects[0];
	process.setFundAllocationId(fundAllocationId);
	new AcquisitionProcessState(process, AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

}
