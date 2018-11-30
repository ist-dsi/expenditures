/*
 * @(#)ExpenditureTrackingSystem.java
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
package pt.ist.expenditureTrackingSystem.domain;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.LocalizedString;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;

import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.servlet.WidgetRegistry.WidgetAditionPredicate;
import module.finance.util.Money;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import module.organization.presentationTier.actions.OrganizationModelAction;
import module.workflow.widgets.ProcessListWidget;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.OrganizationModelPlugin.ExpendituresView;
import pt.ist.expenditureTrackingSystem.util.AquisitionsPendingProcessCounter;
import pt.ist.expenditureTrackingSystem.util.RefundPendingProcessCounter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Diogo Figueiredo
 * @author Pedro Santos
 * @author João Neves
 * @author Bruno Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author Pedro Amaral
 * 
 */
public class ExpenditureTrackingSystem extends ExpenditureTrackingSystem_Base implements ModuleInitializer {

    public static WidgetAditionPredicate EXPENDITURE_TRACKING_PANEL_PREDICATE = new WidgetAditionPredicate() {
        @Override
        public boolean canBeAdded(DashBoardPanel panel, User userAdding) {
            return (DashBoardPanel.class.isAssignableFrom(panel.getClass()));
        }
    };

    public static WidgetAditionPredicate EXPENDITURE_SERVICES_ONLY_PREDICATE = new WidgetAditionPredicate() {

        @Override
        public boolean canBeAdded(DashBoardPanel panel, User userAdding) {
            return EXPENDITURE_TRACKING_PANEL_PREDICATE.canBeAdded(panel, userAdding)
                    && (isAcquisitionCentralGroupMember(userAdding)
                            || !userAdding.getExpenditurePerson().getAccountingUnits().isEmpty() || !userAdding
                            .getExpenditurePerson().getProjectAccountingUnits().isEmpty());
        }
    };

    static {
        ProcessListWidget.register(new AquisitionsPendingProcessCounter());
        ProcessListWidget.register(new RefundPendingProcessCounter());

        registerChecksumFilterException();
        OrganizationModelAction.partyViewHookManager.register(new ExpendituresView());
    }

    public static ExpenditureTrackingSystem getInstance() {
        final Bennu bennu = Bennu.getInstance();
        if (bennu.getExpenditureTrackingSystem() == null) {
            createSystem();
        }
        return Bennu.getInstance().getExpenditureTrackingSystem();
    }

