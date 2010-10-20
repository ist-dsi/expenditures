package module.mission.domain.activity;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

public class AllocateFundsActivityInformation extends FundAllocationActivityInformation {

    public AllocateFundsActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    @Override
    protected boolean canAllocateFunds(final MissionFinancer missionFinancer) {
	final User user = UserView.getCurrentUser();
	final Person person = user.getPerson();
	return missionFinancer.canAllocateFunds(person);
    }

}
