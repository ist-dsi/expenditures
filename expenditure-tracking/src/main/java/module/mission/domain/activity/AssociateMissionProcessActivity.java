package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.mission.domain.RemoteMissionProcess;
import module.workflow.domain.ActivityLog;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class AssociateMissionProcessActivity extends MissionProcessActivity<MissionProcess, AssociateMissionProcessActivityInfo> {

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

    @Atomic
    @Override
    protected void process(final AssociateMissionProcessActivityInfo activityInfo) {
        activityInfo.getProcess().addAssociatedMissionProcess(activityInfo.getRemoteMissionSystem(), activityInfo.getProcessNumber().trim(), activityInfo.getExternalId(), activityInfo.isConnect());
    }

    @Override
    public AssociateMissionProcessActivityInfo getActivityInformation(final MissionProcess process) {
        return new AssociateMissionProcessActivityInfo(process, this);
    }

    @Override
    protected ActivityLog logExecution(MissionProcess thisProcess, String operationName, User user,
            AssociateMissionProcessActivityInfo activityInfo, String... argumentsDescription) {
        return super.logExecution(thisProcess, operationName, user, activityInfo, argumentsDescription);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String[] getArgumentsDescription(AssociateMissionProcessActivityInfo activityInformation) {
        final MissionProcess process = activityInformation.getProcess();
        final StringBuilder builder = new StringBuilder();
        for (final RemoteMissionProcess remoteMissionProcess : process.getRemoteMissionProcessSet()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(remoteMissionProcess.getProcessNumber());
        }
        return new String[] { activityInformation.getProcessNumber(), builder.toString() };
    }
}
