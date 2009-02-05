package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AnnouncementProcessStateType {

    IN_GENESIS,
    SUBMITTED_FOR_APPROVAL,
    APPROVED,
    REJECTED,
    CANCELED, 
    CLOSED;

    private AnnouncementProcessStateType() {
    }

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
	return resourceBundle.getString(AnnouncementProcessStateType.class.getSimpleName() + "." + name());
    }

}
