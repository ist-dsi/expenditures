package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class FundAllocationActivityInformation<T extends PaymentProcess> extends AbstractFundAllocationActivityInformation<T> {

    public FundAllocationActivityInformation(T process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity,
	    final boolean takeProcess) {
	super(process, activity, takeProcess);
    }

    @Override
    public Set<Financer> getFinancers() {
	return getProcess().getFinancersWithFundsAllocated(Person.getLoggedPerson());
    }

}
