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
package module.webservice;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

@Path("/exportStructureService")
/**
 * 
 * @author Luis Cruz
 * 
 */
public class ExportStructureService {

    @GET
    @Path("{username}/{password}/listCostCenters.csv")
    @Produces("text/csv")
    public Response listCostCenters(@PathParam("username") final String username, @PathParam("password") final String password) {
        check(username, password);
        final String content = generateUnitsList(u -> u instanceof CostCenter, this::toNumber);
        return Response.ok(content, "text/csv").build();
    }

    @GET
    @Path("listCostCenters.csv")
    @Produces("text/csv")
    public Response listCostCentersParams(@QueryParam("username") final String username,
            @QueryParam("password") final String password) {
        return listCostCenters(username, password);
    }

    @GET
    @Path("listCostCenters/{username}/{password}")
    @Produces("text/csv")
    public Response listCostCenters2(@PathParam("username") final String username, @PathParam("password") final String password) {
    	return listCostCenters(username, password);
    }

    @GET
    @Path("listProjects.csv")
    @Produces("text/csv")
    public Response listProjects(@QueryParam("username") final String username, @QueryParam("password") final String password) {
        check(username, password);
        final String content = generateUnitsList(u -> u instanceof Project, this::toNumber);
        return Response.ok(content, "text/csv").build();
    }

    @GET
    @Path("listUnits.csv")
    @Produces("text/csv")
    public Response listUnits(@QueryParam("username") final String username, @QueryParam("password") final String password) {
        check(username, password);
        final String content = generateUnitsList(this::isProjectOrCostCenter, this::toCode);
        return Response.ok(content, "text/csv").build();
    }

    private boolean isProjectOrCostCenter(final Unit unit) {
    	return unit instanceof CostCenter || unit instanceof Project;
    }

	private String generateUnitsList(final Predicate<Unit> unitFilter, final Function<Unit, String> toNumber) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
        	if (unitFilter.test(unit) && isActive(unit)) {
        		final Set<User> authorities = getActiveAuthorizationSet(unit);
        		
        		if (authorities.isEmpty()) {
        			appendInfo(stringBuilder, unit, toNumber);
        			
        			// responsáveis
        			stringBuilder.append(" ");
        			stringBuilder.append("\t");
        			
        			// e-mail
        			stringBuilder.append(" ");
        			
        			stringBuilder.append("\n");
        			
        		} else {
        			for (final User user : authorities) {
        				appendInfo(stringBuilder, unit, toNumber);
        				
        				// responsáveis
        				stringBuilder.append(user.getUsername());
        				stringBuilder.append("\t");
        				
        				// e-mail
        				final String email = user.getEmail();
        				stringBuilder.append(email == null ? " " : email);
        				
        				stringBuilder.append("\n");
        			}
        		}
        	}
        }
        return stringBuilder.toString();
    }

    private void appendInfo(final StringBuilder stringBuilder, final Unit unit, final Function<Unit, String> toNumber) {
        stringBuilder.append(toNumber.apply(unit));
        stringBuilder.append("\t");
        stringBuilder.append(unit.getName());
        stringBuilder.append("\t");
        final AccountingUnit accountingUnit = unit.getAccountingUnit();
        if (accountingUnit == null) {
            stringBuilder.append(" ");
        } else {
            stringBuilder.append(accountingUnit.getName());
        }
        stringBuilder.append("\t");
        final CostCenter parent = getParent(unit);
        if (parent == null) {
            stringBuilder.append(" ");
        } else {
            stringBuilder.append(parent.getCostCenter());
        }
        stringBuilder.append("\t");
	}

	private String toNumber(final Unit unit) {
		if (unit instanceof CostCenter) {
			final CostCenter costCenter = (CostCenter) unit;
			return costCenter.getCostCenter();
		}
		if (unit instanceof Project) {
			final Project project = (Project) unit;
			return project.getProjectCode();
		}
		return null;
	}

	private String toCode(final Unit unit) {
		return unit.getUnit().getAcronym();
	}

	private Set<User> getActiveAuthorizationSet(final Unit unit) {
        final Set<User> result = new HashSet<User>();
        for (final Authorization authorization : unit.getAuthorizationsSet()) {
            if (authorization.isValid()) {
                final Person person = authorization.getPerson();
                final User user = person.getUser();
                result.add(user);
            }
        }
        if (result.isEmpty()) {
            final Unit parentUnit = unit.getParentUnit();
            if (parentUnit != null) {
                return getActiveAuthorizationSet(parentUnit);
            }
        }
        return result;
    }

    private CostCenter getParent(final Unit unit) {
        final Unit parent = unit.getParentUnit();
        return parent == null || parent instanceof CostCenter ? (CostCenter) parent : getParent(parent);
    }

    private boolean isActive(final Unit unit) {
        return unit.isActive();
    }

    private void check(final String username, final String password) {
        final String eUsername = ExpenditureConfiguration.get().exportStructureServiceUsername();
        final String ePassword = ExpenditureConfiguration.get().exportStructureServicePassword();

        if (!match(username, eUsername) || !match(password, ePassword)) {
            throw new Error("unauthorized.access");
        }
    }

    private boolean match(final String s1, final String s2) {
        return s1 != null && s1.equals(s2);
    }

}
