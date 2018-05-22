/*
 * @(#)AcquisitionRequest.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;

import module.finance.util.Address;
import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author João Neves
 * @author Shezad Anavarali
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AcquisitionRequest extends AcquisitionRequest_Base {

    public AcquisitionRequest(final AcquisitionProcess acquisitionProcess, final Person person) {
        super();
        checkParameters(acquisitionProcess, person);
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setAcquisitionProcess(acquisitionProcess);
        setRequester(person);
    }

    private void checkParameters(AcquisitionProcess acquisitionProcess, Person person) {
        if (acquisitionProcess == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.acquisition.request.wrong.acquisition.process");
        }
        if (person == null) {
            throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.anonymousNotAllowedToCreate");
        }
    }

    public AcquisitionRequest(AcquisitionProcess acquisitionProcess, List<Supplier> suppliers, Person person) {
        this(acquisitionProcess, person);
        for (Supplier supplier : suppliers) {
            addSuppliers(supplier);
        }
    }

    public AcquisitionRequest(AcquisitionProcess acquisitionProcess, Supplier supplier, Person person) {
        this(acquisitionProcess, person);
        addSuppliers(supplier);
    }

    public void edit(Supplier supplier, Unit requestingUnit, Boolean isRequestingUnitPayingUnit) {
        addSuppliers(supplier);
        setRequestingUnit(requestingUnit);
        if (isRequestingUnitPayingUnit) {
            addFinancers(requestingUnit.finance(this));
        }
    }

    public AcquisitionRequestItem createAcquisitionRequestItem(final AcquisitionRequest acquisitionRequest,
            final String description, final Integer quantity, final Money unitValue, final BigDecimal vatValue,
            final Money additionalCostValue, final String proposalReference, CPVReference reference, String recipient,
            Address address, String phone, String email, AcquisitionItemClassification classification) {
        return new AcquisitionRequestItem(acquisitionRequest, description, quantity, unitValue, vatValue, additionalCostValue,
                proposalReference, reference, recipient, address, phone, email, classification);
    }

    public AcquisitionRequestItem createAcquisitionRequestItem(final AcquisitionRequest acquisitionRequest,
            final String description, final Integer quantity, final Money unitValue, final BigDecimal vatValue,
            final Money additionalCostValue, final String proposalReference, Material material, String recipient, Address address,
            String phone, String email, AcquisitionItemClassification classification) {
        return new AcquisitionRequestItem(acquisitionRequest, description, quantity, unitValue, vatValue, additionalCostValue,
                proposalReference, material, recipient, address, phone, email, classification);
    }

    public String getFiscalIdentificationCode() {
        if (getSuppliers().size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder("");
        Iterator<Supplier> suppliersIterator = getSuppliersSet().iterator();
        while (suppliersIterator.hasNext()) {
            builder.append(suppliersIterator.next().getFiscalIdentificationCode());
            if (suppliersIterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private Money reduce(final Stream<Money> s) {
        return s.reduce(Money.ZERO, Money::add);
    }

    public Money getTotalVatValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalVatValue()));
    }

    public Money getRealTotalVatValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalRealVatValue()).filter(m -> m != null));
    }

    public Money getCurrentValue() {
        Money result = Money.ZERO;
        for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
            Money totalRealValue = acquisitionRequestItem.getTotalRealValue();
            if (totalRealValue != null) {
                result = result.add(totalRealValue);
            } else {
                result = result.add(acquisitionRequestItem.getTotalItemValue());
            }
        }
        return result;
    }

    public Money getCurrentVatValue() {
        Money result = Money.ZERO;
        for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
            Money totalRealVatValue = acquisitionRequestItem.getTotalRealVatValue();
            if (totalRealVatValue != null) {
                result.add(totalRealVatValue);
            } else {
                result.add(getTotalValue());
            }
        }
        return result;
    }

    public Money getTotalAdditionalCostsValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getAdditionalCostValue()).filter(m -> m != null));
    }

    public Money getRealTotalAdditionalCostsValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getRealAdditionalCostValue()).filter(m -> m != null));
    }

    public Money getTotalItemValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalItemValue()));
    }

    public Money getTotalItemValueWithVat() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalItemValueWithVat()));
    }

    public Money getTotalItemValueWithAdditionalCosts() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalItemValueWithAdditionalCosts()));
    }

    public Money getTotalItemValueWithAdditionalCostsAndVat() {
        return getAcquisitionRequestItemStream().map(i -> i.getTotalItemValueWithAdditionalCostsAndVat()).filter(m -> m != null)
                .reduce(Money.ZERO, Money::addAndRound);
    }

    public Money getCurrentTotalItemValueWithAdditionalCostsAndVat() {
        Money result = Money.ZERO;
        for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
            if (acquisitionRequestItem.getTotalRealValueWithAdditionalCostsAndVat() != null) {
                result = result.addAndRound(acquisitionRequestItem.getTotalRealValueWithAdditionalCostsAndVat());
            } else {
                result = result.addAndRound(acquisitionRequestItem.getTotalItemValueWithAdditionalCostsAndVat());
            }
        }
        return result;
    }

    @Override
    public Money getRealTotalValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalRealValue()).filter(m -> m != null));
    }

    public Money getRealTotalValueWithVat() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getTotalRealValueWithVat()));
    }

    public Money getRealTotalValueWithAdditionalCosts() {
        return reduce(
                getAcquisitionRequestItemStream().map(i -> i.getTotalRealValueWithAdditionalCosts()).filter(m -> m != null));
    }

    public Money getRealTotalValueWithAdditionalCostsAndVat() {
        return getAcquisitionRequestItemStream().map(i -> i.getTotalRealValueWithAdditionalCostsAndVat()).filter(m -> m != null)
                .reduce(Money.ZERO, Money::addAndRound);
    }

    public Money getCurrentTotalAdditionalCostsValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getCurrentAdditionalCostValue()).filter(m -> m != null));
    }

    public Money getCurrentTotalValueWithAdditionalCosts() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getCurrentTotalItemValueWithAdditionalCosts()));
    }

    public Money getCurrentTotalVatValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getCurrentTotalVatValue()));
    }

    public Money getCurrentSupplierAllocationValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getCurrentSupplierAllocationValue()));
    }

    public Money getCurrentTotalValue() {
        return reduce(getAcquisitionRequestItemStream().map(i -> i.getCurrentTotalItemValueWithAdditionalCostsAndVat()));
    }

    public Money getCurrentTotalRoundedValue() {
        return getAcquisitionRequestItemStream().map(i -> i.getCurrentTotalItemValueWithAdditionalCostsAndVat())
                .reduce(Money.ZERO, Money::addAndRound);
    }

    public void processReceivedInvoice() {
        if (!isRealCostAvailableForAtLeastOneItem()) {
            copyEstimateValuesToRealValues();
        }
    }

    public boolean isRealCostAvailableForAtLeastOneItem() {
        return getAcquisitionRequestItemStream().anyMatch(i -> i.getRealQuantity() != null && i.getRealUnitValue() != null);
    }

    private void copyEstimateValuesToRealValues() {
        getAcquisitionRequestItemStream().forEach(i -> copyEstimateValuesToRealValues(i));
    }

    private void copyEstimateValuesToRealValues(AcquisitionRequestItem item) {
        item.setRealQuantity(item.getQuantity());
        item.setRealUnitValue(item.getUnitValue());
        item.setRealVatValue(item.getVatValue());
        item.setRealAdditionalCostValue(item.getAdditionalCostValue());

        for (UnitItem unitItem : item.getUnitItems()) {
            unitItem.setRealShareValue(unitItem.getShareValue());
        }
    }

    /*
     * TODO: As soon as we have a decent implementation for the CT75000 this
     * code should be rolled back again.
     */
    public boolean isFilled() {
        AcquisitionProcess process = getProcess();
        return hasAnyRequestItems() && ((!process.isSimplifiedProcedureProcess() && hasAcquisitionProcess()) || (process
                .isSimplifiedProcedureProcess()
                && (((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000
                        || process.hasAcquisitionProposalDocument() || ((SimplifiedProcedureProcess) process).hasInvoiceFile())));
    }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
        if (getAcquisitionRequestItemStream().anyMatch(i -> !i.isValueFullyAttributedToUnits())) {
            return false;
        }
        for (final Financer financer : getFinancersSet()) {
            final Money amountAllocated = financer.getAmountAllocated();
            if (Money.ZERO.equals(amountAllocated)) {
                return false;
            }
        }

        return true;
    }

    public boolean isApprovedByAtLeastOneResponsible() {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            for (final UnitItem unitItem : requestItem.getUnitItems()) {
                if (unitItem.getSubmitedForFundsAllocation().booleanValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAuthorizedByAtLeastOneResponsible() {
        return getAcquisitionRequestItemStream().anyMatch(i -> i.hasAtLeastOneResponsibleApproval());
    }

    public boolean hasAtLeastOneConfirmation() {
        return getAcquisitionRequestItemStream().anyMatch(i -> i.hasAtLeastOneInvoiceConfirmation());
    }

    public boolean isAnyInvoiceConfirmedBy(Person person) {
        return getAcquisitionRequestItemStream().anyMatch(i -> !i.getConfirmedInvoices(person).isEmpty());
    }

    public void confirmInvoiceFor(Person person) {
        getAcquisitionRequestItemStream().forEach(i -> i.confirmInvoiceBy(person));
    }

    public void unconfirmInvoiceFor(Person person) {
        getAcquisitionRequestItemStream().forEach(i -> i.unconfirmInvoiceBy(person));
    }

    public void unconfirmInvoiceForAll(final AcquisitionInvoice acquisitionInvoice) {
        getAcquisitionRequestItemStream().forEach(i -> i.unconfirmInvoiceForAll(acquisitionInvoice));
    }

    public boolean isInvoiceConfirmedBy() {
        return !getAcquisitionRequestItemStream()
                .anyMatch(i -> !Money.ZERO.equals(i.getRealValue()) && !i.isConfirmForAllInvoices());
    }

    public Money getValueAllocated() {
        if (getAcquisitionProcess().isActive()) {
            return (getAcquisitionProcess().getAcquisitionProcessState()
                    .hasBeenAllocatedPermanently()) ? getRealTotalValue() : getTotalItemValue();
        }
        return Money.ZERO;
    }

    public boolean isValueAllowed(Money value) {
        Money totalItemValue = getTotalItemValue();
        Money totalValue = totalItemValue.add(value);
        return totalValue.isLessThanOrEqual(getAcquisitionProcess().getAcquisitionRequestValueLimit());
    }

    @Override
    public void unSubmitForFundsAllocation() {
        getAcquisitionRequestItemStream().forEach(i -> i.unapprove());
    }

    public boolean checkRealValues() {
        return isRealTotalValueEqualsRealShareValue() && isRealUnitShareValueLessThanUnitShareValue()
                && isRealValueLessThanTotalValue();
    }

    public boolean isCurrentTotalRealValueFullyDistributed() {
        for (RequestItem requestItem : getRequestItems()) {
            if (!requestItem.isCurrentRealValueFullyAttributedToUnits()) {
                return false;
            }
        }
        return true;
    }

    public Money getTotalRealShareValue() {
        Money res = Money.ZERO;
        for (Financer financer : getFinancersSet()) {
            res = res.add(financer.getRealShareValue());
        }
        return res;
    }

    public boolean isRealValueLessThanTotalValue() {
        return getRealTotalValue().isLessThanOrEqual(getTotalItemValue());
    }

    public boolean isRealUnitShareValueLessThanUnitShareValue() {
        for (Financer financer : getFinancersSet()) {
            if (!financer.isRealUnitShareValueLessThanUnitShareValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean isRealTotalValueEqualsRealShareValue() {
        return getRealTotalValueWithAdditionalCostsAndVat().equals(getTotalRealShareValue());
    }

    public String getAcquisitionProcessId() {
        return getAcquisitionProcess().getAcquisitionProcessId();
    }

    public String getAcquisitionProposalDocumentId() {
        AcquisitionProcess process = getProcess();

        if (process.hasAcquisitionProposalDocument()) {
            return process.getAcquisitionProposalDocument().getProposalId();
        }
        return null;
    }

    public boolean isFundAllocationAllowed(final CPVReference cpvReference, final Money totalValue) {
        final boolean checkSupplierLimitsByCPV = ExpenditureTrackingSystem.getInstance().checkSupplierLimitsByCPV();

        for (Supplier supplier : getSuppliers()) {
            if ((checkSupplierLimitsByCPV && !supplier.isFundAllocationAllowed(cpvReference.getCode(), totalValue)
                    || (!checkSupplierLimitsByCPV && !supplier.isFundAllocationAllowed(totalValue)))) {
                return false;
            }
        }
        return true;
    }

    public Supplier getSupplier() {
        return getSelectedSupplier();
    }

    public String getRefundeeName() {
        final Person refundee = getRefundee();
        return refundee == null ? null : refundee.getUser().getName();
    }

    /**
     * @deprecated use getAcquisitionRequestItemStream instead
     */
    @Deprecated
    public Set<AcquisitionRequestItem> getAcquisitionRequestItemsSet() {
        return (HashSet<AcquisitionRequestItem>) addAcquisitionRequestItemsSetToArg(new HashSet<AcquisitionRequestItem>());
    }

    public Stream<AcquisitionRequestItem> getAcquisitionRequestItemStream() {
        return getRequestItemsSet().stream().map(i -> (AcquisitionRequestItem) i);
    }

    public Collection<AcquisitionRequestItem> addAcquisitionRequestItemsSetToArg(
            final Collection<AcquisitionRequestItem> collection) {
        for (final RequestItem item : getRequestItems()) {
            collection.add((AcquisitionRequestItem) item);
        }
        return collection;
    }

    @Override
    public AcquisitionProcess getProcess() {
        return getAcquisitionProcess();
    }

    public boolean isPayed() {
        final String reference = getPaymentReference();
        return reference != null && !reference.isEmpty();
    }

    public boolean hasAdditionalCosts() {
        return getAcquisitionRequestItemStream().anyMatch(i -> hasAdditionalCosts(i));
    }

    private boolean hasAdditionalCosts(AcquisitionRequestItem i) {
        final Money additionalCost = i.getAdditionalCostValue();
        return additionalCost != null && additionalCost.isGreaterThan(Money.ZERO);
    }

    @Override
    public SortedSet<AcquisitionRequestItem> getOrderedRequestItemsSet() {
        return (SortedSet<AcquisitionRequestItem>) addAcquisitionRequestItemsSetToArg(
                new TreeSet<AcquisitionRequestItem>(AcquisitionRequestItem.COMPARATOR_BY_REFERENCE));
    }

    public void validateInvoiceNumber(String invoiceNumber) {
        for (PaymentProcessInvoice invoice : getInvoices()) {
            if (invoice.getInvoiceNumber().equals(invoiceNumber)) {
                throw new DomainException(Bundle.ACQUISITION, "acquisitionProcess.message.exception.InvoiceWithSameNumber");
            }
        }
    }

    public boolean isInAllocationPeriod() {
        final AcquisitionProcess acquisitionProcess = getAcquisitionProcess();
        final Integer year = acquisitionProcess.getYear().intValue();
        final int i = Calendar.getInstance().get(Calendar.YEAR);
        return year == i || year == i - 1 || year == i - 2;
    }

    public Set<CPVReference> getCPVReferences() {
        return getAcquisitionRequestItemStream().map(i -> i.getCPVReference()).collect(Collectors.toSet());
    }

    public void createFundAllocationRequest(final boolean isFinalFundAllocation) {
        for (final Financer financer : getFinancersSet()) {
            financer.createFundAllocationRequest(isFinalFundAllocation);
        }
    }

    public void cancelFundAllocationRequest(final boolean isFinalFundAllocation) {
        for (final Financer financer : getFinancersSet()) {
            financer.cancelFundAllocationRequest(isFinalFundAllocation);
        }
    }

    public boolean isCommitted() {
        if (!hasAnyFinancers()) {
            return false;
        }
        for (final Financer financer : getFinancersSet()) {
            if (!financer.isCommitted()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPendingCommitmentByUser(final User user) {
        if (!hasAnyFinancers()) {
            return false;
        }
        for (final Financer financer : getFinancersSet()) {
            //if (!financer.isCommitted() && ExpenditureTrackingSystem.getInstance().isFundCommitmentManagerGroupMember(user)) {
            if (!financer.isCommitted() && financer.isAccountingEmployee(user.getExpenditurePerson())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCommitmentByUser(final User user) {
        if (!hasAnyFinancers()) {
            return false;
        }
        for (final Financer financer : getFinancersSet()) {
            //if (financer.isCommitted() && ExpenditureTrackingSystem.getInstance().isFundCommitmentManagerGroupMember(user)) {
            if (financer.isCommitted() && financer.isAccountingEmployee(user.getExpenditurePerson())) {
                return true;
            }
        }
        return false;
    }

    public String getCommitmentNumbers() {
        final Set<String> numbers = new HashSet<String>();

        final StringBuilder builder = new StringBuilder();
        for (final Financer financer : getFinancersSet()) {
            final String commitmentNumber = financer.getCommitmentNumber();
            if (commitmentNumber != null && !commitmentNumber.isEmpty()) {
                if (!numbers.contains(commitmentNumber)) {
                    numbers.add(commitmentNumber);
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(commitmentNumber);
                }
            }
        }
        return builder.toString();
    }

    @Override
    public boolean hasProposalDocument() {
        return getProcess().hasAcquisitionProposalDocument();
    }

    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        Money goodsValue = Money.ZERO;
        Money servicesValue = Money.ZERO;
        Money fixedAssetsValue = Money.ZERO;
        for (final RequestItem requestItem : getRequestItemsSet()) {
            final AcquisitionItemClassification classification = requestItem.getClassification();
            final Money realValue = requestItem.getRealValue();
            if (realValue != null) {
                if (classification == AcquisitionItemClassification.GOODS) {
                    goodsValue = goodsValue.add(realValue);
                } else if (classification == AcquisitionItemClassification.SERVICES) {
                    servicesValue = servicesValue.add(realValue);
                } else if (classification == AcquisitionItemClassification.FIXED_ASSETS) {
                    fixedAssetsValue = fixedAssetsValue.add(realValue);
                } else {
                    throw new Error("unreachable.code");
                }
            }
        }
        if (goodsValue.isZero() && servicesValue.isZero() && fixedAssetsValue.isZero()) {
            return null;
        }
        if (goodsValue.isGreaterThan(servicesValue)) {
            return goodsValue.isGreaterThan(
                    fixedAssetsValue) ? AcquisitionItemClassification.GOODS : AcquisitionItemClassification.FIXED_ASSETS;
        }
        return servicesValue.isGreaterThan(
                fixedAssetsValue) ? AcquisitionItemClassification.SERVICES : AcquisitionItemClassification.FIXED_ASSETS;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Supplier> getSuppliers() {
        return getSuppliersSet();
    }

    @Deprecated
    public boolean hasAnySuppliers() {
        return !getSuppliersSet().isEmpty();
    }

    @Deprecated
    public boolean hasPaymentReference() {
        return getPaymentReference() != null;
    }

    @Deprecated
    public boolean hasContractSimpleDescription() {
        return getContractSimpleDescription() != null;
    }

    @Deprecated
    public boolean hasSelectedSupplier() {
        return getSelectedSupplier() != null;
    }

    @Deprecated
    public boolean hasAcquisitionProcess() {
        return getAcquisitionProcess() != null;
    }

    @Deprecated
    public boolean hasRefundee() {
        return getRefundee() != null;
    }

    public boolean hasRequestItemForCPV(final CPVReference cpvReference) {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            if (requestItem.getCPVReference() == cpvReference) {
                return true;
            }
        }
        return false;
    }

}
