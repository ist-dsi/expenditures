package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class AnnouncementBean implements Serializable {

    private Announcement announcement;

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

    public void setAnnouncement(Announcement announcement) {
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

    public static AnnouncementBean create(Announcement announcement) {
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
