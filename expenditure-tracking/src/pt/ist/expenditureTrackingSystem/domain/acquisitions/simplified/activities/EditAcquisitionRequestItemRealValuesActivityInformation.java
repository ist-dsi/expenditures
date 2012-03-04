/*
 * @(#)EditAcquisitionRequestItemRealValuesActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class EditAcquisitionRequestItemRealValuesActivityInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private AcquisitionRequestItem item;
    private Integer realQuantity;
    private Money realUnitValue;
    private Money shipment;
    private BigDecimal realVatValue;

    public EditAcquisitionRequestItemRealValuesActivityInformation(RegularAcquisitionProcess process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    public AcquisitionRequestItem getItem() {
	return item;
    }

    public void setItem(AcquisitionRequestItem item) {
	this.item = item;
	setRealQuantity(item.getRealQuantity());
	setRealUnitValue(item.getRealUnitValue());
	setShipment(item.getRealAdditionalCostValue());
	setRealVatValue(item.getRealVatValue());
    }

    public Integer getRealQuantity() {
	return realQuantity;
    }

    public void setRealQuantity(Integer realQuantity) {
	this.realQuantity = realQuantity;
    }

    public Money getRealUnitValue() {
	return realUnitValue;
    }

    public void setRealUnitValue(Money realUnitValue) {
	this.realUnitValue = realUnitValue;
    }

    public Money getShipment() {
	return shipment;
    }

    public void setShipment(Money shipment) {
	this.shipment = shipment;
    }

    public BigDecimal getRealVatValue() {
	return realVatValue;
    }

    public void setRealVatValue(BigDecimal realVatValue) {
	this.realVatValue = realVatValue;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getItem() != null && getRealQuantity() != null && getRealUnitValue() != null
		&& getRealVatValue() != null;
    }

}
