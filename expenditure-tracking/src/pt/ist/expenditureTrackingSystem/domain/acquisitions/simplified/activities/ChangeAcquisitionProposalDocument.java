package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ChangeAcquisitionProposalDocument extends AddAcquisitionProposalDocument {

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return isCurrentUserProcessOwner(process) && process.getAcquisitionProcessState().isInGenesis()
		&& process.getAcquisitionProposalDocument() != null;
    }

}
