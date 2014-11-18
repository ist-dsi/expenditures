package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

public class UnAllocateFundsActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user)
                && missionProcess.hasAnyAllocatedFunds()
                && ((!missionProcess.hasAnyAuthorization() && !missionProcess.hasCommitmentNumber()) || missionProcess
                        .getIsCanceled().booleanValue()) && missionProcess.isAccountingEmployee(user.getExpenditurePerson());
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        final User user = Authenticate.getUser();
        final Person person = user.getPerson();
        final MissionProcess missionProcess = activityInformation.getProcess();
        missionProcess.unAllocateFunds(person);
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
