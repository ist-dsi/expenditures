/*
 * @(#)AuthorizationLog.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.authorizations;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AuthorizationLog extends AuthorizationLog_Base {

	public static final Comparator<AuthorizationLog> COMPARATOR_BY_WHEN = new Comparator<AuthorizationLog>() {

		@Override
		public int compare(final AuthorizationLog authorizationLog1, AuthorizationLog authorizationLog2) {
			final DateTime when1 = authorizationLog1.getWhenOperationWasRan();
			final DateTime when2 = authorizationLog2.getWhenOperationWasRan();
			final int c = when1.compareTo(when2);
			return c == 0 ? authorizationLog2.hashCode() - authorizationLog1.hashCode() : c;
		}

	};

	public AuthorizationLog(final AuthorizationOperation authorizationOperation, final Authorization authorization,
			final String justification) {
		super();
		setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
		setWhenOperationWasRan(new DateTime());
		setWho(Person.getLoggedPerson());
		setAuthorizationOperation(authorizationOperation);

		setAuthorizationType(authorization.getAuthorizationType());
		setCanDelegate(authorization.getCanDelegate());
		setStartDate(authorization.getStartDate());
		setEndDate(authorization.getEndDate());
		setMaxAmount(authorization.getMaxAmount());
		setPerson(authorization.getPerson());
		setUnit(authorization.getUnit());
		setJustification(justification);
	}

	@Override
	public boolean isConnectedToCurrentHost() {
		return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
	}

}
