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
<%@page import="java.util.Set"%>
<%@page import="pt.ist.internalBilling.domain.BillableLog"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@page import="pt.ist.internalBilling.domain.VirtualHostingService"%>
<%@page import="pt.ist.internalBilling.domain.PhoneService"%>
<%@page import="pt.ist.internalBilling.domain.PrintService"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% final String contextPath = request.getContextPath(); %>
<% final BillableService billableService = (BillableService) request.getAttribute("billableService"); %>
<% final Unit unit = (Unit) request.getAttribute("unit"); %>
<% final Set<BillableLog> logs = (Set<BillableLog>) request.getAttribute("logs"); %>

<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<div class="page-header">
    <h2>
        <spring:message code="label.internalBilling.billableService.Logs" text="Logs"/>
        <%= unit.getPresentationName() %>
    </h2>
    <h3>
        <% if (billableService instanceof PrintService) { %>
            <img alt="Print Service" src="<%= request.getContextPath()%>/img/internal-billing/printService.png" width="30px;" style="margin-right: 10px;">
        <% } else if (billableService instanceof PhoneService) { %>
            <img alt="Phone Service" src="<%= request.getContextPath()%>/img/internal-billing/phoneService.png" width="30px;" style="margin-right: 10px;">
        <% } else if (billableService instanceof VirtualHostingService) { %>
            <img alt="Virtual Hosting Service" src="<%= request.getContextPath()%>/img/internal-billing/virtualHostService.png" width="30px;" style="margin-right: 10px;">
        <% } %>
        <%= billableService.getTitle() %>
    </h3>
</div>

<div class="page-body">
    <table class="table">
        <thead>
            <tr>
                <th width="11%">
                    <spring:message code="label.operation.date"/>
                </th>
                <th>
                    <spring:message code="label.operation"/>
                </th>
                <th width="35%">
                    <spring:message code="label.operation.executor"/>
                </th>
            </tr>
        </thead>
        <tbody>
            <% for (final BillableLog log : logs) { %>
                <tr>
                    <td>
                        <%= log.getWhenInstant().toString("yyyy-MM-dd HH:mm") %>
                    </td>
                    <td>
                        <%= log.getDescription() %>
                    </td>
                    <td>
                        <img class="img-circle" width="30" height="30" alt="" src="<%= log.getUser().getProfile().getAvatarUrl() %>">
                        <%= log.getUser().getDisplayName() %>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>

<script type="text/javascript">
</script>
