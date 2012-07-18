/*
 * @(#)WorkingCapitalPayment.java
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
import pt.ist.bennu.core.domain.util.Money;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalPayment extends WorkingCapitalPayment_Base {

    public WorkingCapitalPayment() {
        super();
    }

    public WorkingCapitalPayment(final WorkingCapitalRequest workingCapitalRequest, final Person person, final String paymentIdentification) {
	this();
	setWorkingCapital(workingCapitalRequest.getWorkingCapital());
	setWorkingCapitalRequest(workingCapitalRequest);
	setPerson(person);
	final Money value = workingCapitalRequest.getRequestedValue();
	addDebt(value);
	setValue(value);
	setPaymentIdentification(paymentIdentification);
    }

    @Override
    public String getDescription() {
	final String paymentMethod = getWorkingCapitalRequest().getPaymentMethod().getLocalizedName();
	final String paymentIdentification = getPaymentIdentification();
	return paymentIdentification == null || paymentIdentification.isEmpty() ? paymentMethod : paymentMethod + ": " + paymentIdentification;
    }

    @Override
    public boolean isPayment() {
	return true;
    }

}
