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
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="pt.ist.internalBilling.domain.Billable"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>
<% final Billable billable = (Billable) request.getAttribute("billable"); %>
<% final JsonObject serviceConfig = new JsonParser().parse(billable.getConfiguration()).getAsJsonObject(); %>

<table class="table" style="background-color:#F0F0F0;">
    <thead>
        <tr>
            <th>
                <spring:message code="label.service.print.maxCopiesColour" text="Internal Calls" />
            </th>
            <th>
                <spring:message code="label.service.print.maxValue" text="Mobile Networks" />
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                <%= serviceConfig.get("maxCopiesColour") == null || serviceConfig.get("maxCopiesColour").isJsonNull() ? "" : serviceConfig.get("maxCopiesColour").getAsString() %>
            </td>
            <td>
                <%= serviceConfig.get("maxValue").isJsonNull() ? "" : serviceConfig.get("maxValue").getAsString() %>
            </td>
        </tr>
    </tbody>
</table>
