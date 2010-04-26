package module.workingCapital.domain;

import myorg.domain.util.Money;

public class WorkingCapitalInitializationReenforcement extends WorkingCapitalInitializationReenforcement_Base {
    
    public WorkingCapitalInitializationReenforcement(final WorkingCapitalInitialization workingCapitalInitialization, final Money amount) {
        super();
        setWorkingCapital(workingCapitalInitialization.getWorkingCapital());
        setFiscalId(workingCapitalInitialization.getFiscalId());
        setInternationalBankAccountNumber(workingCapitalInitialization.getInternationalBankAccountNumber());
        setRequestedAnualValue(workingCapitalInitialization.getRequestedAnualValue());
        setAuthorizedAnualValue(workingCapitalInitialization.getAuthorizedAnualValue());
        setMaxAuthorizedAnualValue(workingCapitalInitialization.getMaxAuthorizedAnualValue());
        setRequestedReenforcementValue(amount);
    }

}
