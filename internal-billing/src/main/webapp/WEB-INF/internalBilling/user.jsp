<%--
    Copyright Â© 2014 Instituto Superior Técnico

    This file is part of the Internal Billing Module.

    The Internal Billing Module is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Internal Billing Module is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with MGP Viewer.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="module.finance.util.Money"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="java.text.Collator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<%@page import="pt.ist.internalBilling.domain.BillableTransaction"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="pt.ist.internalBilling.domain.VirtualHostingService"%>
<%@page import="pt.ist.internalBilling.domain.PrintService"%>
<%@page import="pt.ist.internalBilling.domain.PhoneService"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/highcharts/5.0.3/highcharts.js" %>'></script>

<% User user = (User) request.getAttribute("user"); %>
<div class="page-header">
    <h2 class="ng-scope">
        <img class="img-circle" width="50" height="50" alt="" src="<%= user.getProfile().getAvatarUrl() %>">
        <%= user.getProfile().getDisplayName() %> <small><%= user.getUsername() %></small>
    </h2>
</div>
<div class="page-body">

<% int i = 0; %>
<% if (InternalBillingService.canViewUserServices(user)) { %>
    <h3>
        <spring:message code="label.internalBilling.billing.details" text="Billing Details"/>
    </h3>
    <div>

        <div id="movementsChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

        <% final List<BillableTransaction> transactions = new ArrayList<BillableTransaction>(user.getBillableTransactionSet()); %>
        <% Collections.sort(transactions, BillableTransaction.COMPARATOR_BY_DATE.reversed()); %>
        <%
            final Map<String, Money> dayMap = new TreeMap<String, Money>(String.CASE_INSENSITIVE_ORDER);
            for (final BillableTransaction transaction : transactions) {
                    final DateTime dt = transaction.getTxDate();
                    final Money value = transaction.getValue();

                    final int year = dt.getYear();
                    final int month = dt.getMonthOfYear() - 1;
                    final int day = dt.getDayOfMonth();
                    final String dayKey = "" + year + "," + (month < 10 ? "0" : "") + month + "," + (day < 10 ? "0" : "") + day;
                    if (!dayMap.containsKey(dayKey)) {
                        dayMap.put(dayKey, Money.ZERO);
                    }
                    dayMap.put(dayKey, dayMap.get(dayKey).add(value));
            }
        %>

    <script type="text/javascript">
    $(function () {
            Highcharts.chart('movementsChart', {
                chart: {
                    zoomType: 'x'
                },
                title: {
                    text: ''
                },
                subtitle: {
                    text: ''
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: '<spring:message code="label.internalBilling.transactions.value" text="Value"/>'
                    }
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                            stops: [
                                [0, Highcharts.getOptions().colors[0]],
                                [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        },
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                lineWidth: 1
                            }
                        },
                        threshold: null
                    }
                },

                series: [{
                    type: 'area',
                    name: '<spring:message code="label.internalBilling.transactions.value" text="Value"/>',
                    <% boolean isFirst = true; %>
                    data: [<% for (final Entry<String, Money> entry : dayMap.entrySet()) { %>
                                  <% int month = Integer.parseInt(entry.getKey().substring(5, 7)) + 1; %>
                                  <% String dateForUrl = entry.getKey().substring(0, 4) + "-" + (month < 10 ? "0" : "") + month; %>                    
                                  <% if (isFirst) { isFirst = false; } else { %>,<% } %><%= "{x: Date.UTC(" + entry.getKey()
                                          + "), y: " + entry.getValue().getValue().toString()
                                          + "}" %>
                           <% } %>]
                }]
            });
    });
    </script>

        <table class="table">
            <thead>
                <tr>
                    <th>
                        <spring:message code="label.internalBilling.billing.details.txDate" text="Transaction Date"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billing.details.value" text="Value"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billing.details.label" text="Description"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billing.details.details" text="Details"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billing.details.unit" text="Unit"/>
                    </th>
                </tr>
            </thead>
            <tbody>
                <% for (final BillableTransaction transaction : transactions) { %>
                    <% final Billable billable = transaction.getBillable(); %>
                    <% final BillableService service = billable == null ? null : billable.getBillableService(); %>
                    <tr <% if (i++ >= 5) { %>style="display: none;"<% } %> id="billingTransactionDetail<%= i %>">
                        <td>
                            <%= transaction.getTxDate().toString("yyyy-MM-dd HH:mm") %>
                        </td>
                        <td>
                            <%= transaction.getValue().getValue().toString() %>
                        </td>
                        <td>
                            <%= transaction.getLabel() %>
                        </td>
                        <td>
                            <%= transaction.getDescription() %>
                        </td>
                        <td>
                            <% if (transaction.getBillable() == null) { %>
                                <spring:message code="label.internalBilling.billing.details.unit.not.available" text="--"/>
                            <% } else { %>
                                <a href="<%= contextPath + "/internalBilling/unit/" + transaction.getBillable().getUnit().getExternalId() %>">
                                    <%= transaction.getBillable().getUnit().getPresentationName() %>
                                </a>
                            <% } %>
                        </td>
                    </tr>   
                <% } %>
                <% if (transactions.size() > 5) { %>
                    <tr id="showMoreElementsRow">
                        <td colspan="5" style="text-align: center;">
                            <span onclick="showMoreBillingTransactionDetails();" class="btn btn-default">
                                <spring:message code="label.internalBilling.billing.details.show.more" text="Show More"/>
                                (<span id="billingRowCount"><%= transactions.size() - 6 %></span>)
                            </span>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <br/>
<% } %>
    <h3>
        <spring:message code="label.services.subscribed" text="Subscribed Services"/>
        <a class="btn btn-default" href="<%= contextPath + "/internalBilling/billableService/viewUserLogs?user=" + user.getExternalId() %>" style="float: right;">
            <spring:message code="label.internalBilling.billableService.viewLogs"/>
        </a>
    </h3>
    <div>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <spring:message code="label.internalBilling.billableService.title" text="Service"/>
                    </th>
                    <th>
                        <spring:message code="label.unit" text="Financer"/>
                    </th>
                    <th>
                        <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billableService.status" text="Status"/>
                    </th>
                </tr>
            </thead>
            <tbody>
                <% if (user.getUserBeneficiary() != null) { %>
                    <% for (final Billable billable : user.getUserBeneficiary().getBillableSet()) { %>
                        <% if (billable.getBillableStatus() != BillableStatus.REVOKED) { %>
                            <tr>
                                <td>
                                    <% if (billable.getBillableService().getClass().equals(PhoneService.class)) { %>
                                        <img alt="Phone Service" src="<%= request.getContextPath()%>/img/internal-billing/phoneService.png" width="30px;">
                                    <% } else if (billable.getBillableService().getClass().equals(PrintService.class)) { %>
                                        <img alt="Phone Service" src="<%= request.getContextPath()%>/img/internal-billing/printService.png" width="30px;">
                                    <% } else if (billable.getBillableService().getClass().equals(VirtualHostingService.class)) { %>
                                        <img alt="Phone Service" src="<%= request.getContextPath()%>/img/internal-billing/virtualHostService.png" width="30px;">
                                    <% } else { %>
                                        <spring:message code="<%= "label." + billable.getBillableService().getClass().getName() %>"/>
                                    <% } %>
                                </td>
                                <td>
                                    <a href="<%= contextPath + "/internalBilling/unit/" + billable.getUnit().getExternalId() %>">
                                        <%= billable.getUnit().getPresentationName() %>
                                    </a>
                                </td>
                                <td>
                                    <% final JsonObject configuration = billable.getConfigurationAsJson(); %>
                                    <% final JsonElement maxValue = configuration.get("maxValue"); %>
                                    <%= maxValue == null || maxValue.isJsonNull() ? "" : Money.importFromString(maxValue.getAsString()).toFormatString() %>
                                </td>
                                <td>
                                    <% final String styleClassStatus =
                                            billable.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION ? "font-weight: bold; color: orange;" :
                                                billable.getBillableStatus() == BillableStatus.AUTHORIZED ? "font-weight: bold; color: green;" :
                                                    ""; %>
                                    <span style="font-weight: bold; color: red;"></span>
                                    <span style="<%= styleClassStatus %>">
                                        <spring:message code="<%= "label.internalBilling.billableService.status." + billable.getBillableStatus().name() %>"/>
                                    </span>
                                    <% if (billable.getBillableStatus() == BillableStatus.AUTHORIZED) { %>
                                        <br/>
                                        <% final String styleClassState =
                                                billable.getServiceStatus() == ServiceStatus.PENDING_ACTIVATION ? "font-weight: bold; color: orange;" :
                                                    billable.getServiceStatus() == ServiceStatus.ACTIVE ? "font-weight: bold; color: green;" :
                                                        ""; %>
                                        <span style="font-weight: bold; color: red;"></span>
                                        <span style="<%= styleClassState %>">
                                            <spring:message code="<%= "label.internalBilling.billableService.status." + billable.getServiceStatus().name() %>"/>
                                        </span>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                <% } %>
            </tbody>
        </table>
    </div>

    <br/>
    <h3>
        <spring:message code="label.unit.responsibilities" text="Responsibilites"/>
    </h3>
    <div>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <spring:message code="label.unit" text="Unit"/>
                    </th>
                    <th>
                        <spring:message code="label.authorization.start" text="Start"/>
                    </th>
                    <th>
                        <spring:message code="label.authorization.end" text="End"/>
                    </th>
                    <th>
                        <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                    </th>
                </tr>
            </thead>
            <tbody>
                <% for (final Authorization authorization : user.getExpenditurePerson().getAuthorizationsSet()) { %>
                    <tr>
                        <td>
                            <a href="<%= contextPath + "/internalBilling/unit/" + authorization.getUnit().getExternalId() %>">
                                <%= authorization.getUnit().getPresentationName() %>
                            </a>
                        </td>
                        <td>
                            <%= authorization.getStartDate().toString("yyyy-MM-dd") %>
                        </td>
                        <td>
                            <% if (authorization.getEndDate() != null) { %>
                                <%= authorization.getEndDate().toString("yyyy-MM-dd") %>
                            <% } %>
                        </td>
                        <td>
                            <%= authorization.getMaxAmount().toFormatString() %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <br/>
    <h3>
        <spring:message code="label.unit.observe" text="Observations"/>
    </h3>
    <div>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <spring:message code="label.unit" text="Unit"/>
                    </th>
                </tr>
            </thead>
            <tbody>
                <% for (final Unit unit : user.getExpenditurePerson().getObservableUnitsSet()) { %>
                    <tr>
                        <td>
                            <a href="<%= contextPath + "/internalBilling/unit/" + unit.getExternalId() %>">
                                <%= unit.getPresentationName() %>
                            </a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

</div>

<script type="text/javascript" >
    function showAuthorizeForm( billableId ) {
        var rowName = 'authorize' + billableId + 'Row';
        var buttonName = 'authorize' + billableId + 'Button';
        document.getElementById(rowName).style.display = 'table-row';
        document.getElementById(buttonName).style.display = 'none';
    }
    function showDetails( billableId ) {
        var rowName = 'details' + billableId + 'Row';
        var buttonName = 'details' + billableId + 'Button';
        if (document.getElementById(rowName).style.display == 'table-row') {
            document.getElementById(rowName).style.display = 'none';
        } else {
            document.getElementById(rowName).style.display = 'table-row';
        }
    }
    var countBillingTx = 5;
    var max = <%= i %>;
    function showMoreBillingTransactionDetails() {
    	for (i = 0; i <= 10; i++) {
    		if (countBillingTx < max) {
    		    var rowId = 'billingTransactionDetail' + (countBillingTx++);
    		    document.getElementById(rowId).style.display = "table-row";
    		}
    	}
    	if (countBillingTx == max) {
    		document.getElementById('showMoreElementsRow').style.display = "none";
    	}
    	var c = Number(document.getElementById('billingRowCount').textContent) - 10;
    	document.getElementById('billingRowCount').textContent = c;
    }
</script>

