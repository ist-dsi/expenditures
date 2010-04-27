package module.workingCapital.domain;

import myorg.domain.util.Money;

public class WorkingCapitalInitializationReenforcement extends WorkingCapitalInitializationReenforcement_Base {
    
    public WorkingCapitalInitializationReenforcement(final WorkingCapitalInitialization workingCapitalInitialization, final Money amount) {
        super();
        setWorkingCapital(workingCapitalInitialization.getWorkingCapital());
        setFiscalId(workingCapitalInitialization.getFiscalId());
        setInternationalBankAccountNumber(workingCapitalInitialization.getInternationalBankAccountNumber());
        setAcceptedResponsability(workingCapitalInitialization.getAcceptedResponsability());
        Money requestedAnualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
        requestedAnualValue = requestedAnualValue.add(amount);

//        if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
//            final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement = (WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
//            requestedAnualValue = requestedAnualValue.add(workingCapitalInitializationReenforcement.getRequestedReenforcementValue());
//        }

        setRequestedAnualValue(requestedAnualValue);
        setAuthorizedAnualValue(workingCapitalInitialization.getAuthorizedAnualValue());
        setMaxAuthorizedAnualValue(workingCapitalInitialization.getMaxAuthorizedAnualValue());
        setRequestedReenforcementValue(amount);
    }

    @Override
    public void unverify() {
        super.unverify();
        setAuthorizedReenforcementValue(null);
    }

}
