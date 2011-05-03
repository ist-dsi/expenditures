package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;

public class EditSimpleContractDescription extends
	WorkflowActivity<SimplifiedProcedureProcess, EditSimpleContractDescriptionActivityInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
	ProcessClassification processClassification = process.getProcessClassification();
	return (processClassification == ProcessClassification.CT10000 || processClassification == ProcessClassification.CT75000)
		&& (process.isInAuthorizedState()
			&& ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
		|| (process.isInGenesis() && process.getProcessCreator() == user);
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
	return new EditSimpleContractDescriptionActivityInformation(process, this);
    }

    @Override
    protected void process(EditSimpleContractDescriptionActivityInformation activityInformation) {
	activityInformation.getProcess().getAcquisitionRequest().setContractSimpleDescription(
		activityInformation.getContractSimpleDescription());
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
