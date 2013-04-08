package module.mission.domain.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;

public class CommitFundsActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class MissionFinancerCommitFundAllocationBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private final MissionFinancer missionFinancer;
        private String commitmentNumber;

        public MissionFinancerCommitFundAllocationBean(final MissionFinancer missionFinancer) {
            this.missionFinancer = missionFinancer;
            if (missionFinancer != null) {
                commitmentNumber = missionFinancer.getCommitmentNumber();
            }
        }

        public String getCommitmentNumber() {
            return commitmentNumber;
        }

        public void setCommitmentNumber(String commitmentNumber) {
            this.commitmentNumber = commitmentNumber;
        }

        public MissionFinancer getMissionFinancer() {
            return missionFinancer;
        }
    }

    private final Collection<MissionFinancerCommitFundAllocationBean> missionFinancerCommitFundAllocationBeans =
            new ArrayList<MissionFinancerCommitFundAllocationBean>();

    public CommitFundsActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
        final Mission mission = missionProcess.getMission();
        for (final MissionFinancer financer : mission.getFinancerSet()) {
            if (canAllocateFunds(financer)) {
                missionFinancerCommitFundAllocationBeans.add(new MissionFinancerCommitFundAllocationBean(financer));
            }
        }
    }

    public Collection<MissionFinancerCommitFundAllocationBean> getMissionFinancerCommitFundAllocationBeans() {
        return missionFinancerCommitFundAllocationBeans;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

    protected boolean canAllocateFunds(final MissionFinancer missionFinancer) {
        final User user = UserView.getCurrentUser();
        final Person person = user.getPerson();
        return missionFinancer.canAllocateFunds(person);
    }

}
