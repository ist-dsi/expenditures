package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;

public class FundAllocationBean implements Serializable {

    private Financer financer;
    private String fundAllocationId;
    private String effectiveFundAllocationId;
    private boolean allowedToAddNewFund;

    public FundAllocationBean(final Financer financer) {
	setFinancer(financer);
	setAllowedToAddNewFund(true);
    }

    public void setFinancer(Financer financer) {
	this.financer = financer;
    }

    public Financer getFinancer() {
	return this.financer;
    }

    public String getFundAllocationId() {
	return fundAllocationId;
    }

    public void setFundAllocationId(final String fundAllocationId) {
	this.fundAllocationId = fundAllocationId;
    }

    public void setEffectiveFundAllocationId(String effectiveFundAllocationId) {
	this.effectiveFundAllocationId = effectiveFundAllocationId;
    }

    public String getEffectiveFundAllocationId() {
	return effectiveFundAllocationId;
    }

    public boolean isAllowedToAddNewFund() {
	return allowedToAddNewFund;
    }

    public void setAllowedToAddNewFund(boolean allowedToAddNewFund) {
	this.allowedToAddNewFund = allowedToAddNewFund;
    }

}
