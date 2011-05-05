package pt.ist.expenditureTrackingSystem.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister;
import module.dashBoard.WidgetRegister.WidgetAditionPredicate;
import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.widgets.WidgetController;
import module.organization.presentationTier.actions.OrganizationModelAction;
import module.workflow.widgets.ProcessListWidget;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValuesArray;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.OrganizationModelPlugin.ExpendituresView;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.ActivateEmailNotificationWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MyProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MySearchesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingRefundWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PendingSimplifiedWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.PrioritiesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.SearchByInvoiceWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.TakenProcessesWidget;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.UnreadCommentsWidget;
import pt.ist.expenditureTrackingSystem.util.AquisitionsPendingProcessCounter;
import pt.ist.expenditureTrackingSystem.util.RefundPendingProcessCounter;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;

public class ExpenditureTrackingSystem extends ExpenditureTrackingSystem_Base implements ModuleInitializer {

    public static WidgetAditionPredicate EXPENDITURE_TRACKING_PANEL_PREDICATE = new WidgetAditionPredicate() {
	@Override
	public boolean canBeAdded(DashBoardPanel panel, User userAdding) {
	    return (ExpenditureUserDashBoardPanel.class.isAssignableFrom(panel.getClass()));
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

	registerWidget(MySearchesWidget.class);
	registerWidget(UnreadCommentsWidget.class);
	registerWidget(TakenProcessesWidget.class);
	registerWidget(MyProcessesWidget.class);
	registerWidget(PendingRefundWidget.class);
	registerWidget(PendingSimplifiedWidget.class);
	registerWidget(ActivateEmailNotificationWidget.class);
	registerWidget(SearchByInvoiceWidget.class);
	WidgetRegister.registerWidget(PrioritiesWidget.class, EXPENDITURE_SERVICES_ONLY_PREDICATE);

	registerChecksumFilterException();
	OrganizationModelAction.partyViewHookManager.register(new ExpendituresView());
    }

    public static ExpenditureTrackingSystem getInstance() {
	final VirtualHost virtualHostForThread = VirtualHost.getVirtualHostForThread();
	return virtualHostForThread == null ? MyOrg.getInstance().getExpenditureTrackingSystem()
		: virtualHostForThread.getExpenditureTrackingSystem();
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
		return !(httpServletRequest.getRequestURI().endsWith("/viewRCISTAnnouncements.do"));
	    }
	});

    }

    private static void initRoles() {
	for (final RoleType roleType : RoleType.values()) {
	    Role.getRole(roleType);
	}
    }

    private ExpenditureTrackingSystem(final VirtualHost virtualHost) {
	super();
//	setMyOrg(MyOrg.getInstance());
	setAcquisitionRequestDocumentCounter(0);
	virtualHost.setExpenditureTrackingSystem(this);

	new MyOwnProcessesSearch();
	final SavedSearch savedSearch = new PendingProcessesSearch();
	for (final Person person : getPeopleSet()) {
	    person.setDefaultSearch(savedSearch);
	}
    }

    public String nextAcquisitionRequestDocumentID() {
	return "D" + getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    public Integer nextAcquisitionRequestDocumentCountNumber() {
	return getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    private Integer getAndUpdateNextAcquisitionRequestDocumentCountNumber() {
	setAcquisitionRequestDocumentCounter(getAcquisitionRequestDocumentCounter().intValue() + 1);
	return getAcquisitionRequestDocumentCounter();
    }

    @Override
    public void init(final MyOrg root) {
	final ExpenditureTrackingSystem expenditureTrackingSystem = root.getExpenditureTrackingSystem();
	if (expenditureTrackingSystem != null) {
	    for (final VirtualHost virtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
		if (!virtualHost.hasExpenditureTrackingSystem()) {
		    virtualHost.setExpenditureTrackingSystem(expenditureTrackingSystem);
		}
	    }

	    if (!expenditureTrackingSystem.hasAcquisitionCentralGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.ACQUISITION_CENTRAL);
		expenditureTrackingSystem.setAcquisitionCentralGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasAcquisitionCentralManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.ACQUISITION_CENTRAL_MANAGER);
		expenditureTrackingSystem.setAcquisitionCentralManagerGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasAccountingManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.ACCOUNTING_MANAGER);
		expenditureTrackingSystem.setAccountingManagerGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasProjectAccountingManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.PROJECT_ACCOUNTING_MANAGER);
		expenditureTrackingSystem.setProjectAccountingManagerGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasTreasuryMemberGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.TREASURY_MANAGER);
		expenditureTrackingSystem.setTreasuryMemberGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasSupplierManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.SUPPLIER_MANAGER);
		expenditureTrackingSystem.setSupplierManagerGroup(group);		
	    }

	    if (!expenditureTrackingSystem.hasSupplierFundAllocationManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.SUPPLIER_FUND_ALLOCATION_MANAGER);
		expenditureTrackingSystem.setSupplierFundAllocationManagerGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasStatisticsViewerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.STATISTICS_VIEWER);
		expenditureTrackingSystem.setStatisticsViewerGroup(group);
	    }

	    if (!expenditureTrackingSystem.hasAcquisitionsUnitManagerGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.AQUISITIONS_UNIT_MANAGER);
		expenditureTrackingSystem.setAcquisitionsUnitManagerGroup(group);		
	    }

	    if (!expenditureTrackingSystem.hasAcquisitionsProcessAuditorGroup()) {
		final myorg.domain.groups.Role group = myorg.domain.groups.Role.getRole(RoleType.ACQUISITION_PROCESS_AUDITOR);
		expenditureTrackingSystem.setAcquisitionsProcessAuditorGroup(group);
	    }

	    if (expenditureTrackingSystem.getSearchProcessValuesArray() == null) {
		expenditureTrackingSystem.setSearchProcessValuesArray(new SearchProcessValuesArray(SearchProcessValues.values()));
	    }

	    if (expenditureTrackingSystem.getAcquisitionCreationWizardJsp() == null
		    || expenditureTrackingSystem.getAcquisitionCreationWizardJsp().isEmpty()) {
		expenditureTrackingSystem.setAcquisitionCreationWizardJsp("creationWizardPublicInstitution.jsp");
	    }
	}
    }

    private static void registerWidget(Class<? extends WidgetController> widgetClass) {
	WidgetRegister.registerWidget(widgetClass, EXPENDITURE_TRACKING_PANEL_PREDICATE);
    }

    @Service
    public static void createSystem(final VirtualHost virtualHost) {
	if (!virtualHost.hasExpenditureTrackingSystem()) {
	    new ExpenditureTrackingSystem(virtualHost);
	    initRoles();
	}
    }

    public static boolean isAcquisitionCentralGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasAcquisitionCentralGroup() && system.getAcquisitionCentralGroup().isMember(user);
    }

    public static boolean isAcquisitionCentralManagerGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasAcquisitionCentralManagerGroup() && system.getAcquisitionCentralManagerGroup().isMember(user);
    }

    public static boolean isAccountingManagerGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasAccountingManagerGroup() && system.getAccountingManagerGroup().isMember(user);
    }

    public static boolean isProjectAccountingManagerGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasProjectAccountingManagerGroup() && system.getProjectAccountingManagerGroup().isMember(user);
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
	return system != null && system.hasSupplierFundAllocationManagerGroup() && system.getSupplierFundAllocationManagerGroup().isMember(user);
    }

    public static boolean isStatisticsViewerGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasStatisticsViewerGroup() && system.getStatisticsViewerGroup().isMember(user);
    }

    public static boolean isAcquisitionsUnitManagerGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasAcquisitionsUnitManagerGroup() && system.getAcquisitionsUnitManagerGroup().isMember(user);
    }

    public static boolean isAcquisitionsProcessAuditorGroupMember(final User user) {
	final ExpenditureTrackingSystem system = getInstance();
	return system != null && system.hasAcquisitionsProcessAuditorGroup() && system.getAcquisitionsProcessAuditorGroup().isMember(user);
    }

    public static boolean isAcquisitionCentralGroupMember() {
	final User user = UserView.getCurrentUser();
	return isAcquisitionCentralGroupMember(user);
    }

    public static boolean isAcquisitionCentralManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isAcquisitionCentralManagerGroupMember(user);
    }

    public static boolean isAccountingManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isAccountingManagerGroupMember(user);
    }

    public static boolean isProjectAccountingManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isProjectAccountingManagerGroupMember(user);
    }

    public static boolean isTreasuryMemberGroupMember() {
	final User user = UserView.getCurrentUser();
	return isTreasuryMemberGroupMember(user);
    }

    public static boolean isSupplierManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isSupplierManagerGroupMember(user);
    }

    public static boolean isSupplierFundAllocationManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isSupplierFundAllocationManagerGroupMember(user);
    }

    public static boolean isStatisticsViewerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isStatisticsViewerGroupMember(user);
    }

    public static boolean isAcquisitionsUnitManagerGroupMember() {
	final User user = UserView.getCurrentUser();
	return isAcquisitionsUnitManagerGroupMember(user);
    }

    public static boolean isAcquisitionsProcessAuditorGroupMember() {
	final User user = UserView.getCurrentUser();
	return isAcquisitionsProcessAuditorGroupMember(user);
    }

    public static boolean isManager() {
	final User user = UserView.getCurrentUser();
	final myorg.domain.groups.Role role = myorg.domain.groups.Role.getRole(myorg.domain.RoleType.MANAGER);
	return role.isMember(user);
    }

    public boolean contains(final SearchProcessValues values) {
	return getSearchProcessValuesArray() != null && getSearchProcessValuesArray().contains(values);
    }

    @Service
    public void saveConfiguration(final String acquisitionCreationWizardJsp, final SearchProcessValuesArray array) {
	setAcquisitionCreationWizardJsp(acquisitionCreationWizardJsp);
	setSearchProcessValuesArray(array);
    }

}
