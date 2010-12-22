package pt.ist.expenditureTrackingSystem.metadata;

import javax.xml.bind.annotation.XmlRootElement;

import module.signature.exception.SignatureMetaDataInvalidException;
import module.signature.metadata.SignatureMetaData;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;

@XmlRootElement(name = "requestItem")
public class RequestItemMetaData<T extends RequestItem> extends SignatureMetaData<RequestItem> {

    private String description;
    private String CPVReference;

    public RequestItemMetaData() {
	super();
    }

    public RequestItemMetaData(RequestItem item) {
	super(item);

	setDescription(item.getDescription());
	setCPVReference(item.getCPVReference().getCode() + " - " + item.getCPVReference().getDescription());
    }

    @Override
    public void checkData(RequestItem x) throws SignatureMetaDataInvalidException {
	// TODO Auto-generated method stub
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getCPVReference() {
	return CPVReference;
    }

    public void setCPVReference(String cPVReference) {
	CPVReference = cPVReference;
    }

}
