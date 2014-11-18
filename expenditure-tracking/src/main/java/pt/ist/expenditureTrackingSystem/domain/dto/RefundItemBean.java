/*
 * @(#)RefundItemBean.java
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

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class RefundItemBean implements Serializable {

    private Money valueEstimation;
    private CPVReference reference;
    private String description;

    public RefundItemBean() {
        setCPVReference(null);
    }

    public RefundItemBean(RefundItem item) {
        setValueEstimation(item.getValueEstimation());
        setCPVReference(item.getCPVReference());
        setDescription(item.getDescription());
    }

    public Money getValueEstimation() {
        return valueEstimation;
    }

    public void setValueEstimation(Money valueEstimation) {
        this.valueEstimation = valueEstimation;
    }

    public CPVReference getCPVReference() {
        return reference;
    }

    public void setCPVReference(CPVReference reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
