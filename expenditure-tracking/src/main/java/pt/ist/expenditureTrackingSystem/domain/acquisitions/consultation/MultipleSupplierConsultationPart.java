package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;

public class MultipleSupplierConsultationPart extends MultipleSupplierConsultationPart_Base implements Comparable<MultipleSupplierConsultationPart> {

    public MultipleSupplierConsultationPart(final MultipleSupplierConsultation consultation,
            final String description, final Material material, final Money value) {
        setNumber(consultation.nextPartNumber());
        setConsultation(consultation);
        setDescription(description);
        setMaterial(material);
        setValue(value);
    }

    @Override
    public int compareTo(final MultipleSupplierConsultationPart p) {
        final int c = getNumber().compareTo(p.getNumber());
        return c == 0 ? getExternalId().compareTo(p.getExternalId()) : c;
    }
    
}
