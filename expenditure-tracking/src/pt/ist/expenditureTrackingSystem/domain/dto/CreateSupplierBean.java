package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

import myorg.domain.util.Address;

public class CreateSupplierBean implements Serializable {

    private String name;
    private String abbreviatedName;
    private String fiscalIdentificationCode;
    private Address address;
    private String phone;
    private String fax;
    private String email;
    private String nib;
    private DateTime dateTime;

    public CreateSupplierBean() {

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setAbbreviatedName(String abbreviatedName) {
	this.abbreviatedName = abbreviatedName;
    }

    public String getAbbreviatedName() {
	return abbreviatedName;
    }

    public String getFiscalIdentificationCode() {
	return fiscalIdentificationCode;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	this.fiscalIdentificationCode = fiscalIdentificationCode;
    }

    public Address getAddress() {
	return address;
    }

    public void setAddress(Address address) {
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

    public void setDateTime(DateTime dateTime) {
	this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
	return dateTime;
    }

    public void setNib(String nib) {
	this.nib = nib;
    }

    public String getNib() {
	return nib;
    }

}
