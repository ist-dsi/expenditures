package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.fenixframework.DomainObject;

public interface VirtualHostAware extends DomainObject {
    public boolean isConnectedToCurrentHost();
}
