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

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ImportFile extends ImportFile_Base {

    public ImportFile(byte[] bytes, String displayName) {
        super();
        setContent(bytes);
        setDisplayName(displayName);
        setActive(Boolean.TRUE);
    }

    @Override
    @Service
    public void delete() {
        if (getAfterTheFactAcquisitionProcessesCount() > 0) {
            throw new DomainException("exception.domain.ImportFile.cannotDeleteImportFileWithProcesses");
        }

        deleteDomainObject();
    }

    @Service
    public void cancel() {
        if (getAfterTheFactAcquisitionProcessesCount() == 0) {
            delete();

        } else {
            for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
                process.cancel();
            }
            setActive(Boolean.FALSE);
        }
    }

    @Service
    public void reenable() {
        setActive(Boolean.TRUE);
        for (AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
            process.renable();
        }

    }

    @Override
    public boolean isConnectedToCurrentHost() {
        for (final AfterTheFactAcquisitionProcess process : getAfterTheFactAcquisitionProcesses()) {
            return process != null && process.isConnectedToCurrentHost();
        }
        return false;
    }

}
