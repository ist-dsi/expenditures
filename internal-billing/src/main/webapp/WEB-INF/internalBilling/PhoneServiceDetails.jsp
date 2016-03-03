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
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="pt.ist.internalBilling.domain.UnitBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.UserBeneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Beneficiary"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>
<% final Billable billable = (Billable) request.getAttribute("billable"); %>
<% final JsonObject serviceConfig = new JsonParser().parse(billable.getConfiguration()).getAsJsonObject(); %>

<table class="table" style="background-color:#F0F0F0;">
    <thead>
        <tr>
            <th>
                <spring:message code="label.service.phone.screen.name" text="Screen Name"/>
            </th>
            <th>
                <spring:message code="label.service.phone.internal.calls" text="Internal Calls" />
            </th>
            <th>
                <spring:message code="label.service.phone.national.hardline" text="National Calls" />
            </th>
            <th>
                <spring:message code="label.service.phone.mobile.network" text="Mobile Networks" />
            </th>
            <th>
                <spring:message code="label.service.phone.international.calss" text="International Calls" />
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                <%= serviceConfig.get("screenName").getAsString() %>
            </td>
            <td>
                <% final JsonElement internalCalls = serviceConfig.get("internalCalls"); %>
                <% if (internalCalls != null && !internalCalls.isJsonNull() && internalCalls.getAsBoolean()) { %>
                    <img src="<%= request.getContextPath() %>/images/accept.gif">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/incorrect.gif">
                <% } %>
            </td>
            <td>
                <% final JsonElement nationalHardline = serviceConfig.get("nationalHardline"); %>
                <% if (nationalHardline != null && !nationalHardline.isJsonNull() && nationalHardline.getAsBoolean()) { %>
                    <img src="<%= request.getContextPath() %>/images/accept.gif">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/incorrect.gif">
                <% } %>
            </td>
            <td>
                <% final JsonElement mobileNetwork = serviceConfig.get("mobileNetwork"); %>
                <% if (mobileNetwork != null && !mobileNetwork.isJsonNull() && mobileNetwork.getAsBoolean()) { %>
                    <img src="<%= request.getContextPath() %>/images/accept.gif">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/incorrect.gif">
                <% } %>
            </td>
            <td>
                <% final JsonElement internationalCalls = serviceConfig.get("internationalCalls"); %>
                <% if (internationalCalls != null && !internationalCalls.isJsonNull() && internationalCalls.getAsBoolean()) { %>
                    <img src="<%= request.getContextPath() %>/images/accept.gif">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/incorrect.gif">
                <% } %>
            </td>
        </tr>
    </tbody>
</table>
