/*
 * @(#)AnnouncementBean.java
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

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AnnouncementBean implements Serializable {

    private CCPAnnouncement announcement;

    private String description;
    private Money totalPrice;
    private Integer executionDays;
    private String executionAddress;
    private String choiceCriteria;
    private Unit buyingUnit;
    private Unit requestingUnit;
    private Supplier supplier;
    private Acquisition acquisition;

    public Unit getBuyingUnit() {
        return buyingUnit;
    }

    public void setBuyingUnit(Unit buyingUnit) {
        this.buyingUnit = buyingUnit;
    }

    public Unit getRequestingUnit() {
        return requestingUnit;
    }

    public void setRequestingUnit(Unit requestingUnit) {
        this.requestingUnit = requestingUnit;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Acquisition getAcqusition() {
        return acquisition;
    }

    public void setAcquisition(Acquisition acquisition) {
        this.acquisition = acquisition;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(CCPAnnouncement announcement) {
        this.announcement = announcement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getExecutionDays() {
        return executionDays;
    }

    public void setExecutionDays(Integer executionDays) {
        this.executionDays = executionDays;
    }

    public String getExecutionAddress() {
        return executionAddress;
    }

    public void setExecutionAddress(String executionAddress) {
        this.executionAddress = executionAddress;
    }

    public String getChoiceCriteria() {
        return choiceCriteria;
    }

    public void setChoiceCriteria(String choiceCriteria) {
        this.choiceCriteria = choiceCriteria;
    }

    public static AnnouncementBean create(CCPAnnouncement announcement) {
        AnnouncementBean bean = new AnnouncementBean();
        bean.setAnnouncement(announcement);
        bean.setDescription(announcement.getDescription());
        bean.setTotalPrice(announcement.getTotalPrice());
        bean.setExecutionDays(announcement.getExecutionDays());
        bean.setExecutionAddress(announcement.getExecutionAddress());
        bean.setChoiceCriteria(announcement.getChoiceCriteria());
        bean.setBuyingUnit(announcement.getBuyingUnit());
        bean.setRequestingUnit(announcement.getRequestingUnit());
        bean.setSupplier(announcement.getSupplier());
        bean.setAcquisition(announcement.getAcquisition());
        return bean;
    }
}
