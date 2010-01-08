package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;

public abstract class AbstractChangeFinancersAccountUnit<P extends PaymentProcess> extends
	WorkflowActivity<P, AbstractChangeFinancersAccountUnitActivityInformation<P>> {

    @Override
    protected void process(AbstractChangeFinancersAccountUnitActivityInformation<P> activityInformation) {
	for (ChangeFinancerAccountingUnitBean changeFinancerAccountingUnitBean : activityInformation.getBeans()) {
	    changeFinancerAccountingUnitBean.getFinancer()
		    .setAccountingUnit(changeFinancerAccountingUnitBean.getAccountingUnit());
	}
    }

    public AbstractChangeFinancersAccountUnitActivityInformation<P> getActivityInformation(P process) {
	return new AbstractChangeFinancersAccountUnitActivityInformation<P>(process, this);
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

}
