/*
 * @(#)AbstractDistributeRealValuesForPayingUnits.java
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
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public abstract class AbstractDistributeRealValuesForPayingUnits<P extends PaymentProcess> extends
	WorkflowActivity<P, AbstractDistributeRealValuesForPayingUnitsActivityInformation<P>> {

    @Override
    protected void process(AbstractDistributeRealValuesForPayingUnitsActivityInformation<P> activityInformation) {
	List<UnitItemBean> beans = activityInformation.getBeans();
	RequestItem item = activityInformation.getItem();
	Money amount = Money.ZERO;

	item.clearRealShareValues();

	for (UnitItemBean bean : beans) {
	    Money share = bean.getRealShareValue();
	    if (share == null) {
		throw new DomainException("activities.message.exception.monetaryValueMustBeFilled", getName());
	    }
	    amount = amount.add(share);
	    item.getUnitItemFor(bean.getUnit()).setRealShareValue(share);
	}

    }

    public AbstractDistributeRealValuesForPayingUnitsActivityInformation<P> getActivityInformation(P process) {
	return new AbstractDistributeRealValuesForPayingUnitsActivityInformation<P>(process, this);
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
