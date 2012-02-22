package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CommitmentNumberBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CommitFunds extends WorkflowActivity<RegularAcquisitionProcess, CommitFundsActivityInformation> {

    @Override
    public boolean isActive(final RegularAcquisitionProcess process, final User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isAuthorized()
		&& process.isPendingCommitmentByUser(person);
    }

    @Override
    protected void process(final CommitFundsActivityInformation activityInformation) {
	for (final CommitmentNumberBean commitmentNumberBean : activityInformation.getCommitmentNumberBeans()) {
	    final Financer financer = commitmentNumberBean.getFinancer();
	    final String commitmentNumber = commitmentNumberBean.getCommitmentNumber();
	    financer.setCommitmentNumber(commitmentNumber);
	}
	activityInformation.getProcess().releaseProcess();
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(final RegularAcquisitionProcess process) {
        return new CommitFundsActivityInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
