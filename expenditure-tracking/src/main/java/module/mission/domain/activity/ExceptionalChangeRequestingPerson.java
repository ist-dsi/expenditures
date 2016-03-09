/*
 * @(#)ExceptionalChangeRequestingPerson.java
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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.presentationTier.actions.CommentBean;
import pt.ist.expenditureTrackingSystem.domain.RoleType;

/**
 * 
 * @author João Neves
 * 
 */
public class ExceptionalChangeRequestingPerson
        extends MissionProcessActivity<MissionProcess, ExceptionalChangeRequestingPersonInfo> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return RoleType.MANAGER.group().isMember(user);
    }

    @Override
    protected void process(final ExceptionalChangeRequestingPersonInfo activityInformation) {
        MissionProcess process = activityInformation.getProcess();
        process.getMission().setRequestingPerson(activityInformation.getRequester());

        CommentBean commentBean = new CommentBean(process);
        commentBean.setComment(activityInformation.getComment());
        process.createComment(Authenticate.getUser(), commentBean);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ExceptionalChangeRequestingPersonInfo(process, this);
    }

    @Override
    public String getUsedBundle() {
        return "resources/MissionResources";
    }

    @Override
    public boolean isUserAwarenessNeeded(MissionProcess process) {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(MissionProcess process) {
        return true;
    }

    @Override
    protected String[] getArgumentsDescription(ExceptionalChangeRequestingPersonInfo activityInformation) {
        Person oldRequester = activityInformation.getProcess().getMission().getRequestingPerson();
        final User user = activityInformation.getRequester().getUser();
        return new String[] { (oldRequester == null) ? "-" : oldRequester.getPresentationName(),
                user.getDisplayName() + "(" + user.getUsername() + ")" };
    }
}
