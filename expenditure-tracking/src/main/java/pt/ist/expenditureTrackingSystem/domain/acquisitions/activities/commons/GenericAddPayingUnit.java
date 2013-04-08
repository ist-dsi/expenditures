/*
 * @(#)GenericAddPayingUnit.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author João Alfaiate
 * 
 */
public class GenericAddPayingUnit<P extends PaymentProcess> extends
        WorkflowActivity<P, GenericAddPayingUnitActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
        return isUserProcessOwner(process, user) && process.isInGenesis()
                && process.getRequestor() == user.getExpenditurePerson();
    }

    @Override
    protected void process(GenericAddPayingUnitActivityInformation<P> activityInformation) {
        activityInformation.getProcess().getRequest().addPayingUnit(activityInformation.getPayingUnit());
    }

    @Override
    public GenericAddPayingUnitActivityInformation<P> getActivityInformation(P process) {
        return new GenericAddPayingUnitActivityInformation<P>(process, this);
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

}
