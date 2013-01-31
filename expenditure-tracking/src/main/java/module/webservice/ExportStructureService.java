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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
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
		final String content = generateCostCenterList();
		return Response.ok(content, "text/csv").build();
	}

	@GET
	@Path("listCostCenters.csv")
	@Produces("text/csv")
	public Response listCostCentersParams(@QueryParam("username") final String username,
			@QueryParam("password") final String password) {
		return listCostCenters(username, password);
	}

	private String generateCostCenterList() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
			if (unit instanceof CostCenter) {
				final CostCenter costCenter = (CostCenter) unit;
				if (isActive(costCenter)) {
					final Set<User> authorities = getActiveAuthorizationSet(costCenter);

					if (authorities.isEmpty()) {
						stringBuilder.append(costCenter.getCostCenter());
						stringBuilder.append("\t");
						stringBuilder.append(costCenter.getName());
						stringBuilder.append("\t");
						final AccountingUnit accountingUnit = costCenter.getAccountingUnit();
						if (accountingUnit == null) {
							stringBuilder.append(" ");
						} else {
							stringBuilder.append(accountingUnit.getName());
						}
						stringBuilder.append("\t");
						final CostCenter parent = getParent(costCenter);
						if (parent == null) {
							stringBuilder.append(" ");
						} else {
							stringBuilder.append(parent.getCostCenter());
						}
						stringBuilder.append("\t");

						// responsáveis
						stringBuilder.append(" ");
						stringBuilder.append("\t");

						// e-mail
						stringBuilder.append(" ");

						stringBuilder.append("\n");

					} else {
						for (final User user : authorities) {
							stringBuilder.append(costCenter.getCostCenter());
							stringBuilder.append("\t");
							stringBuilder.append(costCenter.getName());
							stringBuilder.append("\t");
							final AccountingUnit accountingUnit = costCenter.getAccountingUnit();
							if (accountingUnit == null) {
								stringBuilder.append(" ");
							} else {
								stringBuilder.append(accountingUnit.getName());
							}
							stringBuilder.append("\t");
							final CostCenter parent = getParent(costCenter);
							if (parent == null) {
								stringBuilder.append(" ");
							} else {
								stringBuilder.append(parent.getCostCenter());
							}
							stringBuilder.append("\t");

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
		}
		return stringBuilder.toString();
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

	private boolean isActive(final CostCenter costCenter) {
		return costCenter.isActive();
	}

	private void check(final String username, final String password) {
		final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
		final String hostname = virtualHost.getHostname();

		final String keyUsername = "exportStructureService.username." + hostname;
		final String keyPassword = "exportStructureService.password." + hostname;

		final String eUsername = PropertiesManager.getProperty(keyUsername);
		final String ePassword = PropertiesManager.getProperty(keyPassword);

		if (!match(username, eUsername) || !match(password, ePassword)) {
			throw new Error("unauthorized.access");
		}
	}

	private boolean match(final String s1, final String s2) {
		return s1 != null && s1.equals(s2);
	}

}
