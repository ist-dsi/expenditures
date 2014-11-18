/*
 * @(#)DeliveryInfo.java
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import module.finance.util.Address;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DeliveryInfo extends DeliveryInfo_Base {

    protected DeliveryInfo() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public DeliveryInfo(Person person, String recipient, Address address, String phone, String email) {
        checkParameters(person, recipient, address);
        setPerson(person);
        setRecipient(recipient);
        setAddress(address);
        setPhone(phone);
        setEmail(email);
    }

    private void checkParameters(Person person, String recipient, Address address) {
        if (person == null || recipient == null || address == null) {
            throw new DomainException(Bundle.EXPENDITURE, "deliveryInfo.message.exception.parametersCannotBeNull");
        }
    }

    @Deprecated
    public boolean hasRecipient() {
        return getRecipient() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasPhone() {
        return getPhone() != null;
    }

    @Deprecated
    public boolean hasEmail() {
        return getEmail() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
