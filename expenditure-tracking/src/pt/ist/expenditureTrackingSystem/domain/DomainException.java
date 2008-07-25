package pt.ist.expenditureTrackingSystem.domain;

public class DomainException extends Error {

    public DomainException(final String message, final Throwable cause) {
	super(message, cause);
    }

    public DomainException(final String message) {
	super(message);
    }

    public DomainException(final Throwable cause) {
	super(cause);
    }

}
