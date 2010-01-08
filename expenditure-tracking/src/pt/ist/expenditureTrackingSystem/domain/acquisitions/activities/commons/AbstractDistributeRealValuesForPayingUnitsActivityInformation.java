package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;

public class AbstractDistributeRealValuesForPayingUnitsActivityInformation<P extends PaymentProcess> extends
	GenericAssignPayingUnitToItemActivityInformation<P> {

    public AbstractDistributeRealValuesForPayingUnitsActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public void setItem(RequestItem item) {
	this.item = item;
	for (final UnitItem unitItem : item.getUnitItemsSet()) {
	    this.beans.add(new UnitItemBean(unitItem.getUnit(), item));
	}
    }
}
