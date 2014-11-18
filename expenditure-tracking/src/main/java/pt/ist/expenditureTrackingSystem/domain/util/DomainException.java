package pt.ist.expenditureTrackingSystem.domain.util;

public class DomainException extends org.fenixedu.bennu.core.domain.exceptions.DomainException {

    public DomainException(final String bundle, final String key, final String... args) {
        super(bundle, key, args);
    }

    public DomainException(final Throwable cause, final String bundle, final String key, final String... args) {
        super(cause, bundle, key, args);
    }

}
