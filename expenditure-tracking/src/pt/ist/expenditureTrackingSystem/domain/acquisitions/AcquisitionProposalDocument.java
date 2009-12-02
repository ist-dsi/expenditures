package pt.ist.expenditureTrackingSystem.domain.acquisitions;

public class AcquisitionProposalDocument extends AcquisitionProposalDocument_Base {

    public AcquisitionProposalDocument() {
	super();
    }

    public void delete() {
	removeProcess();
	super.delete();
    }

}
