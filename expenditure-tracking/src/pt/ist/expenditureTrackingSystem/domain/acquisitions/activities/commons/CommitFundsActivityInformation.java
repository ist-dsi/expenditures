package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.ArrayList;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CommitmentNumberBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CommitFundsActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private List<CommitmentNumberBean> commitmentNumberBeans = new ArrayList<CommitmentNumberBean>();

    public CommitFundsActivityInformation(final RegularAcquisitionProcess process,
	    final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	process.takeProcess();

	final User user = UserView.getCurrentUser();
	Person person = user.getExpenditurePerson();
	final AcquisitionRequest acquisitionRequest = process.getAcquisitionRequest();
	for (final Financer financer : acquisitionRequest.getFinancersSet()) {
	    if (!financer.isCommitted() && financer.isAccountingEmployee(person)) {
		commitmentNumberBeans.add(new CommitmentNumberBean(financer));
	    }
	}
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && hasAllCommitmentNumbers();
    }

    private boolean hasAllCommitmentNumbers() {
	if (commitmentNumberBeans.isEmpty()) {
	    return false;
	}
	for (final CommitmentNumberBean commitmentNumberBean : commitmentNumberBeans) {
	    final String commitmentNumber = commitmentNumberBean.getCommitmentNumber();
	    if (commitmentNumber == null || commitmentNumber.isEmpty()) {
		return false;
	    }
	}
	return true;
    }

    public List<CommitmentNumberBean> getCommitmentNumberBeans() {
        return commitmentNumberBeans;
    }

    public void setCommitmentNumberBeans(List<CommitmentNumberBean> commitmentNumberBeans) {
        this.commitmentNumberBeans = commitmentNumberBeans;
    }

}
