package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AcquisitionProcessStateType {

    IN_GENESIS,
    SUBMITTED_FOR_APPROVAL,
    APPROVED,
    FUNDS_ALLOCATED,
    FUNDS_ALLOCATED_TO_SERVICE_PROVIDER,
    ACQUISITION_PROCESSED,
    INVOICE_RECEIVED,
    INVOICE_CONFIRMED,
    ACQUISITION_PAYED,
    FUNDS_ALLOCATED_PERMANENTLY;

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(getClass().getSimpleName() + "." + name());
    }

}
