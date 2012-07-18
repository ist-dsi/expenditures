/*
 * @(#)WorkingCapitalRequest.java
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
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;

import org.joda.time.DateTime;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalRequest extends WorkingCapitalRequest_Base {

    public WorkingCapitalRequest() {
	super();
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
	setWorkingCapitalSystem(workingCapitalSystem);
	setRequestCreation(new DateTime());
	final User user = UserView.getCurrentUser();
	if (user == null || !user.hasPerson()) {
	    throw new Error("error.requester.must.be.specified");
	}
	setWorkingCapitalRequester(user.getPerson());
    }

    public WorkingCapitalRequest(final WorkingCapital workingCapital, final Money requestedValue,
	    final PaymentMethod paymentMethod) {
	this();
	setWorkingCapital(workingCapital);
	setRequestedValue(requestedValue);
	setPaymentMethod(paymentMethod);
    }

    public boolean isRequestProcessedByTreasury() {
	return getProcessedByTreasury() != null;
    }

    public void pay(final User user, final String paymentIdentification) {
	setProcessedByTreasury(new DateTime());
	final Person person = user.getPerson();
	setWorkingCapitalTreasuryProcessor(person);
	new WorkingCapitalPayment(this, person, paymentIdentification);
    }

    @Override
    public void setRequestedValue(final Money requestedValue) {
	final WorkingCapital workingCapital = getWorkingCapital();
	if (workingCapital == null) {
	    throw new NullPointerException();
	}
	if (!workingCapital.canRequestValue(requestedValue)) {
	    throw new DomainException(BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources",
		    "error.insufficient.authorized.funds"));
	}
	super.setRequestedValue(requestedValue);
    }

    public void delete() {
	removeWorkingCapital();
	removeWorkingCapitalPayment();
	removeWorkingCapitalRequester();
	removeWorkingCapitalTreasuryProcessor();
	removeWorkingCapitalSystem();
	super.deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getWorkingCapitalSystem() == WorkingCapitalSystem.getInstanceForCurrentHost();
    }
}
