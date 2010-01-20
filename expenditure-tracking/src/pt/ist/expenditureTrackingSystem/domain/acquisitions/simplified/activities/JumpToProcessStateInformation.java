package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class JumpToProcessStateInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AcquisitionProcessStateType acquisitionProcessStateType;

    public JumpToProcessStateInformation(SimplifiedProcedureProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
        return acquisitionProcessStateType;
    }

    public void setAcquisitionProcessStateType(AcquisitionProcessStateType acquisitionProcessStateType) {
        this.acquisitionProcessStateType = acquisitionProcessStateType;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getAcquisitionProcessStateType() != null;
    }

}
