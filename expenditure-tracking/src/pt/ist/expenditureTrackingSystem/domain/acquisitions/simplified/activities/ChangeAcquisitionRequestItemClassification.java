package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ChangeAcquisitionRequestItemClassification extends
	WorkflowActivity<RegularAcquisitionProcess, ChangeAcquisitionRequestItemClassificationInfo> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    protected void process(ChangeAcquisitionRequestItemClassificationInfo activityInformation) {
	activityInformation.getItem().setClassification(activityInformation.getClassification());
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new ChangeAcquisitionRequestItemClassificationInfo(process, this);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }
}
