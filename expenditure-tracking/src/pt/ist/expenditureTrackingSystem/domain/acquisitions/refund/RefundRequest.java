package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RefundRequest extends RefundRequest_Base {

    public RefundRequest(RefundProcess process, Person requestor, Person refundee, Unit requestingUnit) {
	super();
	setProcess(process);
	setRefundee(refundee);
	setRequester(requestor);
	setRequestingUnit(requestingUnit);
    }

    public void createRefundItem(RefundItemBean bean) {
	RefundItem refundItem = new RefundItem(this, bean.getValueEstimation(), bean.getCPVReference(), bean.getDescription());
	List<Unit> payingUnits = this.getProcess().getPayingUnits();
	if (payingUnits.size() == 1) {
	    refundItem.createUnitItem(payingUnits.get(0), bean.getValueEstimation());
	}
    }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
	for (RefundItem item : getRefundItemsSet()) {
	    if (!item.isValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isApprovedByAtLeastOneResponsible() {
	for (RefundItem item : getRefundItemsSet()) {
	    if (item.hasAtLeastOneResponsibleApproval()) {
		return true;
	    }
	}
	return false;
    }

    public Set<RefundItem> getRefundItemsSet() {
	Set<RefundItem> refundItems = new HashSet<RefundItem>();
	for (RequestItem item : getRequestItems()) {
	    refundItems.add((RefundItem) item);
	}
	return refundItems;
    }

}
