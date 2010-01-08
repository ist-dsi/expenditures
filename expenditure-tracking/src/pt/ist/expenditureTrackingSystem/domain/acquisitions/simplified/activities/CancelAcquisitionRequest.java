package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class CancelAcquisitionRequest extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	AcquisitionProcessState acquisitionProcessState = process.getAcquisitionProcessState();
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& ((acquisitionProcessState.isAcquisitionProcessed() && person.hasRoleType(RoleType.ACQUISITION_CENTRAL))
			|| (acquisitionProcessState.isInGenesis() && process.getRequestor() == person)
			|| (acquisitionProcessState.isInAllocatedToUnitState() && isUserResponsibleForAuthorizingPayment(process,
				person))
			|| (acquisitionProcessState.isPendingInvoiceConfirmation() && isUserResponsibleForUnit(process, person)) || (acquisitionProcessState
			.isInvoiceReceived() && person.hasRoleType(RoleType.ACQUISITION_CENTRAL)));
    }

    private boolean isUserResponsibleForAuthorizingPayment(RegularAcquisitionProcess process, Person person) {

	return person != null
		&& process.isResponsibleForUnit(person, process.getAcquisitionRequest()
			.getTotalItemValueWithAdditionalCostsAndVat())
		&& !process.getAcquisitionRequest().hasBeenAuthorizedBy(person);
    }

    private boolean isUserResponsibleForUnit(RegularAcquisitionProcess process, Person person) {
	return person != null && process.isResponsibleForUnit(person);
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().cancel();
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
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
	return true;
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }
}
