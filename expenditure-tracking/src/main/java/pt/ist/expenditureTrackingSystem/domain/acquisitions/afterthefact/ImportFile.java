/*
 * @(#)ImportFile.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ImportFile extends ImportFile_Base {

    public ImportFile(byte[] bytes, String displayName) {
        super();
        init(displayName, displayName, bytes);
        setActive(Boolean.TRUE);
    }

    @Override
    @Atomic
    public void delete() {
        if (getAfterTheFactAcquisitionProcessesSet().size() > 0) {
            throw new DomainException(Bundle.EXPENDITURE, "exception.domain.ImportFile.cannotDeleteImportFileWithProcesses");
        }

        deleteDomainObject();
    }

    @Atomic
    public void cancel() {
        if (getAfterTheFactAcquisitionProcessesSet().size() == 0) {
            delete();

        } else {
            for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
                process.cancel();
            }
            setActive(Boolean.FALSE);
        }
    }

    @Atomic
    public void reenable() {
        setActive(Boolean.TRUE);
        for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
            process.renable();
        }

    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess> getAfterTheFactAcquisitionProcesses() {
        return getAfterTheFactAcquisitionProcessesSet();
    }

    @Deprecated
    public boolean hasAnyAfterTheFactAcquisitionProcesses() {
        return !getAfterTheFactAcquisitionProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Override
    public boolean isAccessible(User user) {
        // TODO Auto-generated method stub
        return false;
    }

}
