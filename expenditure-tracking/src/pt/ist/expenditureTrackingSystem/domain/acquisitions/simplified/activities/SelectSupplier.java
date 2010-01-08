package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SelectSupplier extends WorkflowActivity<RegularAcquisitionProcess, SelectSupplierActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER) && isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isAuthorized() && process.isSimplifiedAcquisitionProcess()
		&& ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000;
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new SelectSupplierActivityInformation(process, this);
    }

    @Override
    protected void process(SelectSupplierActivityInformation activityInformation) {
	activityInformation.getProcess().getRequest().setSelectedSupplier(activityInformation.getSupplier());
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
