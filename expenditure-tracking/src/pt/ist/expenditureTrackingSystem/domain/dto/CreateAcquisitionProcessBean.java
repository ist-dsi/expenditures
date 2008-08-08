package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateAcquisitionProcessBean implements Serializable {

    private String project;
    private String subproject;
    private String recipient;
    private String receptionAddress;
    private DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit;
    private DomainReference<Supplier> supplier;
    private DomainReference<Person> requester;

    public CreateAcquisitionProcessBean() {
	setRequestingUnit(null);
	setSupplier(null);
    }

    public CreateAcquisitionProcessBean(AcquisitionRequest acquisitionRequest) {
	setProject(acquisitionRequest.getProject());
	setSubproject(acquisitionRequest.getSubproject());
	setRecipient(acquisitionRequest.getRecipient());
	setReceptionAddress(acquisitionRequest.getReceptionAddress());
	setRequestingUnit(acquisitionRequest.getRequestingUnit());
	setSupplier(acquisitionRequest.getSupplier());
	if (acquisitionRequest.getPayingUnits().contains(acquisitionRequest.getRequestingUnit())) {
	    setRequestUnitPayingUnit(true);
	}
    }

    public String getProject() {
	return project;
    }

    public void setProject(String project) {
	this.project = project;
    }

    public String getSubproject() {
	return subproject;
    }

    public void setSubproject(String subproject) {
	this.subproject = subproject;
    }

    public String getRecipient() {
	return recipient;
    }

    public void setRecipient(String recipient) {
	this.recipient = recipient;
    }

    public String getReceptionAddress() {
	return receptionAddress;
    }

    public void setReceptionAddress(String receptionAddress) {
	this.receptionAddress = receptionAddress;
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
	this.supplier = new DomainReference<Supplier>(supplier);
    }

    public Supplier getSupplier() {
	return supplier.getObject();
    }

    public void setRequester(Person requester) {
	this.requester = new DomainReference<Person>(requester);
    }

    public Person getRequester() {
	return requester.getObject();
    }

}
