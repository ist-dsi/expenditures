package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionProposalDocument extends AcquisitionProposalDocument_Base {

    public AcquisitionProposalDocument() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeAcquisitionRequest();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
