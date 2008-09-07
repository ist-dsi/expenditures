package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.fenixWebFramework.util.DomainReference;

public class FundAllocationBean implements Serializable {

    private DomainReference<Financer> financer;
    private String fundAllocationId;

    public FundAllocationBean(final Financer financer) {
	setFinancer(financer);
    }

    public void setFinancer(Financer financer) {
	this.financer = new DomainReference<Financer>(financer);
    }

    public Financer getFinancer() {
	return this.financer.getObject();
    }

    public String getFundAllocationId() {
	return fundAllocationId;
    }

    public void setFundAllocationId(final String fundAllocationId) {
	this.fundAllocationId = fundAllocationId;
    }

}
