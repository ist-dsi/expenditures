/*
 * @(#)PayingUnitTotalBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class PayingUnitTotalBean implements Serializable {

    Financer financer;
    Money amount;

    public PayingUnitTotalBean(Financer financer) {
        setFinancer(financer);
        setAmount(financer.getAmountAllocated());
    }

    public Unit getPayingUnit() {
        return getFinancer().getUnit();
    }

    public Financer getFinancer() {
        return financer;
    }

    public void setFinancer(Financer financer) {
        this.financer = financer;
    }

    public Acquisition getRequest() {
        return getFinancer().getFundedRequest();
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PayingUnitTotalBean && ((PayingUnitTotalBean) obj).getPayingUnit() == getPayingUnit()
                && ((PayingUnitTotalBean) obj).getRequest() == getRequest()
                && ((PayingUnitTotalBean) obj).getAmount().equals(getAmount());
    }

    @Override
    public int hashCode() {
        return getPayingUnit().hashCode() + getRequest().hashCode();
    }
}
