package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Collections;
import java.util.Set;

import myorg.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.dto.AnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

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
	    throw new DomainException("error.wrong.Announcement.arguments");
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
	    throw new DomainException("error.wrong.Announcement.arguments.description");
	}
	if (bean.getTotalPrice() == null) {
	    throw new DomainException("error.wrong.Announcement.arguments.description.totalPrice");
	}
	if (bean.getExecutionDays() == null) {
	    throw new DomainException("error.wrong.Announcement.arguments.description.executionDays");
	}
	if (StringUtils.isEmpty(bean.getExecutionAddress())) {
	    throw new DomainException("error.wrong.Announcement.arguments.executionAddress");
	}
	if (bean.getBuyingUnit() == null) {
	    throw new DomainException("error.wrong.Announcement.arguments.buyingUnit");
	}
	if (bean.getSupplier() == null) {
	    throw new DomainException("error.wrong.Announcement.arguments.supplier");
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

}
