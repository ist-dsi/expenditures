/*
 * @(#)CreateRefundItemActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class CreateRefundItemActivityInformation extends ActivityInformation<RefundProcess> {

    private Money valueEstimation;
    private CPVReference reference;
    private Material material;
    private String description;
    private AcquisitionItemClassification classification;

    public CreateRefundItemActivityInformation(RefundProcess refundProcess,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(refundProcess, activity);
    }

    public Money getValueEstimation() {
        return valueEstimation;
    }

    public void setValueEstimation(Money valueEstimation) {
        this.valueEstimation = valueEstimation;
    }

    public CPVReference getCPVReference() {
        return reference;
    }

    public void setCPVReference(CPVReference reference) {
        this.reference = reference;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClassification(AcquisitionItemClassification classification) {
        this.classification = classification;
    }

    public AcquisitionItemClassification getClassification() {
        return classification;
    }

    @Override
    public boolean hasAllneededInfo() {
        return getValueEstimation() != null && (getCPVReference() != null || getMaterial() != null) && getDescription() != null;
    }
}
