/*
 * @(#)SearchPaymentProcessesAction.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.ActivityLog;
import module.workflow.domain.WorkflowLog;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Invoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Approve;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.Authorize;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.SubmitForFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.announcements.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.dto.UserSearchBean;
import pt.ist.expenditureTrackingSystem.domain.dto.VariantBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Mapping(path = "/search")
/**
 * 
 * @author João Neves
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class SearchPaymentProcessesAction extends BaseAction {

    private static final int REQUESTS_PER_PAGE = 50;

    private static final String DEFAULT_SORT = "acquisitionProcessId";

    private static final String STATE_SORT = "processStateDescription";

    private static final Comparator<PaymentProcess> STATE_SORT_COMPARATOR = new BeanComparator("processStateOrder");

    private static final ComparatorChain DEFAULT_COMPARATOR = new ComparatorChain();

    static {
        DEFAULT_COMPARATOR.addComparator(new Comparator<PaymentProcess>() {

            @Override
            public int compare(PaymentProcess process1, PaymentProcess process2) {
                return process1.getPaymentProcessYear().getYear().compareTo(process2.getPaymentProcessYear().getYear());
            }

        });
        DEFAULT_COMPARATOR.addComparator(new Comparator<PaymentProcess>() {

            @Override
            public int compare(PaymentProcess process1, PaymentProcess process2) {
                return process1.getAcquisitionProcessNumber().compareTo(process2.getAcquisitionProcessNumber());

            }

        });

    }

    public ActionForward search(final ActionMapping mapping, final HttpServletRequest request, SearchPaymentProcess searchBean,
            boolean advanced) {

        return search(mapping, request, searchBean, advanced, false, getComparator(request));
    }

    private ActionForward search(final ActionMapping mapping, final HttpServletRequest request, SearchPaymentProcess searchBean,
            boolean advanced, boolean skipSearch, Comparator<PaymentProcess> comparator) {
        Person loggedPerson = getLoggedPerson();

        List<PaymentProcess> processes = new ArrayList<PaymentProcess>();
        if (!skipSearch) {
            processes.addAll(searchBean.search());

            Collections.sort(processes, comparator);
        }
        final CollectionPager<SearchPaymentProcess> pager =
                new CollectionPager<SearchPaymentProcess>((Collection) processes, REQUESTS_PER_PAGE);

        request.setAttribute("collectionPager", pager);
        request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);
        request.setAttribute("resultPage", pager.getPage(page));

        request.setAttribute("results", pager.getPage(page));
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("person", loggedPerson);

        UserSearchBean userSearchBean = new UserSearchBean(loggedPerson);
        if (searchBean.isSearchObjectAvailable()) {
            userSearchBean.setSelectedSearch(searchBean.getSavedSearch());
        }
        request.setAttribute("savingName", new VariantBean());
        request.setAttribute("mySearches", userSearchBean);
        request.setAttribute("advanced", advanced);
        request.setAttribute("pagerString", getJumpParameters(searchBean));
        return forward(request, "/acquisitions/search/searchProcesses.jsp");
    }

    public ActionForward searchJump(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SearchPaymentProcess searchBean = materializeBeanFromRequest(request);
        return search(mapping, request, searchBean, false);
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SearchPaymentProcess searchBean = getRenderedObject("searchBean");
        Person loggedPerson = getLoggedPerson();
        if (searchBean == null) {
            searchBean =
                    loggedPerson.hasDefaultSearch() ? new SearchPaymentProcess(loggedPerson.getDefaultSearch()) : new SearchPaymentProcess();
            return search(mapping, request, searchBean, false);
        } else {
            searchBean.setSavedSearch(null);
            return search(mapping, request, searchBean, true);
        }
    }

    public ActionForward exportCurrentSearchToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SearchPaymentProcess searchBean = getRenderedObject("searchBean");
        final Set<PaymentProcess> processes = searchBean.search();

/*	final SearchProcessValues searchProcess = searchBean.getSearchProcess();
	final Integer year = searchBean.getPaymentProcessYear().getYear();
	final String filename = searchProcess == null ? year.toString() : searchProcess.getLocalizedName() + " - " + year;
*/
        final String filename = "res";
        exportInfoToExcel(processes, filename, response);

        return null;
    }

    public ActionForward viewSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        SavedSearch search = getDomainObject(request, "searchOID");
        return search(mapping, request, new SearchPaymentProcess(search), false);
    }

    public ActionForward saveSearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SearchPaymentProcess searchBean = getRenderedObject("beanToSave");
        String name = getRenderedObject("searchName");
        if (name != null && name.length() > 0) {
            searchBean.persistSearch(name);
            RenderUtils.invalidateViewState("searchName");
        } else {
            request.setAttribute("invalidName", true);
        }
        return search(mapping, request, searchBean, true);
    }

    public ActionForward changeSelectedClass(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        SearchPaymentProcess searchBean = getRenderedObject("searchBean");
        RenderUtils.invalidateViewState("searchBean");
        return search(mapping, request, searchBean, true, true, getComparator(request));
    }

    public ActionForward mySearches(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        UserSearchBean bean = getRenderedObject("mySearches");
        SavedSearch search = bean.getSelectedSearch();
        if (search == null) {
            search = getLoggedPerson().getDefaultSearch();
            bean.setSelectedSearch(search);
            RenderUtils.invalidateViewState("mySearches");
        }
        return search(mapping, request, new SearchPaymentProcess(search), false);
    }

    public ActionForward exportMySearchToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        UserSearchBean bean = getRenderedObject("mySearches");
        SavedSearch search = bean.getSelectedSearch();
        SearchPaymentProcess searchBean = new SearchPaymentProcess(search);
        final Set<PaymentProcess> processes = searchBean.search();

        exportInfoToExcel(processes, search.getSearchName(), response);

        return null;
    }

    private void exportInfoToExcel(Set<PaymentProcess> processes, String filename, HttpServletResponse response) {
        filename = filename.replace(' ', '_');
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(filename);
        try {
            ServletOutputStream writer = response.getOutputStream();
            fillXlsInfo(processes, spreadsheet, writer);
            spreadsheet.getWorkbook().write(writer);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void fillXlsInfo(Set<PaymentProcess> processes, StyledExcelSpreadsheet spreadsheet, OutputStream outputStream)
            throws IOException {
        spreadsheet.newRow();
        spreadsheet.addCell(processes.size() + " " + getAcquisitionResourceMessage("label.processes"));
        spreadsheet.newRow();

        setHeaders(spreadsheet);
        TreeSet<PaymentProcess> sortedProcesses =
                new TreeSet<PaymentProcess>(PaymentProcess.COMPARATOR_BY_YEAR_AND_ACQUISITION_PROCESS_NUMBER);
        sortedProcesses.addAll(processes);

        for (PaymentProcess process : sortedProcesses) {
            spreadsheet.newRow();

            spreadsheet.addCell(process.getAcquisitionProcessId());
            spreadsheet.addCell(process.getTypeShortDescription());
            final AcquisitionItemClassification classification = process.getGoodsOrServiceClassification();
            spreadsheet.addCell(classification == null ? " " : classification.getLocalizedName());
            spreadsheet.addCell(process.getSuppliersDescription());
            spreadsheet.addCell(process.getRequest().getRequestItemsSet().size());
            spreadsheet.addCell(process.getProcessStateDescription());
            DateTime date = process.getDateFromLastActivity();
            spreadsheet.addCell((date == null) ? "" : date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear()
                    + " " + date.getHourOfDay() + ":" + date.getMinuteOfHour());
            spreadsheet.addCell(process.getRequest().getRequester().getFirstAndLastName());
            spreadsheet.addCell(process.getRequest().getRequestingUnit().getName());

            final StringBuilder builderAccountingUnit = new StringBuilder();
            final StringBuilder builderUnits = new StringBuilder();
            for (final Financer financer : process.getFinancersWithFundsAllocated()) {
                final AccountingUnit accountingUnit = financer.getAccountingUnit();
                if (accountingUnit != null) {
                    if (builderAccountingUnit.length() > 0) {
                        builderAccountingUnit.append(", ");
                    }
                    builderAccountingUnit.append(accountingUnit.getName());
                }
                final Unit unit = financer.getUnit();
                if (unit != null) {
                    if (builderUnits.length() > 0) {
                        builderUnits.append(", ");
                    }
                    builderUnits.append(unit.getUnit().getAcronym());
                }
            }
            spreadsheet.addCell(builderAccountingUnit.length() == 0 ? " " : builderAccountingUnit.toString());
            spreadsheet.addCell(builderUnits.length() == 0 ? " " : builderUnits.toString());

            final Money totalValue = process.getTotalValue();
            spreadsheet.addCell((totalValue == null ? Money.ZERO : totalValue).toFormatString());

            final StringBuilder fundAllocationNumbers = new StringBuilder();
            final StringBuilder commitmentNumbers = new StringBuilder();
            final StringBuilder efectiveFundAllocationNumbers = new StringBuilder();
            LocalDate invoiceDate = null;
            Money invoiceValue = Money.ZERO;

            if (process instanceof SimplifiedProcedureProcess) {
                SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) process;
                final AcquisitionRequest acquisitionRequest = simplifiedProcedureProcess.getAcquisitionRequest();
                for (PayingUnitTotalBean payingUnitTotal : acquisitionRequest.getTotalAmountsForEachPayingUnit()) {
                    if ((simplifiedProcedureProcess.getFundAllocationPresent())
                            && (payingUnitTotal.getFinancer().isFundAllocationPresent())) {
                        if (fundAllocationNumbers.length() > 0) {
                            fundAllocationNumbers.append(", ");
                        }
                        fundAllocationNumbers.append(payingUnitTotal.getFinancer().getFundAllocationIds().trim());
                    }

                    if (commitmentNumbers.length() > 0) {
                        fundAllocationNumbers.append(", ");
                    }
                    String commitmentNumber = payingUnitTotal.getFinancer().getCommitmentNumber();
                    if (commitmentNumber != null) {
                        commitmentNumber = commitmentNumber.trim();
                        if (commitmentNumber.length() > 0) {
                            commitmentNumbers.append(commitmentNumber);
                        }
                    }

                    if ((simplifiedProcedureProcess.getEffectiveFundAllocationPresent())
                            && (payingUnitTotal.getFinancer().isEffectiveFundAllocationPresent())) {
                        if (efectiveFundAllocationNumbers.length() > 0) {
                            efectiveFundAllocationNumbers.append(", ");
                        }
                        efectiveFundAllocationNumbers
                                .append(payingUnitTotal.getFinancer().getEffectiveFundAllocationIds().trim());
                    }
                }

                boolean hasFullInvoice = false;
                for (final Invoice invoice : acquisitionRequest.getInvoices()) {
//		    final AcquisitionInvoice acquisitionInvoice = (AcquisitionInvoice) invoice;
                    final LocalDate localDate = invoice.getInvoiceDate();
                    if (invoiceDate == null || invoiceDate.isBefore(localDate)) {
                        invoiceDate = localDate;
                    }

                    hasFullInvoice = true;
//		    if (!hasFullInvoice) {
//			final String confirmationReport = acquisitionInvoice.getConfirmationReport();
//			if (confirmationReport == null) {
//			    hasFullInvoice = true;
//			} else {
//			    for (int i = 0; i < confirmationReport.length(); ) {
//				final int ulli = confirmationReport.indexOf("<ul><li>", i);
//				final int q = confirmationReport.indexOf(" - Quantidade:", ulli);
//				final int ulliClose = confirmationReport.indexOf("</li></ul>", q);
//				final String itemDescription = confirmationReport.substring(i + "<ul><li>".length(), q);
//				final int quantity = Integer.parseInt(confirmationReport.substring(q + " - Quantidade:".length(), ulliClose));
//
//				invoiceValue = invoiceValue.add(calculate(acquisitionRequest, itemDescription, quantity));
//			    }
//			}
//		    }
                }

                if (hasFullInvoice) {
                    invoiceValue = totalValue;
                }
            }

            spreadsheet.addCell(fundAllocationNumbers.length() == 0 ? " " : fundAllocationNumbers.toString());
            spreadsheet.addCell(commitmentNumbers.length() == 0 ? " " : commitmentNumbers.toString());
            spreadsheet.addCell(efectiveFundAllocationNumbers.length() == 0 ? " " : efectiveFundAllocationNumbers.toString());

            spreadsheet.addCell(invoiceDate == null ? " " : invoiceDate.toString("yyyy-MM-dd"));
            spreadsheet.addCell(invoiceValue.toFormatString());

            final DateTime creationDate = process.getCreationDate();
            spreadsheet.addCell(creationDate == null ? " " : creationDate.toString("yyyy-MM-dd"));
            final SortedSet<WorkflowLog> executionLogsSet = new TreeSet<WorkflowLog>(WorkflowLog.COMPARATOR_BY_WHEN_REVERSED);
            executionLogsSet.addAll(process.getExecutionLogsSet());
            final DateTime approvalDate = getApprovalDate(process, executionLogsSet);
            spreadsheet.addCell(approvalDate == null ? " " : approvalDate.toString("yyyy-MM-dd"));
            final DateTime authorizationDate = getAuthorizationDate(process, executionLogsSet);
            spreadsheet.addCell(authorizationDate == null ? " " : authorizationDate.toString("yyyy-MM-dd"));
        }
    }

    private DateTime getApprovalDate(final PaymentProcess process, final SortedSet<WorkflowLog> executionLogsSet) {
        for (final WorkflowLog log : executionLogsSet) {
            if (log instanceof ActivityLog) {
                final ActivityLog activityLog = (ActivityLog) log;
                if (SubmitForFundAllocation.class.getSimpleName().equals(activityLog.getOperation())
                        || Approve.class.getSimpleName().equals(activityLog.getOperation())) {
                    return log.getWhenOperationWasRan();
                }
            }
            if (log instanceof OperationLog) {
            }
        }

        return null;
    }

    private DateTime getAuthorizationDate(final PaymentProcess process, final SortedSet<WorkflowLog> executionLogsSet) {
        for (final WorkflowLog log : executionLogsSet) {
            if (log instanceof ActivityLog) {
                final ActivityLog activityLog = (ActivityLog) log;
                if (Authorize.class.getSimpleName().equals(activityLog.getOperation())) {
                    return log.getWhenOperationWasRan();
                }
            }
        }

        return null;
    }

    private Money calculate(final AcquisitionRequest acquisitionRequest, final String itemDescription, final int quantity) {
        for (final AcquisitionRequestItem requestItem : acquisitionRequest.getAcquisitionRequestItemsSet()) {
            if (requestItem.getDescription().equals(itemDescription)) {
                final Money unitValue = requestItem.getCurrentUnitValue();
                //final Integer currentQuantity = requestItem.getCurrentQuantity();
                final BigDecimal currentVatValue = requestItem.getCurrentVatValue();
                return unitValue.multiply(quantity).addPercentage(currentVatValue);
            }
        }
        return Money.ZERO;
    }

    private void setHeaders(StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(getExpenditureResourceMessage("label.acquisitionProcessId"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.acquisitionType"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.classification"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.suppliersDescription"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.itemsCount"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.process.state.description"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.inactiveSince"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.requesterName"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("acquisitionProcess.label.requestingUnit"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.accounting.units"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.financing.units"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.value"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("financer.label.fundAllocation.identification"));
        spreadsheet.addHeader(getExpenditureResourceMessage("label.commitmentNumbers"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("financer.label.effectiveFundAllocation.identification"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.invoice.date"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.invoice.value.acumulated"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.creation.date"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.approval.date"));
        spreadsheet.addHeader(getAcquisitionResourceMessage("label.authorization.date"));
    }

    private static String getAcquisitionResourceMessage(String key) {
        return getResourceMessage("resources/AcquisitionResources", key);
    }

    private static String getExpenditureResourceMessage(String key) {
        return getResourceMessage("resources/ExpenditureResources", key);
    }

    private static String getResourceMessage(String bundle, String key) {
        try {
            return replaceAllXMLTags(BundleUtil.getFormattedStringFromResourceBundle(bundle, key), " ");
        } catch (MissingResourceException ex) {
            return key;
        }
    }

    private static String replaceAllXMLTags(String source, String replacement) {
        return source.replaceAll("<.*>", replacement);
    }

    public ActionForward configurateMySearches(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        Set<SavedSearch> systemSearches = ExpenditureTrackingSystem.getInstance().getSystemSearches();
        Set<SavedSearch> userSearches = getLoggedPerson().getSaveSearches();
        request.setAttribute("systemSearches", systemSearches);
        request.setAttribute("userSearches", userSearches);

        return forward(request, "/acquisitions/search/manageMySearches.jsp");
    }

    public ActionForward deleteMySearch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SavedSearch search = getDomainObject(request, "savedSearchOID");
        Person person = getLoggedPerson();
        if (person == search.getPerson()) {
            search.delete();
        }

        return configurateMySearches(mapping, form, request, response);
    }

    public ActionForward setSearchAsDefault(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        SavedSearch search = getDomainObject(request, "savedSearchOID");
        Person person = getLoggedPerson();

        person.setDefaultSearch(search);
        return configurateMySearches(mapping, form, request, response);
    }

    private String getJumpParameters(SearchPaymentProcess searchBean) {
        StringBuilder builder = new StringBuilder("&processId=");
        if (searchBean.getProcessId() != null) {
            builder.append(searchBean.getProcessId());
        }

        builder.append("&payingUnit=");
        if (searchBean.getPayingUnit() != null) {
            builder.append(searchBean.getPayingUnit().getExternalId());
        }
        builder.append("&requestDocumentId=");
        if (searchBean.getRequestDocumentId() != null) {
            builder.append(searchBean.getRequestDocumentId());
        }
        builder.append("&proposalId=");
        if (searchBean.getProposalId() != null) {
            builder.append(searchBean.getProposalId());
        }
        builder.append("&refundeeName=");
        if (searchBean.getRefundeeName() != null) {
            builder.append(searchBean.getRefundeeName());
        }
        builder.append("&requestingPerson=");
        if (searchBean.getRequestingPerson() != null) {
            builder.append(searchBean.getRequestingPerson().getExternalId());
        }
        builder.append("&taker=");
        if (searchBean.getTaker() != null) {
            builder.append(searchBean.getTaker().getExternalId());
        }
        builder.append("&accountManager=");
        if (searchBean.getAccountManager() != null) {
            builder.append(searchBean.getAccountManager().getExternalId());
        }
        builder.append("&requestingUnit=");
        if (searchBean.getRequestingUnit() != null) {
            builder.append(searchBean.getRequestingUnit().getExternalId());
        }
        builder.append("&savedSearch=");
        if (searchBean.getSavedSearch() != null) {
            builder.append(searchBean.getSavedSearch().getExternalId());
        }
        builder.append("&supplier=");
        if (searchBean.getSupplier() != null) {
            builder.append(searchBean.getSupplier().getExternalId());
        }
        builder.append("&accountingUnit=");
        if (searchBean.getAccountingUnit() != null) {
            builder.append(searchBean.getAccountingUnit().getExternalId());
        }
        builder.append("&year=");
        if (searchBean.getPaymentProcessYear() != null) {
            builder.append(searchBean.getPaymentProcessYear().getExternalId());
        }
        builder.append("&hasAvailableAndAccessibleActivityForUser=");
        builder.append(searchBean.getHasAvailableAndAccessibleActivityForUser());

        builder.append("&responsibleUnitSetOnly=");
        builder.append(searchBean.getResponsibleUnitSetOnly());

        builder.append("&showOnlyAcquisitionsExcludedFromSupplierLimit=");
        builder.append(searchBean.getShowOnlyAcquisitionsExcludedFromSupplierLimit());

        builder.append("&showOnlyAcquisitionsWithAdditionalCosts=");
        builder.append(searchBean.getShowOnlyAcquisitionsWithAdditionalCosts());

        builder.append("&showOnlyWithUnreadComments=");
        builder.append(searchBean.getShowOnlyWithUnreadComments());

        builder.append("&acquisitionProcessStateType=");
        if (searchBean.getAcquisitionProcessStateType() != null) {
            builder.append(searchBean.getAcquisitionProcessStateType().name());
        }

        builder.append("&showPriorityOnly=");
        builder.append(searchBean.getShowPriorityOnly());

        builder.append("&refundProcessStateType=");
        if (searchBean.getRefundProcessStateType() != null) {
            builder.append(searchBean.getRefundProcessStateType().name());
        }

        builder.append("&searchProcessValue=");
        if (searchBean.getSearchProcess() != null) {
            builder.append(searchBean.getSearchProcess().name());
        }

        builder.append("&cpvReference=");
        if (searchBean.getCpvReference() != null) {
            builder.append(searchBean.getCpvReference().getCode());
        }

        return builder.toString();
    }

    private SearchPaymentProcess materializeBeanFromRequest(HttpServletRequest request) {
        SearchPaymentProcess bean = new SearchPaymentProcess();
        bean.setProcessId(request.getParameter("processId"));
        bean.setRequestDocumentId(request.getParameter("requestDocumentId"));
        bean.setProposalId(request.getParameter("proposalId"));
        bean.setRefundeeName(request.getParameter("refundeeName"));

        bean.setPayingUnit((Unit) getDomainObject(request, "payingUnit"));
        bean.setRequestingPerson((Person) getDomainObject(request, "requestingPerson"));
        bean.setTaker((Person) getDomainObject(request, "taker"));
        bean.setAccountManager((Person) getDomainObject(request, "accountManager"));
        bean.setRequestingUnit((Unit) getDomainObject(request, "requestingUnit"));
        bean.setSavedSearch((SavedSearch) getDomainObject(request, "savedSearch"));
        bean.setSupplier((Supplier) getDomainObject(request, "supplier"));
        bean.setAccountingUnit((AccountingUnit) getDomainObject(request, "accountingUnit"));
        bean.setPaymentProcessYear((PaymentProcessYear) getDomainObject(request, "year"));

        bean.setHasAvailableAndAccessibleActivityForUser(Boolean.valueOf(request
                .getParameter("hasAvailableAndAccessibleActivityForUser")));
        bean.setResponsibleUnitSetOnly(Boolean.valueOf(request.getParameter("responsibleUnitSetOnly")));
        bean.setShowOnlyAcquisitionsExcludedFromSupplierLimit(Boolean.valueOf(request
                .getParameter("showOnlyAcquisitionsExcludedFromSupplierLimit")));
        bean.setShowOnlyAcquisitionsWithAdditionalCosts(Boolean.valueOf(request
                .getParameter("showOnlyAcquisitionsWithAdditionalCosts")));
        bean.setShowOnlyWithUnreadComments(Boolean.valueOf(request.getParameter("showOnlyWithUnreadComments")));
        bean.setShowPriorityOnly(Boolean.valueOf(request.getParameter("showPriorityOnly")));

        String searchValue = request.getParameter("searchProcessValue");
        if (!StringUtils.isEmpty(searchValue)) {
            bean.setSearchProcess(SearchProcessValues.valueOf(searchValue));
        }

        String type = request.getParameter("acquisitionProcessStateType");
        if (!StringUtils.isEmpty(type)) {
            bean.setAcquisitionProcessStateType(AcquisitionProcessStateType.valueOf(type));
        }

        type = request.getParameter("refundProcessStateType");
        if (!StringUtils.isEmpty(type)) {
            bean.setRefundProcessStateType(RefundProcessStateType.valueOf(type));
        }

        String cpvReference = request.getParameter("cpvReference");
        if (!StringUtils.isEmpty(cpvReference)) {
            bean.setCpvReference(CPVReference.getCPVCode(cpvReference));
        }

        return bean;
    }

    protected Comparator<PaymentProcess> getComparator(HttpServletRequest request) {
        String sortParameter = request.getParameter("sortBy");
        Comparator<PaymentProcess> comparator = null;

        if (StringUtils.isEmpty(sortParameter)) {
            comparator = DEFAULT_COMPARATOR;
        } else {
            String[] split = sortParameter.split("=");
            if (split[0].equals(DEFAULT_SORT)) {
                comparator = DEFAULT_COMPARATOR;
            } else if (split[0].equals(STATE_SORT)) {
                comparator = STATE_SORT_COMPARATOR;
            } else {
                comparator = new NullResilientBeanComparator(split[0]);
            }

            if (split.length == 2 && split[1].indexOf("desc") > -1) {
                comparator = new ReverseComparator(comparator);
            }
        }

        return comparator;
    }

    public static class NullResilientBeanComparator extends BeanComparator {

        public NullResilientBeanComparator(String property) {
            super(property);
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == null && o2 != null) {
                return 1;
            }
            if (o2 == null && o1 != null) {
                return -1;
            }
            if (o1 == null && o2 == null) {
                return 0;
            }
            return super.compare(o1, o2);
        }
    }
}
