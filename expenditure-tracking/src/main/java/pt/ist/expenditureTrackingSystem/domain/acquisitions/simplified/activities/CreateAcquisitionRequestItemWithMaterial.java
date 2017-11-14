package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;

import module.finance.util.Address;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;

public class CreateAcquisitionRequestItemWithMaterial extends CreateAcquisitionRequestItem {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return isUserProcessOwner(process, user) && process.getRequestor() == user.getExpenditurePerson()
                && process.getAcquisitionProcessState().isInGenesis()
                && !ExpenditureTrackingSystem.getInstance().getMaterialsSet().isEmpty();
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
                activityInformation.getProposalReference(), activityInformation.getMaterial(), recipient, address, phone, email,
                activityInformation.getClassification());
    }

}
