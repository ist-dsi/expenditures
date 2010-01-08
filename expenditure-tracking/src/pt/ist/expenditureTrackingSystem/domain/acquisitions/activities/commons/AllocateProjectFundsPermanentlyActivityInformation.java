package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.utl.ist.fenix.tools.util.Strings;

public class AllocateProjectFundsPermanentlyActivityInformation<P extends PaymentProcess> extends
	AbstractFundAllocationActivityInformation<P> {

    public AllocateProjectFundsPermanentlyActivityInformation(P process,
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
	    if (financer.isProjectFinancer()) {
		beans.addAll(getProjectFundAllocationBeans((ProjectFinancer) financer));
	    }
	}
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput();
    }

    private List<FundAllocationBean> getProjectFundAllocationBeans(ProjectFinancer projectFinancer) {
	List<FundAllocationBean> beans = new ArrayList<FundAllocationBean>();
	Strings effectiveFunds = projectFinancer.getEffectiveProjectFundAllocationId();

	if (effectiveFunds == null) {
	    FundAllocationBean fundAllocationBean = new FundAllocationBean(projectFinancer);
	    fundAllocationBean.setFundAllocationId(projectFinancer.getProjectFundAllocationId());
	    fundAllocationBean.setEffectiveFundAllocationId(projectFinancer.getProjectFundAllocationId());
	    fundAllocationBean.setAllowedToAddNewFund(true);
	    beans.add(fundAllocationBean);
	} else {
	    int i = 0;
	    for (String effectiveFund : effectiveFunds) {
		FundAllocationBean fundAllocationBean = new FundAllocationBean(projectFinancer);
		fundAllocationBean.setFundAllocationId(projectFinancer.getProjectFundAllocationId());
		fundAllocationBean.setEffectiveFundAllocationId(effectiveFund);
		fundAllocationBean.setAllowedToAddNewFund(i++ == 0);
		beans.add(fundAllocationBean);
	    }
	}

	return beans;
    }

    @Override
    public Set<? extends Financer> getFinancers() {
	return getProcess().getFinancersWithFundsInitiallyAllocated();
    }

}
