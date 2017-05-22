<jsp:include page="user_common.jsp"/>

<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<div id="serivces"></div>

<table id="printServiceTable" class="table" style="display: none;">
    <thead>
        <tr>
            <th><spring:message code="label.unit" text="Financer"/></th>
            <th><spring:message code="label.authorization.maxValue" text="Max. Value"/></th>
            <th><spring:message code="label.internalBilling.billableService.status" text="Status"/></th>
        </tr>
    </thead>
    <tbody id="printServiceTableBody"/>
</table>

<script type="text/javascript">
    var user = ${user};
    var contextPath = '<%= contextPath %>';
    var services = <%= request.getAttribute("services")%>;
    $(document).ready(function() {
        document.getElementById("servicesTab").parentNode.classList.add("active");

        $(services).each(function(i, s) {
            var servicePanel = $('<div class="panel panel-default"/>').appendTo($('#serivces'));
            var servicePanelHeader = $('<div class="panel-heading"/>').appendTo(servicePanel);
            var servicePanelBody = $('<div class="panel-body"/>').appendTo(servicePanel);

            var serviceLogoUrl = contextPath + '/img/internal-billing/' + s.serviceClass + '.png';
            servicePanelHeader.append($('<img class="serviceIcon" alt="" src="' + serviceLogoUrl + '"/>'));
            servicePanelHeader.append($('<span/>').text(s.title));

            var printServiceTable = document.getElementById('printServiceTable');
            servicePanelBody.append(printServiceTable);
            $(s.billables).each(function(j, b) {
                var brow = $('<tr/>').appendTo($('#printServiceTableBody'));
                brow.append();

                var hrefUnit = contextPath + b.financer.relativePath;
                var unitColumn = $('<td/>').appendTo(brow);
                unitColumn.append($('<a href="' + hrefUnit + '">').text(b.financer.name));

                var valueColumn = $('<td/>').appendTo(brow);
                valueColumn.append($('<span/>').append(b.authorizedValue));

                var statusCellId = 'statusCell_' + i + '_' + j;
                var statusColumn = $('<td id="' + statusCellId + '"/>').appendTo(brow);
                var billableStatusClass = b.billableStatus == '<%= BillableStatus.PENDING_AUTHORIZATION.name() %>' ? 'status_warning'
                        : b.billableStatus == '<%= BillableStatus.AUTHORIZED.name() %>' ? 'status_ok'
                        : b.billableStatus == '<%= BillableStatus.REVOKED.name() %>' ? 'status_error'
                        : '';
                var serviceStatusClass = b.serviceStatus == '<%= ServiceStatus.PENDING_ACTIVATION.name() %>' ? 'status_warning'
                        : b.serviceStatus == '<%= ServiceStatus.ACTIVE.name() %>' ? 'status_ok'
                        : b.serviceStatus == '<%= ServiceStatus.INACTIVE.name() %>' ? 'status_error'
                        : '';
                statusColumn.append($('<span class="' + billableStatusClass + '"/>').text(b.billableStatusDescription));
                statusColumn.append($('<br/>'));
                statusColumn.append($('<span class="' + serviceStatusClass + '"/>').text(b.serviceStatusDescription));
            });
            printServiceTable.style.display = 'table';

        });
    });
</script>
