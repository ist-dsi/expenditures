package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

public class CreateRefundItemActivityInformation extends ActivityInformation<RefundProcess> {

    private Money valueEstimation;
    private CPVReference reference;
    private String description;
    private AcquisitionItemClassification classification;

    public CreateRefundItemActivityInformation(RefundProcess refundProcess,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(refundProcess, activity);
    }

    public Money getValueEstimation() {
	return valueEstimation;
    }

    public void setValueEstimation(Money valueEstimation) {
	this.valueEstimation = valueEstimation;
    }

    public CPVReference getCPVReference() {
	return reference;
    }

    public void setCPVReference(CPVReference reference) {
	this.reference = reference;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setClassification(AcquisitionItemClassification classification) {
	this.classification = classification;
    }

    public AcquisitionItemClassification getClassification() {
	return classification;
    }

    @Override
    public boolean hasAllneededInfo() {
	return getValueEstimation() != null && getCPVReference() != null && getDescription() != null;
    }
}
