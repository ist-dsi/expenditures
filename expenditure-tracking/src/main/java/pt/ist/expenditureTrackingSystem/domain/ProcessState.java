/*
 * @(#)ProcessState.java
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
package pt.ist.expenditureTrackingSystem.domain;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public abstract class ProcessState extends ProcessState_Base {

	public ProcessState() {
		super();
		setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	}

	protected void initFields(final GenericProcess process, final Person person) {
		setProcess(process);
		setWho(person);
		setWhenDateTime(new DateTime());
		process.setCurrentProcessState(this);
	}

	protected void checkArguments(GenericProcess process, Person person) {
		if (process == null || person == null) {
			throw new DomainException("error.wrong.ProcessState.arguments");
		}
	}

	protected Person getPerson() {
		return Person.getLoggedPerson();
	}

	public static final Comparator<ProcessState> COMPARATOR_BY_WHEN = new Comparator<ProcessState>() {
		public int compare(ProcessState o1, ProcessState o2) {
			int r = o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
			return r == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : r;
		}
	};

	public abstract boolean isInFinalStage();

	@Override
	public boolean isConnectedToCurrentHost() {
		return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
	}

}
