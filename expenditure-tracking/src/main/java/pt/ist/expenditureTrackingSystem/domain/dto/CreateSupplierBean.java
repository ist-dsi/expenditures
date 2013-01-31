/*
 * @(#)CreateSupplierBean.java
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

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.util.Address;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
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
