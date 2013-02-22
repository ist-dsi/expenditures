package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.domain.ActivityLog;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class DisassociateMissionProcessActivity extends MissionProcessActivity<MissionProcess, AssociateMissionProcessActivityInfo> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/MissionResources";
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.isRequestor(user);
    }

    @Service
    @Override
    protected void process(final AssociateMissionProcessActivityInfo activityInfo) {
        activityInfo.getProcess().removeAssociatedMissionProcess(activityInfo.getMissionProcessToAssociate());
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new AssociateMissionProcessActivityInfo(process, this);
    }

    @Override
    protected ActivityLog logExecution(MissionProcess thisProcess, String operationName, User user,
            AssociateMissionProcessActivityInfo activityInfo, String... argumentsDescription) {

        for (MissionProcess otherProcess : thisProcess.getAssociatedMissionProcesses()) {
            super.logExecution(otherProcess, operationName, user, activityInfo, argumentsDescription);
        }
        
        return super.logExecution(thisProcess, operationName, user, activityInfo, argumentsDescription);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String[] getArgumentsDescription(AssociateMissionProcessActivityInfo activityInformation) {
        MissionProcess processToAssociate = activityInformation.getMissionProcessToAssociate();
        String alreadyAssociatedProcessIds = processToAssociate.getProcessIdentification();
        if (processToAssociate.hasMissionProcessAssociation()) {
            for (MissionProcess otherAssociatedProcess : processToAssociate.getAssociatedMissionProcesses()) {
                alreadyAssociatedProcessIds += ", " + otherAssociatedProcess.getProcessIdentification();
            }
        }
        return new String[] { processToAssociate.getProcessIdentification(), alreadyAssociatedProcessIds };
    }
}
