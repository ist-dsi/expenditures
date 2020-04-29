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

import module.finance.util.Money;
import module.internalrequest.domain.InternalRequestItem;
import org.fenixedu.bennu.core.domain.User;

import module.internalrequest.domain.InternalRequestProcess;
import module.internalrequest.domain.util.InternalRequestState;
import module.workflow.activities.ActivityInformation;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class BudgetItemActivity extends InternalRequestProcessActivity<InternalRequestProcess, BudgetItemActivityInformation> {

    @Override
    public boolean isActive(final InternalRequestProcess internalRequestProcess, final User user) {
        return super.isActive(internalRequestProcess, user) && InternalRequestState.BUDGETING.isPending(internalRequestProcess)
                && internalRequestProcess.canSubmitBudget(user.getPerson())
                ;
    }

    @Override
    protected void process(final BudgetItemActivityInformation activityInformation) {
        final InternalRequestItem item = activityInformation.getItem();
        Money totalPrice = activityInformation.getPrice();
        if(activityInformation.isUnitPrice()) {
            totalPrice = totalPrice.multiply(item.getQuantity());
        }
        item.budget(totalPrice, activityInformation.getObservations());
    }

    @Override
    public ActivityInformation<InternalRequestProcess> getActivityInformation(InternalRequestProcess process) {
        return new BudgetItemActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
