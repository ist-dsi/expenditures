package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.fenixWebFramework.util.DomainReference;
import pt.ist.fenixframework.DomainObject;

public class DomainObjectBean<T extends DomainObject> implements Serializable {

    DomainReference<T> domainObject;
    
    public DomainObjectBean() {
	this(null);
    }
    
    public DomainObjectBean(T object) {
	setDomainObject(object);
    }
    
    public void setDomainObject(T domainObject) {
	this.domainObject = new DomainReference<T>(domainObject);
    }
    
    public T getDomainObject() {
	return this.domainObject.getObject();
    }
}
