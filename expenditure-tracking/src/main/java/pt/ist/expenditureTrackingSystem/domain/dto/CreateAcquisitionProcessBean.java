/*
 * @(#)CreateAcquisitionProcessBean.java
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class CreateAcquisitionProcessBean implements Serializable {

    private boolean isForMission = false;
    private MissionProcess missionProcess;
    private Unit requestingUnit;
    private boolean requestUnitPayingUnit;
    private List<Supplier> suppliers;
    private Person requester;
    private Supplier supplierToAdd;
    private ProcessClassification classification;
    private String simpleDescription;
    private boolean isUnderMandatorySupplierScope;

    public CreateAcquisitionProcessBean(ProcessClassification classification) {
        setRequestingUnit(null);
        setSupplier(null);
        setSupplierToAdd(null);
        setRequestUnitPayingUnit(true);
        setClassification(classification);
        setUnderMandatorySupplierScope(false);
    }

    public CreateAcquisitionProcessBean(AcquisitionRequest acquisitionRequest) {
        setSupplierToAdd(null);
        setRequestingUnit(acquisitionRequest.getRequestingUnit());
        setSuppliers(acquisitionRequest.getSuppliers());
        if (acquisitionRequest.getPayingUnits().contains(acquisitionRequest.getRequestingUnit())) {
            setRequestUnitPayingUnit(true);
        }
        AcquisitionProcess process = acquisitionRequest.getProcess();
        if (process instanceof SimplifiedProcedureProcess) {
            setClassification(((SimplifiedProcedureProcess) process).getProcessClassification());
        }

    }

    public Unit getRequestingUnit() {
        return requestingUnit;
    }

    public void setRequestingUnit(Unit requestingUnit) {
        this.requestingUnit = requestingUnit;
    }

    public boolean isRequestUnitPayingUnit() {
        return requestUnitPayingUnit;
    }

    public void setRequestUnitPayingUnit(boolean requestUnitPayingUnit) {
        this.requestUnitPayingUnit = requestUnitPayingUnit;
    }

    public void setSupplier(Supplier supplier) {
        this.suppliers = new ArrayList<Supplier>();
        this.suppliers.add(supplier);
    }

    public Supplier getSupplier() {
        return this.suppliers.isEmpty() ? null : this.suppliers.get(0);
    }

    public void setSuppliers(Collection<Supplier> suppliers) {
        this.suppliers = new ArrayList<Supplier>();
        for (Supplier supplier : suppliers) {
            this.suppliers.add(supplier);
        }
    }

    public List<Supplier> getSuppliers() {
        List<Supplier> suppliers = new ArrayList<Supplier>();
        for (Supplier supplier : this.suppliers) {
            if (supplier != null) {
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    public void setRequester(Person requester) {
        this.requester = requester;
    }

    public Person getRequester() {
        return requester;
    }

    public Supplier getSupplierToAdd() {
        return supplierToAdd;
    }

    public void setSupplierToAdd(Supplier supplierToAdd) {
        this.supplierToAdd = supplierToAdd;
    }

    public void addSupplierToList(Supplier supplier) {
        if (this.suppliers == null) {
            this.suppliers = new ArrayList<Supplier>();
        }
        this.suppliers.add(supplier);
    }

    public void removeSupplierFromList(int index) {
        if (this.suppliers != null && index < this.suppliers.size()) {
            this.suppliers.remove(index);
        }
    }

    public ProcessClassification getClassification() {
        return classification;
    }

    public void setClassification(ProcessClassification classification) {
        this.classification = classification;
    }

    public boolean isForMission() {
        return isForMission;
    }

    public boolean getIsForMission() {
        return isForMission;
    }

    public void setIsForMission(boolean isForMission) {
        this.isForMission = isForMission;
    }

    public void setForMission(boolean isForMission) {
        this.isForMission = isForMission;
    }

    public MissionProcess getMissionProcess() {
        return missionProcess;
    }

    public void setMissionProcess(MissionProcess missionProcess) {
        this.missionProcess = missionProcess;
    }

    public String getContractSimpleDescription() {
        return simpleDescription;
    }

    public void setContractSimpleDescription(String simpleDescription) {
        this.simpleDescription = simpleDescription;
    }

    public boolean isUnderMandatorySupplierScope() {
        return isUnderMandatorySupplierScope;
    }

    public boolean getIsUnderMandatorySupplierScope() {
        return isUnderMandatorySupplierScope;
    }

    public void setIsUnderMandatorySupplierScope(boolean isUnderMandatorySupplierScope) {
        setUnderMandatorySupplierScope(isUnderMandatorySupplierScope);
    }

    public void setUnderMandatorySupplierScope(boolean isUnderMandatorySupplierScope) {
        this.isUnderMandatorySupplierScope = isUnderMandatorySupplierScope;
        if (isUnderMandatorySupplierScope) {
            final Supplier supplier = MissionSystem.getInstance().getMandatorySupplier();
            if (supplier != null) {
                setSupplier(supplier);            
            }
        }
    }

}
