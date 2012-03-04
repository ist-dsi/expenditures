/*
 * @(#)EditInitializationActivityInformation.java
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
package module.workingCapital.domain.activity;

import java.math.BigDecimal;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class EditInitializationActivityInformation extends WorkingCapitalInitializationInformation {

    private Person movementResponsible;
    private Money requestedMonthlyValue;
    private String fiscalId;
    private String internationalBankAccountNumber;

    public EditInitializationActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && super.hasAllneededInfo();
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
	super.setWorkingCapitalInitialization(workingCapitalInitialization);
	if (workingCapitalInitialization != null) {
	    movementResponsible = workingCapitalInitialization.getWorkingCapital().getMovementResponsible();
	    requestedMonthlyValue = workingCapitalInitialization.getRequestedAnualValue().divideAndRound(new BigDecimal(12));
	    fiscalId = workingCapitalInitialization.getFiscalId();
	    internationalBankAccountNumber = workingCapitalInitialization.getInternationalBankAccountNumber();
	}
    }

    public Person getMovementResponsible() {
	return movementResponsible;
    }

    public void setMovementResponsible(Person movementResponsible) {
	this.movementResponsible = movementResponsible;
    }

    public Money getRequestedMonthlyValue() {
	return requestedMonthlyValue;
    }

    public void setRequestedMonthlyValue(Money requestedMonthlyValue) {
	this.requestedMonthlyValue = requestedMonthlyValue;
    }

    public String getFiscalId() {
	return fiscalId;
    }

    public void setFiscalId(String fiscalId) {
	this.fiscalId = fiscalId;
    }

    public String getInternationalBankAccountNumber() {
	return internationalBankAccountNumber;
    }

    public void setInternationalBankAccountNumber(String internationalBankAccountNumber) {
	this.internationalBankAccountNumber = internationalBankAccountNumber;
    }

}
