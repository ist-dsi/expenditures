/*
 * @(#)Supplier.java
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;

import module.finance.util.Address;
import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateSupplierBean;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class Supplier extends Supplier_Base /* implements Indexable, Searchable */ implements Comparable<Supplier> {

    private Supplier() {
        super();
        setBennu(Bennu.getInstance());
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Supplier(String fiscalCode) {
        this();
        if (fiscalCode == null || fiscalCode.length() == 0) {
            throw new DomainException(Bundle.EXPENDITURE, "error.fiscal.code.cannot.be.empty");
        }
        setFiscalIdentificationCode(fiscalCode);
    }

    public Supplier(String name, String abbreviatedName, String fiscalCode, String nib) {
        this(fiscalCode);
        setName(name);
        setAbbreviatedName(abbreviatedName);
        setNib(nib);
    }

    @Override
    @Atomic
    public void delete() {
        if (checkIfCanBeDeleted()) {
            setBennu(null);
            setExpenditureTrackingSystem(null);
            super.delete();
        }
    }

    @Override
    protected boolean checkIfCanBeDeleted() {
        return !hasAnyAcquisitionRequests() && !hasAnyAcquisitionsAfterTheFact() && !hasAnyRefundInvoices()
                && !hasAnyAnnouncements() && !hasAnySupplierSearches() && !hasAnyPossibleAcquisitionRequests()
                && !hasAnyRefundItems() && super.checkIfCanBeDeleted();
    }

    public static Supplier readSupplierByFiscalIdentificationCode(String fiscalIdentificationCode) {
        for (Supplier supplier : Bennu.getInstance().getSuppliersSet()) {
            if (supplier.getFiscalIdentificationCode().equals(fiscalIdentificationCode)) {
                return supplier;
            }
        }
        return null;
    }

    public static Supplier readSupplierByName(final String name) {
        for (Supplier supplier : Bennu.getInstance().getSuppliersSet()) {
            if (supplier.getName().equalsIgnoreCase(name)) {
                return supplier;
            }
        }
        return null;
    }

    @Deprecated
    public Money getTotalAllocated() {
        Money result = getAllocated();
        for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && acquisitionProcess.isAllocatedToSupplier()) {
                    result = result.add(acquisitionRequest.getValueAllocated());
                }
            }
        }
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod()) {
                if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                    result = result.add(acquisitionAfterTheFact.getValue());
                }
            }
        }
        for (final RefundableInvoiceFile refundInvoice : getActiveRefundInvoicesSet()) {
            if (refundInvoice.isInAllocationPeriod()) {
                final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundInvoice.getRefundableValue());
                }
            }
        }
        for (final RefundItem refundItem : getRefundItemSet()) {
            if (refundItem.isInAllocationPeriod() && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundItem.getValueEstimation());
                }
            }
        }
        return result;
    }

    @Deprecated
    public Money getTotalAllocated(final CPVReference cpvReference) {
        Money result = getAllocated();
        for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && acquisitionProcess.isAllocatedToSupplier()) {
                    final boolean hasBeenAllocatedPermanently =
                            acquisitionProcess.getAcquisitionProcessState().hasBeenAllocatedPermanently();
                    for (final RequestItem requestItem : acquisitionRequest.getRequestItemsSet()) {
                        if (requestItem.getCPVReference().getCode().equals(cpvReference.getCode())) {
                            final Money valueToAdd = hasBeenAllocatedPermanently ? requestItem
                                    .getTotalRealAssigned() : requestItem.getTotalAssigned();
                            result = result.add(valueToAdd);
                        }
                    }
                }
            }
        }
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod()) {
                if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                    // TODO : eventually check CPV stuff
                    result = result.add(acquisitionAfterTheFact.getValue());
                }
            }
        }
        for (final RefundableInvoiceFile refundInvoice : getActiveRefundInvoicesSet()) {
            if (refundInvoice.isInAllocationPeriod()
                    && refundInvoice.getRefundItem().getCPVReference().getCode().equals(cpvReference.getCode())) {
                final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundInvoice.getRefundableValue());
                }
            }
        }
        for (final RefundItem refundItem : getRefundItemSet()) {
            if (refundItem.getCPVReference().getCode().equals(cpvReference.getCode()) && refundItem.isInAllocationPeriod()
                    && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundItem.getValueEstimation());
                }
            }
        }
        return result;
    }

    public Map<CPVReference, Money> getUnconfirmedAllocationsByCPVReference() {
        final SortedMap<CPVReference, Money> result = new TreeMap<CPVReference, Money>(CPVReference.COMPARATOR_BY_CODE) {
            @Override
            public Money put(CPVReference cpvReference, Money value) {
                if (!containsKey(cpvReference)) {
                    return super.put(cpvReference, value);
                } else {
                    return super.put(cpvReference, get(cpvReference).add(value));
                }
            }
        };

        for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && !acquisitionProcess.getShouldSkipSupplierFundAllocation()) {
                    for (final RequestItem requestItem : acquisitionRequest.getRequestItemsSet()) {
                        AcquisitionRequestItem acqRequestItem = (AcquisitionRequestItem) requestItem;
                        final Money valueToAdd;
                        if (acquisitionProcess.getAcquisitionProcessState().hasBeenAllocatedPermanently()) {
                            valueToAdd = acqRequestItem.getTotalRealValue();
                        } else {
                            valueToAdd = acqRequestItem.getTotalItemValue();
                        }
                        result.put(requestItem.getCPVReference(), valueToAdd);
                    }
                }
            }
        }
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod() && !acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                result.put(acquisitionAfterTheFact.getCpvReference(), acquisitionAfterTheFact.getValue());
            }
        }
        for (final RefundableInvoiceFile refundInvoice : getActiveRefundInvoicesSet()) {
            if (refundInvoice.isInAllocationPeriod()) {
                final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result.put(refundInvoice.getRefundItem().getCPVReference(), refundInvoice.getRefundableValue());
                }
            }
        }
        for (final RefundItem refundItem : getRefundItemSet()) {
            if (refundItem.isInAllocationPeriod() && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result.put(refundItem.getCPVReference(), refundItem.getValueEstimation());
                }
            }
        }

        return result;
    }

    public Map<CPVReference, Money> getAllocationsByCPVReference() {
        final SortedMap<CPVReference, Money> result = new TreeMap<CPVReference, Money>(CPVReference.COMPARATOR_BY_CODE) {
            @Override
            public Money put(CPVReference cpvReference, Money value) {
                if (!containsKey(cpvReference)) {
                    return super.put(cpvReference, value);
                } else {
                    return super.put(cpvReference, get(cpvReference).add(value));
                }
            }
        };

        for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
            if (acquisitionRequest.isInAllocationPeriod()) {
                final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
                if (acquisitionProcess.isActive() && acquisitionProcess.isAllocatedToSupplier()) {
                    for (final RequestItem requestItem : acquisitionRequest.getRequestItemsSet()) {
                        AcquisitionRequestItem acqRequestItem = (AcquisitionRequestItem) requestItem;
                        final Money valueToAdd;
                        if (acquisitionProcess.getAcquisitionProcessState().hasBeenAllocatedPermanently()) {
                            valueToAdd = acqRequestItem.getTotalRealValue();
                        } else {
                            valueToAdd = acqRequestItem.getTotalItemValue();
                        }
                        result.put(requestItem.getCPVReference(), valueToAdd);
                    }
                }
            }
        }
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod() && !acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                result.put(acquisitionAfterTheFact.getCpvReference(), acquisitionAfterTheFact.getValue());
            }
        }
        for (final RefundableInvoiceFile refundInvoice : getActiveRefundInvoicesSet()) {
            if (refundInvoice.isInAllocationPeriod()) {
                final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result.put(refundInvoice.getRefundItem().getCPVReference(), refundInvoice.getRefundableValue());
                }
            }
        }
        for (final RefundItem refundItem : getRefundItemSet()) {
            if (refundItem.isInAllocationPeriod() && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result.put(refundItem.getCPVReference(), refundItem.getValueEstimation());
                }
            }
        }

        return result;
    }

    @Deprecated
    public Money getSoftTotalAllocated() {
        Money result = getAllocated();
        result = result.add(getTotalAllocatedByAcquisitionProcesses(true));

        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod()) {
                if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                    result = result.add(acquisitionAfterTheFact.getValue());
                }
            }
        }
        for (final RefundableInvoiceFile refundInvoice : getActiveRefundInvoicesSet()) {
            if (refundInvoice.isInAllocationPeriod()) {
                final RefundProcess refundProcess = refundInvoice.getRefundItem().getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundInvoice.getRefundableValue());
                }
            }
        }
        for (final RefundItem refundItem : getRefundItemSet()) {
            if (refundItem.isInAllocationPeriod() && refundItem.getInvoicesFilesSet().isEmpty()) {
                final RefundProcess refundProcess = refundItem.getRequest().getProcess();
                if (refundProcess.isActive() && !refundProcess.getShouldSkipSupplierFundAllocation()) {
                    result = result.add(refundItem.getValueEstimation());
                }
            }
        }
        return result;
    }

    private Money getTotalAllocatedByAcquisitionProcesses(boolean allProcesses) {
        Money result = Money.ZERO;
        for (final AcquisitionRequest acquisitionRequest : getAcquisitionRequestsSet()) {
            if ((allProcesses && !acquisitionRequest.getProcess().getShouldSkipSupplierFundAllocation())
                    || acquisitionRequest.getAcquisitionProcess().isAllocatedToSupplier()) {
                if (acquisitionRequest.isInAllocationPeriod()) {
                    result = result.add(acquisitionRequest.getValueAllocated());
                }
            }
        }
        return result;
    }

    public Money getTotalAllocatedByAcquisitionProcesses() {
        return getTotalAllocatedByAcquisitionProcesses(false);
    }

    public Money getTotalAllocatedByAfterTheFactAcquisitions(final AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
        Money result = Money.ZERO;
        for (final AcquisitionAfterTheFact acquisitionAfterTheFact : getAcquisitionsAfterTheFactSet()) {
            if (acquisitionAfterTheFact.isInAllocationPeriod()) {
                if (!acquisitionAfterTheFact.getDeletedState().booleanValue()) {
                    if (acquisitionAfterTheFact.getAfterTheFactAcquisitionType() == afterTheFactAcquisitionType) {
                        result = result.add(acquisitionAfterTheFact.getValue());
                    }
                }
            }
        }
        return result;
    }

    public Money getTotalAllocatedByPurchases() {
        return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.PURCHASE);
    }

    public Money getTotalAllocatedByWorkingCapitals() {
        return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.WORKING_CAPITAL);
    }

    public Money getTotalAllocatedByRefunds() {
        return getTotalAllocatedByAfterTheFactAcquisitions(AfterTheFactAcquisitionType.REFUND);
    }

    @Atomic
    public static Supplier createNewSupplier(final CreateSupplierBean createSupplierBean) {
        final Supplier supplier = new Supplier(createSupplierBean.getName(), createSupplierBean.getAbbreviatedName(),
                createSupplierBean.getFiscalIdentificationCode(), createSupplierBean.getNib());
        supplier.registerContact(createSupplierBean.getAddress(), createSupplierBean.getPhone(), createSupplierBean.getFax(),
                createSupplierBean.getEmail());
        return supplier;
    }

    @Override
    public boolean isFundAllocationAllowed(final Money value) {
        final Money totalAllocated = getTotalAllocated();
        final Money totalValue = totalAllocated; // .add(value);
        return totalValue.isLessThan(SUPPLIER_LIMIT) && totalValue.isLessThan(getSupplierLimit());
    }

    @Override
    public boolean isFundAllocationAllowed(final String cpvReference, final Money value) {
        final Money totalAllocated = getTotalAllocated(CPVReference.getCPVCode(cpvReference));
        final Money totalValue = totalAllocated; // .add(value);
        return totalValue.isLessThan(SUPPLIER_LIMIT) && totalValue.isLessThan(getSupplierLimit());
    }

    @Atomic
    public void merge(final Supplier supplier) {
        if (supplier != this) {
            final Set<AcquisitionAfterTheFact> acquisitionAfterTheFacts = supplier.getAcquisitionsAfterTheFactSet();
            getAcquisitionsAfterTheFactSet().addAll(acquisitionAfterTheFacts);
            acquisitionAfterTheFacts.clear();

            final Set<RefundableInvoiceFile> refundInvoices = supplier.getActiveRefundInvoicesSet();
            getRefundInvoicesSet().addAll(refundInvoices);
            refundInvoices.clear();

            final Set<RefundItem> refundItems = supplier.getRefundItemSet();
            getRefundItemSet().addAll(refundItems);
            refundItems.clear();

            final Set<CCPAnnouncement> announcements = supplier.getAnnouncementsSet();
            getAnnouncementsSet().addAll(announcements);
            announcements.clear();

            final Set<SavedSearch> savedSearches = supplier.getSupplierSearchesSet();
            getSupplierSearchesSet().addAll(savedSearches);
            savedSearches.clear();

            final Set<AcquisitionRequest> acquisitionRequests = supplier.getAcquisitionRequestsSet();
            getAcquisitionRequestsSet().addAll(acquisitionRequests);
            acquisitionRequests.clear();

            final Set<AcquisitionRequest> possibleAcquisitionRequests = supplier.getPossibleAcquisitionRequestsSet();
            getPossibleAcquisitionRequestsSet().addAll(possibleAcquisitionRequests);
            possibleAcquisitionRequests.clear();

            supplier.getSupplierContactSet().forEach(sc -> sc.setSupplier(this));
            supplier.getProvisionsSet().forEach(p -> p.setSupplier(this));

            if (getFiscalIdentificationCode() == null || getFiscalIdentificationCode().isEmpty()) {
                setFiscalIdentificationCode(supplier.getFiscalIdentificationCode());
            }
            if (getName() == null || getName().isEmpty()) {
                setName(supplier.getName());
            }
            if (getAbbreviatedName() == null || getAbbreviatedName().isEmpty()) {
                setAbbreviatedName(supplier.getAbbreviatedName());
            }
            if (getNib() == null || getNib().isEmpty()) {
                setNib(supplier.getNib());
            }
            if (getGiafKey() == null || getGiafKey().isEmpty()) {
                setGiafKey(supplier.getGiafKey());
            }
            if (getSapKey() == null || getSapKey().isEmpty()) {
                setSapKey(supplier.getSapKey());
            }

            supplier.delete();
        }
    }

