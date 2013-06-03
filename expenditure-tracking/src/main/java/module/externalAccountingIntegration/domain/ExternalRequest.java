/*
 * @(#)ExternalRequest.java
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

import java.sql.SQLException;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.scheduler.Task;
import pt.ist.bennu.core.domain.scheduler.TaskConfiguration;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.dbUtils.ExternalDbQuery;

/**
 * 
 * @author Luis Cruz
 * 
 */
public abstract class ExternalRequest extends ExternalRequest_Base implements ExternalDbQuery {

    public ExternalRequest() {
        super();
        setInteractionId(getExternalId());
        final ExternalAccountingIntegrationSystem instance = ExternalAccountingIntegrationSystem.getInstance();
        setExternalAccountingIntegrationSystem(instance);
        setExternalAccountingIntegrationSystemFromPendingResult(instance);
        setCreationDate(new DateTime());
        preemptTaskSync();
    }

    public void registerOnExternalSystem() {
        setExternalRegistrationDate(new DateTime());
    }

    protected String insertQuery(final Object... args) {
        final StringBuilder builder = new StringBuilder("insert into ");
        builder.append(getTableName());
        builder.append(" (");
        for (int i = 0; i < args.length; i += 2) {
            if (i > 0) {
                builder.append(", ");
            }
            final String columnName = (String) args[i];
            builder.append(columnName);
        }
        builder.append(") values (");
        for (int i = 1; i < args.length; i += 2) {
            if (i > 1) {
                builder.append(", ");
            }
            final Object object = args[i];
            appendObjectValue(builder, object);
        }
        builder.append(")");
        return builder.toString();
    }

    protected void appendObjectValue(final StringBuilder builder, final Object object) {
        if (object == null) {
            builder.append("null");
        } else if (object.getClass().isPrimitive()) {
            builder.append(object);
        } else if (object instanceof String) {
            final String string = (String) object;
            if (string.trim().isEmpty()) {
                builder.append("null");
            } else {
                builder.append('\'');
                builder.append(string.replace('\'', '"'));
                builder.append('\'');
            }
        } else if (object instanceof Money) {
            final Money money = (Money) object;
            builder.append(money.getValue());
        } else if (object instanceof Number) {
            final Number number = (Number) object;
            builder.append(number.toString());
        }
    }

    protected String selectQuery(final String interactionColumnName, final long interactionId, String... columns) {
        final StringBuilder builder = new StringBuilder("select ");
        for (final String column : columns) {
            if (builder.length() > 7) {
                builder.append(", ");
            }
            builder.append(column);
        }
        builder.append(" from ");
        builder.append(getTableName());
        builder.append(" where  ");
        builder.append(interactionColumnName);
        builder.append(" = ");
        builder.append(interactionId);
        return builder.toString();
    }

    protected String updateQuery(final Object... args) {
        final StringBuilder builder = new StringBuilder("update ");
        builder.append(getTableName());
        builder.append(" set");
        for (int i = 0; i < args.length; i += 2) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(' ');
            final String columnName = (String) args[i];
            builder.append(columnName);
            final Object value = args[i + 1];
            appendObjectValue(builder, value);
        }

        appendWhereClause(builder);

        return builder.toString();
    }

    protected void appendWhereClause(final StringBuilder builder) {
    }

    protected String limitStringSize(final String string, int size) {
        if (string == null) {
            return null;
        }
        return string.length() <= size ? string : string.substring(0, size - 3) + "...";
    }

    protected abstract String getTableName();

    public boolean canShareTransaction() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }

    private void preemptTaskSync() {
        final Package p = getClass().getPackage();
        for (final TaskConfiguration configuration : MyOrg.getInstance().getTaskConfigurationsSet()) {
            final Task task = configuration.getTask();
            if (task.getClass().getPackage() == p) {
                task.invokeNow();
            }
        }
    }

    public void handle(final SQLException e) {
        System.out.println("SQL Exception for query: ");
        System.out.println(getQueryString());
        e.printStackTrace();
        System.out.println("End of stack trace.");
        throw new Error(e);
    }

}
