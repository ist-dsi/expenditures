package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.util.SortedSet;
import java.util.TreeSet;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

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

    public Money getTotalAllocatedToSupplier(final Supplier supplier) {
        return isAllocatedToSupplier(supplier) ? getValue() : Money.ZERO;
    }

    public boolean isAllocatedToSupplier(final Supplier supplier) {
        final Supplier selectedSupplier = getSupplier();
        return supplier == selectedSupplier || (selectedSupplier == null && getConsultation().getSupplierSet().contains(supplier));
    }

    public void delete() {
        setConsultation(null);
        setMaterial(null);
        setSupplier(null);
        deleteDomainObject();
    }

    public void setexecutionByYear(final Integer year, final Money value) {
        final MultipleSupplierConsultationPartYearExecution yearExecution = getYearExecutionSet().stream()
            .filter(ye -> ye.getYear().intValue() == year.intValue())
            .findAny().orElseGet(() -> new MultipleSupplierConsultationPartYearExecution(this, year));
        yearExecution.setValue(value);
    }

    public SortedSet<MultipleSupplierConsultationPartYearExecution> getOrderedYearExecutionSet() {
        return new TreeSet<MultipleSupplierConsultationPartYearExecution>(getYearExecutionSet());
    }

}
