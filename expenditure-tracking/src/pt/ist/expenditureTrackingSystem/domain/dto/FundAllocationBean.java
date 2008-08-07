package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class FundAllocationBean implements Serializable {

    private String fundAllocationId;

    public String getFundAllocationId() {
        return fundAllocationId;
    }

    public void setFundAllocationId(final String fundAllocationId) {
        this.fundAllocationId = fundAllocationId;
    }

}
