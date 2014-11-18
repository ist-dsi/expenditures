/*
 * @(#)WorkingCapitalAction.java
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
package module.workingCapital.presentationTier.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.finance.util.Money;
import module.organization.domain.Party;
import module.organization.domain.Person;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workingCapital.domain.AcquisitionClassification;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalTransaction;
import module.workingCapital.domain.WorkingCapitalYear;
import module.workingCapital.domain.util.AcquisitionClassificationBean;
import module.workingCapital.domain.util.WorkingCapitalInitializationBean;
import module.workingCapital.presentationTier.action.util.WorkingCapitalContext;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;

@Mapping(path = "/workingCapital")
/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalAction extends BaseAction {

    public ActionForward frontPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        WorkingCapitalContext workingCapitalContext = getRenderedObject("workingCapitalContext");
        if (workingCapitalContext == null) {
            workingCapitalContext = new WorkingCapitalContext();
        }
        return frontPage(request, workingCapitalContext);
    }

    public ActionForward frontPage(final HttpServletRequest request, final WorkingCapitalContext workingCapitalContext) {
        request.setAttribute("workingCapitalContext", workingCapitalContext);
        return forward("/workingCapital/frontPage.jsp");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapitalContext workingCapitalContext = getRenderedObject("workingCapitalInitializationBean");
        if (workingCapitalContext.getParty() == null) {
            request.setAttribute("workingCapitalYearOid", workingCapitalContext.getWorkingCapitalYear().getExternalId());
            return listProcesses(mapping, form, request, response);
        }
        final SortedSet<WorkingCapitalProcess> unitProcesses = workingCapitalContext.getWorkingCapitalSearchByUnit();
        return showList(request, workingCapitalContext, unitProcesses);
    }

    public ActionForward sort(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapitalContext workingCapitalContext = new WorkingCapitalContext();
        final String partyId = request.getParameter("partyId");
        if (partyId != null && !partyId.isEmpty() && !partyId.equals("null")) {
            workingCapitalContext.setParty((Party) getDomainObject(request, partyId));
            final SortedSet<WorkingCapitalProcess> unitProcesses = workingCapitalContext.getWorkingCapitalSearchByUnit();
            return showList(request, workingCapitalContext, unitProcesses);
        }
        return listProcesses(mapping, form, request, response);
    }

    private ActionForward showList(final HttpServletRequest request, final WorkingCapitalContext workingCapitalContext,
            final SortedSet<WorkingCapitalProcess> unitProcesses) {
        if (unitProcesses.size() == 1) {
            final WorkingCapitalProcess workingCapitalProcess = unitProcesses.first();
            return ProcessManagement.forwardToProcess(workingCapitalProcess);
        } else {
            final List<WorkingCapitalProcess> list = new ArrayList<WorkingCapitalProcess>(unitProcesses);
            final String sortByArg = request.getParameter("sortBy");
            if (sortByArg != null && !sortByArg.isEmpty()) {
                final int i = sortByArg.indexOf('=');
                if (i > 0) {
                    final BeanComparator comparator = new BeanComparator(sortByArg.substring(0, i));
                    Collections.sort(list, comparator);
                    final char c = sortByArg.charAt(i + 1);
                    if (c == 'd' || c == 'D') {
                        Collections.reverse(list);
                    }
                }
            }
            request.setAttribute("unitProcesses", list);
            return frontPage(request, workingCapitalContext);
        }
    }

    public ActionForward listProcesses(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapitalContext workingCapitalContext = new WorkingCapitalContext();
        final WorkingCapitalYear workingCapitalYear = getDomainObject(request, "workingCapitalYearOid");
        workingCapitalContext.setWorkingCapitalYear(workingCapitalYear);
        final SortedSet<WorkingCapitalProcess> unitProcesses = getAllProcesses(workingCapitalYear);
        return showList(request, workingCapitalContext, unitProcesses);
    }

    private SortedSet<WorkingCapitalProcess> getAllProcesses(final WorkingCapitalYear workingCapitalYear) {
        final SortedSet<WorkingCapitalProcess> unitProcesses =
                new TreeSet<WorkingCapitalProcess>(WorkingCapitalProcess.COMPARATOR_BY_UNIT_NAME);
        for (final WorkingCapital workingCapital : workingCapitalYear.getWorkingCapitalsSet()) {
            final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
            if (workingCapitalProcess.isAccessibleToCurrentUser()) {
                unitProcesses.add(workingCapitalProcess);
            }
        }
        return unitProcesses;
    }

    public ActionForward configuration(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        request.setAttribute("currentWorkingCapitalSystem", workingCapitalSystem);
        return forward("/workingCapital/configuration.jsp");
    }

    public ActionForward createNewSystem(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        WorkingCapitalSystem.createSystem();

        return configuration(mapping, form, request, response);
    }

    public ActionForward prepareCreateWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalInitializationBean workingCapitalInitializationBean = new WorkingCapitalInitializationBean();
        return prepareCreateWorkingCapitalInitialization(request, workingCapitalInitializationBean);
    }

    public ActionForward prepareCreateWorkingCapitalInitialization(final HttpServletRequest request,
            final WorkingCapitalInitializationBean workingCapitalInitializationBean) {
        request.setAttribute("workingCapitalInitializationBean", workingCapitalInitializationBean);
        return forward("/workingCapital/createWorkingCapitalInitialization.jsp");
    }

    public ActionForward createWorkingCapitalInitialization(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalInitializationBean workingCapitalInitializationBean = getRenderedObject();
        try {
            final WorkingCapitalInitialization workingCapitalInitialization = workingCapitalInitializationBean.create();
            final WorkingCapital workingCapital = workingCapitalInitialization.getWorkingCapital();
            final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
            return viewWorkingCapital(request, workingCapitalProcess);
        } catch (final DomainException domainException) {
            RenderUtils.invalidateViewState();
            addLocalizedMessage(request, BundleUtil.getString("resources.WorkingCapitalResources",
                    domainException.getKey()));
            return prepareCreateWorkingCapitalInitialization(request, workingCapitalInitializationBean);
        }
    }

    public ActionForward viewWorkingCapital(final HttpServletRequest request, final WorkingCapitalProcess workingCapitalProcess) {
        return ProcessManagement.forwardToProcess(workingCapitalProcess);
    }

    public ActionForward viewWorkingCapital(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final WorkingCapital workingCapital = getDomainObject(request, "workingCapitalOid");
        final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();
        return viewWorkingCapital(request, workingCapitalProcess);
    }

    public ActionForward configureManagementUnit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        request.setAttribute("workingCapitalSystem", workingCapitalSystem);
        return forward("/workingCapital/configureManagementUnit.jsp");
    }

    public ActionForward configureAcquisitionLimit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        request.setAttribute("workingCapitalSystem", workingCapitalSystem);
        return forward("/workingCapital/configureAcquisitionLimit.jsp");
    }

    public ActionForward resetAcquisitionLimit(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        workingCapitalSystem.resetAcquisitionValueLimit();
        return configuration(mapping, form, request, response);
    }

    public ActionForward prepareAddAcquisitionClassification(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AcquisitionClassificationBean acquisitionClassificationBean = new AcquisitionClassificationBean();
        request.setAttribute("acquisitionClassificationBean", acquisitionClassificationBean);
        return forward("/workingCapital/addAcquisitionClassification.jsp");
    }

    public ActionForward addAcquisitionClassification(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AcquisitionClassificationBean acquisitionClassificationBean = getRenderedObject();
        acquisitionClassificationBean.create();
        return configuration(mapping, form, request, response);
    }

    public ActionForward deleteAcquisitionClassification(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AcquisitionClassification acquisitionClassification = getDomainObject(request, "acquisitionClassificationOid");
        acquisitionClassification.delete();
        return configuration(mapping, form, request, response);
    }

    public ActionForward viewWorkingCapitalTransaction(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapitalTransaction workingCapitalTransaction = getDomainObject(request, "workingCapitalTransactionOid");
        request.setAttribute("workingCapitalTransaction", workingCapitalTransaction);
        request.setAttribute("process", workingCapitalTransaction.getWorkingCapital().getWorkingCapitalProcess());
        request.setAttribute("viewWorkingCapitalTransaction", Boolean.TRUE);
        return forward("/module/workingCapital/domain/WorkingCapitalProcess/workingCapitalTransaction.jsp");
    }

    public ActionForward viewAllCapitalInitializations(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final WorkingCapital workingCapital = getDomainObject(request, "workingCapitalOid");
        request.setAttribute("workingCapital", workingCapital);

        return forward("/workingCapital/viewAllWorkingCapitalInitializations.jsp");
    }

    public final ActionForward exportSearchToExcel(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String yearid = request.getParameter("yearOid");
        final String partyid = request.getParameter("party0id");
        final WorkingCapitalContext workingCapitalContext = new WorkingCapitalContext();

        if (!partyid.equals("blank")) {
            workingCapitalContext.setParty((Party) getDomainObject(request, partyid));
        }
        workingCapitalContext.setWorkingCapitalYear((WorkingCapitalYear) getDomainObject(request, yearid));
        final SortedSet<WorkingCapitalProcess> unitProcesses;
        if (workingCapitalContext.getParty() == null) {
            unitProcesses = getAllProcesses(workingCapitalContext.getWorkingCapitalYear());
        } else {
            unitProcesses = workingCapitalContext.getWorkingCapitalSearchByUnit();
        }
        return exportInfoToExcel(unitProcesses, workingCapitalContext, response);
    }

    private ActionForward exportInfoToExcel(Set<WorkingCapitalProcess> processes, WorkingCapitalContext context,
            HttpServletResponse response) throws Exception {

        final Integer year = context.getWorkingCapitalYear().getYear();
        SheetData<WorkingCapitalProcess> sheetData = new SheetData<WorkingCapitalProcess>(processes) {
            @Override
            protected void makeLine(WorkingCapitalProcess workingCapitalProcess) {
                if (workingCapitalProcess == null) {
                    return;
                }
                final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
                final WorkingCapitalInitialization initialization = workingCapital.getWorkingCapitalInitialization();
                final AccountingUnit accountingUnit = workingCapital.getAccountingUnit();

                addCell(getLocalizedMessate("label.module.workingCapital.year"), year);
                addCell(getLocalizedMessate("label.module.workingCapital"), workingCapitalProcess.getWorkingCapital().getUnit()
                        .getPresentationName());
                addCell(getLocalizedMessate("WorkingCapitalProcessState"), workingCapitalProcess
                        .getPresentableAcquisitionProcessState().getLocalizedName());
                addCell(getLocalizedMessate("label.module.workingCapital.unit.responsible"), getUnitResponsibles(workingCapital));
                addCell(getLocalizedMessate("label.module.workingCapital.initialization.accountingUnit"),
                        accountingUnit == null ? "" : accountingUnit.getName());

                addCell(getLocalizedMessate("label.module.workingCapital.requestingDate"), initialization.getRequestCreation()
                        .toString("yyyy-MM-dd HH:mm:ss"));
                addCell(getLocalizedMessate("label.module.workingCapital.requester"), initialization.getRequestor().getName());
                final Person movementResponsible = workingCapital.getMovementResponsible();
                addCell(getLocalizedMessate("label.module.workingCapital.movementResponsible"),
                        movementResponsible == null ? "" : movementResponsible.getName());
                addCell(getLocalizedMessate("label.module.workingCapital.fiscalId"), initialization.getFiscalId());
                addCell(getLocalizedMessate("label.module.workingCapital.internationalBankAccountNumber"),
                        initialization.getInternationalBankAccountNumber());
                addCell(getLocalizedMessate("label.module.workingCapital.fundAllocationId"), initialization.getFundAllocationId());
                final Money requestedAnualValue = initialization.getRequestedAnualValue();
                addCell(getLocalizedMessate("label.module.workingCapital.requestedAnualValue.requested"), requestedAnualValue);
                addCell(getLocalizedMessate("label.module.workingCapital.requestedMonthlyValue.requested"),
                        requestedAnualValue.divideAndRound(new BigDecimal(6)));
                final Money authorizedAnualValue = initialization.getAuthorizedAnualValue();
                addCell(getLocalizedMessate("label.module.workingCapital.authorizedAnualValue"),
                        authorizedAnualValue == null ? "" : authorizedAnualValue);
                final Money maxAuthorizedAnualValue = initialization.getMaxAuthorizedAnualValue();
                addCell(getLocalizedMessate("label.module.workingCapital.maxAuthorizedAnualValue"),
                        maxAuthorizedAnualValue == null ? "" : maxAuthorizedAnualValue);
                final DateTime lastSubmission = initialization.getLastSubmission();
                addCell(getLocalizedMessate("label.module.workingCapital.initialization.lastSubmission"),
                        lastSubmission == null ? "" : lastSubmission.toString("yyyy-MM-dd"));
                final DateTime refundRequested = initialization.getRefundRequested();
                addCell(getLocalizedMessate("label.module.workingCapital.initialization.refundRequested"),
                        refundRequested == null ? "" : refundRequested.toString("yyyy-MM-dd"));

                final WorkingCapitalTransaction lastTransaction = workingCapital.getLastTransaction();
                if (lastTransaction == null) {
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.accumulatedValue"), Money.ZERO);
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.balance"), Money.ZERO);
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.debt"), Money.ZERO);
                } else {
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.accumulatedValue"),
                            lastTransaction.getAccumulatedValue());
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.balance"), lastTransaction.getBalance());
                    addCell(getLocalizedMessate("label.module.workingCapital.transaction.debt"), lastTransaction.getDebt());
                }

                for (final AcquisitionClassification classification : WorkingCapitalSystem.getInstance()
                        .getAcquisitionClassificationsSet()) {
                    final String description = classification.getDescription();
                    final String pocCode = classification.getPocCode();
                    final String key = pocCode + " - " + description;

                    final Money value = calculateValueForClassification(workingCapital, classification);

                    addCell(key, value);
                }

            }

            private Money calculateValueForClassification(final WorkingCapital workingCapital,
                    final AcquisitionClassification classification) {
                Money result = Money.ZERO;
                for (final WorkingCapitalTransaction transaction : workingCapital.getWorkingCapitalTransactionsSet()) {
                    if (transaction.isAcquisition()) {
                        final WorkingCapitalAcquisitionTransaction acquisitionTransaction =
                                (WorkingCapitalAcquisitionTransaction) transaction;
                        final WorkingCapitalAcquisition acquisition = acquisitionTransaction.getWorkingCapitalAcquisition();
                        final AcquisitionClassification acquisitionClassification = acquisition.getAcquisitionClassification();
                        if (acquisitionClassification == classification) {
                            result = result.add(acquisition.getValueAllocatedToSupplier());
                        }
                    }
                }
                return result;
            }

            private String getUnitResponsibles(final WorkingCapital workingCapital) {
                final StringBuilder builder = new StringBuilder();
                final SortedMap<Person, Set<Authorization>> authorizations = workingCapital.getSortedAuthorizations();
                if (!authorizations.isEmpty()) {
                    for (final Entry<Person, Set<Authorization>> entry : authorizations.entrySet()) {
                        if (builder.length() > 0) {
                            builder.append("; ");
                        }
                        builder.append(entry.getKey().getName());
                    }
                }

                return builder.toString();
            }

        };

        final LocalDate currentLocalDate = new LocalDate();
        final SpreadsheetBuilder builder = new SpreadsheetBuilder();
        builder.addConverter(Money.class, new CellConverter() {

            @Override
            public Object convert(final Object source) {
                final Money money = (Money) source;
                return money == null ? null : new Double(money.getValue().round(new MathContext(2, RoundingMode.HALF_EVEN)).doubleValue());
            }

        });

        builder.addSheet(getLocalizedMessate("label.module.workingCapital") + " " + year + " - " + currentLocalDate.toString(),
                sheetData);

        final List<WorkingCapitalTransaction> transactions = new ArrayList<WorkingCapitalTransaction>();
        for (final WorkingCapitalProcess process : processes) {
            final WorkingCapital workingCapital = process.getWorkingCapital();
            transactions.addAll(workingCapital.getSortedWorkingCapitalTransactions());
        }
        final SheetData<WorkingCapitalTransaction> transactionsSheet = new SheetData<WorkingCapitalTransaction>(transactions) {
            @Override
            protected void makeLine(WorkingCapitalTransaction transaction) {
                final WorkingCapital workingCapital = transaction.getWorkingCapital();
                final WorkingCapitalProcess workingCapitalProcess = workingCapital.getWorkingCapitalProcess();

                addCell(getLocalizedMessate("label.module.workingCapital.year"), year);
                addCell(getLocalizedMessate("label.module.workingCapital"), workingCapitalProcess.getWorkingCapital().getUnit()
                        .getPresentationName());
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.number"), transaction.getNumber());
                addCell(getLocalizedMessate("WorkingCapitalProcessState.CANCELED"),
                        transaction.isCanceledOrRejected() ? getLocalizedMessate("label.yes") : getLocalizedMessate("label.no"));
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.description") + " "
                        + getLocalizedMessate("label.module.workingCapital.transaction.number"), transaction.getDescription());
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.approval"), approvalLabel(transaction));
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.verification"),
                        verificationLabel(transaction));
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.value"), transaction.getValue());
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.accumulatedValue"),
                        transaction.getAccumulatedValue());
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.balance"), transaction.getBalance());
                addCell(getLocalizedMessate("label.module.workingCapital.transaction.debt"), transaction.getDebt());

                if (transaction.isAcquisition()) {
                    final WorkingCapitalAcquisitionTransaction acquisitionTx = (WorkingCapitalAcquisitionTransaction) transaction;
                    final WorkingCapitalAcquisition acquisition = acquisitionTx.getWorkingCapitalAcquisition();
                    final AcquisitionClassification acquisitionClassification = acquisition.getAcquisitionClassification();
                    final String economicClassification = acquisitionClassification.getEconomicClassification();
                    final String pocCode = acquisitionClassification.getPocCode();
                    addCell(getLocalizedMessate("label.module.workingCapital.configuration.acquisition.classifications.economicClassification"),
                            economicClassification);
                    addCell(getLocalizedMessate("label.module.workingCapital.configuration.acquisition.classifications.pocCode"),
                            pocCode);
                    addCell(getLocalizedMessate("label.module.workingCapital.configuration.acquisition.classifications.description"),
                            acquisition.getDescription());
                }
            }

            private Object verificationLabel(final WorkingCapitalTransaction transaction) {
                if (transaction.isAcquisition()) {
                    final WorkingCapitalAcquisitionTransaction acquisition = (WorkingCapitalAcquisitionTransaction) transaction;
                    if (acquisition.getWorkingCapitalAcquisition().getVerified() != null) {
                        return getLocalizedMessate("label.verified");
                    }
                    if (acquisition.getWorkingCapitalAcquisition().getNotVerified() != null) {
                        return getLocalizedMessate("label.notVerified");
                    }
                }
                return "-";
            }

            private String approvalLabel(final WorkingCapitalTransaction transaction) {
                if (transaction.isAcquisition()) {
                    final WorkingCapitalAcquisitionTransaction acquisition = (WorkingCapitalAcquisitionTransaction) transaction;
                    if (acquisition.getWorkingCapitalAcquisition().getApproved() != null) {
                        return getLocalizedMessate("label.approved");
                    }
                    if (acquisition.getWorkingCapitalAcquisition().getRejectedApproval() != null) {
                        return getLocalizedMessate("label.rejected");
                    }
                }
                return "-";
            }

        };
        builder.addSheet(getLocalizedMessate("label.module.workingCapital.transactions"), transactionsSheet);

        return streamSpreadsheet(response, "FundosManeio_" + year + "-" + currentLocalDate.getDayOfMonth() + "-"
                + currentLocalDate.getMonthOfYear() + "-" + currentLocalDate.getYear(), builder);

    }

    private String getLocalizedMessate(String msg) {
        return BundleUtil.getString(WorkingCapital.bundleResource, msg);
    }

    private ActionForward streamSpreadsheet(final HttpServletResponse response, final String fileName,
            final SpreadsheetBuilder spreadSheetBuilder) throws IOException {
        response.setContentType("application/xls ");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

        ServletOutputStream outputStream = response.getOutputStream();

        spreadSheetBuilder.build(WorkbookExportFormat.EXCEL, outputStream);
        outputStream.flush();
        outputStream.close();

        return null;
    }

}
