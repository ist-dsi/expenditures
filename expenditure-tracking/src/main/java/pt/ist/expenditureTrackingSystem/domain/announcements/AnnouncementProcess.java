/*
 * @(#)AnnouncementProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.announcement.AnnouncementProcessState;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

/*
 * TODO: This should be deleted
 */
@ClassNameBundle(bundle = "ExpenditureResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class AnnouncementProcess extends AnnouncementProcess_Base {

    public boolean isProcessInState(AnnouncementProcessStateType state) {
        return getAnnouncementProcessStateType().equals(state);
    }

    public AnnouncementProcessStateType getAnnouncementProcessState() {
        return getAnnouncementProcessStateType();
    }

    protected AnnouncementProcessState getLastAnnouncementProcessState() {
        return (AnnouncementProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public AnnouncementProcessStateType getAnnouncementProcessStateType() {
        return getLastAnnouncementProcessState().getAnnouncementProcessStateType();
    }

    @Atomic
    public static AnnouncementProcess createNewAnnouncementProcess(Person publisher, AnnouncementBean announcementBean) {
        if (!isCreateNewProcessAvailable()) {
            throw new DomainException(Bundle.EXPENDITURE, "announcementProcess.message.exception.invalidStateToRun.create");
        }

        announcementBean.setBuyingUnit(ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet().iterator().next());
        return new AnnouncementProcess(publisher, announcementBean);
    }

    private AnnouncementProcess(final Person publisher, AnnouncementBean announcementBean) {
        super();
        // new AnnouncementProcessState(this,
        // AnnouncementProcessStateType.IN_GENESIS);
        // new Announcement(this, publisher, announcementBean);
    }

    @Override
    public boolean hasAnyAvailableActivitity() {
        return false;
    }

    public String getRejectionJustification() {
        if (getLastAnnouncementProcessState().getAnnouncementProcessStateType().equals(AnnouncementProcessStateType.REJECTED)) {
            return getLastAnnouncementProcessState().getJustification();
        }
        return null;
    }

    public boolean isVisible(Person person) {
        final User user = person == null ? null : person.getUser();
        return getLastAnnouncementProcessState().equals(AnnouncementProcessStateType.APPROVED)
                || getAnnouncement().getPublisher() == person || ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                || ExpenditureTrackingSystem.isAcquisitionCentralManagerGroupMember(user);
    }

    @Override
    public <T extends ActivityLog> T logExecution(User user, String operationName, String... args) {
        return (T) new OperationLog(this, user, operationName, getAnnouncementProcessStateType());
    }

    @Override
    public User getProcessCreator() {
        return getAnnouncement().getPublisher().getUser();
    }

    @Override
    public void notifyUserDueToComment(User user, String comment) {
        // no nothing
    }

    @Override
    public boolean isAccessible(User user) {
        return user == getProcessCreator();
    }

    /*
     * TODO: Implement this methods correctly
     */
    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return Collections.EMPTY_LIST;
    }

    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> Stream<T> getActivityStream() {
        return Stream.empty();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Deprecated
    public boolean hasAnnouncement() {
        return getAnnouncement() != null;
    }

}
