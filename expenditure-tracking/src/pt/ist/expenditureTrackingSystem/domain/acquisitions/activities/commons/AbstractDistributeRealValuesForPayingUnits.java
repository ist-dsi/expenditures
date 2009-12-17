package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.List;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;

public abstract class AbstractDistributeRealValuesForPayingUnits<P extends PaymentProcess> extends
	WorkflowActivity<P, GenericAssignPayingUnitToItemActivityInformation<P>> {

    @Override
    protected void process(GenericAssignPayingUnitToItemActivityInformation<P> activityInformation) {
	List<UnitItemBean> beans = activityInformation.getBeans();
	RequestItem item = activityInformation.getItem();
	Money amount = Money.ZERO;

	item.clearRealShareValues();

	for (UnitItemBean bean : beans) {
	    Money share = bean.getRealShareValue();
	    if (share == null) {
		throw new DomainException("activities.message.exception.monetaryValueMustBeFilled", getName());
	    }
	    amount = amount.add(share);
	    item.getUnitItemFor(bean.getUnit()).setRealShareValue(share);
	}

    }

    public GenericAssignPayingUnitToItemActivityInformation<P> getActivityInformation(P process) {
	return new GenericAssignPayingUnitToItemActivityInformation<P>(process, this);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
