/*
 * @(#)MigrateVehicleItemAuthorizations.java
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

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.VehiclItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * 
 */
public class MigrateVehicleItemAuthorizations {

    private static final Logger logger = LoggerFactory.getLogger(MigrateVehicleItemAuthorizations.class);

    @Atomic
    public static void migrateForAllVirtualHosts() {
        VirtualHost originalVirtualHost = VirtualHost.getVirtualHostForThread();
        try {
            for (VirtualHost vHost : MyOrg.getInstance().getVirtualHostsSet()) {
                VirtualHost.setVirtualHostForThread(vHost);
                MissionSystem system = vHost.getMissionSystem();
                if (system != null && !system.isVehicleItemAuthorizationMigrated()) {
                    logger.info("Migrating Vehicle Item Authorization Slot for VirtualHost: " + vHost.getHostname());
                    migrate(system);
                    system.setIsVehicleItemAuthorizationMigrated(true);
                    logger.info("Finished migrating Vehicle Item Authorization Slot for VirtualHost: " + vHost.getHostname());
                }
            }
        } finally {
            VirtualHost.setVirtualHostForThread(originalVirtualHost);

        }
    }

    private static void migrate(MissionSystem system) {
        for (MissionProcess process : system.getMissionProcessesSet()) {
            if (process.getMission().hasAnyVehicleItems() && !areAllVehicleItemsAuthorized(process)
                    && wereVehiclesAlreadyAuthorized(process)) {
                authorizeAllVehicles(process);
            }
        }
    }

    private static boolean areAllVehicleItemsAuthorized(MissionProcess process) {
        for (VehiclItem vehicleItem : process.getMission().getVehicleItems()) {
            if (!vehicleItem.isAuthorized()) {
                return false;
            }
        }
        return true;
    }

    private static boolean wereVehiclesAlreadyAuthorized(MissionProcess process) {
        return process.areAllParticipantsAuthorized();
    }

    private static void authorizeAllVehicles(MissionProcess process) {
        for (VehiclItem vehicleItem : process.getMission().getVehicleItems()) {
            vehicleItem.authorize();
        }
    }
}
