package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import module.finance.util.Money;

public class MultipleSupplierConsultationPartYearExecution extends MultipleSupplierConsultationPartYearExecution_Base implements Comparable<MultipleSupplierConsultationPartYearExecution> {
    
    public MultipleSupplierConsultationPartYearExecution(final MultipleSupplierConsultationPart part, final Integer year) {
        super();
        setPart(part);
        setYear(year);
        setValue(Money.ZERO);
    }

    @Override
    public int compareTo(final MultipleSupplierConsultationPartYearExecution o) {
        final int c = getYear().compareTo(o.getYear());
        return c == 0 ? getExternalId().compareTo(o.getExternalId()) : c;
    }

    public void delete() {
        setPart(null);
        deleteDomainObject();
    }

}
