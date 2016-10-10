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

<% User user = (User) request.getAttribute("user"); %>
<div class="page-header">
    <h2 class="ng-scope">
        <img class="img-circle" width="50" height="50" alt="" src="<%= user.getProfile().getAvatarUrl() %>">
        <%= user.getProfile().getDisplayName() %> <small><%= user.getUsername() %></small>
    </h2>
</div>
<div class="page-body">
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
</script>

