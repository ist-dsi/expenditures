package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ChangeAcquisitionProposalDocument extends AddAcquisitionProposalDocument {

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	return isCurrentUserProcessOwner(process) && process.getAcquisitionProcessState().isInGenesis()
		&& acquisitionRequest.getAcquisitionProposalDocument() != null;
    }

}
