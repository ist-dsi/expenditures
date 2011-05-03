package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractDistributeRealValuesForPayingUnits;

public class DistributeRealValuesForPayingUnits extends AbstractDistributeRealValuesForPayingUnits<RegularAcquisitionProcess> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
		&& isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isInvoiceReceived();
    }

}
