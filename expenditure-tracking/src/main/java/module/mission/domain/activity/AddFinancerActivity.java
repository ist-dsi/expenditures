package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class AddFinancerActivity extends MissionProcessActivity<MissionProcess, AddFinancerActivityInformation> {

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
	}

	@Override
	public boolean isActive(final MissionProcess missionProcess, final User user) {
		return super.isActive(missionProcess, user) && super.isActive(missionProcess, user)
				&& missionProcess.isUnderConstruction() && missionProcess.isRequestor(user);
	}

	@Override
	protected void process(final AddFinancerActivityInformation addFinancerActivityInformation) {
		final MissionProcess missionProcess = addFinancerActivityInformation.getProcess();
		final Mission mission = missionProcess.getMission();
		mission.addFinancer(addFinancerActivityInformation.getUnit());
	}

	@Override
	public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
		return new AddFinancerActivityInformation(process, this);
	}

	@Override
	public boolean isVisible() {
		return false;
	}

}
