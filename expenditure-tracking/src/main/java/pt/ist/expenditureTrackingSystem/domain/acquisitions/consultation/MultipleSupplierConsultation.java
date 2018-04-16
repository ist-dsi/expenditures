package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ContractType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Material;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class MultipleSupplierConsultation extends MultipleSupplierConsultation_Base {
    
    public MultipleSupplierConsultation(final MultipleSupplierConsultationProcess process, final String description,
            final Material material, final String justification, final ContractType contractType) {
        setProcess(process);
        edit(description, material, justification, contractType, null, null, null, null, 66, BigDecimal.ZERO, 0);
    }

    public void edit(final String description, final Material material, final String justification,
            final ContractType contractType, final Integer contractDuration, final User contractManager,
            final String supplierCountJustification, final LocalDate proposalDeadline,
            final Integer proposalValidity, final BigDecimal collateral, final Integer numberOfAlternativeProposals) {
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

}
