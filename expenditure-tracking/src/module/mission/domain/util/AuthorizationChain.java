package module.mission.domain.util;

import java.io.Serializable;

import module.organization.domain.Unit;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AuthorizationChain implements Serializable {

    private Unit unit;
    private AuthorizationChain next;

    public AuthorizationChain(final Unit unit) {
	setUnit(unit);
    }

    public AuthorizationChain(final Unit unit, final AuthorizationChain next) {
	this(unit);
	setNext(next);
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public AuthorizationChain getNext() {
	return next;
    }

    public void setNext(AuthorizationChain next) {
	this.next = next;
    }

    public AuthorizationChain last() {
	return next == null ? this : next.last();
    }

    public String exportAsString() {
	final StringBuilder stringBuilder = new StringBuilder();
	AuthorizationChain authorizationChain = this;
	while (authorizationChain != null) {
	    final Unit unit = authorizationChain.getUnit();
	    if (stringBuilder.length() > 0) {
		stringBuilder.append('_');
	    }
	    stringBuilder.append(unit.getExternalId());
	    authorizationChain = authorizationChain.next;
	}
	return stringBuilder.toString();
    }

    public String getExportString() {
	return exportAsString();
    }

    public static AuthorizationChain importFromString(final String string) {
	AuthorizationChain authorizationChain = null;
	for (final String externalUnitId : string.split("_")) {
	    final Unit unit = AbstractDomainObject.fromExternalId(externalUnitId);
	    AuthorizationChain next = new AuthorizationChain(unit);
	    if (authorizationChain == null) {
		authorizationChain = next;
	    } else {
		authorizationChain.last().setNext(next);
	    }
	}
	return authorizationChain;
    }

    public int getChainSize() {
	return next == null ? 1 : next.getChainSize() + 1;
    }

}
