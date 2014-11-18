/*
 * @(#)PaymentProcessInvoice.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class PaymentProcessInvoice extends PaymentProcessInvoice_Base {

    public PaymentProcessInvoice() {
        super();
    }

    @Override
    public void delete() {
        getUnitItems().clear();
        getRequestItems().clear();
        getProjectFinancers().clear();
        getFinancers().clear();
        super.delete();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem> getUnitItems() {
        return getUnitItemsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer> getProjectFinancers() {
        return getProjectFinancersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem> getRequestItems() {
        return getRequestItemsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer> getFinancers() {
        return getFinancersSet();
    }

    @Deprecated
    public boolean hasAnyUnitItems() {
        return !getUnitItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyProjectFinancers() {
        return !getProjectFinancersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRequestItems() {
        return !getRequestItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyFinancers() {
        return !getFinancersSet().isEmpty();
    }

}
