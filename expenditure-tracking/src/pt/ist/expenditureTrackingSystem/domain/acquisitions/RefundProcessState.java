package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RefundProcessState extends RefundProcessState_Base {
    
    protected RefundProcessState(final RefundProcess process) {
        super();
        final Person person = getPerson();
        super.checkArguments(process, person);
        super.initFields(process, person);
	process.systemProcessRelease();

    }

    public RefundProcessState(final RefundProcess refundProcess, final RefundProcessStateType refundProcessStateType) {
	this(refundProcess);
	if (refundProcessStateType == null) {
	    throw new DomainException("error.wrong.ProcessState.arguments");
	}
	setRefundProcessStateType(refundProcessStateType);
    }

    public boolean isInGenesis() {
	return getRefundProcessStateType() == RefundProcessStateType.IN_GENESIS;
    }

    public boolean isPendingApproval() {
	return getRefundProcessStateType() == RefundProcessStateType.SUBMITTED_FOR_APPROVAL;
    }

    public boolean isInApprovedState() {
	return getRefundProcessStateType() == RefundProcessStateType.APPROVED;
    }

    public boolean isInAllocatedToUnitState() {
	return getRefundProcessStateType() == RefundProcessStateType.FUNDS_ALLOCATED;
    }

    public boolean isAuthorized() {
	return getRefundProcessStateType() == RefundProcessStateType.AUTHORIZED;
    }

    public boolean isPendingInvoicesConfirmation() {
	return getRefundProcessStateType() == RefundProcessStateType.SUBMITTED_FOR_INVOICE_CONFIRMATION;
    }
    
}
