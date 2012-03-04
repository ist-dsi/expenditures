/*
 * @(#)SearchPredicate.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.search.predicates;

import java.util.Collection;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.DomainObject;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class SearchPredicate {

    public abstract boolean evaluate(PaymentProcess process, SearchPaymentProcess searchBean);

    protected boolean matchCriteria(final Unit unit, final Set<Financer> financers) {
	if (unit == null) {
	    return true;
	}
	for (final Financer financer : financers) {
	    if (unit == financer.getUnit()) {
		return true;
	    }
	}
	return false;
    }

    protected boolean matchCriteria(final DomainObject object, final DomainObject otherDomainObject) {
	return object == null || object == otherDomainObject;
    }

    protected boolean matchCriteria(final Person requester, final Person person) {
	return requester == null || requester == person;
    }

    protected boolean matchCriteria(final String string, final String otherString) {
	return string == null || string.length() == 0 || string.equalsIgnoreCase(otherString);
    }

    protected boolean matchContainsCriteria(DomainObject object, Collection collection) {
	return object == null || collection.contains(object);
    }

}
