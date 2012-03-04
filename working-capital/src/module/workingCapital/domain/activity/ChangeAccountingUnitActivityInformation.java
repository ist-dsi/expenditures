/*
 * @(#)ChangeAccountingUnitActivityInformation.java
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

import java.util.HashSet;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ChangeAccountingUnitActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private AccountingUnit accountingUnit;

    public ChangeAccountingUnitActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
	final WorkingCapitalProcess process = getProcess();
	final WorkingCapital workingCapital = process.getWorkingCapital();
	accountingUnit = workingCapital.getAccountingUnit();
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && super.hasAllneededInfo() && accountingUnit != null;
    }

    public AccountingUnit getAccountingUnit() {
        return accountingUnit;
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
        this.accountingUnit = accountingUnit;
    }

    public Object getAccountingUnits() {
	final Set<AccountingUnit> res = new HashSet<AccountingUnit>();
	final WorkingCapitalProcess process = getProcess();
	final WorkingCapital workingCapital = process.getWorkingCapital();
	final Unit unit = workingCapital.getUnit();
	final AccountingUnit accountingUnit = unit.getAccountingUnit();
	if (accountingUnit != null) {
	    res.add(accountingUnit);
	}
	res.add(AccountingUnit.readAccountingUnitByUnitName("10"));
	return res;
    }

}
