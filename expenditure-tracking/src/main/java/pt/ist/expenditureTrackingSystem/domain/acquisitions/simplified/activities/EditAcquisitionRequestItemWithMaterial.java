package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.finance.util.Address;
import module.workflow.activities.ActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class EditAcquisitionRequestItemWithMaterial extends EditAcquisitionRequestItem {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        Person person = user.getExpenditurePerson();
        return isUserProcessOwner(process, user) && !ExpenditureTrackingSystem.getInstance().getMaterialsSet().isEmpty()
                && ((process.getRequestor() == person && process.getAcquisitionProcessState().isInGenesis()
                        && process.getAcquisitionRequest().hasAnyRequestItems())
                        || (process.isSimplifiedAcquisitionProcess()
                                && ((SimplifiedProcedureProcess) process)
                                        .getProcessClassification() == ProcessClassification.CT75000
                                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                                && process.getAcquisitionProcessState().isAuthorized()));
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
				activityInformation.getQuantity(), activityInformation.getUnitValue(),
				activityInformation.getVatValue(), activityInformation.getAdditionalCostValue(),
				activityInformation.getProposalReference(), activityInformation.getMaterial(),
				activityInformation.getResearchAndDevelopmentPurpose(), recipient, address, phone, email,
				activityInformation.getClassification());
    }

}
