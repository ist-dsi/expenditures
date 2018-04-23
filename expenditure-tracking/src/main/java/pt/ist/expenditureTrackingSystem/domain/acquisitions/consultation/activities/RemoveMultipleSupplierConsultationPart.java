package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class RemoveMultipleSupplierConsultationPart extends WorkflowActivity<MultipleSupplierConsultationProcess, RemoveMultipleSupplierConsultationPartInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final RemoveMultipleSupplierConsultationPartInformation information) {
        final MultipleSupplierConsultationPart part = information.getPart();
        final int currentPartNumber = part.getNumber().intValue();
        part.delete();

        final MultipleSupplierConsultation consultation = information.getProcess().getConsultation();
        consultation.getPartSet().stream()
            .filter(p -> p.getNumber().intValue() > currentPartNumber)
            .forEach(p -> p.setNumber(p.getNumber() - 1));
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new RemoveMultipleSupplierConsultationPartInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final MultipleSupplierConsultationProcess process, final User user) {
        return false;
    }
    
}
