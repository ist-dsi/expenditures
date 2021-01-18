/*
 * @(#)WorkingCapitalAcquisition.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain;

import module.finance.domain.Supplier;
import module.finance.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalAcquisition extends WorkingCapitalAcquisition_Base {

    public WorkingCapitalAcquisition() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
    }

    public WorkingCapitalAcquisition(final WorkingCapital workingCapital, final String documentNumber, final Supplier supplier,
            final String description, final AcquisitionClassification acquisitionClassification, final Money valueWithoutVat, final Money money,
            final byte[] invoiceContent, final String displayName, final String fileName, final boolean rapid,
            final Boolean researchAndDevelopmentPurpose) {
        this();
        setWorkingCapital(workingCapital);
        edit(documentNumber, supplier, description, acquisitionClassification, valueWithoutVat, researchAndDevelopmentPurpose);
        Money limit = getWorkingCapitalSystem().getAcquisitionValueLimit();
        final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction;

        if ((limit != null) && (money.compareTo(limit) == 1)) {
            workingCapitalAcquisitionTransaction = new ExceptionalWorkingCapitalAcquisitionTransaction(this, money);
        } else {
            workingCapitalAcquisitionTransaction = new WorkingCapitalAcquisitionTransaction(this, money);
        }
        if (invoiceContent != null) {
            WorkingCapitalInvoiceFile workingCapitalInvoiceFile =
                    new WorkingCapitalInvoiceFile(displayName, fileName, invoiceContent, workingCapitalAcquisitionTransaction);
            workingCapital.getWorkingCapitalProcess().addFiles(workingCapitalInvoiceFile);
        }
        setRapid(rapid);
        setResearchAndDevelopmentPurpose(researchAndDevelopmentPurpose);
    }

    public void edit(String documentNumber, Supplier supplier, String description, AcquisitionClassification acquisitionClassification,
            Money valueWithoutVat, Boolean researchAndDevelopmentPurpose) {
        setDocumentNumber(documentNumber);
        setAcquisitionClassification(acquisitionClassification);
        setSupplier(supplier);
        setDescription(description);
        setValueWithoutVat(valueWithoutVat);
        setResearchAndDevelopmentPurpose(researchAndDevelopmentPurpose);
    }

    public void edit(String documentNumber, Supplier supplier, String description, AcquisitionClassification acquisitionClassification,
            Money valueWithoutVat, Money value, Boolean researchAndDevelopmentPurpose) {
        edit(documentNumber, supplier, description, acquisitionClassification, valueWithoutVat, researchAndDevelopmentPurpose);
        final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction =
                getWorkingCapitalAcquisitionTransaction();
        Money limit = getWorkingCapitalSystem().getAcquisitionValueLimit();
        if ((workingCapitalAcquisitionTransaction instanceof ExceptionalWorkingCapitalAcquisitionTransaction) && (limit != null)
                && (value.compareTo(limit) == 1)) {
            ((ExceptionalWorkingCapitalAcquisitionTransaction) workingCapitalAcquisitionTransaction).resetValue(value);
        } else {
            workingCapitalAcquisitionTransaction.resetValue(value);
        }
    }

    public void edit(String documentNumber, Supplier supplier, String description, AcquisitionClassification acquisitionClassification,
            Money valueWithoutVat, Money value, byte[] invoiceContent, String displayName, String fileName, Boolean researchAndDevelopmentPurpose) {
        edit(documentNumber, supplier, description, acquisitionClassification, valueWithoutVat, value, researchAndDevelopmentPurpose);
        getWorkingCapitalAcquisitionTransaction().getInvoice().delete();

        WorkingCapitalInvoiceFile workingCapitalInvoiceFile =
                new WorkingCapitalInvoiceFile(displayName, fileName, invoiceContent, getWorkingCapitalAcquisitionTransaction());
        getWorkingCapital().getWorkingCapitalProcess().addFiles(workingCapitalInvoiceFile);

    }

    public void approve(final User user) {
        setApproved(new DateTime());
        final Money valueForAuthorization = Money.ZERO;
        final WorkingCapital workingCapital = getWorkingCapital();
        final Authorization authorization = workingCapital.findUnitResponsible(user.getPerson(), valueForAuthorization);
        setApprover(authorization);
    }

    public void reject(final User user) {
        setRejectedApproval(new DateTime());
        final Money valueForAuthorization = Money.ZERO;
        final WorkingCapital workingCapital = getWorkingCapital();
        final Authorization authorization = workingCapital.findUnitResponsible(user.getPerson(), valueForAuthorization);
        setApprover(authorization);
    }

    public void unApprove() {
        setApproved(null);
        setApprover(null);
    }

    public void verify(User user) {
        setVerified(new DateTime());
        setVerifier(user.getPerson());
    }

    public void rejectVerify(final User user) {
        setNotVerified(new DateTime());
        setVerifier(user.getPerson());
    }

    public void unVerify() {
        setVerified(null);
        setVerifier(null);
    }

    public boolean isCanceledOrRejected() {
        return (getIsCanceled() != null && getIsCanceled().booleanValue()) || getRejectedApproval() != null
                || getNotVerified() != null;
    }

    public void cancel() {
        setIsCanceled(Boolean.TRUE);
    }

    public Money getValue() {
        return getWorkingCapitalAcquisitionTransaction().getValue();
    }

    @Override
    public Money getValueAllocatedToSupplier() {
        return isCanceledOrRejected() || isRapid() ? Money.ZERO : getValue();
    }

    @Override
    public Money getValueAllocatedToSupplier(final String cpvReference) {
        return isCanceledOrRejected() || isRapid() ? Money.ZERO : getValue();
    }

    @Override
    public Money getValueAllocatedToSupplierForLimit() {
        return isCanceledOrRejected() || isRapid() ? Money.ZERO : getValueWithoutVat();
    }

    private boolean isRapid() {
        return getRapid() != null && getRapid();
    }

    @Deprecated
    public boolean hasDocumentNumber() {
        return getDocumentNumber() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasValueWithoutVat() {
        return getValueWithoutVat() != null;
    }

    @Deprecated
    public boolean hasApproved() {
        return getApproved() != null;
    }

    @Deprecated
    public boolean hasRejectedApproval() {
        return getRejectedApproval() != null;
    }

    @Deprecated
    public boolean hasVerified() {
        return getVerified() != null;
    }

    @Deprecated
    public boolean hasNotVerified() {
        return getNotVerified() != null;
    }

    @Deprecated
    public boolean hasSubmitedForVerification() {
        return getSubmitedForVerification() != null;
    }

    @Deprecated
    public boolean hasIsCanceled() {
        return getIsCanceled() != null;
    }

    @Deprecated
    public boolean hasWorkingCapitalSystem() {
        return getWorkingCapitalSystem() != null;
    }

    @Deprecated
    public boolean hasWorkingCapitalAcquisitionTransaction() {
        return getWorkingCapitalAcquisitionTransaction() != null;
    }

    @Deprecated
    public boolean hasAcquisitionClassification() {
        return getAcquisitionClassification() != null;
    }

    @Deprecated
    public boolean hasWorkingCapital() {
        return getWorkingCapital() != null;
    }

    @Deprecated
    public boolean hasVerifier() {
        return getVerifier() != null;
    }

    @Deprecated
    public boolean hasApprover() {
        return getApprover() != null;
    }
    
    @Override
    public Boolean getResearchAndDevelopmentPurpose() {
        return super.getResearchAndDevelopmentPurpose() != null && super.getResearchAndDevelopmentPurpose();
    }

}
