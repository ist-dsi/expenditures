package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAfterTheFactAcquisition extends
	WorkflowActivity<AfterTheFactAcquisitionProcess, EditAfterTheFactProcessActivityInformation> {

    @Override
    public boolean isActive(AfterTheFactAcquisitionProcess process, User user) {
	final Person loggedPerson = Person.getLoggedPerson();
	final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();
	return loggedPerson != null
		&& (loggedPerson.hasRoleType(RoleType.ACQUISITION_CENTRAL) || loggedPerson
			.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER))
		&& !acquisitionAfterTheFact.getDeletedState().booleanValue();
    }

    @Override
    public ActivityInformation<AfterTheFactAcquisitionProcess> getActivityInformation(AfterTheFactAcquisitionProcess process) {
	return new EditAfterTheFactProcessActivityInformation(process, this);
    }

    @Override
    protected void process(EditAfterTheFactProcessActivityInformation activityInformation) {
	activityInformation.getProcess().edit(activityInformation.getAfterTheFactAcquisitionType(),
		activityInformation.getValue(), activityInformation.getVatValue(), activityInformation.getSupplier(),
		activityInformation.getDescription());
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
    public boolean isUserAwarenessNeeded(AfterTheFactAcquisitionProcess process, User user) {
	return false;
    }
}
