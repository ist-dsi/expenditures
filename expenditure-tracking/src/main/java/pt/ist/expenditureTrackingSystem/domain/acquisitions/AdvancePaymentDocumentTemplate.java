package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class AdvancePaymentDocumentTemplate extends AdvancePaymentDocumentTemplate_Base {

    public AdvancePaymentDocumentTemplate(LocalizedString type, LocalizedString subject, Boolean partialValue,
            Boolean needAcquisitionJustification, Boolean needEntityJustification) {
        super();
        setType(type);
        setSubject(subject);
        setPartialValue(partialValue);
        setNeedAcquisitionJustification(needAcquisitionJustification);
        setNeedEntityJustification(needEntityJustification);
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

}
