/*
 * @(#)UnitItemBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class UnitItemBean implements Serializable {

	private Unit unit;
	private RequestItem item;
	private Money shareValue;
	private Money realShareValue;
	private Boolean approved;
	private Boolean assigned;

	public UnitItemBean(UnitItem unitItem) {
		setUnit(unitItem.getUnit());
		setItem(unitItem.getItem());
		setShareValue(unitItem.getShareValue());
		setRealShareValue(unitItem.getRealShareValue());
		setAssigned(Boolean.TRUE);
	}

	public UnitItemBean(Unit unit, RequestItem item) {
		setItem(item);
		setUnit(unit);
		setAssigned(Boolean.FALSE);

		UnitItem unitItem = item.getUnitItemFor(unit);
		if (unitItem != null) {
			setAssigned(Boolean.TRUE);
			setApproved(unitItem.getItemAuthorized());
			setShareValue(unitItem.getShareValue());
			setRealShareValue(unitItem.getRealShareValue());
		}
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public RequestItem getItem() {
		return item;
	}

	public void setItem(RequestItem item) {
		this.item = item;
	}

	public Money getShareValue() {
		return shareValue;
	}

	public void setShareValue(Money shareValue) {
		this.shareValue = shareValue;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean isApproved) {
		this.approved = isApproved;
	}

	public Boolean getAssigned() {
		return assigned;
	}

	public void setAssigned(Boolean assigned) {
		this.assigned = assigned;
	}

	public Money getRealShareValue() {
		return realShareValue;
	}

	public void setRealShareValue(Money realShareValue) {
		this.realShareValue = realShareValue;
	}
}
