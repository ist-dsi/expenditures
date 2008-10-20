package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.util.Address;
import pt.ist.expenditureTrackingSystem.util.ReportUtils;

public class CreateAcquisitionPurchaseOrderDocument extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isApproved();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	String requestID = ExpenditureTrackingSystem.getInstance().nextAcquisitionRequestDocumentID();
	byte[] file = createPurchaseOrderDocument(process.getAcquisitionRequest(), requestID);
	new PurchaseOrderDocument(process.getAcquisitionRequest(), file, requestID + "." + File.EXTENSION_PDF, requestID);
    }

    protected byte[] createPurchaseOrderDocument(final AcquisitionRequest acquisitionRequest, final String requestID) {
	final Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("acquisitionRequest", acquisitionRequest);
	paramMap.put("requestID", requestID);
	paramMap.put("responsibleName", getUser().getPerson().getName());
	DeliveryLocalList deliveryLocalList = new DeliveryLocalList();
	List<AcquisitionRequestItemBean> acquisitionRequestItemBeans = new ArrayList<AcquisitionRequestItemBean>();
	createBeansLists(acquisitionRequest, deliveryLocalList, acquisitionRequestItemBeans);
	paramMap.put("deliveryLocals", deliveryLocalList);

	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources/AcquisitionResources");
	try {
	    byte[] byteArray = ReportUtils.exportToPdfFileAsByteArray("acquisitionRequestPurchaseOrder", paramMap,
		    resourceBundle, acquisitionRequestItemBeans);
	    return byteArray;
	} catch (JRException e) {
	    e.printStackTrace();
	    throw new DomainException("acquisitionRequestDocument.message.exception.failedCreation");
	}

    }

    private void createBeansLists(AcquisitionRequest acquisitionRequest, DeliveryLocalList deliveryLocalList,
	    List<AcquisitionRequestItemBean> acquisitionRequestItemBeans) {
	for (AcquisitionRequestItem acquisitionRequestItem : acquisitionRequest.getAcquisitionRequestItemsSet()) {
	    DeliveryLocal deliveryLocal = deliveryLocalList.getDeliveryLocal(acquisitionRequestItem.getRecipient(),
		    acquisitionRequestItem.getRecipientPhone(), acquisitionRequestItem.getRecipientEmail(),
		    acquisitionRequestItem.getAddress());
	    acquisitionRequestItemBeans.add(new AcquisitionRequestItemBean(deliveryLocal.getIdentification(),
		    acquisitionRequestItem));
	}
    }

    public static class AcquisitionRequestItemBean {
	private String identification;
	private AcquisitionRequestItem acquisitionRequestItem;

	public AcquisitionRequestItemBean(String identification, AcquisitionRequestItem acquisitionRequestItem) {
	    setIdentification(identification);
	    setAcquisitionRequestItem(acquisitionRequestItem);
	}

	public String getIdentification() {
	    return identification;
	}

	public void setIdentification(String identification) {
	    this.identification = identification;
	}

	public void setAcquisitionRequestItem(AcquisitionRequestItem acquisitionRequestItem) {
	    this.acquisitionRequestItem = acquisitionRequestItem;
	}

	public AcquisitionRequestItem getAcquisitionRequestItem() {
	    return acquisitionRequestItem;
	}

    }

    public static class DeliveryLocalList extends ArrayList<DeliveryLocal> {
	private char id = 'A';

	public DeliveryLocal getDeliveryLocal(String personName, String phone, String email, Address address) {
	    for (DeliveryLocal deliveryLocal : this) {
		if (deliveryLocal.getPersonName().equals(personName) && deliveryLocal.getPhone().equals(phone)
			&& deliveryLocal.getEmail().equals(email) && deliveryLocal.getAddress().equals(address)) {
		    return deliveryLocal;
		}
	    }

	    DeliveryLocal newDelDeliveryLocal = new DeliveryLocal("" + id, personName, phone, email, address);
	    id++;
	    add(newDelDeliveryLocal);
	    return newDelDeliveryLocal;
	}
    }

    public static class DeliveryLocal {
	private String identification;
	private String personName;
	private String phone;
	private String email;
	private Address address;

	public DeliveryLocal(String identification, String personName, String phone, String email, Address address) {
	    setIdentification(identification);
	    setPersonName(personName);
	    setPhone(phone);
	    setEmail(email);
	    setAddress(address);
	}

	public String getIdentification() {
	    return identification;
	}

	public void setIdentification(String identification) {
	    this.identification = identification;
	}

	public String getPersonName() {
	    return personName;
	}

	public void setPersonName(String personName) {
	    this.personName = personName;
	}

	public String getPhone() {
	    return phone;
	}

	public void setPhone(String phone) {
	    this.phone = phone;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public Address getAddress() {
	    return address;
	}

	public void setAddress(Address address) {
	    this.address = address;
	}

    }

}
