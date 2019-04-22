<jsp:include page="common.jsp"/>

<%@page import="pt.ist.internalBilling.domain.ServiceStatus"%>
<%@page import="pt.ist.internalBilling.domain.BillableStatus"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% final String contextPath = request.getContextPath(); %>

<div class="page-body">
    <br/>
    <form class="form-horizontal" action="<%= contextPath + "/internalBilling/search" %>" method="POST">
    ${csrf.field()}
        <div class="form-group">
            <div class="col-sm-1">
            </div>
            <div class="col-sm-8">
                <spring:message var="searchPlaceholder" scope="request" code="label.internalBilling.billableService.unitOrPerson"/>
                <input type="text" id="searchTerm" name="searchTerm" required="required" class="form-control"
                    placeholder="<%= request.getAttribute("searchPlaceholder") %>"/>
                <input type="hidden" id="unitOrUser" name="unitOrUser" value="">
            </div>
            <div class="col-sm-3">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.select" text="Select" />
                </button>
            </div>
        </div>
        <div class="form-group">
        </div>
    </form>

    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.services.subscribed.my" text="My Services"/>
            </div>
            <div class="panel-body">
                <div>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.internalBilling.billableService.title" text="Service"/>
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
                                    <spring:message code="label.consumed.value" text="Consumed Value"/> <sup>*</sup>
                                </th>
                                <th>
                                </th>
                            </tr>
                        </thead>
                        <tbody id="myServices"/>
                    </table>
                    <span>* <spring:message code="label.consumed.value.current.year" text="Consumed Value This Year"/></span>
                    <br/>
                    <span>** <spring:message code="label.unit.selected" text="Selected unit for print costs"/></span>
                </div>
            </div>
        </div>
    </div>

    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.internalBilling.billableService.pendingMyAuthorization" text="Pending Authorizations"/>
            </div>
            <div class="panel-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <spring:message code="label.internalBilling.billableService.title" text="Service"/>
                                </th>
                                <th>
                                    <spring:message code="label.unit" text="Financer"/>
                                </th>
                                <th>
                                    <spring:message code="label.internalBilling.billableService.beneficiary" text="Beneficiary"/>
                                </th>
                                <th>
                                    <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody id="pendingAuthorization"/>
                    </table>
            </div>
        </div>
    </div>

    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="label.unit.mine" text="My Units"/>
            </div>
            <div class="panel-body">
                    <table class="table">
                        <thead>
                            <tr>
                                <th rowspan="2">
                                    <spring:message code="label.unit" text="Unit"/>
                                </th>
                                <th colspan="3">
                                    <spring:message code="label.services.active" text="Active Services"/>
                                </th>
                                <th rowspan="2">
                                    <spring:message code="label.authorization.maxValue" text="Max. Value"/>
                                </th>
                                <th rowspan="2">
                                    <spring:message code="label.consumed.value" text="Consumed Value"/> <sup>*</sup>
                                </th>
                            </tr>
                            <tr>
                                <th>
                                    <spring:message var="pendingAuthorizationCount" code="label.services.pendingAuthorization.count" text="Pending Authorization"/>
                                    <spring:message code="label.services.pendingAuthorization.count.short" text="P"/>
                                </th>
                                <th>
                                    <spring:message var="activeCount" code="label.services.active.count" text="Active"/>
                                    <spring:message code="label.services.active.count.short" text="A"/>
                                </th>
                                <th>
                                    <spring:message var="revokedCount" code="label.services.revoked.count" text="Revoked"/>
                                    <spring:message code="label.services.revoked.count.short" text="R"/>
                                </th>
                            </tr>
                        </thead>
                        <tbody id="myUnits"/>
                    </table>
                    <span><spring:message code="label.services.pendingAuthorization.count.short" text="P"/> - <spring:message code="label.services.pendingAuthorization.count" text="P"/></span>
                    <br/>
                    <span><spring:message code="label.services.active.count.short" text="A"/> - <spring:message code="label.services.active.count" text="A"/></span>
                    <br/>
                    <span><spring:message code="label.services.revoked.count.short" text="R"/> - <spring:message code="label.services.revoked.count" text="R"/></span>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';
    var myServices = ${myServices};
    var pendingAuthorization = ${pendingAuthorization};
    var myUnits = ${myUnits};

    $(document).ready(function() {
        $(myServices).each(function(i, b) {
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

            if (b.currentBillable) {
                var currentBillableColumn = $('<td/>').appendTo(row);
                currentBillableColumn.append($('<a class="btn btn-primary" disabled href="#"/>').append('<spring:message code="label.selected" text="Selected"/> **'));
            } else if (b.canSetAsCurrentBillable) {
                var currentBillableColumn = $('<td/>').appendTo(row);
                currentBillableColumn.append($('<a class="btn btn-default" href="' + contextPath + b.beneficiary.relativePath + '/setUnit/' + b.unit.id + '"/>').append('<spring:message code="label.select" text="Select"/>'));
            } else {
                var currentBillableColumn = $('<td/>').appendTo(row);
            }
        });

        $(pendingAuthorization).each(function(i, b) {
            var imgServicePath = contextPath + "/img/internal-billing/" + b.serviceClass + ".png";
            var hrefUnit = contextPath + b.unit.relativePath + "/services";
        	var row = $('<tr/>').appendTo($('#pendingAuthorization'));
            row.append($('<td/>').html($('<img alt="' + b.serviceClassDescription + '" src="' + imgServicePath + '"/>')));
            row.append($('<td/>').html($('<a href="' + hrefUnit + '">').text(b.unit.name)));
            var userColumn = $('<td/>').appendTo(row);
            userColumn.append($('<img class="img-circle" alt="" src="' + b.beneficiary.avatarUrl + '"/>'));
            userColumn.append($('<span/>').text(b.beneficiary.name));

            var valueColumn = $('<td/>').appendTo(row);
            valueColumn.append($('<span/>').append(b.authorizedValue));
        });
        
        $(myUnits).each(function(i, u) {
            var hrefUnit = contextPath + u.relativePath;
            var row = $('<tr/>').appendTo($('#myUnits'));
            row.append($('<td/>').html($('<a href="' + hrefUnit + '">').text(u.name)));
            row.append($('<td/>').html($('<span class="pendingAuthorization">').text(u.pendingAuthorizationCount)));
            row.append($('<td/>').html($('<span class="active">').text(u.activeCount)));
            row.append($('<td/>').html($('<span class="revoked">').text(u.revokedCount)));
            row.append($('<td/>').html($('<span class="revoked">').text(u.authorizedValue)));
            row.append($('<td/>').html($('<span class="revoked">').text(u.consumedValue)));
        });
    });

    $(function() {
        $('#searchTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(contextPath + "/internalBilling/billableService/availableParties", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#searchTerm" ).val( ui.item.label );
                $( "#unitOrUser" ).val( ui.item.value );
                return false;
            }
        });
    });
</script>
