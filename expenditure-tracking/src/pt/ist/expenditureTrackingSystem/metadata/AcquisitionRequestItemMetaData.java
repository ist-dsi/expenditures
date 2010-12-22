package pt.ist.expenditureTrackingSystem.metadata;

import javax.xml.bind.annotation.XmlRootElement;

import module.signature.exception.SignatureMetaDataInvalidException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;

@XmlRootElement(name = "acquisitionRequestItem")
public class AcquisitionRequestItemMetaData extends RequestItemMetaData<AcquisitionRequestItem> {

    private String proposalReference;
    private String address;
    private String sortedUnitItems;

    private int quantity;
    private int realQuantity;
    private String unitValue;
    private String realUnitValue;
    private String totalItemValue;
    private String totalRealValue;
    private String vatValue;
    private String realVatValue;
    private String totalVatValue;
    private String totalRealVatValue;
    private String additionalCostValue;
    private String realAdditionalCostValue;
    private String totalItemValueWithAdditionalCostsAndVat;
    private String totalRealValueWithAdditionalCostsAndVat;

    public AcquisitionRequestItemMetaData() {
	super();
    }

    public AcquisitionRequestItemMetaData(AcquisitionRequestItem item) {
	super(item);

	setProposalReference(item.getProposalReference());
	setAddress(item.getAddress().exportAsString());

	StringBuffer buffer = new StringBuffer();
	for (UnitItem unitItem : item.getSortedUnitItems()) {
	    if (unitItem.getShareValue() != null) {
		buffer.append("<p>" + unitItem.getUnit().getPresentationName() + ": " + unitItem.getShareValue().toFormatString()
			+ "</p>");
	    } else if (unitItem.getRealShareValue() != null) {
		buffer.append("<p>" + unitItem.getUnit().getPresentationName() + ": "
			+ unitItem.getRealShareValue().toFormatString() + "</p>");
	    }
	}

	if (notNull(item.getQuantity())) {
	    setQuantity(item.getQuantity());
	}

	if (notNull(item.getRealQuantity())) {
	    setRealQuantity(item.getRealQuantity());
	}

	if (notNull(item.getUnitValue())) {
	    setUnitValue(item.getUnitValue().toFormatString());
	}
	if (notNull(item.getRealUnitValue())) {
	    setRealUnitValue(item.getRealUnitValue().toFormatString());
	}
	if (notNull(item.getTotalItemValue())) {
	    setTotalItemValue(item.getTotalItemValue().toFormatString());
	}
	if (notNull(item.getTotalRealValue())) {
	    setTotalRealValue(item.getTotalRealValue().toFormatString());
	}

	if (notNull(item.getVatValue())) {
	    setVatValue(item.getVatValue().toPlainString());
	}
	if (notNull(item.getRealVatValue())) {
	    setRealVatValue(item.getRealVatValue().toPlainString());
	}

	if (notNull(item.getTotalVatValue())) {
	    setTotalVatValue(item.getTotalVatValue().toFormatString());
	}
	if (notNull(item.getTotalRealVatValue())) {
	    setTotalRealVatValue(item.getTotalRealVatValue().toFormatString());
	}

	if (notNull(item.getAdditionalCostValue())) {
	    setAdditionalCostValue(item.getAdditionalCostValue().toFormatString());
	}
	if (notNull(item.getRealAdditionalCostValue())) {
	    setRealAdditionalCostValue(item.getRealAdditionalCostValue().toFormatString());
	}

	if (notNull(item.getTotalItemValueWithAdditionalCostsAndVat())) {
	    setTotalItemValueWithAdditionalCostsAndVat(item.getTotalItemValueWithAdditionalCostsAndVat().toFormatString());
	}
	if (notNull(item.getTotalRealValueWithAdditionalCostsAndVat())) {
	    setTotalRealValueWithAdditionalCostsAndVat(item.getTotalRealValueWithAdditionalCostsAndVat().toFormatString());
	}
    }

    @Override
    public void checkData(RequestItem x) throws SignatureMetaDataInvalidException {
	// TODO Auto-generated method stub
    }

    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }

    public String getProposalReference() {
	return proposalReference;
    }

    public void setProposalReference(String proposalReference) {
	this.proposalReference = proposalReference;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getSortedUnitItems() {
	return sortedUnitItems;
    }

    public void setSortedUnitItems(String sortedUnitItems) {
	this.sortedUnitItems = sortedUnitItems;
    }

    public String getUnitValue() {
	return unitValue;
    }

    public void setUnitValue(String unitValue) {
	this.unitValue = unitValue;
    }

    public String getRealUnitValue() {
	return realUnitValue;
    }

    public void setRealUnitValue(String realUnitValue) {
	this.realUnitValue = realUnitValue;
    }

    public String getTotalItemValue() {
	return totalItemValue;
    }

    public void setTotalItemValue(String totalItemValue) {
	this.totalItemValue = totalItemValue;
    }

    public String getTotalRealValue() {
	return totalRealValue;
    }

    public void setTotalRealValue(String totalRealValue) {
	this.totalRealValue = totalRealValue;
    }

    public String getVatValue() {
	return vatValue;
    }

    public void setVatValue(String vatValue) {
	this.vatValue = vatValue;
    }

    public String getRealVatValue() {
	return realVatValue;
    }

    public void setRealVatValue(String realVatValue) {
	this.realVatValue = realVatValue;
    }

    public String getTotalVatValue() {
	return totalVatValue;
    }

    public void setTotalVatValue(String totalVatValue) {
	this.totalVatValue = totalVatValue;
    }

    public String getTotalRealVatValue() {
	return totalRealVatValue;
    }

    public void setTotalRealVatValue(String totalRealVatValue) {
	this.totalRealVatValue = totalRealVatValue;
    }

    public String getAdditionalCostValue() {
	return additionalCostValue;
    }

    public void setAdditionalCostValue(String additionalCostValue) {
	this.additionalCostValue = additionalCostValue;
    }

    public String getRealAdditionalCostValue() {
	return realAdditionalCostValue;
    }

    public void setRealAdditionalCostValue(String realAdditionalCostValue) {
	this.realAdditionalCostValue = realAdditionalCostValue;
    }

    public String getTotalItemValueWithAdditionalCostsAndVat() {
	return totalItemValueWithAdditionalCostsAndVat;
    }

    public void setTotalItemValueWithAdditionalCostsAndVat(String totalItemValueWithAdditionalCostsAndVat) {
	this.totalItemValueWithAdditionalCostsAndVat = totalItemValueWithAdditionalCostsAndVat;
    }

    public String getTotalRealValueWithAdditionalCostsAndVat() {
	return totalRealValueWithAdditionalCostsAndVat;
    }

    public void setTotalRealValueWithAdditionalCostsAndVat(String totalRealValueWithAdditionalCostsAndVat) {
	this.totalRealValueWithAdditionalCostsAndVat = totalRealValueWithAdditionalCostsAndVat;
    }

    public int getRealQuantity() {
	return realQuantity;
    }

    public void setRealQuantity(int realQuantity) {
	this.realQuantity = realQuantity;
    }

}
