package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class AddMultipleSupplierConsultationPartInformation extends ActivityInformation<MultipleSupplierConsultationProcess> {

    private String description;
    private Material material;
    private Money value;
    
    public AddMultipleSupplierConsultationPartInformation(final MultipleSupplierConsultationProcess process,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        final MultipleSupplierConsultation consultation = process.getConsultation();
        setDescription(consultation.getDescription());
        setMaterial(consultation.getMaterial());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput();
    }

}
