package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MultipleSupplierConsultationProcessState implements IPresentableEnum {

    IN_GENESIS,
    SUBMITTED_FOR_APPROVAL,
    SUBMITTED_FOR_VERIFICATION,
    SUBMITTED_FOR_FUNDS_ALLOCATION,
    DOCUMENTS_UNDER_ELABORATION,
    PENDING_AUTHORIZATION,
    PENDING_PUBLICATION,
    PENDING_CANDIDATE_DOCUMENTATION,
    PENDING_EVALUATION,
    PENDING_EVALUATION_PUBLICATION,
    PENDING_ADJUDICATION,
    PENDING_FUND_COMMITMENT,
    PENDING_CANDIDATE_NOTIFICATION,
    PENDING_SUPPLIER_SELECTION,
    SUPPLIERS_SELECTED,
    CANCELLED
    ;

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureResources", I18N.getLocale());
        return resourceBundle.getString(MultipleSupplierConsultationProcessState.class.getSimpleName() + "." + name());
    }

    public String getCompletedTitle() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureResources", I18N.getLocale());
        return resourceBundle.getString(MultipleSupplierConsultationProcessState.class.getSimpleName() + "." + name() + ".completed");
    }

}
