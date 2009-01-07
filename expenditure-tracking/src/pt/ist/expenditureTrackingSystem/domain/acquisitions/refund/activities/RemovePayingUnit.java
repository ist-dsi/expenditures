package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RemovePayingUnit extends GenericRefundProcessActivity {
    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return  super.isAvailable(process) && process.isInGenesis();
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	List<Unit> units = (List<Unit>) objects[0];
	RefundRequest request = process.getRequest();
	for (Unit unit : units) {
	    request.removePayingUnit(unit);
	}
    }
}
