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
<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.VirtualHostingService"%>
<%@page import="pt.ist.internalBilling.domain.PrintService"%>
<%@page import="pt.ist.internalBilling.domain.PhoneService"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="java.util.stream.Stream"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization"%>
<%@page import="java.util.Set"%>
<%@page import="pt.ist.internalBilling.domain.UnitBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.UserBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/bennu-portal/js/angular.min.js" %>'></script>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<script src='<%= contextPath + "/webjars/angular-ui-bootstrap/0.9.0/ui-bootstrap-tpls.min.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<div class="page-body">
    <br/>
    <form class="form-horizontal" action="<%= contextPath + "/internalBilling/search" %>" method="POST">
        <div class="form-group">
            <div class="col-sm-1">
            </div>
            <div class="col-sm-8">
                <spring:message var="searchPlaceholder" scope="request" code="label.internalBilling.billableService.unitOrPerson"/>
                <input type="text" id="searchTerm" name="searchTerm" required="required" class="form-control"
                    placeholder="<%= request.getAttribute("searchPlaceholder") %>"/>
                <input type="hidden" id="unitOrUser" name="unitOrUser" value="">
            </div>
            <div class="col-sm-3">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.select" text="Select" />
                </button>
            </div>
        </div>
        <div class="form-group">
        </div>
    </form>

    <% User user = Authenticate.getUser(); %>
    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.services.subscribed.my" text="My Services"/>
            </div>
            <div class="panel-body">
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
                                    <spring:message code="label.internalBilling.billableService.status" text="Status"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (user != null && user.getUserBeneficiary() != null) { %>
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
            </div>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.internalBilling.billableService.pendingMyAuthorization" text="Pending Authorizations"/>
            </div>
            <div class="panel-body">
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
                                    <spring:message code="label.internalBilling.billableService.beneficiary" text="Beneficiary"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (final Billable billable : (Set<Billable>) request.getAttribute("pendingAuthorization")) { %>
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
                                            <%= billable.getUnit().getUnit().getAcronym() %>
                                        </a>
                                    </td>
                                    <td>
                                        <% if (billable.getBeneficiary() instanceof UserBeneficiary) { %>
                                            <% final UserBeneficiary beneficiary = (UserBeneficiary) billable.getBeneficiary(); %>
                                            <img class="img-circle" width="30" height="30" alt=""
                                                src="<%= beneficiary.getUser().getProfile().getAvatarUrl() %>">
                                            <%= beneficiary.getUser().getProfile().getDisplayName() %>
                                        <% } else if (billable.getBeneficiary() instanceof UnitBeneficiary) { %>
                                            <% final UnitBeneficiary beneficiary = (UnitBeneficiary) billable.getBeneficiary(); %>
                                            <%= beneficiary.getUnit().getPresentationName() %>
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
            </div>
        </div>
    </div>

    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.unit.mine" text="My Units"/>
            </div>
            <div class="panel-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.unit" text="Unit"/>
                                </th>
                                <th>
                                    <spring:message code="label.services.active" text="Active Services"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (user != null && user.getExpenditurePerson() != null) { %>
                                <% for (final Authorization authorization : user.getExpenditurePerson().getAuthorizationsSet()) { %>
                                    <% if (authorization.isValid()) { %>
                                        <% final Unit unit = authorization.getUnit(); %>
                                        <tr>
                                            <td>
                                                <a href="<%= contextPath + "/internalBilling/unit/" + unit.getExternalId() %>">
                                                    <%= unit.getPresentationName() %>
                                                </a>
                                            </td>
                                            <td>
                                                <%
                                                    int[] counts = new int[BillableStatus.values().length];
                                                    for (final Billable billable : unit.getBillableSet()) {
                                                        counts[billable.getBillableStatus().ordinal()]++;
                                                    }
                                                %>
                                                <spring:message var="pendingAuthorizationCount" code="label.services.pendingAuthorization.count"
                                                        text="PendingAuthorization" arguments="<%= counts[BillableStatus.PENDING_AUTHORIZATION.ordinal()] %>"/>
                                                <spring:message var="activeCount" code="label.services.active.count" text="Active" arguments="<%= counts[BillableStatus.AUTHORIZED.ordinal()] %>"/>
                                                <spring:message var="revokedCount" code="label.services.revoked.count" text="Revoked" arguments="<%= counts[BillableStatus.REVOKED.ordinal()] %>"/>
                                                <div title="${pendingAuthorizationCount}
${activeCount}
${revokedCount}">
                                                <span style="color: orange; font-weight: bold;">
                                                    <%= counts[BillableStatus.PENDING_AUTHORIZATION.ordinal()] %>
                                                </span>
                                                /
                                                <span style="color: green; font-weight: bold;">
                                                    <%= counts[BillableStatus.AUTHORIZED.ordinal()] %>
                                                </span>
                                                /
                                                <span>
                                                    <%= counts[BillableStatus.REVOKED.ordinal()] %>
                                                </span>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                <% } %>
                            <% } %>
                        </tbody>
                    </table>
            </div>
        </div>
    </div>

</div>

<style>
    .ui-autocomplete-loading{background: url(<%= contextPath %>/images/autocomplete/spinner.gif) no-repeat right center}
</style>

<script type="text/javascript" >
    var pageContext= '<%=contextPath%>';
    $(function() {
        $('#searchTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(pageContext + "/internalBilling/billableService/availableUnits", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#searchTerm" ).val( ui.item.label );
                $( "#unitOrUser" ).val( ui.item.value );               
                return false;
            }
        });
    });
</script>
