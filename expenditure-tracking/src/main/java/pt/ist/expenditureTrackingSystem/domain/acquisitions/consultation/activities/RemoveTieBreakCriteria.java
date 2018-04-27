package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

public class RemoveTieBreakCriteria extends WorkflowActivity<MultipleSupplierConsultationProcess, RemoveTieBreakCriteriaInformation> {

    @Override
    public boolean isActive(final MultipleSupplierConsultationProcess process, final User user) {
        return process.getState() == MultipleSupplierConsultationProcessState.IN_GENESIS
                && process.getCreator() == Authenticate.getUser();
    }

    @Override
    protected void process(final RemoveTieBreakCriteriaInformation information) {
        information.getTieBreakCriteria().delete();
    }

    @Override
    public ActivityInformation<MultipleSupplierConsultationProcess> getActivityInformation(final MultipleSupplierConsultationProcess process) {
        return new RemoveTieBreakCriteriaInformation(process, this);
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
