package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AnnouncementProcessStateType {

    IN_GENESIS {

	@Override
	public boolean showFor(final AnnouncementProcessStateType currentStateType) {
	    return true;
	}

	@Override
	public boolean isCurrent(final AnnouncementProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean isCompleted(final AnnouncementProcessStateType currentStateType) {
	    return currentStateType.ordinal() > ordinal();
	}

    },

    SUBMITTED_FOR_APPROVAL {

	@Override
	public boolean showFor(final AnnouncementProcessStateType currentStateType) {
	    return true;
	}

	@Override
	public boolean isCurrent(final AnnouncementProcessStateType currentStateType) {
	    return false;
	}

    },

    APPROVED,
    REJECTED {

	@Override
	public boolean showFor(final AnnouncementProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final AnnouncementProcessStateType currentStateType) {
	    return true;
	}

    },
    CANCELED {

	@Override
	public boolean showFor(final AnnouncementProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final AnnouncementProcessStateType currentStateType) {
	    return true;
	}

    };

    private AnnouncementProcessStateType() {
    }

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(AnnouncementProcessStateType.class.getSimpleName() + "." + name());
    }

    public boolean showFor(final AnnouncementProcessStateType currentStateType) {
	return currentStateType.isActive();
    }

    public boolean hasNextState() {
	return true;
    }

    public boolean isCurrent(final AnnouncementProcessStateType currentStateType) {
	return currentStateType.ordinal() == ordinal() - 1;
    }

    public boolean isCompleted(final AnnouncementProcessStateType currentStateType) {
	return currentStateType.ordinal() >= ordinal();
    }

    public boolean isBlocked(final AnnouncementProcessStateType currentStateType) {
	return false;
    }

    public boolean isActive() {
	return (this != REJECTED && this != CANCELED);
    }

}
