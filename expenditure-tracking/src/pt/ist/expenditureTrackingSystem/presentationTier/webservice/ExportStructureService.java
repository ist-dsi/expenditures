/*
 * @(#)ExportStructureService.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import myorg._development.PropertiesManager;
import myorg.domain.VirtualHost;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

@Path("/exportStructureService")
/**
 * 
 * @author Luis Cruz
 * 
 */
public class ExportStructureService {

    @GET
    @Path("listCostCenters/{username}/{password}")
    @Produces("text/csv")
    public Response listCostCenters(@PathParam("username") final String username,
	    @PathParam("password") final String password) {
	check(username, password);
	final String content = generateCostCenterList();
	return Response.ok(content, "text/csv").build();
    }

    private String generateCostCenterList() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
	    if (unit instanceof CostCenter) {
		final CostCenter costCenter = (CostCenter) unit;
		if (isActive(costCenter)) {
		    stringBuilder.append(costCenter.getCostCenter());
		    stringBuilder.append("\t");
		    stringBuilder.append(costCenter.getName());
		    stringBuilder.append("\n");
		}
	    }
	}
	return stringBuilder.toString();
    }

    private boolean isActive(final CostCenter costCenter) {
	// TODO : review this...
	return true;
    }

    private void check(final String username, final String password) {
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	final String hostname = virtualHost.getHostname();

	final String keyUsername = "exportStructureService.username." + hostname;
	final String keyPassword = "exportStructureService.password." + hostname;

	final String eUsername = PropertiesManager.getProperty(keyUsername);
	final String ePassword = PropertiesManager.getProperty(keyPassword);

	if (!match(username, eUsername) && match(password, ePassword)) {
	    throw new Error("unauthorized.access");
	}
    }

    private boolean match(final String s1, final String s2) {
	return s1 != null && s1.equals(s2);
    }

}
