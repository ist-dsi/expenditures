/*
 * @(#)ExceptionalWorkingCapitalRefund.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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

import module.organization.domain.Person;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

/**
 * 
 * @author João Neves
 * 
 */
public class ExceptionalWorkingCapitalRefund extends ExceptionalWorkingCapitalRefund_Base {

    public ExceptionalWorkingCapitalRefund() {
	super();
    }

    public ExceptionalWorkingCapitalRefund(final WorkingCapital workingCapital, final Person person, final Money value,
	    final String caseDescription) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
	setValue(value);
	setAccumulatedValue(getPreviousTransaction().getAccumulatedValue());
	setBalance(getPreviousTransaction().getBalance().subtract(value));
	setDebt(getPreviousTransaction().getDebt().subtract(value));
	setCaseDescription(caseDescription);
    }

    @Override
    public String getDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources",
		"label.module.workingCapital.transaction.ExceptionalWorkingCapitalRefund");
    }

    @Override
    public boolean isRefund() {
	return true;
    }

    @Override
    public boolean isExceptionalRefund() {
	return true;
    }

    public Money getRefundedValue() {
	return getValue();
    }

    public Person getOrigin() {
	final WorkingCapital workingCapital = getWorkingCapital();
	return workingCapital.getMovementResponsible();
    }
}
