package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class CreateAcquisitionProcessBean implements Serializable {

    private String project;
    private String subproject;
    private String recipient;
    private String receptionAddress;
    private String fiscalIdentificationCode;
    private DomainReference<Unit> requestingUnit;
    private boolean requestUnitPayingUnit; 
    
    public CreateAcquisitionProcessBean() {
	setRequestingUnit(null);
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

    public String getFiscalIdentificationCode() {
	return fiscalIdentificationCode;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	this.fiscalIdentificationCode = fiscalIdentificationCode;
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

}
