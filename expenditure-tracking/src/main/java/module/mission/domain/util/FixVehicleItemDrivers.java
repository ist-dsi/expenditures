/*
 * @(#)FillInVehicleItemDrivers.java
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

import module.mission.domain.Mission;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionSystem;
import module.mission.domain.VehiclItem;
import module.organization.domain.Person;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;

/**
 * 
 * @author João Neves
 * 
 */
public class FixVehicleItemDrivers extends WriteCustomTask {

    private static final boolean PERFORM_CHANGES = false;

    private static int noDriverVehicles = 0;
    private static int noDriverVehiclesFixable = 0;

    @Override
    public void doService() {
        for (VirtualHost vHost : MyOrg.getInstance().getVirtualHosts()) {
            try {
                VirtualHost.setVirtualHostForThread(vHost);
                fixVehicleItemDrivers();
            } finally {
                VirtualHost.releaseVirtualHostFromThread();
            }
        }

        out.println("Total Vehicles with no driver: " + noDriverVehicles);
        out.println("Total Vehicles with no driver that can be fixed (single-participant-item): " + noDriverVehiclesFixable);
    }

    private void fixVehicleItemDrivers() {
        for (MissionItem item : MissionSystem.getInstance().getMissionItems()) {
            if (item.isTemporary()) {
                continue;
            }
            if (item instanceof VehiclItem) {
                VehiclItem vehicle = (VehiclItem) item;
                Mission mission = vehicle.getMissionVersion().getMission();
                if (!vehicle.hasDriver()) {
                    noDriverVehicles++;
                    if (vehicle.getPeopleCount() == 1) {
                        noDriverVehiclesFixable++;
                        if (PERFORM_CHANGES) {
                            vehicle.setDriver(vehicle.getPeople().iterator().next());
                        }
                    } else if ((mission.getMissionResponsible() instanceof Person)
                            && (vehicle.getPeople().contains(mission.getMissionResponsible()))) {
                        noDriverVehiclesFixable++;
                        if (PERFORM_CHANGES) {
                            vehicle.setDriver((Person) mission.getMissionResponsible());
                        }
                    } else {
                        noDriverVehiclesFixable++;
                        if (PERFORM_CHANGES) {
                            vehicle.setDriver(vehicle.getPeople().iterator().next());
                        }
                    }
                }
            }
        }
    }
}
