/*
 * @(#)AcquisitionRequestItem.java
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import module.finance.util.Address;
import module.finance.util.Money;
import module.workflow.domain.ProcessFile;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author João Neves
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AcquisitionRequestItem extends AcquisitionRequestItem_Base {

    public static final Comparator<AcquisitionRequestItem> COMPARATOR_BY_REFERENCE = new Comparator<AcquisitionRequestItem>() {

        @Override
        public int compare(final AcquisitionRequestItem acquisitionRequestItem1,
                final AcquisitionRequestItem acquisitionRequestItem2) {
            final int c =
                    acquisitionRequestItem1.getProposalReference().compareTo(acquisitionRequestItem2.getProposalReference());
            return c == 0 ? acquisitionRequestItem1.getExternalId().compareTo(acquisitionRequestItem2.getExternalId()) : c;
        }

    };

    protected AcquisitionRequestItem() {
        super();
    }

    private AcquisitionRequestItem(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final String proposalReference, CPVReference reference) {

        this();
        checkLimits(acquisitionRequest, quantity, unitValue);

        setRequest(acquisitionRequest);
        setDescription(description);
        setQuantity(quantity);
        setUnitValue(unitValue);
        setVatValue(vatValue);
        setProposalReference(proposalReference);
        setCPVReference(reference);
    }

    public AcquisitionRequestItem(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final Money additionalCostValue, final String proposalReference,
            CPVReference reference, String recipient, Address address, String phone, String email,
            AcquisitionItemClassification classification) {
        this(acquisitionRequest, description, quantity, unitValue, vatValue, proposalReference, reference);
        setAdditionalCostValue(additionalCostValue);
        setRecipient(recipient);
        setAddress(address);
        setRecipientEmail(email);
        setRecipientPhone(phone);
        setClassification(classification);

        createUnitItem();

        for (final ProcessFile processFile : acquisitionRequest.getProcess().getFilesSet()) {
            if (processFile instanceof AcquisitionInvoice && !processFile.isArchieved()) {
                final AcquisitionInvoice acquisitionInvoice = (AcquisitionInvoice) processFile;
                acquisitionInvoice.addRequestItems(this);
            }
        }
    }

    public AcquisitionRequestItem(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final Money additionalCostValue, final String proposalReference,
            Material material, String recipient, Address address, String phone, String email,
            AcquisitionItemClassification classification) {
        this(acquisitionRequest, description, quantity, unitValue, vatValue, additionalCostValue, proposalReference,
                material.getMaterialCpv(), recipient, address, phone, email, classification);
        setMaterial(material);
    }

    private void checkLimits(AcquisitionRequest acquisitionRequest, Integer quantity, Money unitValue) {
        Money totalValue = unitValue.multiply(quantity.longValue());

        if (getUnitValue() != null && getQuantity() != null) {
            Money currentValue = getUnitValue().multiply(getQuantity().longValue());
            totalValue = totalValue.subtract(currentValue);
        }

        if (!checkAcquisitionRequestValueLimit(acquisitionRequest, totalValue)) {
            throw new DomainException(Bundle.ACQUISITION, "acquisitionRequestItem.message.exception.totalValueExceed",
                    acquisitionRequest.getAcquisitionProcess().getAcquisitionRequestValueLimit().toFormatString());
        }

        if (!acquisitionRequest.getAcquisitionProcess().getShouldSkipSupplierFundAllocation()
                && !checkSupplierFundAllocation(acquisitionRequest, totalValue)) {
            throw new DomainException(Bundle.ACQUISITION, "acquisitionRequestItem.message.exception.fundAllocationNotAllowed");
        }
    }

    private boolean checkAcquisitionRequestValueLimit(AcquisitionRequest acquisitionRequest, Money totalValue) {
        return acquisitionRequest.isValueAllowed(totalValue);
    }

    private boolean checkSupplierFundAllocation(AcquisitionRequest acquisitionRequest, Money totalValue) {
        final CPVReference cpvReference = getCPVReference();
        return acquisitionRequest.isFundAllocationAllowed(cpvReference, totalValue);
    }

    private void createUnitItem() {
        if (getAcquisitionRequest().getFinancers().size() == 1) {
            createUnitItem(getAcquisitionRequest().getFinancers().iterator().next(),
                    getTotalItemValueWithAdditionalCostsAndVat());
        }
    }

    public Money getTotalItemValueWithAdditionalCosts() {
        if (getAdditionalCostValue() == null) {
            return getTotalItemValue();
        }
        return getTotalItemValue().add(getAdditionalCostValue());
    }

    public Money getTotalItemValueWithAdditionalCostsAndVat() {
        return getAdditionalCostValue() != null ? getTotalItemValueWithVat()
                .add(getAdditionalCostValue()) : getTotalItemValueWithVat();
    }

    public Money getTotalItemValue() {
        return getUnitValue().multiply(getQuantity());
    }

    public Money getTotalRealValue() {
        if (getRealUnitValue() == null || getRealQuantity() == null) {
            return null;
        }
        return getRealUnitValue().multiply(getRealQuantity());
    }

    public Money getTotalRealValueWithAdditionalCosts() {
        if (getRealUnitValue() == null || getRealQuantity() == null) {
            return null;
        }
        Money totalRealValue = getTotalRealValue();
        return getRealAdditionalCostValue() == null ? totalRealValue : totalRealValue.add(getRealAdditionalCostValue());
    }

    public Money getTotalRealValueWithAdditionalCostsAndVat() {
        return getRealAdditionalCostValue() != null ? (getTotalRealVatValue() != null ? getTotalRealValueWithVat()
                .add(getRealAdditionalCostValue()) : null) : getTotalRealValueWithVat();
    }

    public Money getTotalItemValueWithVat() {
        return getTotalItemValue().addPercentage(getVatValue());
    }

    public Money getTotalRealValueWithVat() {
        return getTotalRealValue() != null ? getTotalRealValue().addPercentage(getRealVatValue()) : null;
    }

    public void edit(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final Money additionalCostValue, final String proposalReference,
            CPVReference reference, String recipient, Address address, String phone, String email,
            AcquisitionItemClassification classification) {

        checkLimits(getAcquisitionRequest(), quantity, unitValue);
        setDescription(description);
        setQuantity(quantity);
        setUnitValue(unitValue);
        setProposalReference(proposalReference);
        setVatValue(vatValue);
        setAdditionalCostValue(additionalCostValue);
        setRecipient(recipient);
        setAddress(address);
        setRecipientEmail(email);
        setRecipientPhone(phone);
        setCPVReference(reference);
        setClassification(classification);
    }

    public void edit(final AcquisitionRequest acquisitionRequest, final String description, final Integer quantity,
            final Money unitValue, final BigDecimal vatValue, final Money additionalCostValue, final String proposalReference,
            Material material, String recipient, Address address, String phone, String email,
            AcquisitionItemClassification classification) {
        edit(acquisitionRequest, description, quantity, unitValue, vatValue, additionalCostValue, proposalReference, material.getMaterialCpv(),
                recipient, address, phone, email, classification);
        setMaterial(material);
    }

    public void editRealValues(Integer realQuantity, Money realUnitValue, Money shipment, BigDecimal realVatValue) {
        setRealQuantity(realQuantity);
        setRealUnitValue(realUnitValue);
        setRealAdditionalCostValue(shipment);
        setRealVatValue(realVatValue);
    }

    @Override
    public void delete() {
        setRequest(null);
        setExpenditureTrackingSystem(null);
        for (; !getUnitItems().isEmpty(); getUnitItems().iterator().next().delete()) {
            ;
        }
        super.delete();
    }

    public boolean isAssignedTo(Unit unit) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit() == unit) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFilledWithRealValues() {
        return getRealQuantity() != null && getRealUnitValue() != null
                && (getAdditionalCostValue() == null || getRealAdditionalCostValue() != null);
    }

    @Override
    public void createUnitItem(Unit unit, Money shareValue) {
        createUnitItem(getAcquisitionRequest().addPayingUnit(unit), shareValue);
    }

    public List<Unit> getPayingUnits() {
        List<Unit> payingUnits = new ArrayList<Unit>();
        for (UnitItem unitItem : getUnitItems()) {
            payingUnits.add(unitItem.getUnit());
        }
        return payingUnits;
    }

    public boolean hasAtLeastOneResponsibleApproval() {
        return getUnitItemsSet().stream().anyMatch(i -> i.getItemAuthorized());
    }

    @Override
    public Collection<AcquisitionInvoice> getConfirmedInvoices() {
        return super.getConfirmedInvoices();
    }

    @Override
    public Collection<AcquisitionInvoice> getConfirmedInvoices(Person person) {
        return super.getConfirmedInvoices(person);
    }

    @Override
    public List<AcquisitionInvoice> getUnconfirmedInvoices(Person person) {
        return super.getUnconfirmedInvoices(person);
    }

    public List<AcquisitionInvoice> getAllUnconfirmedInvoices() {
        return super.getUnconfirmedInvoices(null);
    }

    public Money getTotalVatValue() {
        Money percentage = getTotalItemValue().percentage(getVatValue());
        return new Money(percentage.getRoundedValue());
    }

    public Money getTotalRealVatValue() {
        if (getTotalRealValue() == null) {
            return null;
        }

        Money percentage = getTotalRealValue().percentage(getRealVatValue());
        return new Money(percentage.getRoundedValue());
    }

    // replaced with hasBeenApprovedBy()
    // public boolean hasBeenSubmittedForFundsAllocationBy(Person person) {
    // for (UnitItem unitItem : getUnitItems()) {
    // if (unitItem.getUnit().isResponsible(person) &&
    // unitItem.getSubmitedForFundsAllocation()) {
    // return true;
    // }
    // }
    // return false;
    // }

    @Override
    public void unapprove() {
        for (UnitItem unitItem : getUnitItems()) {
            unitItem.setSubmitedForFundsAllocation(false);
        }
    }

    @Override
    public Money getRealValue() {
        return getTotalRealValueWithAdditionalCostsAndVat();
    }

    @Override
    public Money getValue() {
        return getTotalItemValueWithAdditionalCostsAndVat();
    }

    @Override
    public Money getValueWithoutVat() {
        return getTotalItemValueWithAdditionalCosts();
    }

    public AcquisitionRequest getAcquisitionRequest() {
        return (AcquisitionRequest) getRequest();
    }

    public Integer getCurrentQuantity() {
        final Integer realQuantity = getRealQuantity();
        return realQuantity == null ? getQuantity() : realQuantity;
    }

    public Money getCurrentUnitValue() {
        final Money realUnitValue = getRealUnitValue();
        return realUnitValue == null ? getUnitValue() : realUnitValue;
    }

    public Money getCurrentSupplierAllocationValue() {
        final Money currentUnitValue = getCurrentUnitValue();
        return currentUnitValue.multiply(getCurrentQuantity());
    }

    public BigDecimal getCurrentVatValue() {
        final BigDecimal realVatValue = getRealVatValue();
        return realVatValue == null ? getVatValue() : realVatValue;
    }

    public Money getCurrentAdditionalCostValue() {
        final Money realAdditionalCostValue = getRealAdditionalCostValue();
        return realAdditionalCostValue == null ? getAdditionalCostValue() : realAdditionalCostValue;
    }

    public Money getCurrentTotalVatValue() {
        final Money totalRealVatValue = getTotalRealVatValue();
        return totalRealVatValue == null ? getTotalVatValue() : totalRealVatValue;
    }

    public Money getCurrentTotalItemValueWithAdditionalCostsAndVat() {
        final Money totalRealItemValueWithAdditionalCostsAndVat = getTotalRealValueWithAdditionalCostsAndVat();
        return totalRealItemValueWithAdditionalCostsAndVat == null ? getTotalItemValueWithAdditionalCostsAndVat() : totalRealItemValueWithAdditionalCostsAndVat;
    }

    public Money getCurrentTotalItemValueWithAdditionalCosts() {
        final Money totalRealItemValueWithAdditionalCosts = getTotalRealValueWithAdditionalCosts();
        return totalRealItemValueWithAdditionalCosts == null ? getTotalItemValueWithAdditionalCosts() : totalRealItemValueWithAdditionalCosts;
    }

    @Override
    public Money getTotalAmountForCPV(final int year) {
        return isAppliableForCPV(year) ? getCurrentSupplierAllocationValue() : Money.ZERO;
    }

    private boolean isAppliableForCPV(final int year) {
        final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
        final AcquisitionProcess acquisitionProcess = acquisitionRequest.getProcess();
        return acquisitionProcess.isActive() && acquisitionProcess.isAppiableForYear(year);
    }

    @Deprecated
    public boolean hasQuantity() {
        return getQuantity() != null;
    }

    @Deprecated
    public boolean hasUnitValue() {
        return getUnitValue() != null;
    }

    @Deprecated
    public boolean hasVatValue() {
        return getVatValue() != null;
    }

    @Deprecated
    public boolean hasAdditionalCostValue() {
        return getAdditionalCostValue() != null;
    }

    @Deprecated
    public boolean hasProposalReference() {
        return getProposalReference() != null;
    }

    @Deprecated
    public boolean hasRecipient() {
        return getRecipient() != null;
    }

    @Deprecated
    public boolean hasRecipientPhone() {
        return getRecipientPhone() != null;
    }

    @Deprecated
    public boolean hasRecipientEmail() {
        return getRecipientEmail() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasRealQuantity() {
        return getRealQuantity() != null;
    }

    @Deprecated
    public boolean hasRealVatValue() {
        return getRealVatValue() != null;
    }

    @Deprecated
    public boolean hasRealUnitValue() {
        return getRealUnitValue() != null;
    }

    @Deprecated
    public boolean hasRealAdditionalCostValue() {
        return getRealAdditionalCostValue() != null;
    }

}
