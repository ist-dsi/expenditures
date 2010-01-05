package module.workingCapital.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapitalProcess extends WorkingCapitalProcess_Base {

    private static final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activities;

    static {
	final List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> activitiesAux = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	activities = Collections.unmodifiableList(activitiesAux);
    }

    public WorkingCapitalProcess() {
        super();
    }

    public WorkingCapitalProcess(final Integer year, final Unit unit, final Person person,
	    final Money requestedAnualValue, final String fiscalId, final String bankAccountId) {
	this();
	final WorkingCapitalInitialization workingCapitalInitialization = new WorkingCapitalInitialization(year, unit, person, requestedAnualValue, fiscalId, bankAccountId);
	final WorkingCapital workingCapital = workingCapitalInitialization.getWorkingCapital();
	setWorkingCapital(workingCapital);
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
	return (List) activities;
    }

    @Override
    public boolean isActive() {
	return true;
    }

}
