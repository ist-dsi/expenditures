package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class CreateAcquisitionProcessBean implements Serializable {
    String costCenter;
    String project;
    String subproject;
    String recipient;
    String receptionAddress;
    String fiscalIdentificationCode;

    public String getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
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

}
