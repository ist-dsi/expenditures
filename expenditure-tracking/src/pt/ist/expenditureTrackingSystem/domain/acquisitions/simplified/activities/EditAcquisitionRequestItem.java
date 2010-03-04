package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.domain.util.Address;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAcquisitionRequestItem extends
	WorkflowActivity<RegularAcquisitionProcess, EditAcquisitionRequestItemActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& (process.getRequestor() == person && process.getAcquisitionProcessState().isInGenesis() && process
			.getAcquisitionRequest().hasAnyRequestItems());
	// || (process.isSimplifiedAcquisitionProcess()
	// && ((SimplifiedProcedureProcess) process).getProcessClassification()
	// == ProcessClassification.CT75000
	// && person.hasRoleType(RoleType.ACQUISITION_CENTRAL) &&
	// process.getAcquisitionProcessState()
	// .isAuthorized());
    }

    @Override
    protected void process(EditAcquisitionRequestItemActivityInformation activityInformation) {
	AcquisitionRequest acquisitionRequest = activityInformation.getAcquisitionRequest();
	DeliveryInfo deliveryInfo = activityInformation.getDeliveryInfo();

	String recipient;
	Address address;
	String phone;
	String email;

	if (deliveryInfo != null) {
	    recipient = deliveryInfo.getRecipient();
	    email = deliveryInfo.getEmail();
	    phone = deliveryInfo.getPhone();
	    address = deliveryInfo.getAddress();
	} else {
	    recipient = activityInformation.getRecipient();
	    email = activityInformation.getEmail();
	    phone = activityInformation.getPhone();
	    address = activityInformation.getAddress();
	    acquisitionRequest.getRequester().createNewDeliveryInfo(recipient, address, phone, email);
	}

	activityInformation.getItem().edit(acquisitionRequest, activityInformation.getDescription(),
		activityInformation.getQuantity(), activityInformation.getUnitValue(), activityInformation.getVatValue(),
		activityInformation.getAdditionalCostValue(), activityInformation.getProposalReference(),
		activityInformation.getCPVReference(), recipient, address, phone, email);
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new EditAcquisitionRequestItemActivityInformation(process, this);
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

    @Override
    public boolean isVisible() {
	return false;
    }
}
