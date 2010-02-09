package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class EditSimpleContractDescriptionActivityInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private String contractSimpleDescription;

    public EditSimpleContractDescriptionActivityInformation(SimplifiedProcedureProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public void setProcess(SimplifiedProcedureProcess process) {
	super.setProcess(process);
	setContractSimpleDescription(process.getAcquisitionRequest().getContractSimpleDescription());
    }

    public String getContractSimpleDescription() {
	return contractSimpleDescription;
    }

    public void setContractSimpleDescription(String simpleContractDescription) {
	this.contractSimpleDescription = simpleContractDescription;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && !StringUtils.isEmpty(getContractSimpleDescription());
    }
}
