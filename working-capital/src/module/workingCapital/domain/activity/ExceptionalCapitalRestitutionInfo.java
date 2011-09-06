package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

import org.apache.commons.lang.StringUtils;

public class ExceptionalCapitalRestitutionInfo extends ActivityInformation<WorkingCapitalProcess> {

    private Money value;
    private String caseDescription;

    public ExceptionalCapitalRestitutionInfo(WorkingCapitalProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public Money getValue() {
	return value;
    }

    public void setValue(Money value) {
	this.value = value;
    }

    public String getCaseDescription() {
	return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
	this.caseDescription = caseDescription;
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && isForwardedFromInput() && value.isPositive() && !StringUtils.isEmpty(caseDescription);
    }
}
