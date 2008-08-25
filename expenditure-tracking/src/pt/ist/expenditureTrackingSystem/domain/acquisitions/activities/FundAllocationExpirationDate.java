package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationExpirationDateBean;

public class FundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

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
	final FundAllocationExpirationDateBean fundAllocationExpirationDateBean = (FundAllocationExpirationDateBean) objects[0];
	final LocalDate fundAllocationExpirationDate = fundAllocationExpirationDateBean.getFundAllocationExpirationDate();
	process.setFundAllocationExpirationDate(fundAllocationExpirationDate);
	new AcquisitionProcessState(process, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);

    }

}
