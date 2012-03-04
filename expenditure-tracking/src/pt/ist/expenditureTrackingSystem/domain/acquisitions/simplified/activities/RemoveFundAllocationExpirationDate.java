/*
 * @(#)RemoveFundAllocationExpirationDate.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem._development.ExternalIntegration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class RemoveFundAllocationExpirationDate extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    private boolean checkActiveConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isInAllocatedToSupplierState();
    }

    private boolean checkCanceledConditions(RegularAcquisitionProcess process) {
	return process.getAcquisitionProcessState().isCanceled();
    }

    private boolean hasAnyAssociatedProject(final RegularAcquisitionProcess process) {
	for (final Financer financer : process.getAcquisitionRequest().getFinancersSet()) {
	    if (financer.isProjectFinancer()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& (checkActiveConditions(process) || checkCanceledConditions(process))
		&& !process.hasAnyAllocatedFunds()
		&& ((process.isAccountingEmployee(person) && !hasAnyAssociatedProject(process))
			|| process.isProjectAccountingEmployee(person)
			|| ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user))
		&& ((!process.getShouldSkipSupplierFundAllocation() && process.getFundAllocationExpirationDate() != null) || (process
			.getShouldSkipSupplierFundAllocation() && process.isPendingFundAllocation()));
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	RegularAcquisitionProcess process = activityInformation.getProcess();
	process.removeFundAllocationExpirationDate();
	process.getRequest().unSubmitForFundsAllocation();
	if (!process.getAcquisitionProcessState().isCanceled()) {
	    process.submitForApproval();
	}

	if (ExternalIntegration.isActive()) {
	    process.cancelFundAllocationRequest(false);
	}
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
