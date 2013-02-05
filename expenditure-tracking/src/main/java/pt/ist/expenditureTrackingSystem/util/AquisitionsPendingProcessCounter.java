/*
 * @(#)AquisitionsPendingProcessCounter.java
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
package pt.ist.expenditureTrackingSystem.util;

import module.workflow.domain.ProcessCounter;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.standard.StandardProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AquisitionsPendingProcessCounter extends ProcessCounter {

    public AquisitionsPendingProcessCounter() {
        super(AcquisitionProcess.class);
    }

    @Override
    public Class getProcessClassForForwarding() {
        return StandardProcedureProcess.class;
    }

    @Override
    public int getCount() {
        int result = 0;
        final User user = UserView.getCurrentUser();

        try {
            for (final Acquisition acquisition : ExpenditureTrackingSystem.getInstance().getAcquisitionsSet()) {
                if (acquisition instanceof AcquisitionRequest) {
                    final AcquisitionRequest acquisitionRequest = (AcquisitionRequest) acquisition;
                    final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                    if (shouldCountProcess(acquisitionProcess, user)) {
                        result++;
                    }
                }
            }
        } catch (final Throwable t) {
            t.printStackTrace();
            //throw new Error(t);
        }
        return result;
    }

}
