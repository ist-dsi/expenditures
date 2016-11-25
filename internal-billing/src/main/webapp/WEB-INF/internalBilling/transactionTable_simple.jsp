<%@page import="pt.ist.internalBilling.domain.BillableTransaction"%>
<%@page import="java.util.Collection"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.time.format.DateTimeFormatter"%>

<% final String contextPath = request.getContextPath(); %>
<% final Collection<BillableTransaction> txs = (Collection<BillableTransaction>) request.getAttribute("txs"); %>
<% final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<table class="table">
    <thead>
        <tr>
            <th width="11%">
                <spring:message code="label.equitrac.entry.txDate" text="Transaction Date"/>
            </th>
            <th>
                <spring:message code="label.equitrac.entry.amount" text="Value"/>
            </th>
            <th width="14%">
                <spring:message code="label.equitrac.entry.label" text="Description"/>
            </th>
            <th>
                <spring:message code="label.equitrac.entry.details" text="Details"/>
            </th>
        </tr>
    </thead>
    <tbody>
        <% for (final BillableTransaction tx : txs) { %>
            <% final BigDecimal value = tx.getValue().getValue(); %>
            <tr <% if (value.intValue() > 10) { %> style="background-color: #ffaaaa;" <% } else if (value.intValue() > 5) { %> style="background-color: #ffd4aa; "<% } %>>
                <td>
                    <%= tx.getTxDate().toString("yyyy-MM.dd HH:mm") %>
                </td>
                <td>
                    <%= value.toPlainString() %>
                </td>
                <td>
                    <%= tx.getLabel() %>
                </td>
                <td>
                    <% final int l = tx.getDescription().length(); %>
                    <%= l > 50 ? tx.getDescription().substring(0, 50) + " ..." : tx.getDescription() %>
                </td>
            </tr>
        <% } %>   
    </tbody>
</table>

