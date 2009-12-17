package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.ChangeFinancerAccountingUnitBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AbstractChangeFinancersAccountUnitActivityInformation<P extends PaymentProcess> extends ActivityInformation<P> {

    private List<ChangeFinancerAccountingUnitBean> beans;

    public AbstractChangeFinancersAccountUnitActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	Set<Financer> financersWithFundsAllocated = process.getRequest().getAccountingUnitFinancerWithNoFundsAllocated(
		Person.getLoggedPerson());
	Set<ChangeFinancerAccountingUnitBean> financersBean = new HashSet<ChangeFinancerAccountingUnitBean>(
		financersWithFundsAllocated.size());
	for (Financer financer : financersWithFundsAllocated) {
	    financersBean.add(new ChangeFinancerAccountingUnitBean(financer, financer.getAccountingUnit()));
	}
	beans = new ArrayList<ChangeFinancerAccountingUnitBean>();
	beans.addAll(financersBean);
    }

    public List<ChangeFinancerAccountingUnitBean> getBeans() {
	return beans;
    }

    public void setBeans(List<ChangeFinancerAccountingUnitBean> beans) {
	this.beans = beans;
    }

}
