/*
 * @(#)CancelProcessActivity.java
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
package module.mission.domain.activity;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.workflow.activities.ActivityInformation;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CancelProcessActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user)
                && !missionProcess.isCanceled()
                && (missionProcess.isRequestor(user) || RoleType.MANAGER.group().isMember(user) || MissionSystem.getInstance()
                        .getUsersWhoCanCancelMissionSet().contains(user)) && !missionProcess.isPersonalInformationProcessed();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
        final MissionProcess missionProcess = activityInformation.getProcess();

        if (hasConnectedPaymentProcess(missionProcess)) {
            final String processes = getConnectedPaymentProcess(missionProcess);
            throw new DomainException(Bundle.MISSION, "error.mission.has.active.payment.processes.cannot.cancel", processes);
        }

        missionProcess.cancel();
        // missionProcess.addToProcessParticipantInformationQueues();
    }

    private boolean hasConnectedPaymentProcess(final MissionProcess missionProcess) {
        for (final PaymentProcess paymentProcess : missionProcess.getPaymentProcessSet()) {
            if (!paymentProcess.isCanceled()) {
                return true;
            }
        }
        return false;
    }

    private String getConnectedPaymentProcess(final MissionProcess missionProcess) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final PaymentProcess paymentProcess : missionProcess.getPaymentProcessSet()) {
            if (!paymentProcess.isCanceled()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(paymentProcess.getAcquisitionProcessId());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ActivityInformation<MissionProcess>(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(MissionProcess process, User user) {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(final MissionProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/MissionResources", "label.module.mission.cancel.process.confirm");
    }

    @Override
    public String getUsedBundle() {
        return "resources/MissionResources";
    }

}
