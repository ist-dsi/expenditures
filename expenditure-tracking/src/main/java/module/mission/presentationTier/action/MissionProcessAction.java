/*
 * @(#)MissionProcessAction.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.mission.presentationTier.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.mission.domain.AuthorizeDislocationService;
import module.mission.domain.ForeignMissionProcess;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.NationalMissionProcess;
import module.mission.domain.PersonMissionAuthorization;
import module.mission.domain.VehiclItem;
import module.mission.domain.activity.AddItemActivity;
import module.mission.domain.activity.AllocateFundsActivityInformation;
import module.mission.domain.activity.AllocateProjectFundsActivityInformation;
import module.mission.domain.activity.CommitFundsActivityInformation;
import module.mission.domain.activity.DefineParticipantAuthorizationChainActivity;
import module.mission.domain.activity.DefineParticipantAuthorizationChainActivityInformation;
import module.mission.domain.activity.DistributeItemCostsActivityInformation;
import module.mission.domain.activity.ItemActivityInformation;
import module.mission.domain.util.AuthorizationChain;
import module.mission.domain.util.MissionProcessCreationBean;
import module.mission.presentationTier.action.util.MissionContext;
import module.organization.domain.Person;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.util.WorkflowProcessViewer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsApplication(bundle = "MissionResources", path = "mission", titleKey = "link.sideBar.missionProcess",
        accessGroup = "logged", hint = "Expendutures")
@StrutsFunctionality(app = MissionProcessAction.class, path = "missionProcess", titleKey = "link.missionProcess.front.page")
@Mapping(path = "/missionProcess")
/**
 * 
 * @author Sérgio Silva
 * @author Luis Cruz
 * 
 */
@WorkflowProcessViewer(value = {NationalMissionProcess.class, ForeignMissionProcess.class})
public class MissionProcessAction extends BaseAction {

    public ActionForward help(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        return forward("/module/mission/help/manual.jsp");
    }

    @EntryPoint
    public ActionForward frontPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        MissionContext missionContext = getRenderedObject("missionContext");
        if (missionContext == null) {
            missionContext = new MissionContext();
        }
        request.setAttribute("missionContext", missionContext);
        return forward("/mission/frontPage.jsp");
    }

