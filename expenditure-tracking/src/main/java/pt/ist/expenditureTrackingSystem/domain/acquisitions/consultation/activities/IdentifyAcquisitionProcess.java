package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class IdentifyAcquisitionProcess extends WorkflowActivity<MultipleSupplierConsultationProcess, IdentifyAcquisitionProcessInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.PENDING_ACQUISITION_PROCESS_IDENTIFICATION
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user);
    }

    @Override
    protected void process(final IdentifyAcquisitionProcessInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        consultation.setAcquisitionProcessIdentification(information.getAcquisitionProcessIdentification());
        process.setState(MultipleSupplierConsultationProcessState.PENDING_FUND_COMMITMENT);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new IdentifyAcquisitionProcessInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

}
