package pt.ist.expenditureTrackingSystem.domain.requests;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RequestForProposalProcessStateType {

    SUBMITTED_FOR_APPROVAL, APPROVED, REJECTED, ACTIVE, CANCELED, ENDED;

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(RequestForProposalProcessStateType.class.getSimpleName() + "." + name());
    }

}