//    @Override
//    public Context createContext(String contextPathString, HttpServletRequest request) {
//        final MissionProcess missionProcess = getDomainObject(request, "processId");
//        if (missionProcess == null) {
//            return super.createContext(contextPathString, request);
//        }
//        final WorkflowLayoutContext workflowLayoutContext = missionProcess.getLayout();
//        workflowLayoutContext.setElements(contextPathString);
//        return workflowLayoutContext;
//    }

    public ActionForward showProcesses(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Set<MissionProcess> missionProcesses = MissionSystem.getInstance().getMissionProcessesSet();
        request.setAttribute("missionProcesses", missionProcesses);
        return forward("/mission/showProcesses.jsp");
    }

    public ActionForward prepareNewMissionCreation(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        MissionProcessCreationBean missionProcessCreationBean = getRenderedObject();
        if (missionProcessCreationBean == null) {
            final String grantOwnerEquivalenceString = request.getParameter("grantOwnerEquivalence");
            final Boolean grantOwnerEquivalence =
                    grantOwnerEquivalenceString == null || grantOwnerEquivalenceString.isEmpty() ? Boolean.FALSE : Boolean
                            .valueOf(grantOwnerEquivalenceString);
            missionProcessCreationBean = new MissionProcessCreationBean(grantOwnerEquivalence);
        }
        request.setAttribute("missionProcessCreationBean", missionProcessCreationBean);
        return forward("/mission/createNewMission.jsp");
    }

    public ActionForward newMissionCreation(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final MissionProcessCreationBean missionProcessCreationBean = getRenderedObject();
        try {
            final MissionProcess missionProcess = missionProcessCreationBean.createNewMissionProcess();
            request.setAttribute("missionProcess", missionProcess);
            return ProcessManagement.forwardToProcess(missionProcess);
        } catch (final DomainException ex) {
            RenderUtils.invalidateViewState();
            addLocalizedMessage(request, ex.getLocalizedMessage());
            request.setAttribute("missionProcessCreationBean", missionProcessCreationBean);
            return forward("/mission/createNewMission.jsp");
        }
    }

    public ActionForward addMissionItemSelectType(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final MissionProcess missionProcess = getDomainObject(request, "processId");

        final String missionItemType = request.getParameter("missionItemType");
        final Class missionItemClass = Class.forName(missionItemType);

        final ItemActivityInformation activityInformation =
                new ItemActivityInformation(missionProcess, missionProcess.getActivity(AddItemActivity.class));
        activityInformation.setTopLevelMissionItemType(missionItemClass.getSuperclass().getSuperclass());
        activityInformation.setConcreteMissionItemType(missionItemClass);
        activityInformation.setMissionItem();

        return new ProcessManagement().performActivityPostback(activityInformation, request);
    }

    public ActionForward activityInformationPostback(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ItemActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.setMissionItem();
        RenderUtils.invalidateViewState();
        return new ProcessManagement().performActivityPostback(activityInformation, request);
    }

    public ActionForward addMissionItem(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final ItemActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward distributeMissionItemCosts(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DistributeItemCostsActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward allocateFunds(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final AllocateFundsActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward allocateProjectFunds(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final AllocateProjectFundsActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward commitFunds(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final CommitFundsActivityInformation activityInformation = getRenderedObject("information");
        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward defineParticipantAuthorizationChain(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final MissionProcess missionProcess = getDomainObject(request, "processId");
        final DefineParticipantAuthorizationChainActivity activity =
                (DefineParticipantAuthorizationChainActivity) missionProcess
                        .getActivity(DefineParticipantAuthorizationChainActivity.class);
        final DefineParticipantAuthorizationChainActivityInformation activityInformation =
                (DefineParticipantAuthorizationChainActivityInformation) activity.getActivityInformation(missionProcess);

        final Person person = getDomainObject(request, "personId");
        activityInformation.setPerson(person);

        final String authorizationChainExternalId = request.getParameter("authorizationChainExternalId");
        final AuthorizationChain authorizationChain = AuthorizationChain.importFromString(authorizationChainExternalId);
        activityInformation.setAuthorizationChain(authorizationChain);

        activityInformation.execute();
        return ProcessManagement.forwardToProcess(activityInformation.getProcess());
    }

    public ActionForward distributeMissionItemFinancerValues(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final DistributeItemCostsActivityInformation distributeItemCostsActivityInformation = getRenderedObject("information");
        distributeItemCostsActivityInformation.distributeMissionItemFinancerValues();
        RenderUtils.invalidateViewState();
        return new ProcessManagement().performActivityPostback(distributeItemCostsActivityInformation, request);
    }

    public ActionForward aproveDislocations(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final String[] personMissionAuthorizationIds = request.getParameterValues("personMissionAuthorizationIds");
        final Set<PersonMissionAuthorization> personMissionAuthorizations = new HashSet<PersonMissionAuthorization>();
        for (final String personMissionAuthorizationId : personMissionAuthorizationIds) {
            final PersonMissionAuthorization personMissionAuthorization =
                    FenixFramework.getDomainObject(personMissionAuthorizationId);
            personMissionAuthorizations.add(personMissionAuthorization);
        }
        Collection<PersonMissionAuthorization> noLongerActiveForAuthorization = AuthorizeDislocationService.authorizeDislocation(personMissionAuthorizations);
        request.setAttribute("noLongerActiveForAuthorization", noLongerActiveForAuthorization);

        return frontPage(mapping, form, request, response);
    }

    public ActionForward massAuthorizeVehicles(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final String[] vehicleItemIds = request.getParameterValues("vehicleItemIds");
        final Set<VehiclItem> vehicleItems = new HashSet<VehiclItem>();
        for (final String id : vehicleItemIds) {
            final VehiclItem vehicleItem = FenixFramework.getDomainObject(id);
            vehicleItems.add(vehicleItem);
        }
        Collection<VehiclItem> noLongerActiveVehicleItems = MissionSystem.massAuthorizeVehicles(vehicleItems);
        request.setAttribute("noLongerActiveVehicleItems", noLongerActiveVehicleItems);

        return frontPage(mapping, form, request, response);
    }

}
