/*
 * @(#)EditAfterTheFactProcessActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import java.io.Serializable;
import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.util.Money;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * 
 */
public class EditAfterTheFactProcessActivityInformation extends ActivityInformation<AfterTheFactAcquisitionProcess> {

    public static class AfterTheFactAcquisitionProcessBean implements Serializable {
	private AfterTheFactAcquisitionType afterTheFactAcquisitionType;
	private Supplier supplier;
	private Money value;
	private BigDecimal vatValue;
	private AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess;
	private String description;
	private Integer year = Integer.valueOf(new LocalDate().getYear());

	public AfterTheFactAcquisitionProcessBean() {

	}

	public AfterTheFactAcquisitionProcessBean(final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
	    setAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcess);
	    final AcquisitionAfterTheFact acquisitionAfterTheFact = afterTheFactAcquisitionProcess.getAcquisitionAfterTheFact();
	    setAfterTheFactAcquisitionType(acquisitionAfterTheFact.getAfterTheFactAcquisitionType());
	    setSupplier(acquisitionAfterTheFact.getSupplier());
	    setValue(acquisitionAfterTheFact.getValue());
	    setVatValue(acquisitionAfterTheFact.getVatValue());
	    setDescription(acquisitionAfterTheFact.getDescription());
	    setYear(acquisitionAfterTheFact.getAfterTheFactAcquisitionProcess().getYear());
	}

	public void setSupplier(Supplier supplier) {
	    this.supplier = supplier;
	}

	public Supplier getSupplier() {
	    return supplier;
	}

	public Money getValue() {
	    return value;
	}

	public void setValue(Money value) {
	    this.value = value;
	}

	public BigDecimal getVatValue() {
	    return vatValue;
	}

	public void setVatValue(BigDecimal vatValue) {
	    this.vatValue = vatValue;
	}

	public AfterTheFactAcquisitionType getAfterTheFactAcquisitionType() {
	    return afterTheFactAcquisitionType;
	}

	public void setAfterTheFactAcquisitionType(AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
	    this.afterTheFactAcquisitionType = afterTheFactAcquisitionType;
	}

	public AfterTheFactAcquisitionProcess getAfterTheFactAcquisitionProcess() {
	    return afterTheFactAcquisitionProcess;
	}

	public void setAfterTheFactAcquisitionProcess(AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
	    this.afterTheFactAcquisitionProcess = afterTheFactAcquisitionProcess;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public Integer getYear() {
	    return year;
	}

	public void setYear(Integer year) {
	    this.year = year;
	}
    }

    private final AfterTheFactAcquisitionProcessBean bean;

    public EditAfterTheFactProcessActivityInformation(AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(afterTheFactAcquisitionProcess, activity);
	bean = new AfterTheFactAcquisitionProcessBean(afterTheFactAcquisitionProcess);
    }

    public void setSupplier(Supplier supplier) {
	this.bean.setSupplier(supplier);
    }

    public Supplier getSupplier() {
	return this.bean.getSupplier();
    }

    public Money getValue() {
	return this.bean.getValue();
    }

    public void setValue(Money value) {
	this.bean.setValue(value);
    }

    public BigDecimal getVatValue() {
	return this.bean.getVatValue();
    }

    public void setVatValue(BigDecimal vatValue) {
	this.bean.setVatValue(vatValue);
    }

    public AfterTheFactAcquisitionType getAfterTheFactAcquisitionType() {
	return this.bean.getAfterTheFactAcquisitionType();
    }

    public void setAfterTheFactAcquisitionType(AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
	this.bean.setAfterTheFactAcquisitionType(afterTheFactAcquisitionType);
    }

    public AfterTheFactAcquisitionProcess getAfterTheFactAcquisitionProcess() {
	return this.bean.getAfterTheFactAcquisitionProcess();
    }

    public void setAfterTheFactAcquisitionProcess(AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
	this.bean.setAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcess);
    }

    public String getDescription() {
	return this.bean.getDescription();
    }

    public void setDescription(String description) {
	this.bean.setDescription(description);
    }

    public Integer getYear() {
	return this.bean.getYear();
    }

    public void setYear(Integer year) {
	this.bean.setYear(year);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getYear() != null && getAfterTheFactAcquisitionType() != null && getSupplier() != null
		&& getValue() != null && getVatValue() != null;
    }

}
