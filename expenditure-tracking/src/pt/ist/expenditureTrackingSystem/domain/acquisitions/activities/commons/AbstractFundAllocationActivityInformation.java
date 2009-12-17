package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;

public abstract class AbstractFundAllocationActivityInformation<T extends PaymentProcess> extends ActivityInformation<T> {

    protected List<FundAllocationBean> beans;

    public AbstractFundAllocationActivityInformation(T process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	beans = new ArrayList<FundAllocationBean>();
	if (process.getCurrentOwner() == null) {
	    process.takeProcess();
	}
	generateBeans();
    }

    public void generateBeans() {
	for (Financer financer : getFinancers()) {
	    beans.add(new FundAllocationBean(financer));
	}
    }

    public List<FundAllocationBean> getBeans() {
	return beans;
    }

    public void setBeans(List<FundAllocationBean> beans) {
	this.beans = beans;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput();
    }

    public abstract Set<? extends Financer> getFinancers();
}
