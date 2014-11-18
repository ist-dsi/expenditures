/*
 * @(#)CCPAnnouncement.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class CCPAnnouncement extends CCPAnnouncement_Base {

    public CCPAnnouncement(AnnouncementProcess announcementProcess, Person publisher, AnnouncementBean announcementBean) {
        super();
        checkArguments(announcementProcess, publisher);
        setAnnouncementProcess(announcementProcess);
        setPublisher(publisher);
        edit(announcementBean);
    }

    private void checkArguments(AnnouncementProcess announcementProcess, Person publisher) {
        if (announcementProcess == null || publisher == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments");
        }
    }

    public void edit(AnnouncementBean announcementBean) {
        checkArguments(announcementBean);
        setDescription(announcementBean.getDescription());
        setTotalPrice(announcementBean.getTotalPrice());
        setExecutionDays(announcementBean.getExecutionDays());
        setExecutionAddress(announcementBean.getExecutionAddress());
        setChoiceCriteria(announcementBean.getChoiceCriteria());
        setSupplier(announcementBean.getSupplier());
        setAcquisition(announcementBean.getAcqusition());
        setRequestingUnit(announcementBean.getRequestingUnit());
        if (announcementBean.getBuyingUnit() != null) {
            setBuyingUnit(announcementBean.getBuyingUnit());
        }
    }

    private void checkArguments(AnnouncementBean bean) {
        if (StringUtils.isEmpty(bean.getDescription())) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.description");
        }
        if (bean.getTotalPrice() == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.description.totalPrice");
        }
        if (bean.getExecutionDays() == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.description.executionDays");
        }
        if (StringUtils.isEmpty(bean.getExecutionAddress())) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.executionAddress");
        }
        if (bean.getBuyingUnit() == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.buyingUnit");
        }
        if (bean.getSupplier() == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.wrong.Announcement.arguments.supplier");
        }
    }

    public boolean isFilled() {
        return hasBuyingUnit() && hasSupplier() && !StringUtils.isEmpty(getDescription()) && getTotalPrice() != null
                && getExecutionDays() != null && !StringUtils.isEmpty(getExecutionAddress());
    }

    public String getRequestingUnitName() {
        if (hasRequestingUnit()) {
            return getRequestingUnit().getName();
        }
        return null;
    }

    @Override
    public Set<Unit> getBuyingUnits() {
        return Collections.singleton(getBuyingUnit());
    }

    @Deprecated
    public boolean hasExecutionDays() {
        return getExecutionDays() != null;
    }

    @Deprecated
    public boolean hasExecutionAddress() {
        return getExecutionAddress() != null;
    }

    @Deprecated
    public boolean hasChoiceCriteria() {
        return getChoiceCriteria() != null;
    }

    @Deprecated
    public boolean hasAnnouncementProcess() {
        return getAnnouncementProcess() != null;
    }

    @Deprecated
    public boolean hasRequestingUnit() {
        return getRequestingUnit() != null;
    }

    @Deprecated
    public boolean hasSupplier() {
        return getSupplier() != null;
    }

    @Deprecated
    public boolean hasPublisher() {
        return getPublisher() != null;
    }

    @Deprecated
    public boolean hasBuyingUnit() {
        return getBuyingUnit() != null;
    }

}
