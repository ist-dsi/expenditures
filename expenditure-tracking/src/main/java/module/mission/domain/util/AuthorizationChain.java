/*
 * @(#)AuthorizationChain.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.mission.domain.util;

import java.io.Serializable;

import module.mission.domain.MissionSystem;
import module.organization.domain.Unit;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author Luis Cruz
 * 
 */
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

	public boolean isForCurrentInstitution() {
		return next == null ? MissionSystem.getInstance().getOrganizationalModel().getPartiesSet().contains(getUnit()) : next
				.isForCurrentInstitution();
	}
}
