package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.util.DomainReference;

public class AnnouncementBean implements Serializable {

    private DomainReference<Announcement> announcement;

    private String description;
    private Money totalPrice;
    private Integer executionDays;
    private String executionAddress;
    private String choiceCriteria;
    private DomainReference<Unit> buyingUnit;
    private DomainReference<Unit> requestingUnit;
    private DomainReference<Supplier> supplier;
    private DomainReference<Acquisition> acquisition;
    
    public Unit getBuyingUnit() {
	return (buyingUnit != null ? buyingUnit.getObject() : null);
    }
    
    public void setBuyingUnit(Unit buyingUnit) {
	this.buyingUnit = new DomainReference<Unit>(buyingUnit);
    }

    public Unit getRequestingUnit() {
	return (requestingUnit != null ? requestingUnit.getObject() : null);
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = new DomainReference<Unit>(requestingUnit);
    }

    public Supplier getSupplier() {
	return (supplier != null ? supplier.getObject() : null);
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = new DomainReference<Supplier>(supplier);
    }

    public Acquisition getAcqusition() {
	return (acquisition != null ? acquisition.getObject() : null);
    }

    public void setAcquisition(Acquisition acquisition) {
	this.acquisition = new DomainReference<Acquisition>(acquisition);
    }

    public Announcement getAnnouncement() {
	return (announcement != null ? announcement.getObject() : null);
    }

    public void setAnnouncement(Announcement announcement) {
	this.announcement = new DomainReference<Announcement>(announcement);
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
