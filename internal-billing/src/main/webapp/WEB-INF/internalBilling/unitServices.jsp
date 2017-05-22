<jsp:include page="unit_common.jsp"/>

<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<% final String contextPath = request.getContextPath(); %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="serivces"></div>

<spring:message var="subscribeLabel" code="label.internalBilling.billableService.subscribe" scope="request"/>
<spring:message var="logsLabel" code="label.internalBilling.billableService.viewLogs" scope="request"/>
<spring:message var="authorizeLabel" code="label.authorize" text="Authorize" scope="request"/>
<spring:message var="revokeLabel" code="label.revoke" text="Revoke" scope="request"/>

<span id="AUTHORIZED_LABEL" class="status_ok" style="display: none;">
    <spring:message code="<%= "label.internalBilling.billableService.status." + BillableStatus.AUTHORIZED.name() %>"/>
</span>
<span id="REVOKE_LABEL" class="status_error" style="display: none;">
    <spring:message code="<%= "label.internalBilling.billableService.status." + BillableStatus.REVOKED.name() %>"/>
</span>
<span id="PENDING_ACTIVATION_LABEL" class="status_warning" style="display: none;">
    <spring:message code="<%= "label.internalBilling.billableService.status." + ServiceStatus.PENDING_ACTIVATION.name() %>"/>
</span>

<table id="printServiceTable" class="table" style="display: none;">
    <thead>
        <tr>
            <th><spring:message code="label.internalBilling.billableService.beneficiary" text="Beneficiary"/></th>
            <th><spring:message code="label.authorization.maxValue" text="Max. Value"/></th>
            <th><spring:message code="label.internalBilling.billableService.status" text="Status"/></th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
    </thead>
    <tbody id="printServiceTableBody"/>
</table>

<script type="text/javascript">
    var unit = ${unit};
    var services = ${services};
    var contextPath = '<%= contextPath %>';

    function authorizePrintService( b, authOpId, statusCellId ) {
        $.ajax({
            type: 'POST',
            url: contextPath + '/internalBilling/billable/' + b.id + '/authorize',
            success: function() {}
        });

    	var authOp = document.getElementById(authOpId);
    	clearChildren(authOp);

    	var authLabel = document.getElementById("AUTHORIZED_LABEL");
    	var authLabelClone = authLabel.cloneNode(true);
    	authLabelClone.style.display = 'inline';

        var pendingLabel = document.getElementById("PENDING_ACTIVATION_LABEL");
        var pendingLabelClone = pendingLabel.cloneNode(true);
        pendingLabelClone.style.display = 'inline';

    	var statusCell = document.getElementById(statusCellId);
    	clearChildren(statusCell);
    	statusCell.appendChild(authLabelClone);
    	$('<br/>').appendTo(statusCell);
    	statusCell.appendChild(pendingLabelClone);

        return false;
    }

    function revokePrintService( b, revokeOpId, statusCellId, authOpId ) {
        $.ajax({
            type: 'POST',
            url: contextPath + '/internalBilling/billable/' + b.id + '/revoke',
            success: function() {}
        });

        var revokeOp = document.getElementById(revokeOpId);
        clearChildren(revokeOp);

        var revokeLabel = document.getElementById("REVOKE_LABEL");
        var revokeLabelClone = revokeLabel.cloneNode(true);
        revokeLabelClone.style.display = 'inline';

        var statusCell = document.getElementById(statusCellId);
        clearChildren(statusCell);
        statusCell.appendChild(revokeLabelClone);

        var authOp = document.getElementById(authOpId);
        clearChildren(authOp);

        return false;
    }

    function clearChildren( e ) {
        while (e.firstChild) {
            e.removeChild(e.firstChild);
        }
    }

    $(document).ready(function() {
    	document.getElementById("servicesTab").parentNode.classList.add("active");

        $(services).each(function(i, s) {
        	var servicePanel = $('<div class="panel panel-default"/>').appendTo($('#serivces'));
            var servicePanelHeader = $('<div class="panel-heading"/>').appendTo(servicePanel);
            var servicePanelBody = $('<div class="panel-body"/>').appendTo(servicePanel);
            
            var serviceLogoUrl = contextPath + '/img/internal-billing/' + s.serviceClass + '.png';
            servicePanelHeader.append($('<img class="serviceIcon" alt="" src="' + serviceLogoUrl + '"/>'));
            servicePanelHeader.append($('<span/>').text(s.title));

            var subscribeUrl = contextPath + '/internalBilling/billableService/subscribeService?unit=' 
            		+ unit.id + "&amp;billableService=" + s.id;
            servicePanelHeader.append($('<a class="btn btn-primary subscribe" href="' + subscribeUrl + '"/>').text('<%= request.getAttribute("subscribeLabel").toString() %>'));

            var printServiceTable = document.getElementById('printServiceTable');
            servicePanelBody.append(printServiceTable);
            $(s.billables).each(function(j, b) {
            	var brow = $('<tr/>').appendTo($('#printServiceTableBody'));
            	brow.append();

            	var hrefUser = contextPath + b.beneficiary.relativePath;
                var userColumn = $('<td/>').appendTo(brow);
                userColumn.append($('<img class="img-circle avatarIcon" alt="" src="' + b.beneficiary.avatarUrl + '"/>'));
                userColumn.append($('<a href="' + hrefUser + '">').text(b.beneficiary.name));

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

                var authOpId = 'authOp_' + i + '_' + j;
                var op1Column = $('<td id="' + authOpId + '"/>').appendTo(brow);
                if (unit.isCurrentUserResponsibleForUnit && b.billableStatus == '<%= BillableStatus.PENDING_AUTHORIZATION.name() %>') {
                    var authLink = $('<a class="btn btn-success">');
                    op1Column.append(authLink.text('<%= request.getAttribute("authorizeLabel").toString() %>'));
                    authLink.click( function(e) {
                        e.preventDefault();
                        authorizePrintService(b, authOpId, statusCellId);
                        return false;
                    });
                }

                var revokeOpId = 'revokeOp_' + i + '_' + j;
                var op2Column = $('<td id="' + revokeOpId + '"/>').appendTo(brow);
                if (unit.isCurrentUserResponsibleForUnit && b.billableStatus != '<%= BillableStatus.REVOKED.name() %>') {
                    var revokeLink = $('<a href="#" class="btn btn-danger">');
                    op2Column.append(revokeLink.text('<%= request.getAttribute("revokeLabel").toString() %>'));
                    revokeLink.click( function(e) {
                        e.preventDefault();
                        revokePrintService(b, revokeOpId, statusCellId, authOpId );
                        return false;
                    });
                }
            });
            printServiceTable.style.display = 'table';
        });
    });
</script>
