package pt.ist.expenditureTrackingSystem.domain.announcements;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Announcement extends Announcement_Base {

    private Announcement() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Announcement(AnnouncementProcess announcementProcess, Person publisher, CreateAnnouncementBean announcementBean) {
	this();
	checkArguments(announcementProcess, publisher);
	setAnnouncementProcess(announcementProcess);
	setPublisher(publisher);
	setCreationDate(new DateTime());
	edit(announcementBean);
    }

    // TODO finish when creating process
    private void checkArguments(AnnouncementProcess announcementProcess, Person publisher) {
	if (announcementProcess == null || publisher == null) {
	    throw new DomainException("error.wrong.Announcement.arguments");
	}
    }

    private void edit(CreateAnnouncementBean b) {
	setDescription(b.getDescription());
	setTotalPrice(b.getTotalPrice());
	setExecutionDays(b.getExecutionDays());
	setExecutionAddress(b.getExecutionAddress());
	setChoiceCriteria(b.getChoiceCriteria());
	setBuyingUnit(b.getBuyingUnit());
	setSupplier(b.getSupplier());
	setAcquisition(b.getAcqusition());
    }

}
