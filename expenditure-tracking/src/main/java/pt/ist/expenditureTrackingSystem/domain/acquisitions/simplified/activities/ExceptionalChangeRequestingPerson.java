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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.presentationTier.actions.CommentBean;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author João Neves
 * 
 */
public class ExceptionalChangeRequestingPerson extends
	WorkflowActivity<RegularAcquisitionProcess, ExceptionalChangeRequestingPersonInfo> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final RegularAcquisitionProcess process, final User user) {
	return user.hasRoleType(RoleType.MANAGER);
    }

    @Override
    protected void process(final ExceptionalChangeRequestingPersonInfo activityInformation) {
	PaymentProcess process = activityInformation.getProcess();
	process.getRequest().setRequester(activityInformation.getRequester().getUser().getExpenditurePerson());

	CommentBean commentBean = new CommentBean(process);
	commentBean.setComment(activityInformation.getComment());
	process.createComment(UserView.getCurrentUser(), commentBean);
    }

    @Override
    public ActivityInformation<RegularAcquisitionProcess> getActivityInformation(final RegularAcquisitionProcess process) {
	return new ExceptionalChangeRequestingPersonInfo(process, this);
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
	return true;
    }

    @Override
    protected String[] getArgumentsDescription(ExceptionalChangeRequestingPersonInfo activityInformation) {
	Person oldRequester = activityInformation.getProcess().getRequestor();
	return new String[] { (oldRequester == null) ? "-" : oldRequester.getUser().getPresentationName(),
		activityInformation.getRequester().getUser().getPresentationName() };
    }

    @Override
    public boolean isUserAwarenessNeeded(RegularAcquisitionProcess process, User user) {
	return false;
    }

}
