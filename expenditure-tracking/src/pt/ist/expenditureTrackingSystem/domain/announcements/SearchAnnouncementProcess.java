/*
 * @(#)SearchAnnouncementProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import myorg.domain.util.Money;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.Search;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class SearchAnnouncementProcess extends Search<AnnouncementProcess> {

    private String publisherName;
    private String supplierName;
    private Money totalPrice;
    private String requestingUnitName;
    private AnnouncementProcessStateType processStateType;

    private class SearchAnnouncementResult extends SearchResultSet<AnnouncementProcess> {

	public SearchAnnouncementResult(Collection<? extends AnnouncementProcess> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final AnnouncementProcess announcementProcess) {
	    final Person loggedPerson = Person.getLoggedPerson();
	    return announcementProcess.isVisible(loggedPerson) && matchCriteria(announcementProcess);
	}

	private boolean matchCriteria(AnnouncementProcess announcementProcess) {

	    CCPAnnouncement a = announcementProcess.getAnnouncement();
	    return matchCriteria(a.getPublisher().getName(), getPublisherName())
		    && matchCriteria(a.getSupplier().getName(), getSupplierName())
		    && matchCriteria(a.getTotalPrice(), getTotalPrice())
		    && matchCriteria(a.getRequestingUnitName(), getRequestingUnitName())
		    && matchCriteria(announcementProcess.getAnnouncementProcessStateType(), getProcessStateType());
	}

	private boolean matchCriteria(final AnnouncementProcessStateType value, final AnnouncementProcessStateType criteria) {
	    return criteria == null || value == criteria;
	}

	protected boolean matchCriteria(Money recordPrice, Money inputPrice) {
	    return inputPrice == null || recordPrice.isLessThanOrEqual(inputPrice);
	}

	@Override
	protected boolean matchCriteria(String recordName, String inputName) {
	    inputName = inputName.trim();
	    if (StringUtils.isEmpty(recordName) && !StringUtils.isEmpty(inputName)) {
		return false;
	    }

	    if (StringUtils.isEmpty(inputName)) {
		return true;
	    }

	    recordName = StringNormalizer.normalize(recordName);
	    inputName = StringNormalizer.normalize(inputName);

	    String[] recordNameArray = recordName.split(" ");
	    for (String inputString : recordNameArray) {
		if (inputString.trim().indexOf(inputName) >= 0) {
		    return true;
		}
	    }
	    return false;
	}

    }

    @Override
    public Set<AnnouncementProcess> search() {
	try {
	    return hasAnyCriteria() ? new SearchAnnouncementResult(GenericProcess.getAllProcesses(AnnouncementProcess.class))
		    : Collections.EMPTY_SET;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new Error(ex);
	}
    }

    protected boolean hasAnyCriteria() {
	return hasCriteria(publisherName) || hasCriteria(supplierName) || totalPrice != null || hasCriteria(requestingUnitName)
		|| processStateType != null;
    }

    protected boolean hasCriteria(String criteria) {
	return !StringUtils.isEmpty(criteria);
    }

    public String getPublisherName() {
	return publisherName;
    }

    public void setPublisherName(String publisherName) {
	this.publisherName = publisherName;
    }

    public String getSupplierName() {
	return supplierName;
    }

    public void setSupplierName(String supplierName) {
	this.supplierName = supplierName;
    }

    public Money getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(Money totalPrice) {
	this.totalPrice = totalPrice;
    }

    public String getRequestingUnitName() {
	return requestingUnitName;
    }

    public void setRequestingUnitName(String requestingUnitName) {
	this.requestingUnitName = requestingUnitName;
    }

    public AnnouncementProcessStateType getProcessStateType() {
	return processStateType;
    }

    public void setProcessStateType(AnnouncementProcessStateType processStateType) {
	this.processStateType = processStateType;
    }

}
