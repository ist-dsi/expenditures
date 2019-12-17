<jsp:include page="common.jsp"/>
<%@page import="pt.ist.internalBilling.domain.BillableTransaction"%>
<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@page import="pt.ist.internalBilling.domain.InternalBillingService"%>
<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<% final String contextPath = request.getContextPath(); %>
<% final BillableTransaction tran= (BillableTransaction) request.getAttribute("transaction") ; %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<div class="page-header">
    <h2 class="ng-scope">
        <img id="userAvatar" class="img-circle" alt="" src="#"/>
        <span id="userDisplayName"></span> <small id="username"></small>
    </h2>
</div>

 
 <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
            <h2>
                <span>
                 <%= tran.getTxDate().toLocalDate()  + " - " + tran.getBillable().getUnit().getPresentationName()+":  Valor= " +tran.getValue().getValue() %>               
                </span>
            </h2>
            </div>
            <div class="panel-body">
                <div>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.internalBilling.billableTransaction.title" text="Transferir"/>
                                </th>
                                <th>
                                    <spring:message code="label.unit" text="Financer"/>
                                </th>
                                <th>
                                    <spring:message code="label.internalBilling.billableService.status" text="Status"/>
                                </th>
                                <th>
                                    <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                                </th>
                                <th>
                                    <spring:message code="label.consumed.value" text="Consumed Value"/>
                                </th>
                                <th>
                                </th>
                            </tr>
                        </thead>
                        <tbody id="myServices"/>
                    </table>
                   </div>
            </div>
        </div>
    </div>
  

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';
    var myServices = ${services};
    var user =${user};
    var transactionId = '${transaction.externalId}';
    var billableId = '${transaction.billable.unit.externalId}';
    
    
    
   
    $(document).ready(function() {
    	$('#userDisplayName').html(user.name);
        $('#username').html(user.username);
        $('#userAvatar').attr('src', user.avatarUrl);
        
        $(myServices).each(function(i, b) {
        	if(b.unit.id!=billableId){
            var imgServicePath = contextPath + "/img/internal-billing/" + b.serviceClass + ".png";
            var hrefUnit = contextPath + b.unit.relativePath + "/services";
            var row = $('<tr/>').appendTo($('#myServices'));
            row.append($('<td/>').html($('<img alt="' + b.serviceClassDescription + '" src="' + imgServicePath + '"/>')));
            row.append($('<td/>').html($('<a href="' + hrefUnit + '">').text(b.unit.name)));
            var userColumn = $('<td/>').appendTo(row);
            var billableStatusClass = b.billableStatus == '<%= BillableStatus.PENDING_AUTHORIZATION.name() %>' ? 'status_warning'
            		: b.billableStatus == '<%= BillableStatus.AUTHORIZED.name() %>' ? 'status_ok'
            		: b.billableStatus == '<%= BillableStatus.REVOKED.name() %>' ? 'status_error'
            		: '';
            var serviceStatusClass = b.serviceStatus == '<%= ServiceStatus.PENDING_ACTIVATION.name() %>' ? 'status_warning'
            		: b.serviceStatus == '<%= ServiceStatus.ACTIVE.name() %>' ? 'status_ok'
                    : b.serviceStatus == '<%= ServiceStatus.INACTIVE.name() %>' ? 'status_error'
            		: '';
            userColumn.append($('<span class="' + billableStatusClass + '"/>').text(b.billableStatusDescription));
            userColumn.append($('<br/>'));
            userColumn.append($('<span class="' + serviceStatusClass + '"/>').text(b.serviceStatusDescription));

            var valueColumn = $('<td/>').appendTo(row);
            valueColumn.append($('<span/>').append(b.authorizedValue));

            var consumedColumn = $('<td/>').appendTo(row);
            consumedColumn.append($('<span/>').append(b.consumedValue));
            
           	var formId="'submform'";
            var submitForm = '<form id="submform" method="POST" action="' + contextPath + '/internalBilling/transaction/'+transactionId +'/changeUnit/' + b.id + '"/>'
			 + '${csrf.field()}'
            + '<button class="btn btn-default" onclick="document.getElementById('+formId+').submit();">'
            + '<spring:message code="label.select" text="Select"/>'
            + '</button>'
            + '</form>';
            var currentBillableColumn = $('<td/>').appendTo(row).html(submitForm);

        	}
        });

 
    });
</script>