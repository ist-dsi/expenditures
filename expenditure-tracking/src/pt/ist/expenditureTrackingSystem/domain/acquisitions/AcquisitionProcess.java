package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionProcess extends AcquisitionProcess_Base {

    protected AcquisitionProcess() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisitionProcessState(AcquisitionProcessState.IN_GENESIS);
	new AcquisitionRequest(this);
    }

    public static boolean isCreateNewAcquisitionProcessAvailable() {
	return UserView.getUser() != null;
    }

    @Service
    public static AcquisitionProcess createNewAcquisitionProcess() {
	if (!isCreateNewAcquisitionProcessAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.createNewAcquisitionProcess");
	}
	final AcquisitionProcess acquisitionProcess = new AcquisitionProcess();
	return acquisitionProcess;
    }

    public boolean isAcquisitionProposalDocumentAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessState.IN_GENESIS)
		&& user.getUsername().equalsIgnoreCase(getRequestor());
    }

    @Service
    public void addAcquisitionProposalDocument(final String filename, final byte[] bytes) {
	if (!isAcquisitionProposalDocumentAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.addAcquisitionProposalDocument");
	}
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.addAcquisitionProposalDocument(filename, bytes);
    }

    public boolean isCreateAcquisitionRequestItemAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessState.IN_GENESIS)
		&& user.getUsername().equalsIgnoreCase(getRequestor());
    }

    @Service
    public AcquisitionRequestItem createAcquisitionRequestItem() {
	if (!isCreateAcquisitionRequestItemAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.createAcquisitionRequestItem");
	}
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.createAcquisitionRequestItem();
    }

    public boolean isSubmitForApprovalAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessState.IN_GENESIS)
		&& user.getUsername().equalsIgnoreCase(getRequestor());
    }

    @Service
    public void submitForApproval() {
	if (!isSubmitForApprovalAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.submitForApproval");
	}
	setAcquisitionProcessState(AcquisitionProcessState.SUBMITTED_FOR_APPROVAL);
    }

    public String getRequestor() {
	return getAcquisitionRequest().getAcquisitionRequestInformation().getRequester();
    }

    public boolean isApproveAvailable() {
	User user = UserView.getUser();
	if (!isPendingApproval() || user == null) {
	    return false;
	}

	String costCenter = getAcquisitionRequest().getAcquisitionRequestInformation().getCostCenter();

	for (Authorization authorization : user.getPerson().getAuthorizations()) {
	    if (authorization.getUnit().getCostCenter().equals((costCenter))) {
		return true;
	    }
	}
	return false;
    }

    @Service
    public void approve() {
	if (!isApproveAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.approve");
	}
	setAcquisitionProcessState(AcquisitionProcessState.APPROVED);
    }

    public boolean isDeleteAvailable() {
	User user = UserView.getUser();
	return user != null && getRequestor().equalsIgnoreCase(user.getUsername())
		&& isProcessInState(AcquisitionProcessState.IN_GENESIS);
    }

    @Service
    public void delete() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public boolean isFundAllocationIdAvailable() {
	User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(RoleType.ACCOUNTABILITY)
		&& isProcessInState(AcquisitionProcessState.APPROVED);
    }

    @Override
    public void setFundAllocationId(final String fundAllocationId) {
	if (!isFundAllocationIdAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationId");
	}
	super.setFundAllocationId(fundAllocationId);
	setAcquisitionProcessState(AcquisitionProcessState.FUNDS_ALLOCATED);
    }

    public boolean isFundAllocationExpirationDateAvailable() {
	User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(RoleType.ACQUISITION_CENTRAL)
		&& isProcessInState(AcquisitionProcessState.FUNDS_ALLOCATED);
    }

    @Override
    public void setFundAllocationExpirationDate(final DateTime fundAllocationExpirationDate) {
	if (!isFundAllocationExpirationDateAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationExpirationDate");
	}
	super.setFundAllocationExpirationDate(fundAllocationExpirationDate);
	setAcquisitionProcessState(AcquisitionProcessState.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    public boolean isPendingApproval() {
	return isProcessInState(AcquisitionProcessState.SUBMITTED_FOR_APPROVAL);
    }

    private boolean isProcessInState(AcquisitionProcessState state) {
	return getAcquisitionProcessState().equals(state);
    }

    public boolean isPersonAbleToExecuteActivities() {
	return isAcquisitionProposalDocumentAvailable() || isCreateAcquisitionRequestItemAvailable()
		|| isSubmitForApprovalAvailable() || isApproveAvailable() || isDeleteAvailable() || isFundAllocationIdAvailable()
		|| isFundAllocationExpirationDateAvailable();
    }
}
