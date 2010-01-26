package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.mission.domain.MissionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class CreateAcquisitionProcessBean implements Serializable {

    private boolean isForMission = false;
    private MissionProcess missionProcess;
    private Unit requestingUnit;
    private boolean requestUnitPayingUnit;
    private List<Supplier> suppliers;
    private Person requester;
    private Supplier supplierToAdd;
    private ProcessClassification classification;

    public CreateAcquisitionProcessBean(ProcessClassification classification) {
	setRequestingUnit(null);
	setSupplier(null);
	setSupplierToAdd(null);
	setRequestUnitPayingUnit(true);
	setClassification(classification);
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

    public void setSuppliers(List<Supplier> suppliers) {
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

}
