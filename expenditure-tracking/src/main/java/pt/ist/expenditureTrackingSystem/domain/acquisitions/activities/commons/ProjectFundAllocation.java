/*
 * @(#)ProjectFundAllocation.java
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

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ProjectFundAllocation<P extends PaymentProcess> extends
        WorkflowActivity<P, ProjectFundAllocationActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
        Person person = user.getExpenditurePerson();
        return (process.isProjectAccountingEmployee(person) || process.isAccountingEmployee(person))
                && isUserProcessOwner(process, user)
                && process.isPendingFundAllocation() && !process.hasAllocatedFundsForAllProjectFinancers(person)
                && (!process.hasMissionProcess() || process.getMissionProcess().hasAllAllocatedProjectFunds());
    }

    @Override
    public ProjectFundAllocationActivityInformation<P> getActivityInformation(P process) {
        return getActivityInformation(process, true);
    }

    protected boolean requiresPriorFundAllocation() {
        final ExpenditureTrackingSystem instance = ExpenditureTrackingSystem.getInstance();
        final Boolean b = instance.getRequireFundAllocationPriorToAcquisitionRequest();
        return b != null && b.booleanValue();
    }

    @Override
    protected void process(ProjectFundAllocationActivityInformation<P> activityInformation) {
        for (FundAllocationBean fundAllocationBean : activityInformation.getBeans()) {
            final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
            projectFinancer.setProjectFundAllocationId(fundAllocationBean.getFundAllocationId());
        }

        final P process = activityInformation.getProcess();
        if (!requiresPriorFundAllocation() && process.hasAllocatedFundsForAllProjectFinancers()) {
            process.allocateFundsToUnit();
        }

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

    public ProjectFundAllocationActivityInformation<P> getActivityInformation(P process, boolean takeProcess) {
        return new ProjectFundAllocationActivityInformation<P>(process, this, takeProcess);
    }

    @Override
    public boolean isUserAwarenessNeeded(P process, User user) {
        return isVisible(process, user);
    }

}
