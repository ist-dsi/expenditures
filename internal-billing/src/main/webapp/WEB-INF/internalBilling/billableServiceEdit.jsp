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
        ${csrf.field()}
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
