/*
 * @(#)ExportAuthorizations.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.dbUtils.ExternalDbQuery;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.pstm.Transaction;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public abstract class ExportAuthorizations extends ExportAuthorizations_Base {

	private Map<String, String> employees = new HashMap<String, String>();

	private void loadEmployees() throws SQLException {
		final Connection connection = Transaction.getCurrentJdbcConnection();

		Statement statementQuery = null;
		ResultSet resultSetQuery = null;
		try {
			statementQuery = connection.createStatement();
			resultSetQuery =
					statementQuery
							.executeQuery("select fenix.USER.USER_U_ID, fenix.EMPLOYEE.EMPLOYEE_NUMBER from fenix.EMPLOYEE inner join fenix.USER on fenix.USER.OID_PERSON = fenix.EMPLOYEE.OID_PERSON;");
			int c = 0;
			while (resultSetQuery.next()) {
				final String username = resultSetQuery.getString(1);
				final String employeeNumber = resultSetQuery.getString(2);
				employees.put(username, employeeNumber);
				c++;
			}
		} finally {
			if (resultSetQuery != null) {
				resultSetQuery.close();
			}
			if (statementQuery != null) {
				statementQuery.close();
			}
		}
	}

	public static class InsertAuthorizationQuery implements ExternalDbQuery {

		private String employeeNumber;
		private String costCenterCode;

		public InsertAuthorizationQuery(String employeeNumber, String costCenterCode) {
			super();
			this.employeeNumber = employeeNumber;
			this.costCenterCode = costCenterCode;
		}

		@Override
		public String getQueryString() {
			StringBuilder query = new StringBuilder();
			query.append("INSERT INTO RESPONSAVEL_CC(ID_PROF, ID_CC) VALUES (");
			query.append(employeeNumber).append(",99");
			query.append(costCenterCode).append(")");
			return query.toString();
		}

		@Override
		public void processResultSet(ResultSet resultSet) throws SQLException {
		}
	}

	public static class DeleteAuthorizationQuery implements ExternalDbQuery {

		public DeleteAuthorizationQuery() {
			super();
		}

		@Override
		public String getQueryString() {
			return "DELETE FROM RESPONSAVEL_CC";
		}

		@Override
		public void processResultSet(ResultSet resultSet) throws SQLException {
		}
	}

	public ExportAuthorizations() {
		super();
	}

	@Override
	protected abstract String getDbPropertyPrefix();

	protected abstract String getVirtualHost();

	@Override
	protected void doOperation() throws SQLException {
		final DeleteAuthorizationQuery deleteAuthorizationQuery = new DeleteAuthorizationQuery();
		executeQuery(deleteAuthorizationQuery);

		for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnits()) {
			if (unit instanceof CostCenter) {
				CostCenter costCenter = (CostCenter) unit;
				Set<Person> persons = getCostCenterAuthorizedPersons(costCenter);
				for (Person person : persons) {
					String employeeNumber = employees.get(person.getUsername());
					if (!StringUtils.isEmpty(employeeNumber)) {
						final InsertAuthorizationQuery insertAuthorizationQuery =
								new InsertAuthorizationQuery(employeeNumber, costCenter.getCostCenter());
						executeQuery(insertAuthorizationQuery);
					}
				}
			}
		}
	}

	@Override
	public void executeTask() {
		try {
			VirtualHost.setVirtualHostForThread(getVirtualHost());

			employees = new HashMap<String, String>();
			try {
				loadEmployees();
			} catch (SQLException e) {
				throw new Error(e);
			}

			super.executeTask();
		} finally {
			VirtualHost.releaseVirtualHostFromThread();
		}
	}

	private Set<Person> getCostCenterAuthorizedPersons(Unit costCenter) {
		Set<Person> persons = new HashSet<Person>();
		for (Authorization authorization : costCenter.getAuthorizations()) {
			if (authorization.isValid()) {
				persons.add(authorization.getPerson());
			}
		}
		Unit parentUnit = costCenter.getParentUnit();
		if (parentUnit != null) {
			persons.addAll(getCostCenterAuthorizedPersons(parentUnit));
		}
		return persons;
	}

	@Override
	public String getLocalizedName() {
		return getClass().getName();
	}

	@Override
	protected void handle(final SQLException e, final ExternalDbQuery externalDbQuery) {
		System.out.println(externalDbQuery.getQueryString());
		e.printStackTrace();
	}

}