/*
@Override
public IndexDocument getDocumentToIndex() {
IndexDocument indexDocument = new IndexDocument(this);
if (!StringUtils.isEmpty(getFiscalIdentificationCode())) {
indexDocument.indexField(SupplierIndexes.FISCAL_CODE, getFiscalIdentificationCode());
}
indexDocument.indexField(SupplierIndexes.NAME, StringNormalizer.normalize(getName()));
return indexDocument;
}

@Override
public Set<Indexable> getObjectsToIndex() {
Set<Indexable> set = new HashSet<Indexable>();
set.add(this);
return set;
}
*/

    @Override
    public Address getAddress() {
        return !getSupplierContactSet().isEmpty() ? getSupplierContactSet().iterator().next().getAddress() : super.getAddress();
    }

    @Override
    public String getPhone() {
        return !getSupplierContactSet().isEmpty() ? getSupplierContactSet().iterator().next().getPhone() : super.getPhone();
    }

    @Override
    public String getFax() {
        return !getSupplierContactSet().isEmpty() ? getSupplierContactSet().iterator().next().getFax() : super.getFax();
    }

    @Override
    public String getEmail() {
        return getSupplierContactSet().stream()
            .map(c -> c.getEmail())
            .filter(e -> e != null && !e.isEmpty())
            .findAny().orElseGet(() -> super.getEmail());
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest> getAcquisitionRequests() {
        return getAcquisitionRequestsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getSupplierSearches() {
        return getSupplierSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact> getAcquisitionsAfterTheFact() {
        return getAcquisitionsAfterTheFactSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile> getRefundInvoices() {
        return getActiveRefundInvoicesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest> getPossibleAcquisitionRequests() {
        return getPossibleAcquisitionRequestsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.announcements.CCPAnnouncement> getAnnouncements() {
        return getAnnouncementsSet();
    }

    @Deprecated
    public boolean hasAnyAcquisitionRequests() {
        return !getAcquisitionRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnySupplierSearches() {
        return !getSupplierSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAcquisitionsAfterTheFact() {
        return !getAcquisitionsAfterTheFactSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRefundInvoices() {
        return !getActiveRefundInvoicesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPossibleAcquisitionRequests() {
        return !getPossibleAcquisitionRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAnnouncements() {
        return !getAnnouncementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    private boolean hasAnyRefundItems() {
        return !getRefundItemSet().isEmpty();
    }

    private Stream<MultipleSupplierConsultation> getMultipleSupplierConsultationProcessStream() {
        return getMultipleSupplierConsultationSet().stream().filter(c -> c.getProcess().getState().isActive())
                .filter(c -> c.getProcess().isInAllocationPeriod());
    }

    private Money calculateAllocationAmount(final Stream<MultipleSupplierConsultation> stream) {
        return stream.flatMap(c -> c.getPartSet().stream()).map(p -> p.getTotalAllocatedToSupplier(this)).reduce(Money.ZERO,
                Money::add);
    }

    public Money getTotalPermanentAllocatedForMultipleSupplierConsultation() {
        return getMultipleSupplierConsultationPartSet().stream()
                .filter(p -> p.getConsultation().getProcess().getState().isActive())
                .filter(p -> p.getConsultation().getProcess().isInAllocationPeriod())
                .map(p -> p.getTotalAllocatedToSupplier(this)).reduce(Money.ZERO, Money::add);
    }

    public Money getTotalAllocatedForMultipleSupplierConsultation() {
        return calculateAllocationAmount(
                getMultipleSupplierConsultationProcessStream().filter(c -> c.getProcess().getState().isAllocationState()));
    }

    public Money getTotalAllocatedAndPendingForMultipleSupplierConsultation() {
        return calculateAllocationAmount(getMultipleSupplierConsultationProcessStream());
    }

    public boolean isMultipleSupplierLimitAllocationAvailable() {
        final Money totalAllocated = getTotalAllocatedForMultipleSupplierConsultation();
        return totalAllocated.isLessThan(getMultipleSupplierLimit()) && totalAllocated.isLessThan(MULTIPLE_SUPPLIER_LIMIT);
    }

    @Override
    public int compareTo(final Supplier o) {
        final int c = Collator.getInstance().compare(getPresentationName(), o.getPresentationName());
        return c == 0 ? getExternalId().compareTo(o.getExternalId()) : c;
    }

    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile> getActiveRefundInvoicesSet() {
        return getRefundInvoicesSet().stream().filter(i -> !i.isArchieved()).collect(Collectors.toSet());
    }

}
