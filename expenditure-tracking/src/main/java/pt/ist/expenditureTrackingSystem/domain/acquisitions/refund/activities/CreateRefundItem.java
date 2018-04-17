/*
 * @(#)CreateRefundItem.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class CreateRefundItem extends WorkflowActivity<RefundProcess, CreateRefundItemActivityInformation> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
        return process.getRequestor() == user.getExpenditurePerson() && isUserProcessOwner(process, user) && process.isInGenesis()
                && ExpenditureTrackingSystem.getInstance().getMaterialsSet().isEmpty();
    }

    @Override
    protected void process(CreateRefundItemActivityInformation activityInformation) {
        activityInformation.getProcess().getRequest().createRefundItem(activityInformation.getValueEstimation().round(),
                activityInformation.getCPVReference(), activityInformation.getClassification(),
                activityInformation.getDescription(), activityInformation.getRefundItemNature());
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
        return new CreateRefundItemActivityInformation(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }
}
