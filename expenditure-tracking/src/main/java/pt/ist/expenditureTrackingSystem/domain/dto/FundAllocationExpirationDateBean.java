package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class FundAllocationExpirationDateBean implements Serializable {

	private LocalDate fundAllocationExpirationDate;

	public LocalDate getFundAllocationExpirationDate() {
		return fundAllocationExpirationDate;
	}

	public void setFundAllocationExpirationDate(LocalDate fundAllocationExpirationDate) {
		this.fundAllocationExpirationDate = fundAllocationExpirationDate;
	}

}
