<jsp:include page="user_common_reports.jsp"/>

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<% final String contextPath = request.getContextPath(); %>
<% final String year = request.getParameter("year"); %>
<% final String month = request.getParameter("month"); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<br/>
<br/>

<div>
    <table class="table">
        <thead>
            <tr>
                <th width="13%"><spring:message code="label.internalBilling.billing.details.txDate" text="Transaction Date"/></th>
                <th width="5%"><spring:message code="label.internalBilling.billing.details.value" text="Value"/></th>
                <th width="15%"><spring:message code="label.internalBilling.billing.details.label" text="Description"/></th>
                <th><spring:message code="label.internalBilling.billing.details.details" text="Details"/></th>
                <th width="30%"><spring:message code="label.internalBilling.billing.details.unit" text="Unit"/></th>
            </tr>
        </thead>
        <tbody id="txTable"/>
    </table>
</div>

<script type="text/javascript">
    var user = ${user};
    var contextPath = '<%= contextPath %>';
    var transactions = <%= request.getAttribute("transactions")%>;

    $(document).ready(function() {
    	document.getElementById("reportsTab").parentNode.classList.add("active");
    	document.getElementById("allLink").classList.add("navItemSelected");

        $(transactions).each(function(j, tx) {
            var row = $('<tr/>').appendTo($('#txTable'));
            row.append();

            row.append($('<td/>').html(tx.txDate));
            row.append($('<td/>').html(tx.value));
            row.append($('<td/>').html(tx.label));
            row.append($('<td/>').html(tx.description));
            if (!tx.unit) {
            	row.append($('<td/>').html(''));
            } else {
                var hrefUnit = contextPath + tx.unit.relativePath;
                var unitColumn = $('<td/>').appendTo(row);
                unitColumn.append($('<a href="' + hrefUnit + '">').text(tx.unit.name));
            }
        });
    });
</script>