    private static void registerChecksumFilterException() {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {

            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                return !(request.getQueryString() != null && request.getQueryString().contains(
                        "method=calculateShareValuesViaAjax"));
            }

        });

        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest httpServletRequest) {
                return !(httpServletRequest.getRequestURI().endsWith("/acquisitionSimplifiedProcedureProcess.do")
                        && httpServletRequest.getQueryString() != null && httpServletRequest.getQueryString().contains(
                        "method=checkSupplierLimit"));
            }
        });

        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest httpServletRequest) {
                return !(httpServletRequest.getRequestURI().endsWith("/viewRCISTAnnouncements.do"))
                        && !(httpServletRequest.getRequestURI().endsWith("/viewAcquisitionAnnouncements.do"));
            }
        });

    }

    private ExpenditureTrackingSystem() {
        super();
        setBennu(Bennu.getInstance());
        setAcquisitionRequestDocumentCounter(0);

        new MyOwnProcessesSearch();
//	final SavedSearch savedSearch = new PendingProcessesSearch();
//	for (final Person person : getPeopleSet()) {
//	    person.setDefaultSearch(savedSearch);
//	}

        setRegisterDiaryNumbersAndTransactionNumbers(Boolean.TRUE);
        setRequireCommitmentNumber(Boolean.FALSE);

        setAcquisitionCentralGroup(RoleType.ACQUISITION_CENTRAL.group().toPersistentGroup());

        setFundCommitmentManagerGroup(RoleType.FUND_COMMITMENT_MANAGER.group().toPersistentGroup());

        setAcquisitionCentralManagerGroup(RoleType.ACQUISITION_CENTRAL_MANAGER.group().toPersistentGroup());

        setAccountingManagerGroup(RoleType.ACCOUNTING_MANAGER.group().toPersistentGroup());

        setProjectAccountingManagerGroup(RoleType.PROJECT_ACCOUNTING_MANAGER.group().toPersistentGroup());

        setTreasuryMemberGroup(RoleType.TREASURY_MANAGER.group().toPersistentGroup());

        setSupplierManagerGroup(RoleType.SUPPLIER_MANAGER.group().toPersistentGroup());

        setSupplierFundAllocationManagerGroup(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER.group().toPersistentGroup());

        setStatisticsViewerGroup(RoleType.STATISTICS_VIEWER.group().toPersistentGroup());

        setAcquisitionsUnitManagerGroup(RoleType.AQUISITIONS_UNIT_MANAGER.group().toPersistentGroup());

        setAcquisitionsProcessAuditorGroup(RoleType.ACQUISITION_PROCESS_AUDITOR.group().toPersistentGroup());


        setSearchProcessValuesArray(new SearchProcessValuesArray(SearchProcessValues.values()));

        setAcquisitionCreationWizardJsp("creationWizardPublicInstitution.jsp");

    }

    public String nextAcquisitionRequestDocumentID() {
        final String prefix = getInstitutionalRequestDocumentPrefix();
        return prefix + getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    public Integer nextAcquisitionRequestDocumentCountNumber() {
        return getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    private Integer getAndUpdateNextAcquisitionRequestDocumentCountNumber() {
        setAcquisitionRequestDocumentCounter(getAcquisitionRequestDocumentCounter().intValue() + 1);
        return getAcquisitionRequestDocumentCounter();
    }

    @Atomic
    public static void createSystem() {
        if (Bennu.getInstance().getExpenditureTrackingSystem() == null) {
            new ExpenditureTrackingSystem();
        }
    }

    public static boolean isAcquisitionCentralGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        final PersistentGroup group = system == null ? null : system.getAcquisitionCentralGroup();
        return group != null && group.isMember(user);
    }

    public static boolean isFundCommitmentManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasFundCommitmentManagerGroup() && system.getFundCommitmentManagerGroup().isMember(user);
    }

    public static boolean isAcquisitionCentralManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasAcquisitionCentralManagerGroup()
                && system.getAcquisitionCentralManagerGroup().isMember(user);
    }

    public static boolean isAccountingManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasAccountingManagerGroup() && system.getAccountingManagerGroup().isMember(user);
    }

    public static boolean isProjectAccountingManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasProjectAccountingManagerGroup()
                && system.getProjectAccountingManagerGroup().isMember(user);
    }

    public static boolean isTreasuryMemberGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasTreasuryMemberGroup() && system.getTreasuryMemberGroup().isMember(user);
    }

    public static boolean isSupplierManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasSupplierManagerGroup() && system.getSupplierManagerGroup().isMember(user);
    }

    public static boolean isSupplierFundAllocationManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasSupplierFundAllocationManagerGroup()
                && system.getSupplierFundAllocationManagerGroup().isMember(user);
    }

    public static boolean isStatisticsViewerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasStatisticsViewerGroup() && system.getStatisticsViewerGroup().isMember(user);
    }

    public static boolean isAcquisitionsUnitManagerGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasAcquisitionsUnitManagerGroup()
                && system.getAcquisitionsUnitManagerGroup().isMember(user);
    }

    public static boolean isAcquisitionsProcessAuditorGroupMember(final User user) {
        final ExpenditureTrackingSystem system = getInstance();
        return system != null && system.hasAcquisitionsProcessAuditorGroup()
                && system.getAcquisitionsProcessAuditorGroup().isMember(user);
    }

    public static boolean isAcquisitionCentralGroupMember() {
        final User user = Authenticate.getUser();
        return isAcquisitionCentralGroupMember(user);
    }

    public static boolean isFundCommitmentManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isFundCommitmentManagerGroupMember(user);
    }

    public static boolean isAcquisitionCentralManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isAcquisitionCentralManagerGroupMember(user);
    }

    public static boolean isAccountingManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isAccountingManagerGroupMember(user);
    }

    public static boolean isProjectAccountingManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isProjectAccountingManagerGroupMember(user);
    }

    public static boolean isTreasuryMemberGroupMember() {
        final User user = Authenticate.getUser();
        return isTreasuryMemberGroupMember(user);
    }

    public static boolean isSupplierManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isSupplierManagerGroupMember(user);
    }

    public static boolean isSupplierFundAllocationManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isSupplierFundAllocationManagerGroupMember(user);
    }

    public static boolean isStatisticsViewerGroupMember() {
        final User user = Authenticate.getUser();
        return isStatisticsViewerGroupMember(user);
    }

    public static boolean isAcquisitionsUnitManagerGroupMember() {
        final User user = Authenticate.getUser();
        return isAcquisitionsUnitManagerGroupMember(user);
    }

    public static boolean isAcquisitionsProcessAuditorGroupMember() {
        final User user = Authenticate.getUser();
        return isAcquisitionsProcessAuditorGroupMember(user);
    }

    public static boolean isManager() {
        final User user = Authenticate.getUser();
        return RoleType.MANAGER.group().isMember(user);
    }

    public boolean contains(final SearchProcessValues values) {
        return getSearchProcessValuesArray() != null && getSearchProcessValuesArray().contains(values);
    }

    public SortedSet<ProcessClassification> getAllowdProcessClassifications(final Class processType) {
        final SortedSet<ProcessClassification> classifications = new TreeSet<SimplifiedProcedureProcess.ProcessClassification>();
        for (final SearchProcessValues searchProcessValues : getSearchProcessValuesArray().getSearchProcessValues()) {
            if (processType != null && processType == searchProcessValues.getSearchClass()
                    && searchProcessValues.getSearchClassification() != null) {
                classifications.add(searchProcessValues.getSearchClassification());
            }
        }
        return classifications;
    }

    @Atomic
    public void saveConfiguration(final String institutionalProcessNumberPrefix, final String institutionalRequestDocumentPrefix,
            final String acquisitionCreationWizardJsp, final SearchProcessValuesArray array,
            final Boolean invoiceAllowedToStartAcquisitionProcess, final Boolean requireFundAllocationPriorToAcquisitionRequest,
            final Boolean registerDiaryNumbersAndTransactionNumbers, final Money maxValueStartedWithInvoive,
            final Money valueRequireingTopLevelAuthorization, final String documentationUrl, final String documentationLabel,
            final Boolean requireCommitmentNumber, final Boolean processesNeedToBeReverified,
            final LocalizedString approvalTextForRapidAcquisitions,
            final pt.ist.expenditureTrackingSystem.domain.organization.Unit acquisitionsUnit, final String createSupplierUrl,
            final String createSupplierLabel, final Boolean isPriorConsultationAvailable) {
        setInstitutionalProcessNumberPrefix(institutionalProcessNumberPrefix);
        setInstitutionalRequestDocumentPrefix(institutionalRequestDocumentPrefix);
        setAcquisitionCreationWizardJsp(acquisitionCreationWizardJsp);
        setSearchProcessValuesArray(array);
        setInvoiceAllowedToStartAcquisitionProcess(invoiceAllowedToStartAcquisitionProcess);
        setRequireFundAllocationPriorToAcquisitionRequest(requireFundAllocationPriorToAcquisitionRequest);
        setRegisterDiaryNumbersAndTransactionNumbers(registerDiaryNumbersAndTransactionNumbers);
        setMaxValueStartedWithInvoive(maxValueStartedWithInvoive);
        setValueRequireingTopLevelAuthorization(valueRequireingTopLevelAuthorization);
        setDocumentationUrl(documentationUrl);
        setDocumentationLabel(documentationLabel);
        setRequireCommitmentNumber(requireCommitmentNumber);
        setProcessesNeedToBeReverified(processesNeedToBeReverified);
        setApprovalTextForRapidAcquisitions(approvalTextForRapidAcquisitions);
        setAcquisitionsUnit(acquisitionsUnit);
        setCreateSupplierUrl(createSupplierUrl);
        setCreateSupplierLabel(createSupplierLabel);
        setPriorConsultationAvailable(isPriorConsultationAvailable);
    }

    public static boolean isInvoiceAllowedToStartAcquisitionProcess() {
        final ExpenditureTrackingSystem system = getInstance();
        final Boolean invoiceAllowedToStartAcquisitionProcess = system.getInvoiceAllowedToStartAcquisitionProcess();
        return invoiceAllowedToStartAcquisitionProcess != null && invoiceAllowedToStartAcquisitionProcess.booleanValue();
    }

    public boolean hasProcessPrefix() {
        final String prefix = getInstitutionalProcessNumberPrefix();
        return prefix != null && !prefix.isEmpty();
    }

    public boolean checkSupplierLimitsByCPV() {
        return getCheckSupplierLimitsByCPV() != null && getCheckSupplierLimitsByCPV().booleanValue();
    }

    public boolean processesNeedToBeReverified() {
        final Boolean b = getProcessesNeedToBeReverified();
        return b != null && b.booleanValue();
    }

    public boolean isCommitmentNumberRequired() {
        if (getRequireCommitmentNumber() == null) {
            return false;
        } else {
            return getRequireCommitmentNumber();
        }

    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference> getCPVReferences() {
        return getCPVReferencesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getSystemSearches() {
        return getSystemSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.DeliveryInfo> getDeliveryInfos() {
        return getDeliveryInfosSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem> getRequestItems() {
        return getRequestItemsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit> getAccountingUnits() {
        return getAccountingUnitsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.authorizations.AuthorizationLog> getAuthorizationLogs() {
        return getAuthorizationLogsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference> getPriorityCPVReferences() {
        return getPriorityCPVReferencesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> getPeople() {
        return getPeopleSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear> getPaymentProcessYears() {
        return getPaymentProcessYearsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.Options> getOptions() {
        return getOptionsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.Refundee> getRefundees() {
        return getRefundeesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.SavedSearch> getSavedSearches() {
        return getSavedSearchesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer> getFinancers() {
        return getFinancersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.DashBoard> getDashBoards() {
        return getDashBoardsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.announcements.Announcement> getAnnouncements() {
        return getAnnouncementsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess> getProcesses() {
        return getProcessesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Unit> getTopLevelUnits() {
        return getTopLevelUnitsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization> getAuthorizations() {
        return getAuthorizationsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition> getAcquisitions() {
        return getAcquisitionsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Supplier> getSuppliers() {
        return getSuppliersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.organization.Unit> getUnits() {
        return getUnitsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.ProcessState> getProcessStates() {
        return getProcessStatesSet();
    }

    @Deprecated
    public boolean hasAnyCPVReferences() {
        return !getCPVReferencesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnySystemSearches() {
        return !getSystemSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyDeliveryInfos() {
        return !getDeliveryInfosSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRequestItems() {
        return !getRequestItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAccountingUnits() {
        return !getAccountingUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAuthorizationLogs() {
        return !getAuthorizationLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPriorityCPVReferences() {
        return !getPriorityCPVReferencesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPeople() {
        return !getPeopleSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyPaymentProcessYears() {
        return !getPaymentProcessYearsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyOptions() {
        return !getOptionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyRefundees() {
        return !getRefundeesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnySavedSearches() {
        return !getSavedSearchesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyFinancers() {
        return !getFinancersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyDashBoards() {
        return !getDashBoardsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAnnouncements() {
        return !getAnnouncementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyProcesses() {
        return !getProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyTopLevelUnits() {
        return !getTopLevelUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAuthorizations() {
        return !getAuthorizationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAcquisitions() {
        return !getAcquisitionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnySuppliers() {
        return !getSuppliersSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyUnits() {
        return !getUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyProcessStates() {
        return !getProcessStatesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAcquisitionRequestDocumentCounter() {
        return getAcquisitionRequestDocumentCounter() != null;
    }

    @Deprecated
    public boolean hasSearchProcessValuesArray() {
        return getSearchProcessValuesArray() != null;
    }

    @Deprecated
    public boolean hasAcquisitionCreationWizardJsp() {
        return getAcquisitionCreationWizardJsp() != null;
    }

    @Deprecated
    public boolean hasInstitutionalProcessNumberPrefix() {
        return getInstitutionalProcessNumberPrefix() != null;
    }

    @Deprecated
    public boolean hasInstitutionalRequestDocumentPrefix() {
        return getInstitutionalRequestDocumentPrefix() != null;
    }

    @Deprecated
    public boolean hasInvoiceAllowedToStartAcquisitionProcess() {
        return getInvoiceAllowedToStartAcquisitionProcess() != null;
    }

    @Deprecated
    public boolean hasRequireFundAllocationPriorToAcquisitionRequest() {
        return getRequireFundAllocationPriorToAcquisitionRequest() != null;
    }

    @Deprecated
    public boolean hasRegisterDiaryNumbersAndTransactionNumbers() {
        return getRegisterDiaryNumbersAndTransactionNumbers() != null;
    }

    @Deprecated
    public boolean hasRequireCommitmentNumber() {
        return getRequireCommitmentNumber() != null;
    }

    @Deprecated
    public boolean hasMaxValueStartedWithInvoive() {
        return getMaxValueStartedWithInvoive() != null;
    }

    @Deprecated
    public boolean hasValueRequireingTopLevelAuthorization() {
        return getValueRequireingTopLevelAuthorization() != null;
    }

    @Deprecated
    public boolean hasDocumentationUrl() {
        return getDocumentationUrl() != null;
    }

    @Deprecated
    public boolean hasDocumentationLabel() {
        return getDocumentationLabel() != null;
    }

    @Deprecated
    public boolean hasCheckSupplierLimitsByCPV() {
        return getCheckSupplierLimitsByCPV() != null;
    }

    @Deprecated
    public boolean hasInstitutionManagementEmail() {
        return getInstitutionManagementEmail() != null;
    }

    @Deprecated
    public boolean hasTreasuryMemberGroup() {
        return getTreasuryMemberGroup() != null;
    }

    @Deprecated
    public boolean hasAcquisitionCentralGroup() {
        return getAcquisitionCentralGroup() != null;
    }

    @Deprecated
    public boolean hasOrganizationalAccountabilityType() {
        return getOrganizationalAccountabilityType() != null;
    }

    @Deprecated
    public boolean hasProjectPartyType() {
        return getProjectPartyType() != null;
    }

    @Deprecated
    public boolean hasAcquisitionCentralManagerGroup() {
        return getAcquisitionCentralManagerGroup() != null;
    }

    @Deprecated
    public boolean hasProjectAccountingManagerGroup() {
        return getProjectAccountingManagerGroup() != null;
    }

    @Deprecated
    public boolean hasUnitPartyType() {
        return getUnitPartyType() != null;
    }

    @Deprecated
    public boolean hasCostCenterPartyType() {
        return getCostCenterPartyType() != null;
    }

    @Deprecated
    public boolean hasStatisticsViewerGroup() {
        return getStatisticsViewerGroup() != null;
    }

    @Deprecated
    public boolean hasAccountingManagerGroup() {
        return getAccountingManagerGroup() != null;
    }

    @Deprecated
    public boolean hasFundCommitmentManagerGroup() {
        return getFundCommitmentManagerGroup() != null;
    }

    @Deprecated
    public boolean hasAcquisitionsProcessAuditorGroup() {
        return getAcquisitionsProcessAuditorGroup() != null;
    }

    @Deprecated
    public boolean hasOrganizationalMissionAccountabilityType() {
        return getOrganizationalMissionAccountabilityType() != null;
    }

    @Deprecated
    public boolean hasAcquisitionsUnitManagerGroup() {
        return getAcquisitionsUnitManagerGroup() != null;
    }

    @Deprecated
    public boolean hasSupplierManagerGroup() {
        return getSupplierManagerGroup() != null;
    }

    @Deprecated
    public boolean hasSupplierFundAllocationManagerGroup() {
        return getSupplierFundAllocationManagerGroup() != null;
    }

    @Deprecated
    public boolean hasSubProjectPartyType() {
        return getSubProjectPartyType() != null;
    }

    public interface InfoProvider {
        public String getTitle();

        public Map<String, String> getLinks(String page, Object object);

        public List<List<String>> getSummary(String page, Object object);
    }

    static private InfoProvider infoProvider;

    static public void registerInfoProvider(InfoProvider aInfoProvider) {
        infoProvider = aInfoProvider;
    }

    static public InfoProvider getInfoProvider() {
        return infoProvider;
    }

    @Override
    public void performInit() throws ModuleInitializeException {
        // TODO Auto-generated method stub

    }

    @Atomic
    public void selectOrganizationalModel(final OrganizationalModel model) {
        getTopLevelUnitsSet().clear();
        for (final Party p : model.getPartiesSet()) {
            if (p.isUnit()) {
                final Unit unit = (Unit) p;
                if (unit.getExpenditureUnit() == null) {
                    final pt.ist.expenditureTrackingSystem.domain.organization.Unit exUnit = new pt.ist.expenditureTrackingSystem.domain.organization.Unit();
                    unit.setExpenditureUnit(exUnit);
                    getTopLevelUnitsSet().add(exUnit);
                } else {
                    getTopLevelUnitsSet().add(unit.getExpenditureUnit());
                }
            }
        }
    }

    public static boolean isExpenseAuthority(final User user) {
        return getInstance().getTopLevelUnitsSet().stream()
                .flatMap(u -> u.getAuthorizationsSet().stream())
                .anyMatch((a -> a.isValid() && a.getPerson().getUser() == user));
    }

    public static boolean isPriorConsultationAvailable() {
        final Boolean b = getInstance().getPriorConsultationAvailable();
        return b != null && b.booleanValue();
    }

}
