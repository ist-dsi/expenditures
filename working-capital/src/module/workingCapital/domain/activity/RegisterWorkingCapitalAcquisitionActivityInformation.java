/*
 * @(#)RegisterWorkingCapitalAcquisitionActivityInformation.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import java.io.InputStream;

import module.finance.domain.Supplier;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.AcquisitionClassification;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class RegisterWorkingCapitalAcquisitionActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private Supplier supplier;
    private String documentNumber;
    private String description;
    private AcquisitionClassification acquisitionClassification;
    private Money money;
    private Money valueWithoutVat;
    private transient InputStream inputStream;
    private String filename;
    private String displayName;

    public RegisterWorkingCapitalAcquisitionActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && documentNumber != null && supplier != null && acquisitionClassification != null
		&& money != null && money.isPositive() && valueWithoutVat != null && valueWithoutVat.isPositive()
		&& description != null && !description.isEmpty() /** && getInputStream() != null **/;
    }

    public String getDocumentNumber() {
	return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
	this.documentNumber = documentNumber;
    }

    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
    }

    public AcquisitionClassification getAcquisitionClassification() {
	return acquisitionClassification;
    }

    public void setAcquisitionClassification(AcquisitionClassification acquisitionClassification) {
	this.acquisitionClassification = acquisitionClassification;
    }

    public Money getMoney() {
	return money;
    }

    public void setMoney(Money money) {
	this.money = money;
    }

    public Money getValueWithoutVat() {
	return valueWithoutVat;
    }

    public void setValueWithoutVat(Money valueWithoutVat) {
	this.valueWithoutVat = valueWithoutVat;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

}
