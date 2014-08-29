/*
 * @(#)FundAllocation.java
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
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class FundAllocation<P extends PaymentProcess> extends WorkflowActivity<P, FundAllocationActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
        Person person = user.getExpenditurePerson();
        return process.isActive() && isUserProcessOwner(process, user) && process.isPendingFundAllocation()
                && process.isAccountingEmployee(person) && process.hasAllocatedFundsForAllProjectFinancers()
                && !process.hasAllFundAllocationId(person)
                && (!process.hasMissionProcess() || process.getMissionProcess().hasAllAllocatedFunds());
    }

    @Override
    public FundAllocationActivityInformation<P> getActivityInformation(P process) {
        return new FundAllocationActivityInformation<P>(process, this, true);
    }

    @Override
    protected void process(FundAllocationActivityInformation<P> activityInformation) {
        P process = activityInformation.getProcess();
        for (FundAllocationBean fundAllocationBean : activityInformation.getBeans()) {
            fundAllocationBean.getFinancer().setFundAllocationId(fundAllocationBean.getFundAllocationId());
        }
        if (process.getRequest().hasAllFundAllocationId()) {
            process.allocateFundsToUnit();
        }

    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
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
