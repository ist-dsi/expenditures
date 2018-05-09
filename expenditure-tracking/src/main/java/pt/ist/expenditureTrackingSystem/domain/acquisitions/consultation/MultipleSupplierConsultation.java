package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.User;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document.SupplierCriteriaSelectionDocument;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class MultipleSupplierConsultation extends MultipleSupplierConsultation_Base {
    
    public MultipleSupplierConsultation(final MultipleSupplierConsultationProcess process, final String description,
            final Material material, final String justification, final ContractType contractType) {
        setProcess(process);
        edit(description, material, justification, contractType, null, null, null, 6, 66, BigDecimal.ZERO, 0, Boolean.FALSE, Boolean.TRUE, "");
    }

    public void edit(final String description, final Material material, final String justification,
            final ContractType contractType, final Integer contractDuration, final User contractManager,
            final String supplierCountJustification, final Integer proposalDeadline,
            final Integer proposalValidity, final BigDecimal collateral, final Integer numberOfAlternativeProposals,
            final Boolean negotiation, final Boolean specificEvaluationMethod, final String evaluationMethodJustification) {
        setDescription(description);
        setMaterial(material);
        setJustification(justification);
        setContractType(contractType);
        setContractDuration(contractDuration);
        setContractManager(contractManager);
        setSupplierCountJustification(supplierCountJustification);
        setProposalDeadline(proposalDeadline);
        setProposalValidity(proposalValidity);
        setCollateral(collateral);
        setNumberOfAlternativeProposals(numberOfAlternativeProposals);
        setNegotiation(negotiation);
        setSpecificEvaluationMethod(specificEvaluationMethod);
        setEvaluationMethodJustification(evaluationMethodJustification);
    }

    public Integer nextPartNumber() {
        return getPartSet().stream().mapToInt(p -> p.getNumber()).max().orElse(0) + 1;
    }
    
    public SortedSet<MultipleSupplierConsultationPart> getOrderedPartSet() {
        return new TreeSet<>(getPartSet());
    }

    public Money getValue() {
        return getPartSet().stream().map(p -> p.getValue()).reduce(Money.ZERO, Money::add);
    }

    public SortedSet<MultipleSupplierConsultationJuryMember> getOrderedJuryMemberSet() {
        return new TreeSet<>(getJuryMemberSet());
    }

    public SortedSet<TieBreakCriteria> getOrderedTieBreakCriteriaSet() {
        return new TreeSet<>(getTieBreakCriteriaSet());
    }

    public SortedSet<MultipleSupplierConsultationFinancer> getOrderedFinancerSet() {
        return new TreeSet<>(getFinancerSet());
    }

    public SortedSet<Supplier> getOrderedSupplierSet() {
        return new TreeSet<>(getSupplierSet());
    }

    public boolean canApprove(final User user) {
        return getFinancerSet().stream().anyMatch(f -> !f.isApproved() && f.isUnitResponsible(user));
    }

    public boolean isJuryMember(final User user) {
        return getJuryMemberSet().stream().anyMatch(j -> j.getUser() == user);
    }

    public boolean isValid() {
        return getContractType() != null
                && getMaterial() != null
                && isPresent(getDescription())
                && getValue().isPositive()
                && getProposalDeadline() != null
                && getProposalValidity() != null && getProposalValidity().intValue() >= 66
                && isPresent(getJustification())
                && isPresent(getPriceLimitJustification())
                && isPresent(getSupplierCountJustification())
                && getContractManager() != null
                && isJuryValid()
                && getPartSet().size() > 0
                && getPartSet().stream().allMatch(p -> p.isValid())
                && getTieBreakCriteriaSet().size() > 1
                && areFinancersValid()
                && getSupplierSet().size() > 2
                && getProcess().getFileStream(SupplierCriteriaSelectionDocument.class).findAny().orElse(null) != null;
    }

    public static boolean isPresent(final String s) {
        return s != null && !s.isEmpty();
    }

    public boolean isJuryValid() {
        final int[] counts = new int[] { 0, 0, 0, 0 };
        getJuryMemberSet().forEach(m -> {
            final JuryMemberRole role = m.getJuryMemberRole();
            final int index = role == JuryMemberRole.PRESIDENT ? 0 : role == JuryMemberRole.VOWEL ? 1 : role == JuryMemberRole.SUBSTITUTE ? 2 : -1;
            counts[index]++;
            if (m.getConsultationFromPresidentSubstitute() != null) {
                counts[3]++;
            }
        });
        return counts[0] == 1 && counts[1] > 1 && counts[2] > 1 && counts[3] == 1 && isOdd(counts[0] + counts[1])
                && getJuryMemberSet().stream().map(m -> m.getUser()).count() == getJuryMemberSet().size();
    }

    public boolean areFinancersValid() {
        return getFinancerSet().stream().map(f -> f.getUnit()).distinct().count() == getFinancerSet().size()
                && getFinancerSet().stream().map(f -> f.getValue()).reduce(Money.ZERO, Money::add).equals(getValue());
    }

    private boolean isOdd(final int i) {
        return (i & 1) != 0;
    }

}
