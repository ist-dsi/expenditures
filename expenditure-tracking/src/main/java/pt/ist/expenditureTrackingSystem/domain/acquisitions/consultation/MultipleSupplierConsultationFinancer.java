package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.math.BigDecimal;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class MultipleSupplierConsultationFinancer extends MultipleSupplierConsultationFinancer_Base implements Comparable<MultipleSupplierConsultationFinancer> {

    private static final BigDecimal PCT_FACTOR = new BigDecimal("0.01");

    public MultipleSupplierConsultationFinancer(final MultipleSupplierConsultation consultation, final Unit unit) {
        setConsultation(consultation);
        setUnit(unit);
        setPercentage(BigDecimal.ZERO);
    }

    public Money getValue() {
        return getConsultation().getValue().multiply(getPercentage()).multiply(PCT_FACTOR);
    }

    public void delete() {
        setConsultation(null);
        setUnit(null);
        deleteDomainObject();
    }

    @Override
    public int compareTo(final MultipleSupplierConsultationFinancer financer) {
        final int c = Unit.COMPARATOR_BY_PRESENTATION_NAME.compare(getUnit(), financer.getUnit());
        return c == 0 ? getExternalId().compareTo(financer.getExternalId()) : c;
    }

}
