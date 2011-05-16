package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import myorg.domain.util.Money;

public class FinancerFundAllocationRequest extends FinancerFundAllocationRequest_Base {
    
    public FinancerFundAllocationRequest(final Financer financer,
	    final String processId, final String processUrl,
	    final Integer payingUnitNumber, final String payingAccountingUnit,
	    final Money totalValue, final Boolean finalFundAllocation) {
        super();
        setFinancer(financer);
        setProcessId(processId);
        setProcessUrl(processUrl);
        setPayingUnitNumber(payingUnitNumber);
        setPayingAccountingUnit(payingAccountingUnit);
        setTotalValue(totalValue);
        setFinalFundAllocation(finalFundAllocation);
    }

}
