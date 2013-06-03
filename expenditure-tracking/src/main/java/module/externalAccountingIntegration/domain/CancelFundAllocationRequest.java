/*
 * @(#)CancelFundAllocationRequest.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the External Accounting Integration Module.
 *
 *   The External Accounting Integration Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The External Accounting Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the External Accounting Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.externalAccountingIntegration.domain;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import pt.ist.dbUtils.ExternalDbCall;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class CancelFundAllocationRequest extends CancelFundAllocationRequest_Base implements ExternalDbCall {

    public CancelFundAllocationRequest() {
        super();
    }

    public CancelFundAllocationRequest(final FundAllocationRequest fundAllocationRequest) {
        this();
        setFundAllocationRequest(fundAllocationRequest);
    }

    @Override
    public String getQueryString() {
        if (getExternalRegistrationDate() == null) {
            return "BEGIN ?:=" + getMethodName() + "(?); END;";
        }
        return null;
    }

    protected String getMethodName() {
        return isFinalFundAllocation() ? "FUNC_INT_COMPRAS_AN_JUST" : "FUNC_INT_COMPRAS_AN_CAB";
    }

    @Override
    public void prepareCall(final CallableStatement callableStatement) throws SQLException {
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.setString(2, getInteractionId());
    }

    @Override
    public void processResultSet(final ResultSet resultSet, final CallableStatement callableStatement) throws SQLException {
        if (getExternalRegistrationDate() == null) {
            //final boolean returnValue = callableStatement.getBoolean(1);
            // If true! Really? Yup that is correct, we actually only need to be sure that the statement was 
            // executed and returned a result... the results value itself is unimportant.
            //if (returnValue || !returnValue) {
            registerOnExternalSystem();
            //}
        }
    }

    @Override
    public void processResultSet(final ResultSet resultSet) throws SQLException {
    }

    @Override
    protected String getTableName() {
        return getFundAllocationRequest().getTableName();
    }

    private boolean isFinalFundAllocation() {
        return getFundAllocationRequest().isFinalFundAllocation();
    }

    public boolean canShareTransaction() {
        return false;
    }

}
