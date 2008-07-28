package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AcquisitionProcessState {

    IN_GENESIS,
    SUBMITTED_FOR_APPROVAL,
    APPROVED,
    FUNDS_ALLOCATED,
    FUNDS_ALLOCATED_TO_SERVICE_PROVIDER;

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(name());
    }

}
