package pt.ist.expenditureTrackingSystem.metadata;

import javax.xml.bind.annotation.XmlRootElement;

import module.signature.exception.SignatureMetaDataInvalidException;
import module.signature.metadata.SignatureMetaData;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;

@XmlRootElement(name = "paymentUnitTotal")
public class PaymentUnitTotalMetaData extends SignatureMetaData<PayingUnitTotalBean> {

    private String presentationName;
    private String accountingUnitName;
    private String fundAllocationIds;
    private String effectiveFundAllocationIds;
    private String amount;

    public PaymentUnitTotalMetaData() {
	super();
    }

    public PaymentUnitTotalMetaData(PayingUnitTotalBean item) {
	super(item);

	setPresentationName(item.getPayingUnit().getPresentationName());
	setAccountingUnitName(item.getFinancer().getAccountingUnit().getName());
	if (item.getFinancer().isFundAllocationPresent()) {
	    setFundAllocationIds(item.getFinancer().getFundAllocationIds());
	}
	if (item.getFinancer().isEffectiveFundAllocationPresent()) {
	    setEffectiveFundAllocationIds(item.getFinancer().getEffectiveFundAllocationIds());
	}
	setAmount(item.getAmount().toFormatString());
    }

    @Override
    public void checkData(PayingUnitTotalBean x) throws SignatureMetaDataInvalidException {
	// TODO Auto-generated method stub
    }

    public String getPresentationName() {
	return presentationName;
    }

    public void setPresentationName(String presentationName) {
	this.presentationName = presentationName;
    }

    public String getAccountingUnitName() {
	return accountingUnitName;
    }

    public void setAccountingUnitName(String accountingUnitName) {
	this.accountingUnitName = accountingUnitName;
    }

    public String getFundAllocationIds() {
	return fundAllocationIds;
    }

    public void setFundAllocationIds(String fundAllocationIds) {
	this.fundAllocationIds = fundAllocationIds;
    }

    public String getEffectiveFundAllocationIds() {
	return effectiveFundAllocationIds;
    }

    public void setEffectiveFundAllocationIds(String effectiveFundAllocationIds) {
	this.effectiveFundAllocationIds = effectiveFundAllocationIds;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

}
