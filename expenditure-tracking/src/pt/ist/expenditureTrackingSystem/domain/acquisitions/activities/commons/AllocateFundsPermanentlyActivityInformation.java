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
import pt.utl.ist.fenix.tools.util.Strings;

public class AllocateFundsPermanentlyActivityInformation<P extends PaymentProcess> extends
	AbstractFundAllocationActivityInformation<P> {

    public AllocateFundsPermanentlyActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity,
	    final boolean takeProcess) {
	super(process, activity, takeProcess);
    }

    @Override
    public void generateBeans() {
	for (Financer financer : getFinancers()) {
	    beans.addAll(getFundAllocationBeans(financer));
	}

    }

    @Override
    public Set<? extends Financer> getFinancers() {
	return getProcess().getFinancersWithFundsInitiallyAllocated();
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput();
    }

    private List<FundAllocationBean> getFundAllocationBeans(Financer financer) {
	List<FundAllocationBean> beans = new ArrayList<FundAllocationBean>();
	Strings effectiveFunds = financer.getEffectiveFundAllocationId();

	if (effectiveFunds == null) {
	    FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
	    fundAllocationBean.setFundAllocationId(financer.getFundAllocationId());
	    fundAllocationBean.setEffectiveFundAllocationId(financer.getFundAllocationId());
	    fundAllocationBean.setAllowedToAddNewFund(true);
//	    fundAllocationBean.setDiaryNumber(financer.getPaymentDiaryNumber());
//	    fundAllocationBean.setTransactionNumber(financer.getTransactionNumber());
	    
	    beans.add(fundAllocationBean);
	} else {
	    int i = 0;
	    for (String effectiveFund : effectiveFunds) {
		FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
		fundAllocationBean.setFundAllocationId(financer.getFundAllocationId());
		fundAllocationBean.setEffectiveFundAllocationId(effectiveFund);
		fundAllocationBean.setAllowedToAddNewFund(i++ == 0);
//		fundAllocationBean.setDiaryNumber(financer.getPaymentDiaryNumber());
//		fundAllocationBean.setTransactionNumber(financer.getTransactionNumber());
		beans.add(fundAllocationBean);
	    }
	}

	return beans;
    }

}
