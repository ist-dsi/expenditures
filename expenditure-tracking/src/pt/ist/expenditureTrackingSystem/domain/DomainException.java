package pt.ist.expenditureTrackingSystem.domain;

public class DomainException extends Error {

    private final String[] args;

    public DomainException(final String message, final Throwable cause, String... args) {
	super(message, cause);
	this.args = args;
    }

    public DomainException(final String message, String... args) {
	super(message);
	this.args = args;
    }

    public DomainException(final Throwable cause, String... args) {
	super(cause);
	this.args = args;
    }

    public String[] getArgs() {
	return args;
    }

}
