package module.mission.domain.activity;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class AllocateProjectFundsActivityInformation extends FundAllocationActivityInformation {

    public AllocateProjectFundsActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    protected boolean canAllocateFunds(final MissionFinancer missionFinancer) {
        final User user = Authenticate.getUser();
        final Person person = user.getPerson();
        return missionFinancer.canAllocateProjectFunds(person);
    }

    @Override
    protected boolean checkIfRequiresFundAllocation() {
        return false;
    }

}
