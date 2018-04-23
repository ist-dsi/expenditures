package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class AddFinancer extends WorkflowActivity<MultipleSupplierConsultationProcess, AddFinancerInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final AddFinancerInformation information) {
        final MultipleSupplierConsultationProcess process = information.getProcess();
        final MultipleSupplierConsultation consultation = process.getConsultation();
        final Unit unit = information.getFinancer();
        final BigDecimal percentage = information.getPercentage();
        final MultipleSupplierConsultationFinancer financer = consultation.getFinancerSet().stream()
                .filter(f -> f.getUnit() == unit)
                .findAny()
                .orElseGet(() -> new MultipleSupplierConsultationFinancer(consultation, unit));
        financer.setPercentage(percentage);
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new AddFinancerInformation(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/ExpenditureResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(final MultipleSupplierConsultationProcess process, final User user) {
        return false;
    }

}
