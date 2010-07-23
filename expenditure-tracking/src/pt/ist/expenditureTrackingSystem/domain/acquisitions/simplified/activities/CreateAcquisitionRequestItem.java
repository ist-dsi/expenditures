package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.domain.util.Address;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;

public class CreateAcquisitionRequestItem extends
	WorkflowActivity<RegularAcquisitionProcess, CreateAcquisitionRequestItemActivityInformation> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return isUserProcessOwner(process, user) && process.getRequestor() == user.getExpenditurePerson()
		&& process.getAcquisitionProcessState().isInGenesis();
    }

    @Override
    protected void process(CreateAcquisitionRequestItemActivityInformation activityInformation) {
	final AcquisitionRequest acquisitionRequest = activityInformation.getProcess().getAcquisitionRequest();

	DeliveryInfo deliveryInfo = activityInformation.getDeliveryInfo();
	String recipient;
	String email;
	String phone;
	Address address;
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
	acquisitionRequest.createAcquisitionRequestItem(activityInformation.getAcquisitionRequest(),
		activityInformation.getDescription(), activityInformation.getQuantity(), activityInformation.getUnitValue(),
		activityInformation.getVatValue(), activityInformation.getAdditionalCostValue(),
		activityInformation.getProposalReference(), activityInformation.getCPVReference(), recipient, address, phone,
		email);
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(RegularAcquisitionProcess process) {
	return new CreateAcquisitionRequestItemActivityInformation(process, this);
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
