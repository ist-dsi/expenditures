<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<h2>
    <spring:message code="title.internalBilling" text="Internal Billing" />
</h2>

<h3 id="NoResults" style="display: none;"><spring:message code="label.internalBilling.billableServices.none" text="No services available." /></h3>

<table id="serviceTable" class="table tdmiddle" style="display: none;">
    <thead>
        <tr>
            <th><spring:message code="label.internalBilling.billableService.type" text="Type" /></th>
            <th><spring:message code="label.internalBilling.billableService.title" text="Title"/></th>
            <th><spring:message code="label.internalBilling.billableService.description" text="Description"/></th>
            <% if (ExpenditureTrackingSystem.isManager()) { %>
            <th></th>
            <th></th>
            <% } %>
        </tr>
    </thead>
    <tbody id="serviceList">
    </tbody>
</table>

<% if (ExpenditureTrackingSystem.isManager()) { %>
<button class="btn btn-primary" onclick="<%= "window.open('" + contextPath + "/internalBilling/billableService/createService', '_self')" %>">
    <spring:message code="label.internalBilling.billableService.create"/>
</button>
<% } %>

<spring:message var="subscribeLabel" scope="request" code="label.internalBilling.billableService.subscribe"/>
<spring:message var="editLable" scope="request" code="label.internalBilling.billableService.edit"/>
<spring:message var="deleteLable" scope="request" code="label.internalBilling.billableService.delete"/>
<script type="text/javascript">
    var contextPath = '<%= contextPath %>';
    var billableServices = ${billableServices};
    var subscribeLabel = '<%= request.getAttribute("subscribeLabel") %>';
    var editLabel = '<%= request.getAttribute("editLable") %>';
    var deleteLable = '<%= request.getAttribute("deleteLable") %>';
    $(document).ready(function() {
        if (billableServices.length == 0) {
            document.getElementById("NoResults").style.display = 'block';
        } else {
            document.getElementById("serviceTable").style.display = 'block';
        }
        $(billableServices).each(function(i, s) {
            row = $('<tr/>').appendTo($('#serviceList'));
            row.append($('<td/>').html(s.type));
            row.append($('<td/>').html(s.title));
            row.append($('<td/>').html(s.description));
            <% if (ExpenditureTrackingSystem.isManager()) { %>
            row.append($('<td/>').html('<a class="btn btn-default" href="' + contextPath + '/internalBilling/billableService/' + s.id + '/edit">' + editLabel + '</a>'));
            var deleteForm = '<form method="POST" action="' + contextPath + '/internalBilling/billableService/' + s.id + '/delete">'
            				 + '${csrf.field()}'
                             + '<button class="btn btn-danger warning-border" onclick="return deleteContest();">'
                             + deleteLable
                             + '</button>'
                             + '</form>';
            row.append($('<td/>').html(deleteForm));
            <% } %>
        });
    });
</script>
