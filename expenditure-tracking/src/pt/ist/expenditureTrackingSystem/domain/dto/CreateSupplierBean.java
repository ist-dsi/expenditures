package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class CreateSupplierBean implements Serializable {

    String name;
    String fiscalIdentificationCode;
    String address;
    String phone;
    String fax;
    String email;

    public CreateSupplierBean() {

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFiscalIdentificationCode() {
	return fiscalIdentificationCode;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	this.fiscalIdentificationCode = fiscalIdentificationCode;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

}
