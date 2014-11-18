package module.mission.presentationTier.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.AccountabilityTypeQueue;
import module.mission.domain.DailyPersonelExpenseCategory;
import module.mission.domain.DailyPersonelExpenseTable;
import module.mission.domain.MissionAuthorizationAccountabilityType;
import module.mission.domain.MissionSystem;
import module.mission.domain.util.AccountabilityTypeQueueBean;
import module.mission.domain.util.DailyPersonelExpenseCategoryBean;
import module.mission.domain.util.DailyPersonelExpenseTableBean;
import module.mission.domain.util.MissionAuthorizationAccountabilityTypeBean;
import module.organization.domain.OrganizationalModel;
import module.organization.presentationTier.actions.OrganizationModelAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.domain.dto.SupplierBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MissionProcessAction.class, path = "missionConfiguration", titleKey = "link.sideBar.missionConfiguration")
@Mapping(path = "/configureMissions")
public class MissionsConfigurationAction extends BaseAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ActionForward forward = super.execute(mapping, form, request, response);
//        OrganizationModelAction.addHeadToLayoutContext(request);
        return forward;
    }

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        request.setAttribute("missionSystem", missionSystem);
        request.setAttribute("supplierBean", new SupplierBean());
        return forward("/mission/configureMissions.jsp");
    }

    public ActionForward prepareSelectOrganizationalModel(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        OrganizationModelAction.viewModels(request);
        return forward("/mission/selectOrganizationalModel.jsp");
    }

    public ActionForward selectCountry(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        request.setAttribute("missionSystem", missionSystem);
        return forward("/mission/selectCountry.jsp");
    }

    public ActionForward selectOrganizationalModel(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        final OrganizationalModel organizationalModel = getDomainObject(request, "organizationalModelOid");
        missionSystem.setOrganizationalModel(organizationalModel);
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareAddMissionAuthorizationAccountabilityType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        MissionAuthorizationAccountabilityTypeBean missionAuthorizationAccountabilityTypeBean = getRenderedObject();
        if (missionAuthorizationAccountabilityTypeBean == null) {
            missionAuthorizationAccountabilityTypeBean = new MissionAuthorizationAccountabilityTypeBean();
        }
        request.setAttribute("missionAuthorizationAccountabilityTypeBean", missionAuthorizationAccountabilityTypeBean);
        return forward("/mission/addMissionAuthorizationAccountabilityType.jsp");
    }

    public ActionForward addMissionAuthorizationAccountabilityType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        MissionAuthorizationAccountabilityTypeBean missionAuthorizationAccountabilityTypeBean = getRenderedObject();
        missionAuthorizationAccountabilityTypeBean.createMissionAuthorizationAccountabilityType();
        return prepare(mapping, form, request, response);
    }

    public ActionForward deleteMissionAuthorizationAccountabilityType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType =
                getDomainObject(request, "missionAuthorizationAccountabilityTypeOid");
        missionAuthorizationAccountabilityType.delete();
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreateDailyPersonelExpenseTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTableBean dailyPersonelExpenseTableBean = new DailyPersonelExpenseTableBean();
        request.setAttribute("dailyPersonelExpenseTableBean", dailyPersonelExpenseTableBean);
        return forward("/mission/createDailyPersonelExpenseTable.jsp");
    }

    public ActionForward createDailyPersonelExpenseTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTableBean dailyPersonelExpenseTableBean = getRenderedObject();
        final DailyPersonelExpenseTable dailyPersonelExpenseTable =
                dailyPersonelExpenseTableBean.createDailyPersonelExpenseTable();
        return viewDailyPersonelExpenseTable(request, dailyPersonelExpenseTable);
    }

    public ActionForward viewDailyPersonelExpenseTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = getDomainObject(request, "dailyPersonelExpenseTableOid");
        return viewDailyPersonelExpenseTable(request, dailyPersonelExpenseTable);
    }

    public ActionForward viewDailyPersonelExpenseTable(final HttpServletRequest request,
            final DailyPersonelExpenseTable dailyPersonelExpenseTable) {
        request.setAttribute("dailyPersonelExpenseTable", dailyPersonelExpenseTable);
        return forward("/mission/viewDailyPersonelExpenseTable.jsp");
    }

    public ActionForward editDailyPersonelExpenseTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = getDomainObject(request, "dailyPersonelExpenseTableOid");
        return editDailyPersonelExpenseTable(request, dailyPersonelExpenseTable);
    }

    private ActionForward editDailyPersonelExpenseTable(HttpServletRequest request,
            DailyPersonelExpenseTable dailyPersonelExpenseTable) {
        request.setAttribute("dailyPersonelExpenseTable", dailyPersonelExpenseTable);
        return forward("/mission/editDailyPersonelExpenseTable.jsp");
    }

    public ActionForward deleteDailyPersonelExpenseTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = getDomainObject(request, "dailyPersonelExpenseTableOid");
        dailyPersonelExpenseTable.delete();
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreateDailyPersonelExpenseCategory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = getDomainObject(request, "dailyPersonelExpenseTableOid");
        final DailyPersonelExpenseCategoryBean dailyPersonelExpenseCategoryBean =
                new DailyPersonelExpenseCategoryBean(dailyPersonelExpenseTable);
        request.setAttribute("dailyPersonelExpenseCategoryBean", dailyPersonelExpenseCategoryBean);
        return forward("/mission/createDailyPersonelExpenseCategory.jsp");
    }

    public ActionForward createDailyPersonelExpenseCategory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseCategoryBean dailyPersonelExpenseCategoryBean = getRenderedObject();
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = getDomainObject(request, "dailyPersonelExpenseTableOid");
        dailyPersonelExpenseCategoryBean.setDailyPersonelExpenseTable(dailyPersonelExpenseTable);
        dailyPersonelExpenseCategoryBean.createDailyPersonelExpenseCategory();
        return viewDailyPersonelExpenseTable(request, dailyPersonelExpenseCategoryBean.getDailyPersonelExpenseTable());
    }

    public ActionForward editDailyPersonelExpenseCategory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseCategory dailyPersonelExpenseCategory =
                getDomainObject(request, "dailyPersonelExpenseCategoryOid");
        request.setAttribute("dailyPersonelExpenseCategory", dailyPersonelExpenseCategory);
        return forward("/mission/editDailyPersonelExpenseCategory.jsp");
    }

    public ActionForward deleteDailyPersonelExpenseCategory(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DailyPersonelExpenseCategory dailyPersonelExpenseCategory =
                getDomainObject(request, "dailyPersonelExpenseCategoryOid");
        final DailyPersonelExpenseTable dailyPersonelExpenseTable = dailyPersonelExpenseCategory.getDailyPersonelExpenseTable();
        dailyPersonelExpenseCategory.delete();
        return viewDailyPersonelExpenseTable(request, dailyPersonelExpenseTable);
    }

    public ActionForward prepareAddQueueForAccountabilityType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        AccountabilityTypeQueueBean accountabilityTypeQueueBean = getRenderedObject();
        if (accountabilityTypeQueueBean == null) {
            accountabilityTypeQueueBean = new AccountabilityTypeQueueBean();
        }
        request.setAttribute("accountabilityTypeQueueBean", accountabilityTypeQueueBean);
        return forward("/mission/createAccountabilityTypeQueue.jsp");
    }

    public ActionForward addQueueForAccountabilityType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountabilityTypeQueueBean accountabilityTypeQueueBean = getRenderedObject();
        accountabilityTypeQueueBean.create();
        return prepare(mapping, form, request, response);
    }

    public ActionForward deleteAccountabilityTypeQueue(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AccountabilityTypeQueue accountabilityTypeQueue = getDomainObject(request, "accountabilityTypeQueue");
        accountabilityTypeQueue.delete();
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareAddUserWhoCanCancelMissions(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return forward("/mission/addUserWhoCanCancelMissions.jsp");
    }

    public ActionForward addUserWhoCanCancelMissions(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final String username = getAttribute(request, "username");
        final User user = User.findByUsername(username);
        if (user != null) {
            MissionSystem.getInstance().addUsersWhoCanCancelMission(user);
        }
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeUserWhoCanCancelMissions(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final User user = getDomainObject(request, "userOid");
        MissionSystem.getInstance().removeUsersWhoCanCancelMission(user);
        return prepare(mapping, form, request, response);
    }

    public ActionForward togleAllowGrantOwnerMissionProcessNature(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        missionSystem.toggleAllowGrantOwnerEquivalence();
        return prepare(mapping, form, request, response);
    }

    public ActionForward togleUseWorkingPlaceAuthorizationChain(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final MissionSystem missionSystem = MissionSystem.getInstance();
        missionSystem.togleUseWorkingPlaceAuthorizationChain();
        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareAddVehicleAuthorizer(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return forward("/mission/addVehicleAuthorizer.jsp");
    }

    public ActionForward addVehicleAuthorizer(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final String username = getAttribute(request, "username");
        final User user = User.findByUsername(username);
        if (user != null) {
            MissionSystem.getInstance().addVehicleAuthorizers(user);
        }
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeVehicleAuthorizer(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final User user = getDomainObject(request, "userOid");
        MissionSystem.getInstance().removeVehicleAuthorizers(user);
        return prepare(mapping, form, request, response);
    }

    public ActionForward addMandatorySupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final SupplierBean supplierBean = getRenderedObject("missionSystemMandatorySupplier");
        if (supplierBean != null && supplierBean.getSupplier() != null) {
            final MissionSystem missionSystem = MissionSystem.getInstance();
            missionSystem.addMandatorySupplierService(supplierBean.getSupplier());
        }
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeMandatorySupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Supplier supplier = getDomainObject(request, "supplierOid");
        if (supplier != null) {
            final MissionSystem missionSystem = MissionSystem.getInstance();
            missionSystem.removeMandatorySupplierService(supplier);
        }
        return prepare(mapping, form, request, response);
    }

}
