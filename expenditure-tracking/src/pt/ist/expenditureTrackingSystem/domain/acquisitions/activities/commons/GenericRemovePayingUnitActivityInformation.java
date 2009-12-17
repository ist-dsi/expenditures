package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class GenericRemovePayingUnitActivityInformation<P extends PaymentProcess> extends ActivityInformation<P> {

    private Unit payingUnit;

    public GenericRemovePayingUnitActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public Unit getPayingUnit() {
	return payingUnit;
    }

    public void setPayingUnit(Unit payingUnit) {
	this.payingUnit = payingUnit;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getPayingUnit() != null;
    }
}
