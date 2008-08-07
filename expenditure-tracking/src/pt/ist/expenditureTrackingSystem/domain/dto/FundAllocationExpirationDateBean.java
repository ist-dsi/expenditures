package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class FundAllocationExpirationDateBean implements Serializable {

    private DateTime fundAllocationExpirationDate;

    public DateTime getFundAllocationExpirationDate() {
        return fundAllocationExpirationDate;
    }

    public void setFundAllocationExpirationDate(DateTime fundAllocationExpirationDate) {
        this.fundAllocationExpirationDate = fundAllocationExpirationDate;
    }

}
