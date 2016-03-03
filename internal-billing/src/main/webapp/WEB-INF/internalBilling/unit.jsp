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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<script src='<%= contextPath + "/webjars/angular-ui-bootstrap/0.9.0/ui-bootstrap-tpls.min.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

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

    <br/>
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
                                <th>
                                    <spring:message code="label.authorization.start" text="Start"/>
                                </th>
                                <th>
                                    <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                                </th>
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
                                        <td>
                                            <%= authorization.getStartDate().toString("yyyy-MM-dd") %>
                                        </td>
                                        <td>
                                            <%= authorization.getMaxAmount().toFormatString() %>
                                        </td>
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
