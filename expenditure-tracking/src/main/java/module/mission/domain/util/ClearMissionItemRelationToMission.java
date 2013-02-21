/*
 * @(#)ClearMissionItemRelationToMission.java
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

import module.mission.domain.MissionItem;
import module.mission.domain.MissionSystem;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;

/**
 * 
 * @author João Neves
 * 
 */
public class ClearMissionItemRelationToMission extends WriteCustomTask {

    private static final boolean PERFORM_CHANGES = false;

    private static int missionItemsWithMission = 0;
    private static int inconsistentMissionItemsWithMission = 0;

    @Override
    public void doService() {
        for (VirtualHost vHost : MyOrg.getInstance().getVirtualHosts()) {
            try {
                VirtualHost.setVirtualHostForThread(vHost);
                clearMissionItemRelationToMission();
            } finally {
                VirtualHost.releaseVirtualHostFromThread();
            }
        }

        out.println("Total items with mission: " + missionItemsWithMission);
        out.println("INCONSISTENT items with mission: " + inconsistentMissionItemsWithMission);
    }

    private void clearMissionItemRelationToMission() {
        for (MissionItem item : MissionSystem.getInstance().getMissionItems()) {
            if (item.hasMissionForReal()) {
                missionItemsWithMission++;
                if (!item.getMissionForReal().equals(item.getMissionVersion().getMission())) {
                    inconsistentMissionItemsWithMission++;
                } else {
                    if (PERFORM_CHANGES) {
                        item.removeMission();
                    }
                }
            }
        }
    }
}
