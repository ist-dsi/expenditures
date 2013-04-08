/*
 * @(#)OrganizationModelPlugin.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization;

import javax.servlet.http.HttpServletRequest;

import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.presentationTier.actions.PartyViewHook;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;

/**
 * 
 * @author Luis Cruz
 * 
 */
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
