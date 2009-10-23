package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import javax.servlet.http.HttpServletRequest;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.presentationTier.actions.PartyViewHook;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;

public class OrganizationModelPlugin extends BaseAction {

    public static class ExpendituresView extends PartyViewHook {

	@Override
	public String hook(final HttpServletRequest request, final OrganizationalModel organizationalModel, final Party party) {
	    return "/expenditureTrackingOrganization/expendituresView.jsp";
	}

	@Override
	public String getViewName() {
	    return "04_expendituresView";
	}

	@Override
	public String getPresentationName() {
	    return BundleUtil.getStringFromResourceBundle("resources.ExpenditureOrganizationResources", "label.expendituresView");
	}

	@Override
	public boolean isAvailableFor(final Party party) {
	    return party != null && party.isUnit();
	}
    }

}
