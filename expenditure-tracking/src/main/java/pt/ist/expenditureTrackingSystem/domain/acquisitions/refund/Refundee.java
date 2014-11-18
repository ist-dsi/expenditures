/*
 * @(#)Refundee.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class Refundee extends Refundee_Base {

    public Refundee(Person person) {
        super();
        setPerson(person);
    }

    public Refundee(String name, String fiscalCode) {
        super();
        setName(name);
        setFiscalCode(fiscalCode);
    }

    public boolean isInternalRefundee() {
        return hasPerson();
    }

    public String getRefundeePresentation() {
        Person person = getPerson();
        return person == null ? getName() + " (" + getFiscalCode() + ")" : person.getUser().getName() + " (" + person.getUsername() + ")";
    }

    public static Refundee getExternalRefundee(String name, String fiscalCode) {
        for (Refundee refundee : ExpenditureTrackingSystem.getInstance().getRefundees()) {
            if (refundee.getName().equalsIgnoreCase(name) && refundee.getFiscalCode().equals(fiscalCode)) {
                return refundee;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return hasPerson() ? getPerson().getUser().getName() : super.getName();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest> getRefundRequests() {
        return getRefundRequestsSet();
    }

    @Deprecated
    public boolean hasAnyRefundRequests() {
        return !getRefundRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasFiscalCode() {
        return getFiscalCode() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
