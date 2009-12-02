package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AddAcquisitionProposalDocument extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.equals(process.getRequestor());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process)
		&& process.getAcquisitionProcessState().isInGenesis()
		&& process.getAcquisitionProposalDocument() == null
		&& (!process.isSimplifiedAcquisitionProcess() || ((SimplifiedProcedureProcess) process)
			.getProcessClassification() != ProcessClassification.CT75000);
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	String filename = (String) objects[0];
	byte[] bytes = (byte[]) objects[1];
	String proposalID = (String) objects[2];
	process.addAcquisitionProposalDocument(filename, bytes, proposalID);
    }

}
