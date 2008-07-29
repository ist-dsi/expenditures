package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionProcess extends AcquisitionProcess_Base {

    protected AcquisitionProcess() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	new AcquisitionProcessState(this, AcquisitionProcessStateType.IN_GENESIS);
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
	return new AcquisitionProcess();
    }

    public boolean isAcquisitionProposalDocumentAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
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
	return user != null && isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
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
	return user != null && isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
		&& user.getUsername().equalsIgnoreCase(getRequestor());
    }

    @Service
    public void submitForApproval() {
	if (!isSubmitForApprovalAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.submitForApproval");
	}
	new AcquisitionProcessState(this, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public String getRequestor() {
	return getAcquisitionRequest().getRequester().getUsername();
    }

    public boolean isApproveAvailable() {
	User user = UserView.getUser();
	if (!isPendingApproval() || user == null) {
	    return false;
	}

	String costCenter = getAcquisitionRequest().getCostCenter();

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
	new AcquisitionProcessState(this, AcquisitionProcessStateType.APPROVED);
    }

    public boolean isDeleteAvailable() {
	User user = UserView.getUser();
	return user != null && getRequestor().equalsIgnoreCase(user.getUsername())
		&& isProcessInState(AcquisitionProcessStateType.IN_GENESIS);
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
		&& isProcessInState(AcquisitionProcessStateType.APPROVED);
    }

    @Override
    public void setFundAllocationId(final String fundAllocationId) {
	if (!isFundAllocationIdAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationId");
	}
	super.setFundAllocationId(fundAllocationId);
	new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    public boolean isFundAllocationExpirationDateAvailable() {
	User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(RoleType.ACQUISITION_CENTRAL)
		&& isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    public void setFundAllocationExpirationDate(final DateTime fundAllocationExpirationDate) {
	if (!isFundAllocationExpirationDateAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationExpirationDate");
	}
	super.setFundAllocationExpirationDate(fundAllocationExpirationDate);
	new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    public boolean isPendingApproval() {
	return isProcessInState(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    private boolean isProcessInState(AcquisitionProcessStateType state) {
	return getLastAcquisitionProcessStateType().equals(state);
    }

    protected AcquisitionProcessState getLastAcquisitionProcessState() {
	List<AcquisitionProcessState> processStates = new ArrayList<AcquisitionProcessState>(getAcquisitionProcessStates());
	return Collections.max(processStates, AcquisitionProcessState.COMPARATOR_BY_WHEN);
    }

    protected AcquisitionProcessStateType getLastAcquisitionProcessStateType() {
	return getLastAcquisitionProcessState().getAcquisitionProcessStateType();
    }

    public AcquisitionProcessState getAcquisitionProcessState() {
	return getLastAcquisitionProcessState();
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
	return getLastAcquisitionProcessStateType();
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
