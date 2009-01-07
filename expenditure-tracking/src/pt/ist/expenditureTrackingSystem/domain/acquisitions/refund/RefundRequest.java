package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import pt.ist.expenditureTrackingSystem.domain.dto.RefundItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RefundRequest extends RefundRequest_Base {

    public RefundRequest(RefundProcess process, Person requestor, Person refundee, Unit requestingUnit) {
	super();
	setProcess(process);
	setRefundee(refundee);
	setRequestor(requestor);
	setRequestingUnit(requestingUnit);
    }

    public void createRefundItem(RefundItemBean bean) {
	new RefundItem(this, bean.getValueEstimation(), bean.getCPVReference(), bean.getDescription());
    }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
	for (RefundItem item : getRefundItemsSet()) {
	    if (!item.isValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

}
