package pt.ist.expenditureTrackingSystem.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import module.signature.metadata.SignatureProcessMetaData;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;

@XmlRootElement(name = "simplifiedProcedureProcess")
public class SimplifiedProcedureProcessMetaData extends SignatureProcessMetaData<SimplifiedProcedureProcess> {

    private String acquisitionRequestDocumentID;
    private String processClassification;
    private String requestorName;
    private String requestingUnitPresentationName;

    private String refundeeName;
    private String currentValue;
    private String currentTotalVatValue;
    private String currentTotalAdditionalCostsValue;
    private String currentTotalItemValueWithAdditionalCostsAndVat;
    private String fundAllocationExpirationDate;
    private String paymentReference;
    private String contractSimpleDescription;

    private List<AcquisitionRequestItemMetaData> items;
    private List<PaymentUnitTotalMetaData> payingUnitTotals;

    public SimplifiedProcedureProcessMetaData() {
	super();
    }

    public SimplifiedProcedureProcessMetaData(SimplifiedProcedureProcess process) {
	super(process);

	setAcquisitionRequestDocumentID(process.getAcquisitionRequestDocumentID());
	setProcessClassification(process.getProcessClassification().getShortDescription());
	setRequestorName(process.getRequestor().getName());
	setRequestingUnitPresentationName(process.getRequestingUnit().getPresentationName());

	setRefundeeName(process.getRequest().getRefundeeName());
	setCurrentValue(process.getRequest().getCurrentValue().toFormatString());
	setCurrentTotalVatValue(process.getRequest().getCurrentTotalVatValue().toFormatString());
	setCurrentTotalAdditionalCostsValue(process.getRequest().getCurrentTotalAdditionalCostsValue().toFormatString());
	setCurrentTotalItemValueWithAdditionalCostsAndVat(process.getRequest()
		.getCurrentTotalItemValueWithAdditionalCostsAndVat().toFormatString());
	if (notNull(process.getRequest().getAcquisitionProcess())
		&& notNull(process.getRequest().getAcquisitionProcess().getFundAllocationExpirationDate())) {
	    setFundAllocationExpirationDate(process.getRequest().getAcquisitionProcess().getFundAllocationExpirationDate()
		    .toString());
	}
	setPaymentReference(process.getRequest().getPaymentReference());
	setContractSimpleDescription(process.getRequest().getContractSimpleDescription());

	setItems(new ArrayList<AcquisitionRequestItemMetaData>());

	for (AcquisitionRequestItem item : process.getAcquisitionRequest().getAcquisitionRequestItemsSet()) {
	    getItems().add(new AcquisitionRequestItemMetaData(item));
	}

	setPayingUnitTotals(new ArrayList<PaymentUnitTotalMetaData>());

	for (PayingUnitTotalBean payingUnitTotalBean : process.getAcquisitionRequest().getTotalAmountsForEachPayingUnit()) {
	    getPayingUnitTotals().add(new PaymentUnitTotalMetaData(payingUnitTotalBean));
	}
    }

    public String getAcquisitionRequestDocumentID() {
	return acquisitionRequestDocumentID;
    }

    public void setAcquisitionRequestDocumentID(String acquisitionRequestDocumentID) {
	this.acquisitionRequestDocumentID = acquisitionRequestDocumentID;
    }

    public String getProcessClassification() {
	return processClassification;
    }

    public void setProcessClassification(String processClassification) {
	this.processClassification = processClassification;
    }

    public String getRequestorName() {
	return requestorName;
    }

    public void setRequestorName(String requestorName) {
	this.requestorName = requestorName;
    }

    public String getRequestingUnitPresentationName() {
	return requestingUnitPresentationName;
    }

    public void setRequestingUnitPresentationName(String requestingUnitPresentationName) {
	this.requestingUnitPresentationName = requestingUnitPresentationName;
    }

    public String getRefundeeName() {
	return refundeeName;
    }

    public void setRefundeeName(String refundeeName) {
	this.refundeeName = refundeeName;
    }

    public String getCurrentValue() {
	return currentValue;
    }

    public void setCurrentValue(String currentValue) {
	this.currentValue = currentValue;
    }

    public String getCurrentTotalVatValue() {
	return currentTotalVatValue;
    }

    public void setCurrentTotalVatValue(String currentTotalVatValue) {
	this.currentTotalVatValue = currentTotalVatValue;
    }

    public String getCurrentTotalAdditionalCostsValue() {
	return currentTotalAdditionalCostsValue;
    }

    public void setCurrentTotalAdditionalCostsValue(String currentTotalAdditionalCostsValue) {
	this.currentTotalAdditionalCostsValue = currentTotalAdditionalCostsValue;
    }

    public String getCurrentTotalItemValueWithAdditionalCostsAndVat() {
	return currentTotalItemValueWithAdditionalCostsAndVat;
    }

    public void setCurrentTotalItemValueWithAdditionalCostsAndVat(String currentTotalItemValueWithAdditionalCostsAndVat) {
	this.currentTotalItemValueWithAdditionalCostsAndVat = currentTotalItemValueWithAdditionalCostsAndVat;
    }

    public String getFundAllocationExpirationDate() {
	return fundAllocationExpirationDate;
    }

    public void setFundAllocationExpirationDate(String fundAllocationExpirationDate) {
	this.fundAllocationExpirationDate = fundAllocationExpirationDate;
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    public String getContractSimpleDescription() {
	return contractSimpleDescription;
    }

    public void setContractSimpleDescription(String contractSimpleDescription) {
	this.contractSimpleDescription = contractSimpleDescription;
    }

    public List<AcquisitionRequestItemMetaData> getItems() {
	return items;
    }

    public void setItems(List<AcquisitionRequestItemMetaData> items) {
	this.items = items;
    }

    public List<PaymentUnitTotalMetaData> getPayingUnitTotals() {
	return payingUnitTotals;
    }

    public void setPayingUnitTotals(List<PaymentUnitTotalMetaData> payingUnitTotals) {
	this.payingUnitTotals = payingUnitTotals;
    }
}
