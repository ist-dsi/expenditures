package module.mission.domain.activity;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class RemoveItemActivity extends MissionProcessActivity<MissionProcess, RemoveItemActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.getMission().hasAnyMissionItems()
                && ((missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)));
    }

    @Override
    protected void process(final RemoveItemActivityInformation removeItemActivityInformation) {
        final MissionItem missionItem = removeItemActivityInformation.getMissionItem();
        missionItem.delete();
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new RemoveItemActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/MissionResources", "label.module.mission.operation.not.reversable");
    }

}
