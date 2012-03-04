/*
 * @(#)GenericAssignPayingUnitToItem.java
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

import java.util.List;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class GenericAssignPayingUnitToItem<P extends PaymentProcess> extends
	WorkflowActivity<P, GenericAssignPayingUnitToItemActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& ((person == process.getRequestor() && process.isInGenesis()) || ((process instanceof SimplifiedProcedureProcess)
			&& ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000
			&& ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
			&& process.isAuthorized()));
    }

    @Override
    protected void process(GenericAssignPayingUnitToItemActivityInformation<P> activityInformation) {
	RequestItem item = activityInformation.getItem();
	List<UnitItemBean> beans = activityInformation.getBeans();

	for (; !item.getUnitItems().isEmpty(); item.getUnitItems().get(0).delete())
	    ;

	for (UnitItemBean bean : beans) {
	    if (bean.getAssigned()) {
		item.createUnitItem(bean.getUnit(), bean.getShareValue());
	    }
	}
    }

    public GenericAssignPayingUnitToItemActivityInformation<P> getActivityInformation(P process) {
	return new GenericAssignPayingUnitToItemActivityInformation<P>(process, this);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }
}
