/*
 * @(#)SyncSalary.java
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
package module.mission.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import module.organization.domain.Person;
import pt.ist.bennu.backend.util.ConnectionManager;
import pt.ist.dbUtils.ExternalDbOperation;
import pt.ist.dbUtils.ExternalDbQuery;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SyncSalary extends Thread {

    private static DecimalFormat employeeNumberFormat = new DecimalFormat("000000");

    private static String hackEmployeeNumber(final Integer number) {
        return employeeNumberFormat.format(number);
    }

    private static class GovernmentMemberQuery implements ExternalDbQuery {

        private final Integer number;
        private boolean isGovernmentMember = false;

        private GovernmentMemberQuery(final Integer number) {
            this.number = number;
        }

        @Override
        public String getQueryString() {
            return "select emp_num, data_inicio, data_fim from ( "
                    + "SELECT distinct empregado.EMP_NUM, empregado.dt_inic as DATA_INICIO, max(empregado.dt_fim) as DATA_FIM "
                    + "FROM SLTSIT situacao, SLDEMP24 empregado " + "WHERE empregado.EMP_NUM = '" + hackEmployeeNumber(number)
                    + "' and situacao.EMP_SIT = empregado.EMP_SIT and situacao.EMP_SIT in (61) "
                    + "group by empregado.EMP_NUM, empregado.dt_inic, situacao.SIT_DSC) membros "
                    + "where (data_fim is null and data_inicio < sysdate) "
                    + "or (data_fim is not null and sysdate between data_inicio and data_fim)";
        }

        @Override
        public void processResultSet(final ResultSet resultSet) throws SQLException {
            if (resultSet.next()) {
                isGovernmentMember = true;
            }
        }

    }

    private static class GovernmentMemberReader extends ExternalDbOperation {

        private final GovernmentMemberQuery governmentMemberQuery;

        private GovernmentMemberReader(final Integer number) {
            governmentMemberQuery = new GovernmentMemberQuery(number);
        }

        @Override
        protected void doOperation() throws SQLException {
            executeQuery(governmentMemberQuery);
        }

        @Override
        protected String getDbPropertyPrefix() {
            return "db.giaf";
        }

        private boolean isGovernmentMember() {
            execute();
            return governmentMemberQuery.isGovernmentMember;
        }
    }

    private static class SalaryQuery implements ExternalDbQuery {

        private final Integer number;
        private BigDecimal value = null;

        private SalaryQuery(final Integer number) {
            this.number = number;
        }

        @Override
        public String getQueryString() {
            return "SELECT EMP_VENC FROM SLDEMP04 WHERE EMP_NUM = '" + hackEmployeeNumber(number) + "'";
        }

        @Override
        public void processResultSet(final ResultSet resultSet) throws SQLException {
            if (resultSet.next()) {
                value = resultSet.getBigDecimal(1);
            }
        }

    }

    private static class SalaryReader extends ExternalDbOperation {

        private final SalaryQuery salaryQuery;

        private SalaryReader(final Integer number) {
            salaryQuery = new SalaryQuery(number);
        }

        @Override
        protected void doOperation() throws SQLException {
            executeQuery(salaryQuery);
        }

        @Override
        protected String getDbPropertyPrefix() {
            return "db.giaf";
        }

        private BigDecimal getSalary() {
            execute();
            return salaryQuery.value;
        }
    }

    private final Person person;

    public SyncSalary(final Person person) {
        this.person = person;
    }

    public void execute() {
        final Integer number = getPersonNumber();
        final SalaryReader salaryReader = new SalaryReader(number);
        final GovernmentMemberReader governmentMemberReader = new GovernmentMemberReader(number);
        final BigDecimal value = salaryReader.getSalary();
        final boolean isGovernmentMember = governmentMemberReader.isGovernmentMember();
        Salary.setSalary(person, value, isGovernmentMember);
    }

    private Integer getPersonNumber() {
        try {
            return getPersonNumberUnwrapped();
        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    private Integer getPersonNumberUnwrapped() throws SQLException {
        final Connection connection = ConnectionManager.getCurrentSQLConnection();

        Statement statementQuery = null;
        ResultSet resultSetQuery = null;
        try {
            statementQuery = connection.createStatement();
            resultSetQuery =
                    statementQuery
                            .executeQuery("select fenix.EMPLOYEE.EMPLOYEE_NUMBER from fenix.USER inner join fenix.EMPLOYEE on fenix.EMPLOYEE.OID_PERSON = fenix.USER.OID_PERSON where fenix.USER.USER_U_ID = '"
                                    + person.getUser().getUsername() + "'");

            if (resultSetQuery.next()) {
                return Integer.valueOf(resultSetQuery.getInt(1));
            }
            return null;
        } finally {
            if (resultSetQuery != null) {
                resultSetQuery.close();
            }
            if (statementQuery != null) {
                statementQuery.close();
            }
        }
    }

    @Atomic
    @Override
    public void run() {
        this.execute();
    }

    public static void sync(final Person person) {
        final SyncSalary syncSalary = new SyncSalary(person);
        syncSalary.start();
        try {
            Thread.currentThread().sleep(3000);
        } catch (final InterruptedException e) {
            throw new Error();
        }
    }

}
