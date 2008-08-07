package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public class FundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACCOUNTABILITY);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isApproved();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	final FundAllocationBean fundAllocationBean = (FundAllocationBean) objects[0];
	final String fundAllocationId = fundAllocationBean.getFundAllocationId();
	process.setFundAllocationId(fundAllocationId);
	new AcquisitionProcessState(process, AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

}
