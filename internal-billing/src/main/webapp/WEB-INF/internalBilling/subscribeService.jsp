<%@page import="pt.ist.internalBilling.domain.BillableService"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonObject"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<% final BillableService billableService = (BillableService) request.getAttribute("billableService"); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<div class="page-header">
    <h2>
        <spring:message code="label.internalBilling.billableService.subscribe" text="Subscribe"/>
    </h2>
</div>

<div class="page-body">
    <form id="subscribeForm" class="form-horizontal" action="<%= contextPath + "/internalBilling/billableService/subscribe" %>" method="POST">
    ${csrf.field()}
        <input type="hidden" id="beneficiaryConfig" name="beneficiaryConfig" value="[]">
        <div class="form-group">
            <label class="control-label col-sm-2" for="title">
                <spring:message code="label.internalBilling.billableService.financer" text="Financer" />
            </label>
            <div class="col-sm-10">
                <% final String unitParam = request.getParameter("unit"); %>
                <% final Unit unit = unitParam == null ? null : (Unit) FenixFramework.getDomainObject(unitParam); %>
                <% if (unit == null) { %>
                    <input id="financerTerm" class="form-control" autofocus style="display: inline;"/>
                    <input type="hidden" id="financer" name="financer" value="">
                <% } else { %>
                    <input id="financerTerm" class="form-control" autofocus style="display: inline;"
                        value="<%= unit.getPresentationName() %>" disabled="disabled"/>
                    <input type="hidden" id="financer" name="financer" value="<%= unitParam %>">
                <% } %>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
                <spring:message code="label.internalBilling.billableService" text="Service"/>
            </label>
            <div class="col-sm-10">
                <input type="hidden" name="billableService" value="<%= billableService.getExternalId() %>"/>
                <select name="billableServiceDisplay" class="form-control" id="billableService" required="required" disabled="disabled">
                    <option selected="selected" style="display:none;" disabled="disabled">
                        <%= billableService.getTitle() %>
                    </option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
            </label>
            <div class="col-sm-10">
                <div class="jumbotron">
                <%= billableService.getDescription() %>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
            </label>
            <div class="col-sm-10">
                <table class="table">
                    <thead>
                        <tr>
                            <th width="70%">
                                <spring:message code="label.internalBilling.billableService.beneficiary" text="Beneficiary" />
                            </th>
                            <th width="20%">
                                <spring:message code="label.authorization.maxValue" text="Max. Value" />
                            </th>
                            <th width="10%">
                            </th>
                        </tr>
                    </thead>
                    <tbody id="beneficiaryTable">
                    </tbody>
                </table>
            </div>
        </div>
    </form>

    <form class="form-horizontal" action="#" method="POST">
    ${csrf.field()}
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">
            </label>
            <div class="col-sm-10">
                <table class="table">
                    <tbody>
                        <tr>
                            <td width="70%">
                                <input id="beneficiaryTerm" class="form-control" autofocus style="display: inline;" />
                                <input type="hidden" id="beneficiary" name="beneficiary">
                            </td>
                            <td width="20%">
                                <input id="maxValue" class="form-control" autofocus style="display: inline;" />
                            </td>
                            <td width="10%">
                                <button id="submitRequest" class="btn btn-primary" onclick="addBeneficiary(); return false;">
                                    <spring:message code="label.beneficiary.add" text="+" />
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </form>
    <button id="submitRequest" class="btn btn-primary" onclick="submitMainForm();">
        <spring:message code="label.save" text="Save" />
    </button>
</div>

<style>
    .ui-autocomplete-loading{background: url(<%= contextPath %>/images/autocomplete/spinner.gif) no-repeat right center}
</style>

<script type="text/javascript" >
    var pageContext= '<%=contextPath%>';
    $(function() {
        $('#beneficiaryTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(pageContext + "/internalBilling/billableService/availablePeople", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#beneficiaryTerm" ).val( ui.item.label );
                $( "#beneficiary" ).val( ui.item.value );               
                return false;
            }
        });
    });

    function addBeneficiary() {
    	var beneficiaryTerm = document.getElementById('beneficiaryTerm').value;
    	var beneficiaryId = document.getElementById('beneficiary').value;
    	var maxValue = document.getElementById('maxValue').value;

    	if (isValidNumericValue(maxValue) && maxValue > 0) {
    	    document.getElementById('maxValue').style.borderColor = "";
    		if (beneficiaryId.length > 0) {
    			document.getElementById('beneficiaryTerm').style.borderColor = "";

    		    var row = $('<tr/>').appendTo($('#beneficiaryTable'));
    		    row.append($('<td/>').html(beneficiaryTerm));
    		    row.append($('<td/>').html(maxValue));
    		    row.append($('<td/>').html(''));

    		    document.getElementById('beneficiaryTerm').value = '';
    		    document.getElementById('beneficiary').value = '';

    		    var beneficiaryConfig = JSON.parse(document.getElementById('beneficiaryConfig').value);
    		    var newElement = { "beneficiaryId": beneficiaryId, "maxValue": maxValue };
    		    beneficiaryConfig.push(newElement);
    		    document.getElementById('beneficiaryConfig').value = JSON.stringify(beneficiaryConfig);
    		} else {
    			document.getElementById('beneficiaryTerm').style.borderColor = "red";
    		}
    	} else {
    		if (beneficiaryId.length > 0) {
    			document.getElementById('beneficiaryTerm').style.borderColor = "";
    		} else {
    			document.getElementById('beneficiaryTerm').style.borderColor = "red";
    		}
    		document.getElementById('maxValue').style.borderColor = "red";
    	}
    }

    function submitMainForm() {
    	document.getElementById("subscribeForm").submit();
    }

    function isValidNumericValue(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }
</script>
