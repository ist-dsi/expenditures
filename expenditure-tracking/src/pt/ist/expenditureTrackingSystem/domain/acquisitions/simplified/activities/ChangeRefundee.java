package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ChangeRefundee extends SetRefundee {

    @Override
    protected boolean isAccessible(final RegularAcquisitionProcess process) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	return acquisitionRequest.getRefundee() != null && (isRequester(process) || userHasRole(RoleType.ACQUISITION_CENTRAL));
    }

}
