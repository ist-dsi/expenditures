package module.mission.presentationTier.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;

@StrutsFunctionality(app = MissionProcessAction.class, path = "createMissionProcess", titleKey = "link.sideBar.newMission")
@Mapping(path = "/createMissionProcess")
public class MissionCreateProcessAction extends BaseAction {

    @EntryPoint
    public ActionForward missionCreationInstructions(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        return forward("/mission/missionCreationInstructions.jsp");
    }

}
