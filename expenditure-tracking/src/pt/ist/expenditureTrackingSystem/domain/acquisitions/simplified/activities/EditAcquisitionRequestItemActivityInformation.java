package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class EditAcquisitionRequestItemActivityInformation extends CreateAcquisitionRequestItemActivityInformation {

    private AcquisitionRequestItem item;

    public EditAcquisitionRequestItemActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionRequestItem getItem() {
	return item;
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = item;
	setDescription(item.getDescription());
	setQuantity(item.getQuantity());
	setUnitValue(item.getUnitValue());
	setVatValue(item.getVatValue());
	setAdditionalCostValue(item.getAdditionalCostValue());
	setProposalReference(item.getProposalReference());
	setAcquisitionRequest(item.getAcquisitionRequest());
	setClassification(item.getClassification());
	setCPVReference(item.getCPVReference());
	setDeliveryInfo(UserView.getCurrentUser().getExpenditurePerson()
		.getDeliveryInfoByRecipientAndAddress(item.getRecipient(), item.getAddress()));
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getItem() != null && super.hasAllneededInfo();
    }

}
