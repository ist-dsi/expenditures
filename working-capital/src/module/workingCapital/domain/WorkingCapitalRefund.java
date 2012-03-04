/*
 * @(#)WorkingCapitalRefund.java
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

import module.organization.domain.Person;
import module.workingCapital.domain.util.PaymentMethod;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalRefund extends WorkingCapitalRefund_Base {
    
    public WorkingCapitalRefund() {
        super();
    }

    public WorkingCapitalRefund(final WorkingCapital workingCapital, final Person person, final Money value, final PaymentMethod paymentMethod) {
	this();
	setWorkingCapital(workingCapital);
	setPerson(person);
	setValue(value);
	setPaymentMethod(paymentMethod);
    }

    @Override
    public String getDescription() {
	final String prefix = BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "label.module.workingCapital.transaction.WorkingCapitalRefund");
	final PaymentMethod paymentMethod = getPaymentMethod();
	return paymentMethod == null ? prefix : prefix + ": " + paymentMethod.getLocalizedName();
    }

    @Override
    public Money getAccumulatedValue() {
	return Money.ZERO;
    }

    @Override
    public Money getBalance() {
	return Money.ZERO;
    }

    @Override
    public Money getDebt() {
	return Money.ZERO;
    }

    @Override
    public boolean isRefund() {
	return true;
    }

    public Person getOrigin() {
	final WorkingCapital workingCapital = getWorkingCapital();
	return workingCapital.getMovementResponsible();
    }

    public Money getRefundedValue() {
	return getPreviousTransaction().getBalance();
    }

}
