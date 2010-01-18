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
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateAcquisitionProcessBean implements Serializable {

    private boolean isForMission = false;
    private MissionProcess missionProcess;
    private DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit;
    private List<DomainReference<Supplier>> suppliers;
    private DomainReference<Person> requester;
    private DomainReference<Supplier> supplierToAdd;
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
	return requestingUnit.getObject();
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = new DomainReference<Unit>(requestingUnit);
    }

    public boolean isRequestUnitPayingUnit() {
	return requestUnitPayingUnit;
    }

    public void setRequestUnitPayingUnit(boolean requestUnitPayingUnit) {
	this.requestUnitPayingUnit = requestUnitPayingUnit;
    }

    public void setSupplier(Supplier supplier) {
	this.suppliers = new ArrayList<DomainReference<Supplier>>();
	this.suppliers.add(new DomainReference<Supplier>(supplier));
    }

    public Supplier getSupplier() {
	return this.suppliers.isEmpty() ? null : this.suppliers.get(0).getObject();
    }

    public void setSuppliers(List<Supplier> suppliers) {
	this.suppliers = new ArrayList<DomainReference<Supplier>>();
	for (Supplier supplier : suppliers) {
	    this.suppliers.add(new DomainReference<Supplier>(supplier));
	}
    }

    public List<Supplier> getSuppliers() {
	List<Supplier> suppliers = new ArrayList<Supplier>();
	for (DomainReference<Supplier> supplier : this.suppliers) {
	    if (supplier.getObject() != null) {
		suppliers.add(supplier.getObject());
	    }
	}
	return suppliers;
    }

    public void setRequester(Person requester) {
	this.requester = new DomainReference<Person>(requester);
    }

    public Person getRequester() {
	return requester.getObject();
    }

    public Supplier getSupplierToAdd() {
	return supplierToAdd.getObject();
    }

    public void setSupplierToAdd(Supplier supplierToAdd) {
	this.supplierToAdd = new DomainReference<Supplier>(supplierToAdd);
    }

    public void addSupplierToList(Supplier supplier) {
	if (this.suppliers == null) {
	    this.suppliers = new ArrayList<DomainReference<Supplier>>();
	}
	this.suppliers.add(new DomainReference<Supplier>(supplier));
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
