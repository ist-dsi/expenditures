/*
 * @(#)MigratePersonalInformationProcessedSlot.java
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

import java.util.ArrayList;
import java.util.Collection;

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
public class MigratePersonalInformationProcessedSlot {

    private static final Logger logger = LoggerFactory.getLogger(MigratePersonalInformationProcessedSlot.class);

    @Atomic
    public static void migrateForAllVirtualHosts() {
        for (VirtualHost vHost : MyOrg.getInstance().getVirtualHostsSet()) {
            MissionSystem system = vHost.getMissionSystem();
            if (system != null && !system.isPersonalInformationProcessedSlotMigrated()) {
                logger.info("Migrating Personal Information Processed Slot for VirtualHost: " + vHost.getHostname());
                migrate(system);
                system.setIsPersonalInformationProcessedSlotMigrated(true);
                logger.info("Finished migrating Personal Information Processed Slot for VirtualHost: " + vHost.getHostname());
            }
        }
    }

    private static void migrate(MissionSystem system) {
        for (MissionProcess process : system.getMissionProcessesSet()) {
            process.getMission().setIsPersonalInformationProcessed(wasPersonalInformationAlreadyProcessed(process));
        }
    }

    private static boolean wasPersonalInformationAlreadyProcessed(MissionProcess process) {
        if (!process.isPersonalInformationProcessingNeeded()) {
            if (!process.getCurrentQueuesSet().isEmpty()) {
                logger.warn("Process " + process.getProcessNumber()
                        + " does not need personal information processing, but is in a queue");
            }
            return false;
        }

        if (!wasInAllProcessParticipantInformationQueues(process)) {
            if (process.getQueueHistorySet().size() > 1) {
                logger.warn("Process " + process.getProcessNumber() + " has more than one queue in history");
            }
            return false;
        }

        if (isInAnyProcessParticipantInformationQueue(process)) {
            return false;
        }
        if (!process.areAllParticipantsAuthorized()) {
            return false;
        }

        if (process.getQueueHistorySet().size() == 0) {
            logger.warn("Process " + process.getProcessNumber() + " is already processed but has no queues in history");
        }
        if (process.hasAnyMissionItems() && !process.isAuthorized()) {
            logger.warn("Process " + process.getProcessNumber()
                    + " is already processed but not all mission items are authorized");
        }
        return true;
    }

    private static boolean isInAnyProcessParticipantInformationQueue(MissionProcess process) {
        Collection<WorkflowQueue> processParticipantInformationQueues =
                getAllProcessParticipantInformationQueues(process.getMissionSystem());

        for (WorkflowQueue currentQueue : process.getCurrentQueuesSet()) {
            if (processParticipantInformationQueues.contains(currentQueue)) {
                return true;
            }
        }
        return false;
    }

    public static Collection<WorkflowQueue> getAllProcessParticipantInformationQueues(MissionSystem system) {
        Collection<WorkflowQueue> processParticipantInformationQueues = new ArrayList<WorkflowQueue>();
        for (AccountabilityTypeQueue accTypeQueue : system.getAccountabilityTypeQueuesSet()) {
            processParticipantInformationQueues.add(accTypeQueue.getWorkflowQueue());
        }

        return processParticipantInformationQueues;
    }

    private static boolean wasInAllProcessParticipantInformationQueues(MissionProcess process) {
        return process.getQueueHistorySet().containsAll(process.getProcessParticipantInformationQueues());
    }
}
