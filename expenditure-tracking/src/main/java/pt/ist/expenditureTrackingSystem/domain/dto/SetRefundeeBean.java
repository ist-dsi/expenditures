/*
 * @(#)SetRefundeeBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class SetRefundeeBean implements Serializable {

	private Person refundee;
	private AcquisitionProcess acquisitionProcess;

	public SetRefundeeBean(final Person refundee) {
		setRefundee(refundee);
	}

	public SetRefundeeBean(final AcquisitionProcess acquisitionProcess) {
		this(acquisitionProcess.getAcquisitionRequest().getRefundee());
		setAcquisitionProcess(acquisitionProcess);
	}

	public Person getRefundee() {
		return refundee;
	}

	public void setRefundee(final Person refundee) {
		this.refundee = refundee;
	}

	public AcquisitionProcess getAcquisitionProcess() {
		return acquisitionProcess;
	}

	public void setAcquisitionProcess(final AcquisitionProcess acquisitionProcess) {
		this.acquisitionProcess = acquisitionProcess;
	}

}
