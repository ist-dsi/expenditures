package pt.ist.expenditureTrackingSystem.domain.acquisitions.search.predicates;

import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RefundProcessPredicate extends SearchPredicate {

    @Override
    public boolean evaluate(PaymentProcess refundProcess, SearchPaymentProcess searchBean) {
	final RefundRequest refundRequest = refundProcess.getRequest();
	final Person taker = searchBean.getTaker();
	return matchesSearchCriteria(refundRequest, searchBean)
		&& (refundRequest.getProcess().isAvailableForCurrentUser() || refundProcess.isTakenByCurrentUser() || (taker != null && refundProcess
			.isTakenByPerson(taker)));
    }

    private boolean matchesSearchCriteria(final RefundRequest refundRequest, SearchPaymentProcess searchBean) {
	final Person person = refundRequest.getRequester();
	final String identification = refundRequest.getAcquisitionProcessId();
	final RefundProcessStateType type = refundRequest.getProcess().getProcessState().getRefundProcessStateType();
	final Set<AccountingUnit> accountingUnits = refundRequest.getAccountingUnits();
	final String refundeeName = refundRequest.getRefundee().getName();
	final Boolean showOnlyWithUnreadComments = searchBean.getShowOnlyWithUnreadComments();
	final RefundProcess process = refundRequest.getProcess();
	final Person taker = process.getCurrentOwner();

	Person loggedPerson = Person.getLoggedPerson();

	return matchCriteria(searchBean.getProcessId(), identification)
		&& matchCriteria(searchBean.getRequestingPerson(), person)
		&& matchCriteria(searchBean.getRequestingUnit(), refundRequest.getRequestingUnit())
		&& matchCriteria(searchBean.getHasAvailableAndAccessibleActivityForUser(), refundRequest)
		&& matchCriteria(searchBean.getRefundProcessStateType(), type)
		&& matchCriteria(searchBean.getAccountingUnit(), accountingUnits)
		&& matchCriteria(searchBean.getRefundeeName(), refundeeName)
		&& matchCriteria(searchBean.getTaker(), taker)
		&& (!showOnlyWithUnreadComments || (!process.getUnreadCommentsForPerson(loggedPerson).isEmpty() && process
			.hasActivitiesFromUser(loggedPerson)));
    }

    private boolean matchCriteria(RefundProcessStateType refundProcessStateType, RefundProcessStateType type) {
	return refundProcessStateType == null || refundProcessStateType.equals(type);
    }

    private boolean matchCriteria(final Boolean hasAvailableAndAccessibleActivityForUser, final RefundRequest refundRequest) {
	return hasAvailableAndAccessibleActivityForUser == null || !hasAvailableAndAccessibleActivityForUser.booleanValue()
		|| isPersonAbleToExecuteActivities(refundRequest.getProcess());
    }

    private boolean isPersonAbleToExecuteActivities(final RefundProcess refundProcess) {
	return refundProcess.isPersonAbleToExecuteActivities();
    }
}
