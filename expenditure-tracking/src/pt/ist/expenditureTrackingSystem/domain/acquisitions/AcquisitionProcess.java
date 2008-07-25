package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionProcess extends AcquisitionProcess_Base {

    protected AcquisitionProcess() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setAcquisitionProcessState(AcquisitionProcessState.IN_GENESIS);
        new AcquisitionRequest(this);
    }

    @Service
    public static AcquisitionProcess createNewAcquisitionProcess() {
	final AcquisitionProcess acquisitionProcess = new AcquisitionProcess();
	return acquisitionProcess;
    }

    @Service
    public void addAcquisitionProposalDocument(final String filename, final byte[] bytes) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.addAcquisitionProposalDocument(filename, bytes);
    }

    @Service
    public AcquisitionRequestItem createAcquisitionRequestItem() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.createAcquisitionRequestItem();
    }

    @Service
    public void submitForApproval() {
	setAcquisitionProcessState(AcquisitionProcessState.SUBMITTED_FOR_APPROVAL);
    }

    @Service
    public void approve() {
	setAcquisitionProcessState(AcquisitionProcessState.APPROVED);
    }

    @Service
    public void delete() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
