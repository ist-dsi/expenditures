package pt.ist.expenditureTrackingSystem.domain;

import javax.servlet.http.HttpServletRequest;

import module.dashBoard.WidgetRegister;
import module.dashBoard.WidgetRegister.WidgetAditionPredicate;
import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.widgets.WidgetController;
import module.organization.presentationTier.actions.OrganizationModelAction;
import module.workflow.widgets.ProcessListWidget;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import myorg.domain.User;
import myorg.domain.VirtualHost;
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
		    && (userAdding.getExpenditurePerson().hasRoleType(RoleType.ACQUISITION_CENTRAL)
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

}
