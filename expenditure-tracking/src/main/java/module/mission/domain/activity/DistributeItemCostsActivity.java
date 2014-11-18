package module.mission.domain.activity;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class DistributeItemCostsActivity extends MissionProcessActivity<MissionProcess, DistributeItemCostsActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.getMission().hasAnyMissionItems()
                && missionProcess.getMission().getFinancerSet().size() > 1
                && ((missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)));
    }

    @Override
    protected void process(final DistributeItemCostsActivityInformation distributeItemCostsActivityInformation) {
        final MissionItem missionItem = distributeItemCostsActivityInformation.getMissionItem();
        missionItem.distributeCosts(distributeItemCostsActivityInformation.getMissionItemFinancerBeans());
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new DistributeItemCostsActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
