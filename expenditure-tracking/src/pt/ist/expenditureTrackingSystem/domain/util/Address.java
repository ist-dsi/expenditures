package pt.ist.expenditureTrackingSystem.domain.util;

public class Address {

    private String line1;
    private String line2;
    private String postalCode;
    private String location;
    private String country;

    public String getLine1() {
	return line1;
    }

    public void setLine1(String line1) {
	this.line1 = line1;
    }

    public String getLine2() {
	return line2;
    }

    public void setLine2(String line2) {
	this.line2 = line2;
    }

    public String getPostalCode() {
	return postalCode;
    }

    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

}
