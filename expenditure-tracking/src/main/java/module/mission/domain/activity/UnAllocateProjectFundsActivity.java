package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class UnAllocateProjectFundsActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.hasAnyAllocatedProjectFunds()
                && (!missionProcess.hasAnyAllocatedFunds() || missionProcess.getIsCanceled().booleanValue())
                && (!missionProcess.hasAnyAuthorization() || missionProcess.getIsCanceled().booleanValue())
                && missionProcess.isProjectAccountingEmployee(user.getExpenditurePerson());
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        final User user = UserView.getCurrentUser();
        final Person person = user.getPerson();
        final MissionProcess missionProcess = activityInformation.getProcess();
        missionProcess.unAllocateProjectFunds(person);
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
        return true;
    }

    @Override
    public String getUsedBundle() {
        return "resources/MissionResources";
    }

}
