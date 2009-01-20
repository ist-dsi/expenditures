package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RefundProcessStateType {

    IN_GENESIS {

	@Override
	public boolean showFor(final RefundProcessStateType currentStateType) {
	    return true;
	}

	@Override
	public boolean isCompleted(final RefundProcessStateType currentStateType) {
	    return currentStateType.ordinal() > ordinal();
	}

    },

    SUBMITTED_FOR_APPROVAL {

	@Override
	public boolean showFor(final RefundProcessStateType currentStateType) {
	    return true;
	}

    },

    APPROVED,

    FUNDS_ALLOCATED,

    AUTHORIZED,

    SUBMITTED_FOR_INVOICE_CONFIRMATION,

    INVOICES_CONFIRMED,

    IN_EXECUTION,

    FUNDS_ALLOCATED_PERMANENTLY,

    REJECTED {

	@Override
	public boolean showFor(final RefundProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final RefundProcessStateType currentStateType) {
	    return true;
	}

    },
    CANCELED {

	@Override
	public boolean showFor(final RefundProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final RefundProcessStateType currentStateType) {
	    return true;
	}

    };

    private RefundProcessStateType() {
    }

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(RefundProcessStateType.class.getSimpleName() + "." + name());
    }

    public boolean showFor(final RefundProcessStateType currentStateType) {
	return currentStateType.isActive();
    }

    public boolean hasNextState() {
	return true;
    }

    public boolean isCompleted(final RefundProcessStateType currentStateType) {
	return currentStateType.ordinal() >= ordinal();
    }

    public boolean isBlocked(final RefundProcessStateType currentStateType) {
	return false;
    }

    public boolean isActive() {
	return (this != REJECTED && this != CANCELED);
    }

}
