package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AcquisitionProcessStateType {

    IN_GENESIS {

	@Override
	public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	    return true;
	}

	@Override
	public boolean isInProgress(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean isCompleted(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType.ordinal() > ordinal();
	}

    },
    SUBMITTED_FOR_APPROVAL {

	@Override
	public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	    return true;
	}

	@Override
	public boolean isInProgress(final AcquisitionProcessStateType currentStateType) {
	    return false;
	}

    },
    APPROVED,
    FUNDS_ALLOCATED {

	@Override
	public boolean isInProgress(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType == APPROVED;
	}

    },
    FUNDS_ALLOCATED_TO_SERVICE_PROVIDER,
    ACQUISITION_PROCESSED,
    INVOICE_RECEIVED,
    INVOICE_CONFIRMED,
    FUNDS_ALLOCATED_PERMANENTLY,
    ACQUISITION_PAYED {
	@Override
	public boolean hasNextState() {
	    return false;
	}
    },
    REJECTED {

	@Override
	public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	    return currentStateType == this;
	}

	@Override
	public boolean hasNextState() {
	    return false;
	}

	@Override
	public boolean isBlocked(final AcquisitionProcessStateType currentStateType) {
	    return true;
	}

    };

    private AcquisitionProcessStateType() {
    }

    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	return resourceBundle.getString(AcquisitionProcessStateType.class.getSimpleName() + "." + name());
    }

    public boolean showFor(final AcquisitionProcessStateType currentStateType) {
	return currentStateType != REJECTED;
    }

    public boolean hasNextState() {
	return true;
    }

    public boolean isInProgress(final AcquisitionProcessStateType currentStateType) {
	return currentStateType.ordinal() == ordinal() - 1;
    }

    public boolean isCompleted(final AcquisitionProcessStateType currentStateType) {
	return currentStateType.ordinal() >= ordinal();
    }

    public boolean isBlocked(final AcquisitionProcessStateType currentStateType) {
	return false;
    }

}
