/*
 * @(#)MigrateVerifiedSlot.java
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
package module.mission.domain.util;

import module.mission.domain.AccountabilityTypeQueue;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.workflow.domain.WorkflowQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * This should take about 2 minutes to run.
 * 
 * @author João Neves
 * 
 */
public class MigrateVerifiedSlot {

    private static final Logger logger = LoggerFactory.getLogger(MigrateVerifiedSlot.class);

    @Atomic
    public static void migrateForAllVirtualHosts() {
        for (VirtualHost vHost : MyOrg.getInstance().getVirtualHostsSet()) {
            MissionSystem system = vHost.getMissionSystem();
            if (system != null && !system.isVerifiedSlotMigrated()) {
                if (system.getMissionProcessesSet().isEmpty()) {
                    logger.info("Migrating Verified Slot for VirtualHost: " + vHost.getHostname());
                    system.setIsVerifiedSlotMigrated(true);
                    logger.info("Finished migrating Verified Slot for VirtualHost: " + vHost.getHostname());
                } else if (system.getVerificationQueue() == null) {
                    logger.warn("Cannot migrate Verified Slot for VirtualHost: "
                            + system.getVirtualHostSet().iterator().next().getHostname() + " - NO VERIFICATION QUEUE CONFIGURED!");
                } else {
                    logger.info("Migrating Verified Slot for VirtualHost: " + vHost.getHostname());
                    migrate(system);
                    system.setIsVerifiedSlotMigrated(true);
                    logger.info("Finished migrating Verified Slot for VirtualHost: " + vHost.getHostname());
                }
            }
        }
    }

    private static void migrate(MissionSystem system) {
        for (MissionProcess process : system.getMissionProcessesSet()) {
            process.getMission().setIsVerified(wasProcessAlreadyVerified(process));
            if (MissionState.VERIFICATION.isPending(process)) {
                process.addToVerificationQueue();
            }
        }
    }

    private static boolean wasProcessAlreadyVerified(MissionProcess process) {
        return process.areAllParticipantsAuthorized();
    }
}
