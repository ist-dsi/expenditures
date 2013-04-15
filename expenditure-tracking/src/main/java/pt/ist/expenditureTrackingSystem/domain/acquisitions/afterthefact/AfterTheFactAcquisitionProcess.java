/*
 * @(#)AfterTheFactAcquisitionProcess.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.LabelLog;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Invoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.DeleteAfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactAcquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactProcessActivityInformation.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.fenixframework.Atomic;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "label.process.afterTheFactAcquisition")
/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AfterTheFactAcquisitionProcess extends AfterTheFactAcquisitionProcess_Base {

    private static List<WorkflowActivity<AfterTheFactAcquisitionProcess, ? extends ActivityInformation<AfterTheFactAcquisitionProcess>>> activities =
            new ArrayList<WorkflowActivity<AfterTheFactAcquisitionProcess, ? extends ActivityInformation<AfterTheFactAcquisitionProcess>>>();

    static {
        activities.add(new EditAfterTheFactAcquisition());
        activities.add(new DeleteAfterTheFactAcquisitionProcess());
    }

    protected AfterTheFactAcquisitionProcess() {
        super();
        new AcquisitionAfterTheFact(this);
    }

    private static final ThreadLocal<AfterTheFactAcquisitionProcessBean> threadLocal =
            new ThreadLocal<AfterTheFactAcquisitionProcessBean>();

    @Atomic
    public static AfterTheFactAcquisitionProcess createNewAfterTheFactAcquisitionProcess(
            AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
        threadLocal.set(afterTheFactAcquisitionProcessBean);
        final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess = new AfterTheFactAcquisitionProcess();
        afterTheFactAcquisitionProcess.edit(afterTheFactAcquisitionProcessBean.getAfterTheFactAcquisitionType(),
                afterTheFactAcquisitionProcessBean.getValue(), afterTheFactAcquisitionProcessBean.getVatValue(),
                afterTheFactAcquisitionProcessBean.getSupplier(), afterTheFactAcquisitionProcessBean.getDescription());
        final Person loggedPerson = Person.getLoggedPerson();
        new LabelLog(afterTheFactAcquisitionProcess, loggedPerson.getUser(), "label."
                + afterTheFactAcquisitionProcess.getClass().getName() + ".Create", "resources/AcquisitionResources");
        return afterTheFactAcquisitionProcess;
    }

    @Override
    protected int getYearForConstruction() {
        return threadLocal.get().getYear().intValue();
    }

    public void edit(AfterTheFactAcquisitionType type, Money value, BigDecimal vatValue, Supplier supplier, String description) {
        final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
        acquisitionAfterTheFact.edit(type, value, vatValue, supplier, description);
    }

    public void delete() {
        final AcquisitionAfterTheFact acquisitionAfterTheFact = getAcquisitionAfterTheFact();
        acquisitionAfterTheFact.delete();
    }

    @Override
    public boolean isAvailableForPerson(final Person person) {
        final User user = person.getUser();
        return ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user)
                || ExpenditureTrackingSystem.isAcquisitionCentralManagerGroupMember(user);
    }

    public void cancel() {
        getAcquisitionAfterTheFact().setDeletedState(Boolean.TRUE);
    }

    public void renable() {
        getAcquisitionAfterTheFact().setDeletedState(Boolean.FALSE);
    }

    @Override
    public boolean hasAnyAvailableActivitity() {
        List<WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>>> activeActivities = getActiveActivities();
        return !activeActivities.isEmpty();
    }

    @Override
    public void allocateFundsToUnit() {
        // do nothing
    }

    @Override
    public void submitForApproval() {
        // nothing to do here...
    }

    @Override
    public void submitForFundAllocation() {
        // nothing to do here...
    }

    @Override
    public boolean isInAllocatedToUnitState() {
        return false;
    }

    @Override
    protected void authorize() {
        // nothing to do here...
    }

    @Override
    public boolean isAppiableForYear(final int year) {
        return getAcquisitionAfterTheFact().isAppiableForYear(year);
    }

    public AfterTheFactInvoice receiveInvoice(String filename, byte[] bytes, String invoiceNumber, LocalDate invoiceDate) {

        final AfterTheFactInvoice invoice = hasInvoice() ? getInvoice() : new AfterTheFactInvoice(this);
        invoice.setFilename(filename);
        invoice.setContent(bytes);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceDate(invoiceDate);

        return invoice;
    }

    public String getInvoiceNumber() {
        Invoice invoice = getInvoice();
        return invoice != null ? invoice.getInvoiceNumber() : null;
    }

    public LocalDate getInvoiceDate() {
        Invoice invoice = getInvoice();
        return invoice != null ? invoice.getInvoiceDate() : null;
    }

    public AfterTheFactInvoice getInvoice() {
        List<AfterTheFactInvoice> files = getFiles(AfterTheFactInvoice.class);
        if (files.size() > 1) {
            throw new DomainException("error.should.only.have.one.invoice");
        }
        return files.isEmpty() ? null : files.get(0);
    }

    public boolean hasInvoice() {
        return !getFiles(Invoice.class).isEmpty();
    }

    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return (List<T>) activities;
    }

    @Override
    public boolean isTicketSupportAvailable() {
        return false;
    }

    @Override
    public boolean isFileSupportAvailable() {
        return true;
    }

    @Override
    public boolean isObserverSupportAvailable() {
        return false;
    }

    @Override
    public boolean isCommentsSupportAvailable() {
        return false;
    }

    @Override
    public List<Class<? extends ProcessFile>> getAvailableFileTypes() {
        List<Class<? extends ProcessFile>> availableFileTypes = super.getAvailableFileTypes();
        availableFileTypes.add(AfterTheFactInvoice.class);
        return availableFileTypes;
    }

    @Override
    public boolean isActive() {
        return !isCanceled();
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/AcquisitionResources", "label.AfterTheFactAcquisitionProcess");
    }

    @Override
    public User getProcessCreator() {
        return getExecutionLogs(LabelLog.class).iterator().next().getActivityExecutor();
    }

    @Override
    public Money getTotalValue() {
        return getAcquisitionAfterTheFact().getValue();
    }

    @Override
    public Set<CPVReference> getCPVReferences() {
        return Collections.emptySet();
    }

    @Override
    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        return null;
    }

    @Deprecated
    public boolean hasAcquisitionAfterTheFact() {
        return getAcquisitionAfterTheFact() != null;
    }

    @Deprecated
    public boolean hasImportFile() {
        return getImportFile() != null;
    }

}
