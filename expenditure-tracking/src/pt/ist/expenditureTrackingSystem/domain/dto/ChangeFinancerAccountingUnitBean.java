/*
 * @(#)ChangeFinancerAccountingUnitBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class ChangeFinancerAccountingUnitBean implements Serializable {
    private Financer financer;
    private AccountingUnit accountingUnit;

    public ChangeFinancerAccountingUnitBean(final Financer financer, final AccountingUnit accountingUnit) {
	setFinancer(financer);
	setAccountingUnit(accountingUnit);
    }

    public void setFinancer(Financer financer) {
	this.financer = financer;
    }

    public Financer getFinancer() {
	return this.financer;
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
	this.accountingUnit = accountingUnit;
    }

    public AccountingUnit getAccountingUnit() {
	return this.accountingUnit;
    }

}
