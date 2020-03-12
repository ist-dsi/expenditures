/*
 * @(#)ApproveActivity.java
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
package module.internalrequest.domain.activity;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CancelActivity extends InternalRequestProcessActivity<InternalRequestProcess, CancelActivityInformation> {

    @Override
    public boolean isActive(final InternalRequestProcess internalRequestProcess, final User user) {
        return super.isActive(internalRequestProcess, user) && internalRequestProcess.getIsUnderConstruction()
                && (internalRequestProcess.getProcessCreator().equals(user) || RoleType.MANAGER.group().isMember(user));
    }

    @Override
    protected void process(final CancelActivityInformation activityInformation) {
        activityInformation.getProcess().cancel();
    }

    @Override
    public ActivityInformation<InternalRequestProcess> getActivityInformation(InternalRequestProcess process) {
        return new CancelActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(InternalRequestProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/InternalRequestResources", "activity.CancelActivity.info");
    }

}
