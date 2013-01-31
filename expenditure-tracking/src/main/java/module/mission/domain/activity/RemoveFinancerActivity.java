package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RemoveFinancerActivity extends MissionProcessActivity<MissionProcess, RemoveFinancerActivityInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final MissionProcess missionProcess, final User user) {
		final Mission mission = missionProcess.getMission();
		return super.isActive(missionProcess, user) && missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)
				&& mission.getFinancerCount() > 0;
	}

	@Override
	protected void process(final RemoveFinancerActivityInformation removeFinancerActivityInformation) {
		final MissionFinancer financer = removeFinancerActivityInformation.getFinancer();
		if (financer != null) {
			financer.delete();
		}
	}

	@Override
	public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
		return new RemoveFinancerActivityInformation(process, this);
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
