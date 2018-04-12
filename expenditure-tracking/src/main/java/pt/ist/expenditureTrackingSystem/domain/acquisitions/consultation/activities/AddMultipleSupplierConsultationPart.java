package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class AddMultipleSupplierConsultationPart extends WorkflowActivity<MultipleSupplierConsultationProcess, AddMultipleSupplierConsultationPartInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS;
    }

    @Override
    protected void process(final AddMultipleSupplierConsultationPartInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        new MultipleSupplierConsultationPart(consultation, information.getDescription(), information.getMaterial(), information.getValue());
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new AddMultipleSupplierConsultationPartInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
