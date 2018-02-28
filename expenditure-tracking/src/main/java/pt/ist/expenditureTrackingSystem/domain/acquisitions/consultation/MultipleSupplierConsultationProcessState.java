package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MultipleSupplierConsultationProcessState implements IPresentableEnum {

    IN_GENESIS,
    SUBMITTED_FOR_APPROVAL,
    APPROVED,
    VERIFIED,
    FUNDS_ALLOCATED,
    DOCUMENTS_ELABORATED,
    PROCEDURE_AUTHORIZED,
    PROCEDURE_PUBLISHED,
    CANDIDATE_DOCUMENTATION_RECEIVED,
    EVALUATED,
    EVALUATION_PUBLISHED,
    EVALUATION_ADJUDICATED,
    FUNDS_COMMITTED,
    CANDIDATES_NOTIFIED,
    SUPPLIERS_SELECTED,
    CANCELLED
    ;

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureResources", I18N.getLocale());
        return resourceBundle.getString(MultipleSupplierConsultationProcessState.class.getSimpleName() + "." + name());
    }

}
