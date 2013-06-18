package module.mission.domain.activity;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RemoveItemActivity extends MissionProcessActivity<MissionProcess, RemoveItemActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
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
        return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
                "label.module.mission.operation.not.reversable");
    }

}
