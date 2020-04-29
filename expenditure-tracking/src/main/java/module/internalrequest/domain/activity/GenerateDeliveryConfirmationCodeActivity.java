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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestState;
import module.workflow.activities.ActivityInformation;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class GenerateDeliveryConfirmationCodeActivity
        extends InternalRequestProcessActivity<InternalRequestProcess, GenerateDeliveryConfirmationCodeActivityInformation> {

    @Override
    public boolean isActive(final InternalRequestProcess internalRequestProcess, final User user) {
        return super.isActive(internalRequestProcess, user) && InternalRequestState.DELIVERY.isPending(internalRequestProcess)
                && internalRequestProcess.getInternalRequest().getRequestingPerson().getUser().equals(user);
    }

    @Override
    protected void process(final GenerateDeliveryConfirmationCodeActivityInformation activityInformation) {
        activityInformation.getProcess().generateDeliveryConfirmationCode();
    }

    @Override
    public ActivityInformation<InternalRequestProcess> getActivityInformation(InternalRequestProcess process) {
        return new GenerateDeliveryConfirmationCodeActivityInformation(process, this);
    }

    @Override
    public boolean isConfirmationNeeded(InternalRequestProcess process) {
        return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
        return BundleUtil.getString("resources/InternalRequestResources",
                "activity.GenerateDeliveryConfirmationCodeActivity.info");
    }

}
