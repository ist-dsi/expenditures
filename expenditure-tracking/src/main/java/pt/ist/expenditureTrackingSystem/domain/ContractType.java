package pt.ist.expenditureTrackingSystem.domain;

import org.fenixedu.commons.i18n.LocalizedString;

public class ContractType extends ContractType_Base {
    
    public ContractType(final LocalizedString name) {
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setName(name);
    }
    
}
