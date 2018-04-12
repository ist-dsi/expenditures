package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class EditLowPriceLimitInfo extends WorkflowActivity<MultipleSupplierConsultationProcess, EditLowPriceLimitInfoInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS;
    }

    @Override
    protected void process(final EditLowPriceLimitInfoInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        consultation.setPriceLimitJustification(information.getPriceLimitJustification());
        consultation.setLowPriceLimit(information.getLowPriceLimit());
        consultation.setLowPriceLimitJustification(information.getLowPriceLimitJustification());
        consultation.setLowPriceLimitCriteria(information.getLowPriceLimitCriteria());
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new EditLowPriceLimitInfoInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
