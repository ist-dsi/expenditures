package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;

public abstract class MissionProcessActivity<P extends MissionProcess, AI extends ActivityInformation<P>> extends
        WorkflowActivity<P, AI> {

    @Override
    public boolean isActive(final P process, final User user) {
        return !process.hasCurrentOwner() || process.isTakenByCurrentUser();
    }

}
