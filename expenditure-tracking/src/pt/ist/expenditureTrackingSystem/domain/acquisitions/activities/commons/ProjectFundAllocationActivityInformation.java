package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ProjectFundAllocationActivityInformation<T extends PaymentProcess> extends
	AbstractFundAllocationActivityInformation<T> {

    public ProjectFundAllocationActivityInformation(T process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public Set<ProjectFinancer> getFinancers() {
	return getProcess().getProjectFinancersWithFundsAllocated(Person.getLoggedPerson());
    }

}
