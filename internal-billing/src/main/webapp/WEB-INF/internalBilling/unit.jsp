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
<%@page import="java.util.Collections"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="pt.ist.internalBilling.domain.BillableTransaction"%>
<%@page import="java.util.TreeMap"%>
<%@page import="module.finance.util.Money"%>
<%@page import="java.util.Map"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Person"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.VirtualHostingService"%>
<%@page import="pt.ist.internalBilling.domain.PhoneService"%>
<%@page import="pt.ist.internalBilling.domain.PrintService"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.UnitBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.UserBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>

<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<% Unit unit = (Unit) request.getAttribute("unit"); %>
<div class="page-header">
    <h2 class="ng-scope">
        <%= unit.getPresentationName() %>
    </h2>
</div>
<div class="page-body">
    <% if (unit.getParentUnit() != null) { %>
        <p>
            <spring:message code="label.unit.parent" text="Parent Unit"/>
            <a href="<%= contextPath + "/internalBilling/unit/" + unit.getParentUnit().getExternalId() %>">
                <%= unit.getParentUnit().getPresentationName() %>
            </a>
        </p>
    <% } %>

<% if (InternalBillingService.canViewUnitServices(unit)) { %>
        <%
            final Map<String, Money> dayMap = new TreeMap<String, Money>(String.CASE_INSENSITIVE_ORDER);
            for (final Billable billable : unit.getBillableSet()) {
                for (final BillableTransaction transaction : billable.getBillableTransactionSet()) {
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
            }
        %>

    <% if (!dayMap.isEmpty()) { %>
    <br/>
    <div id="movementsChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

    <script type="text/javascript">
    $(function () {
            Highcharts.chart('movementsChart', {
                chart: {
                    zoomType: 'x'
                },
                title: {
                    text: '<spring:message code="label.internalBilling.transactions" text="Movimentos"/>'
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
                    point: {
                        events: {
                            click: function(){
                            	location.href = this.options.url
                            }
                        }
                    },
                    <% boolean isFirst = true; %>
                    data: [<% for (final Entry<String, Money> entry : dayMap.entrySet()) { %>
                                  <% int month = Integer.parseInt(entry.getKey().substring(5, 7)) + 1; %>
                                  <% String dateForUrl = entry.getKey().substring(0, 4) + "-" + (month < 10 ? "0" : "") + month; %>                    
                                  <% if (isFirst) { isFirst = false; } else { %>,<% } %><%= "{x: Date.UTC(" + entry.getKey()
                                          + "), y: " + entry.getValue().getValue().toString()
                                          + ", url: '" + contextPath + "/internalBilling/unit/" + unit.getExternalId() + "/transactions/" + dateForUrl + "?view=byDay"
                                          + "'}" %>
                           <% } %>]
                }]
            });
    });
    </script>

    <div class="page-header">
    </div>
    <% } %>

    <br/>
<% } %>
    <h3>
        <spring:message code="label.services.subscribed" text="Subscribed Services"/>
    </h3>
    <div>
        <% for (final BillableService billableService : InternalBillingService.getInstance().getBillableServiceSet()) { %>
            <% request.setAttribute("billableService", billableService); %>
            <jsp:include page='<%= "unit_" + billableService.getClass().getSimpleName() + ".jsp" %>'/>
        <% } %>
    </div>

    <div class="page-header">
    </div>

    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-body">
                <div>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.unit.children" text="Subunits"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (final Unit childUnit : unit.getSubUnitsSet()) { %>
                                <tr>
                                    <td>
                                        <a href="<%= contextPath + "/internalBilling/unit/" + childUnit.getExternalId() %>">
                                            <%= childUnit.getPresentationName() %>
                                        </a>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-body">
                <div>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.unit.authorities" text="Authorities"/>
                                </th>
<%-- 
                                <th>
                                    <spring:message code="label.authorization.start" text="Start"/>
                                </th>
                                <th>
                                    <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                                </th>
 --%>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (final Authorization authorization : unit.getAuthorizationsSet()) { %>
                                <% if (authorization.isValid()) { %>
                                    <tr>
                                        <td>
                                            <img class="img-circle" width="30" height="30" alt=""
                                                src="<%= authorization.getPerson().getUser().getProfile().getAvatarUrl() %>">
                                            <a href="<%= contextPath + "/internalBilling/user/" + authorization.getPerson().getUser().getExternalId() %>">
                                                <%= authorization.getPerson().getUser().getProfile().getDisplayName() %>
                                            </a>
                                        </td>
<%-- 
                                        <td>
                                            <%= authorization.getStartDate().toString("yyyy-MM-dd") %>
                                        </td>
                                        <td>
                                            <%= authorization.getMaxAmount().toFormatString() %>
                                        </td>
 --%>
                                    </tr>
                                <% } %>
                            <% } %>
                        </tbody>
                    </table>

                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.unit.observers" text="Observers"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (final Person observer : unit.getObserversSet()) { %>
                                <tr>
                                    <td>
                                        <img class="img-circle" width="30" height="30" alt=""
                                                src="<%= observer.getUser().getProfile().getAvatarUrl() %>">
                                        <a href="<%= contextPath + "/internalBilling/user/" + observer.getUser().getExternalId() %>">
                                            <%= observer.getUser().getProfile().getDisplayName() %>
                                        </a>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
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
</script>
