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
<%@page import="pt.ist.internalBilling.domain.PhoneService"%>
<%@page import="com.google.gson.JsonObject"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<% final JsonObject billableService = (JsonObject) request.getAttribute("billableService"); %>

<div class="page-header">
    <h2>
        <spring:message code="label.internalBilling.billableService.edit" text="Edit Service"/>
    </h2>
</div>

<div class="page-body">
    <form class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/" + billableService.get("id").getAsString() + "/edit" %>" method="POST">
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
                <spring:message code="label.internalBilling.billableService.type" text="Type" />
            </label>
            <div class="col-sm-10">
                <div class="form-control">
                    <%= billableService.get("type").getAsString() %>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="title">
                <spring:message code="label.internalBilling.billableService.title" text="Service" />
            </label>
            <div class="col-sm-10">
                <input name="title" type="text" class="form-control" id="title" required="required"
                    value="<%= billableService.get("title").getAsString() %>"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="description">
                <spring:message code="label.internalBilling.billableService.description" text="Description" />
            </label>
            <div class="col-sm-10">
                <textarea name="description" class="form-control" id="description" required="required" rows="4"><%= billableService.get("description").getAsString() %></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.edit" text="Edit" />
                </button>
            </div>
        </div>
    </form>
</div>
