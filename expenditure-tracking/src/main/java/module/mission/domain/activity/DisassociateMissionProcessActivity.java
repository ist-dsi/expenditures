package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.mission.domain.RemoteMissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.domain.ActivityLog;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class DisassociateMissionProcessActivity extends
        MissionProcessActivity<MissionProcess, DisassociateMissionProcessActivityInfo> {

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
    protected void process(final DisassociateMissionProcessActivityInfo activityInfo) {
        activityInfo.getProcess().removeAssociatedMissionProcess(activityInfo.getRemoteMissionProcess(), activityInfo.isConnect());
    }

    @Override
    public DisassociateMissionProcessActivityInfo getActivityInformation(final MissionProcess process) {
        return new DisassociateMissionProcessActivityInfo(process, this);
    }

    @Override
    protected ActivityLog logExecution(MissionProcess thisProcess, String operationName, User user,
            DisassociateMissionProcessActivityInfo activityInfo, String... argumentsDescription) {
        return super.logExecution(thisProcess, operationName, user, activityInfo, argumentsDescription);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected String[] getArgumentsDescription(DisassociateMissionProcessActivityInfo activityInformation) {
        final RemoteMissionProcess remoteMissionProcess = activityInformation.getRemoteMissionProcess();
        final StringBuilder builder = new StringBuilder();
        for (final RemoteMissionProcess otherRemoteMissionProcess : activityInformation.getProcess().getRemoteMissionProcessSet()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(otherRemoteMissionProcess.getProcessNumber());
        }
        return new String[] { remoteMissionProcess.getProcessNumber(), builder.toString() };
    }
}
