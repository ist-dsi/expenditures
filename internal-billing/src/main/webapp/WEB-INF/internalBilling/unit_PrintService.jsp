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
<%@page import="java.util.Map.Entry"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="pt.ist.internalBilling.domain.BillableTransaction"%>
<%@page import="module.finance.util.Money"%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.UnitBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.UserBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@page import="pt.ist.internalBilling.domain.PrintService"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>
<% Unit unit = (Unit) request.getAttribute("unit"); %>
<% final PrintService printService = (PrintService) request.getAttribute("billableService"); %>

<div class="panel panel-default">
    <div class="panel-heading">
        <img alt="Phone Service" src="<%= request.getContextPath()%>/img/internal-billing/printService.png" width="30px;" style="margin-right: 10px;">
        <%= printService.getTitle() %>
        <a class="btn btn-primary" href="<%= contextPath + "/internalBilling/billableService/subscribeService?unit="
                + unit.getExternalId() + "&amp;billableService=" + printService.getExternalId() %>" style="float: right; margin-left: 5px;">
            <spring:message code="label.internalBilling.billableService.subscribe"/>
        </a>
        <% if (InternalBillingService.canViewUnitServices(unit)) { %>
            <a class="btn btn-default" href="<%= contextPath + "/internalBilling/billableService/viewLogs?unit="
                        + unit.getExternalId() + "&amp;billableService=" + printService.getExternalId() %>" style="float: right;">
                <spring:message code="label.internalBilling.billableService.viewLogs"/>
            </a>
        <% } %>
    </div>
    <div class="panel-body">
        <table class="table">
            <thead>
                <tr>
                    <th>
                        <spring:message code="label.internalBilling.billableService.beneficiary" text="Beneficiary"/>
                    </th>
                    <th>
                        <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billableService.status" text="Status"/>
                    </th>
                    <th width="5%">
                    </th>
                    <th width="5%">
                    </th>
                </tr>
            </thead>
            <tbody>
                <% int pendingAuhtorizationCount = 0; %>
                <% for (final Billable billable : unit.getBillableSet()) { %>
                    <% if (!billable.isCurrentUserAllowedToView()) continue;  %>
                    <% if (billable.getBillableStatus() == BillableStatus.REVOKED || billable.getBillableService() != printService) continue;  %>
                    <% final JsonObject configuration = billable.getConfigurationAsJson(); %>
                    <% final JsonElement maxValue = configuration.get("maxValue"); %>
                    <tr id="<%= "revoke" + billable.getExternalId() %>">
                        <td>
                            <% if (billable.getBeneficiary() instanceof UserBeneficiary) { %>
                                <% final UserBeneficiary beneficiary = (UserBeneficiary) billable.getBeneficiary(); %>
                                <img class="img-circle" width="30" height="30" alt=""
                                    src="<%= beneficiary.getUser().getProfile().getAvatarUrl() %>">
                                <a href="<%= contextPath + "/internalBilling/user/" + beneficiary.getUser().getExternalId() %>">
                                    <%= beneficiary.getUser().getProfile().getDisplayName() %>
                                </a>
                            <% } else if (billable.getBeneficiary() instanceof UnitBeneficiary) { %>
                                <% final UnitBeneficiary beneficiary = (UnitBeneficiary) billable.getBeneficiary(); %>
                                <a href="<%= contextPath + "/internalBilling/unit/" + beneficiary.getUnit().getExternalId() %>">
                                    <%= beneficiary.getUnit().getPresentationName() %>
                                </a>
                            <% } %>
                        </td>
                        <td>
                            <%= maxValue == null || maxValue.isJsonNull() ? "" : Money.importFromString(maxValue.getAsString()).toFormatString() %>
                        </td>
                        <td id="<%= "statusCell" + billable.getExternalId() %>">
                            <% final String styleClass =
                                    billable.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION ? "font-weight: bold; color: orange;" :
                                    billable.getBillableStatus() == BillableStatus.AUTHORIZED ? "font-weight: bold; color: green;" :
                                        ""; %>
                            <span style="font-weight: bold; color: red;"></span>
                            <span style="<%= styleClass %>">
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
                        <td>
                            <% if (billable.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION && unit.isCurrentUserResponsibleForUnit()) { %>
                                <% pendingAuhtorizationCount++; %>
                                <a href="#" onclick="<%= "authorizePrintService('" + billable.getExternalId() + "'); return false;" %>"
                                        id="<%= "authorize" + billable.getExternalId() + "Button" %>"
                                        class="btn btn-success">
                                    <spring:message code="label.authorize" text="Authorize"/>
                                </a>
                            <% } %>
                        </td>
                        <td>
                            <% if (billable.getBillableStatus() != BillableStatus.REVOKED && unit.isCurrentUserResponsibleForUnit()) { %>
                                <a href="#" onclick="<%= "revokePrintService('" + billable.getExternalId() + "'); return false;" %>"
                                        id="<%= "revoke" + billable.getExternalId() + "Button" %>"
                                        class="btn btn-danger">
                                    <spring:message code="label.revoke" text="Revoke"/>
                                </a>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
                <% if (pendingAuhtorizationCount > 1 && unit.isCurrentUserResponsibleForUnit()) { %>
                    <tr>
                        <td colspan="3">
                        </td>
                        <td>
                            <form class="form-horizontal" action="<%= contextPath + "/internalBilling/unit/" + unit.getExternalId() + "/authorizeAll" %>" method="POST">
                                <% for (final Billable billable : printService.getBillableSet()) { %>
                                    <% if (billable.getBillableStatus() == BillableStatus.PENDING_AUTHORIZATION) { %>
                                        <input type="hidden" id="billable" name="billable" value="<%= billable.getExternalId() %>">
                                    <% } %>
                                <% } %>
                                <button id="submitRequest" class="btn btn-success">
                                    <spring:message code="label.authorize.all" text="Authorize All"/>
                                </button>
                            </form>
                        </td>
                        <td>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<div id="authorizedLabel" style="display: none;">
<span style="font-weight: bold; color: green;">
    <spring:message code="<%= "label.internalBilling.billableService.status." + BillableStatus.AUTHORIZED.name() %>"/>
</span>
<br/>
<span style="font-weight: bold; color: orange;">
    <spring:message code="<%= "label.internalBilling.billableService.status." + ServiceStatus.PENDING_ACTIVATION.name() %>"/>
</span>
</div>


<script type="text/javascript" >
    var contextPath = '<%=contextPath%>';

    function revokePrintService( billableId ) {
    	  $.ajax({
    		  type: 'POST',
    		  url: contextPath + '/internalBilling/billable/' + billableId + '/revoke',
    		  success: function() {}
    	  });

    	  var revokeButton = document.getElementById('revoke' + billableId);
    	  var revokeButtonParent = revokeButton.parentNode;
    	  revokeButtonParent.removeChild(revokeButton);

    	  return false;
    }    	 

    function authorizePrintService( billableId ) {
         $.ajax({
             type: 'POST',
             url: contextPath + '/internalBilling/billable/' + billableId + '/authorize',
             success: function() {}
         });

        var authorizeButton = document.getElementById('authorize' + billableId + 'Button');
        var authorizeButtonParent = authorizeButton.parentNode;
        authorizeButtonParent.removeChild(authorizeButton);

        var statusCell = document.getElementById('statusCell' + billableId);
        while (statusCell.firstChild) {
        	statusCell.removeChild(statusCell.firstChild);
        }
        var authorizedLabel = document.getElementById("authorizedLabel");
        var clonedLabel = authorizedLabel.cloneNode(true);
        clonedLabel.style.display = 'block';
        statusCell.appendChild(clonedLabel);

        return false;
  }
</script>
