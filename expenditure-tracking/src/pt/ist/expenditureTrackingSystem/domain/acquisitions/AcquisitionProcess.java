package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collections;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionRequestItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
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

    protected AcquisitionProcess(String fiscalCode, String costCenter, String project, String subproject, String recipient,
	    String receptionAddress) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	new AcquisitionProcessState(this, AcquisitionProcessStateType.IN_GENESIS);
	new AcquisitionRequest(this, fiscalCode, costCenter, project, subproject, recipient, receptionAddress);
    }

    public static boolean isCreateNewAcquisitionProcessAvailable() {
	return UserView.getUser() != null;
    }

    @Service
    public static AcquisitionProcess createNewAcquisitionProcess(final CreateAcquisitionProcessBean createAcquisitionProcessBean) {
	if (!isCreateNewAcquisitionProcessAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.createNewAcquisitionProcess");
	}
	return new AcquisitionProcess(createAcquisitionProcessBean.getFiscalIdentificationCode(), createAcquisitionProcessBean
		.getCostCenter(), createAcquisitionProcessBean.getProject(), createAcquisitionProcessBean.getSubproject(),
		createAcquisitionProcessBean.getRecipient(), createAcquisitionProcessBean.getReceptionAddress());
    }

    public boolean isAcquisitionProposalDocumentAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
		&& user.getPerson().equals(getRequestor());
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
		&& user.getPerson().equals(getRequestor());
    }

    @Service
    public AcquisitionRequestItem createAcquisitionRequestItem(CreateAcquisitionRequestItemBean acquisitionRequestItemBean) {
	if (!isCreateAcquisitionRequestItemAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.createAcquisitionRequestItem");
	}
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.createAcquisitionRequestItem(acquisitionRequestItemBean);
    }

    public boolean isSubmitForApprovalAvailable() {
	User user = UserView.getUser();
	return user != null && isProcessInState(AcquisitionProcessStateType.IN_GENESIS)
		&& user.getPerson().equals(getRequestor()) && getAcquisitionRequest().isFilled();
    }

    @Service
    public void submitForApproval() {
	if (!isSubmitForApprovalAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.submitForApproval");
	}
	new AcquisitionProcessState(this, AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public Person getRequestor() {
	return getAcquisitionRequest().getRequester();
    }

    public boolean isResponsibleForUnit() {
	User user = UserView.getUser();
	if (user == null) {
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

    public boolean isApproveAvailable() {
	return isPendingApproval() && isResponsibleForUnit();
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
	return user != null && user.getPerson().equals(getRequestor())
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
	return userHasRole(RoleType.ACCOUNTABILITY) && isProcessInState(AcquisitionProcessStateType.APPROVED);
    }

    @Override
    public void setFundAllocationId(final String fundAllocationId) {
	if (!isFundAllocationIdAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationId");
	}
	super.setFundAllocationId(fundAllocationId);
	new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    protected boolean userHasRole(final RoleType roleType) {
	final User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(roleType);
    }

    public boolean isFundAllocationExpirationDateAvailable() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL) && isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED);
    }

    @Override
    public void setFundAllocationExpirationDate(final DateTime fundAllocationExpirationDate) {
	if (!isFundAllocationExpirationDateAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setFundAllocationExpirationDate");
	}
	super.setFundAllocationExpirationDate(fundAllocationExpirationDate);
	new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    public boolean isEditRequestItemAvailable() {
	User user = UserView.getUser();
	return user != null && user.getPerson().equals(getRequestor())
		&& isProcessInState(AcquisitionProcessStateType.IN_GENESIS);
    }

    public boolean isPendingApproval() {
	return isProcessInState(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public boolean isCreateAcquisitionRequestAvailable() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL)
		&& isProcessInState(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
    }

    @Service
    public AcquisitionRequestDocument createAcquisitionRequest() {
	if (getAcquisitionRequest().getAcquisitionRequestDocument() != null) {
	    return getAcquisitionRequest().getAcquisitionRequestDocument();
	}

	if (!isCreateAcquisitionRequestAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.createAcquisitionRequest");
	}

	AcquisitionRequestDocument acquisitionRequestDocument = new AcquisitionRequestDocument(getAcquisitionRequest());
	new AcquisitionProcessState(this, AcquisitionProcessStateType.ACQUISITION_PROCESSED);
	return acquisitionRequestDocument;
    }

    private boolean isProcessInState(AcquisitionProcessStateType state) {
	return getLastAcquisitionProcessStateType().equals(state);
    }

    protected AcquisitionProcessState getLastAcquisitionProcessState() {
	return Collections.max(getAcquisitionProcessStates(), AcquisitionProcessState.COMPARATOR_BY_WHEN);
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
		|| isFundAllocationExpirationDateAvailable() || isCreateAcquisitionRequestAvailable()
		|| isReceiveInvoiceAvailable() || isConfirmInvoiceAvailable() || isPayAcquisitionAvailable()
		|| isAlocateFundsPermanentlyAvailable();
    }

    public boolean isAcquisitionProcessed() {
	return isProcessInState(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    public boolean isReceiveInvoiceAvailable() {
	return isAcquisitionProcessed() && userHasRole(RoleType.ACCOUNTABILITY);
    }

    @Service
    public void receiveInvoice(final String filename, final byte[] bytes, final String invoiceNumber, final DateTime invoiceDate) {
	if (!isAcquisitionProcessed()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.setReceiveInvoice");
	}
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.receiveInvoice(filename, bytes, invoiceNumber, invoiceDate);
	new AcquisitionProcessState(this, AcquisitionProcessStateType.INVOICE_RECEIVED);
    }

    public boolean isInvoiceReceived() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return isProcessInState(AcquisitionProcessStateType.INVOICE_RECEIVED) && acquisitionRequest.isInvoiceReceived();
    }

    public boolean isConfirmInvoiceAvailable() {
	return isInvoiceReceived() && isResponsibleForUnit();
    }

    @Service
    public void confirmInvoice() {
	if (!isInvoiceReceived()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.confirmInvoice");
	}
	new AcquisitionProcessState(this, AcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    public boolean isPayAcquisitionAvailable() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL) && isProcessInState(AcquisitionProcessStateType.INVOICE_CONFIRMED);
    }

    @Service
    public void payAcquisition() {
	if (!isPayAcquisitionAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.alocateFundsPermanently");
	}
	new AcquisitionProcessState(this, AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    public boolean isAlocateFundsPermanentlyAvailable() {
	return userHasRole(RoleType.ACCOUNTABILITY) && isProcessInState(AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    @Service
    public void alocateFundsPermanently() {
	if (!isAlocateFundsPermanentlyAvailable()) {
	    throw new DomainException("error.acquisitionProcess.invalid.state.to.run.alocateFundsPermanently");
	}
	new AcquisitionProcessState(this, AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
    }

    public Unit getUnit() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return Unit.findUnitByCostCenter(acquisitionRequest.getCostCenter());
    }

    public boolean isAllowedToViewCostCenterExpenditures() {
	try {
	    return getUnit() != null && isResponsibleForUnit() || userHasRole(RoleType.ACCOUNTABILITY);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new Error(e);
	}
    }

    public boolean isAllowedToViewSupplierExpenditures() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

}
