package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class PurchaseOrderAdditionalInformationTemplate extends PurchaseOrderAdditionalInformationTemplate_Base {

    public PurchaseOrderAdditionalInformationTemplate(LocalizedString additionalInformation) {
        super();
        setAdditionalInformation(additionalInformation);
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

}
