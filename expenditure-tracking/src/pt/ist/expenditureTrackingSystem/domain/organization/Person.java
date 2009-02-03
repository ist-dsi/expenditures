package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.Role;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.AuthorizationBean;
import pt.ist.expenditureTrackingSystem.domain.dto.CreatePersonBean;
import myorg.domain.util.Address;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Person extends Person_Base {

    public Person() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	new Options(this);
    }

    public Person(final String username) {
	this();
	setUsername(username);
	setName(username);
    }

    @Service
    public static Person createEmptyPerson() {
	return new Person();
    }

    @Service
    public static Person createPerson(CreatePersonBean createPersonBean) {
	final Person person = new Person(createPersonBean.getUsername());
	person.setName(createPersonBean.getName());
	return person;
    }

    @Service
    public void delete() {
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    @Service
    public Authorization createAuthorization(final Unit unit) {
	return new Authorization(this, unit);
    }

    @Service
    public Authorization createAuthorization(final AuthorizationBean authorizationBean) {
	return new Authorization(authorizationBean);
    }

    public static Person findByUsername(final String username) {
	if (username != null && username.length() > 0) {
	    for (final Person person : ExpenditureTrackingSystem.getInstance().getPeopleSet()) {
		if (username.equals(person.getUsername())) {
		    return person;
		}
	    }
	}
	return null;
    }

    public Set<AcquisitionProcess> findAcquisitionProcessesPendingAuthorization() {
	final Set<AcquisitionProcess> result = new HashSet<AcquisitionProcess>();
	final Options options = getOptions();
	final boolean recurseSubUnits = options.getRecurseAuthorizationPendingUnits().booleanValue();
	for (final Authorization authorization : getAuthorizationsSet()) {
	    authorization.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
	}
	return result;
    }

    public boolean hasRoleType(RoleType type) {
	for (Role role : getRolesSet()) {
	    if (role.getRoleType().equals(type)) {
		return true;
	    }
	}
	return false;
    }

    @Service
    @Override
    public void addRoles(Role role) {
	if (!hasRoles(role)) {
	    super.addRoles(role);
	}
    }

    @Service
    @Override
    public void removeRoles(Role roles) {
	super.removeRoles(roles);
    }

    public void createNewDeliveryInfo(String recipient, Address address, String phone, String email) {
	new DeliveryInfo(this, recipient, address, phone, email);
    }

    public DeliveryInfo getDeliveryInfoByRecipientAndAddress(String recipient, Address address) {
	for (DeliveryInfo deliveryInfo : getDeliveryInfosSet()) {
	    if (deliveryInfo.getRecipient().equals(recipient) && deliveryInfo.getAddress().equals(address)) {
		return deliveryInfo;
	    }
	}
	return null;
    }

    private <T extends RequestWithPayment> List<T> getRequestsWithClassType(Class<T> clazz) {
	List<T> requests = new ArrayList<T>();
	for (RequestWithPayment acquisition : getRequestsWithyPayment()) {
	    if (acquisition.getClass().equals(clazz)) {
		requests.add((T) acquisition);
	    }
	}
	return requests;
    }

    public List<AcquisitionProcess> getAcquisitionProcesses() {
	List<AcquisitionProcess> processes = new ArrayList<AcquisitionProcess>();
	for (AcquisitionRequest request : getRequestsWithClassType(AcquisitionRequest.class)) {
	    processes.add(request.getAcquisitionProcess());
	}
	return processes;
    }

    public List<AnnouncementProcess> getAnnouncementProcesses() {
	List<AnnouncementProcess> processes = new ArrayList<AnnouncementProcess>();
	for (Announcement announcement : getAnnouncements()) {
	    processes.add(announcement.getAnnouncementProcess());
	}
	return processes;
    }

    public Set<Authorization> getValidAuthorizations() {
	final Set<Authorization> res = new HashSet<Authorization>();
	for (Authorization authorization : getAuthorizationsSet()) {
	    if (authorization.isValid()) {
		res.add(authorization);
	    }
	}
	return res;
    }

    public String getFirstAndLastName() {
	final String name = super.getName();
	int s1 = name.indexOf(' ');
	int s2 = name.lastIndexOf(' ');
	return s1 < 0 || s1 == s2 ? name : name.subSequence(0, s1) + name.substring(s2);
    }

    public List<Unit> getDirectResponsibleUnits() {
	List<Unit> units = new ArrayList<Unit>();
	for (Authorization authorization : getAuthorizations()) {
	    Unit unit = authorization.getUnit();
	    if (!unit.hasResponsibleInSubUnits()) {
		units.add(unit);
	    }
	}
	return units;
    }

    @Override
    @Service
    public void setDefaultSearch(SavedSearch defaultSearch) {
	super.setDefaultSearch(defaultSearch);
    }

}
