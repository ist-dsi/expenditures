package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;

public class FundAllocationExpirationDate extends AbstractActivity<AcquisitionProcess> {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	DateTime dateTime = (DateTime) objects[0];
	process.setFundAllocationExpirationDate(dateTime);
	new AcquisitionProcessState(process, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);

    }

}
