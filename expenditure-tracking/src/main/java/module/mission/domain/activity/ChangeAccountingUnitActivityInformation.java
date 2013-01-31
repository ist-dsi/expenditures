/*
 * @(#)ChangeAccountingUnitActivityInformation.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package module.mission.domain.activity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ChangeAccountingUnitActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

	private MissionFinancer financer;
	private AccountingUnit accountingUnit;

	public ChangeAccountingUnitActivityInformation(final MissionProcess missionProcess,
			final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
		super(missionProcess, activity);
	}

	@Override
	public boolean hasAllneededInfo() {
		return isForwardedFromInput() && getFinancer() != null && accountingUnit != null;
	}

	public MissionFinancer getFinancer() {
		return financer;
	}

	public void setFinancer(final MissionFinancer financer) {
		this.financer = financer;
		accountingUnit = financer.getAccountingUnit();
	}

	public AccountingUnit getAccountingUnit() {
		return accountingUnit;
	}

	public void setAccountingUnit(AccountingUnit accountingUnit) {
		this.accountingUnit = accountingUnit;
	}

	public Object getAccountingUnits() {
		final Set<AccountingUnit> result = new HashSet<AccountingUnit>();
		final Unit unit = financer.getUnit();
		if (unit != null) {
			final AccountingUnit accountingUnit = unit.getAccountingUnit();
			if (accountingUnit != null) {
				result.add(accountingUnit);
			}
		}
		result.add(AccountingUnit.readAccountingUnitByUnitName("10"));
		return result;
	}

}
