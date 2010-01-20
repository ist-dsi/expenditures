package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.RoleType;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class JumpToProcessState extends WorkflowActivity<SimplifiedProcedureProcess, JumpToProcessStateInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(JumpToProcessStateInformation activityInformation) {
	final SimplifiedProcedureProcess simplifiedProcedureProcess = activityInformation.getProcess();
	final AcquisitionProcessStateType acquisitionProcessStateType = activityInformation.getAcquisitionProcessStateType();
	new AcquisitionProcessState(simplifiedProcedureProcess, acquisitionProcessStateType);
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new JumpToProcessStateInformation(process, this);
    }

    @Override
    protected String[] getArgumentsDescription(JumpToProcessStateInformation activityInformation) {
        return new String[] { activityInformation.getAcquisitionProcessStateType().getLocalizedName() };
    }

    @Override
    public boolean isUserAwarenessNeeded(SimplifiedProcedureProcess process, User user) {
	return false;
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
