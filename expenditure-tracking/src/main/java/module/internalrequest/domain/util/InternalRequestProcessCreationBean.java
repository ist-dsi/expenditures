/*
 * @(#)InternalRequestProcessCreationBean.java
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
package module.internalrequest.domain.util;

import java.io.Serializable;

import org.fenixedu.bennu.core.security.Authenticate;

import module.internalrequest.domain.InternalRequestProcess;
import module.organization.domain.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class InternalRequestProcessCreationBean implements Serializable {

    private Unit requestingUnit;
    private Unit requestedUnit;

    @Atomic
    public InternalRequestProcess createNewInternalRequestProcess() {
        Person requester = Authenticate.getUser().getPerson();
        return new InternalRequestProcess(requester, requestingUnit, requestedUnit);
    }

    public Unit getRequestingUnit() {
        return requestingUnit;
    }

    public void setRequestingUnit(Unit requestingUnit) {
        this.requestingUnit = requestingUnit;
    }

    public Unit getRequestedUnit() {
        return requestedUnit;
    }

    public void setRequestedUnit(Unit requestedUnit) {
        this.requestedUnit = requestedUnit;
    }

}
