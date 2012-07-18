/*
 * @(#)AcquisitionClassification.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class AcquisitionClassification extends AcquisitionClassification_Base {

    public static final Comparator<AcquisitionClassification> COMPARATOR_BY_DESCRIPTION = new Comparator<AcquisitionClassification>() {

	@Override
	public int compare(final AcquisitionClassification o1, final AcquisitionClassification o2) {
	    final int c = o1.getDescription().compareTo(o2.getDescription());
	    return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
	}

    };

    public AcquisitionClassification() {
	super();
	setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
    }

    public AcquisitionClassification(final String description, final String economicClassification, final String pocCode) {
	this();
	setDescription(description);
	setEconomicClassification(economicClassification);
	setPocCode(pocCode);
    }

    public static SortedSet<AcquisitionClassification> getAvailableClassifications() {
	final SortedSet<AcquisitionClassification> result = new TreeSet<AcquisitionClassification>(COMPARATOR_BY_DESCRIPTION);
	result.addAll(WorkingCapitalSystem.getInstanceForCurrentHost().getAcquisitionClassificationsSet());
	return result;
    }

    @Service
    public void delete() {
	if (!hasAnyWorkingCapitalAcquisitions()) {
	    removeWorkingCapitalSystem();
	    deleteDomainObject();
	}
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getWorkingCapitalSystem() == WorkingCapitalSystem.getInstanceForCurrentHost();
    }
}
