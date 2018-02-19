<%@page import="pt.ist.expenditureTrackingSystem.domain.ContractType"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<div class="page-header">
    <h2><spring:message code="title.create.multipleSupplierConsultationProcess" text="New Prior Consultation Procedure"/></h2>
</div>

<br/>

<div class="page-body">
    <form class="form-horizontal" action="<%= contextPath + "/consultation/createNewMultipleSupplierConsultationProcess" %>" method="POST">
        <div class="form-group">
            <label class="control-label col-sm-2" for="description">
                <spring:message code="label.multipleSupplierConsultationProcess.description" text="Description" />
            </label>
            <div class="col-sm-10">
                <input name="description" type="text" class="form-control" id="description" required="required"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="materialTerm">
                <spring:message code="label.internalBilling.billableService.material" text="Material" />
            </label>
            <div class="col-sm-10">
                <input name="materialTern" type="text" class="form-control" id="materialTern" required="required"/>
                <input type="hidden" id="material" name="material" value="">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="justification">
                <spring:message code="label.internalBilling.billableService.justification" text="Justification" />
            </label>
            <div class="col-sm-10">
                <textarea name="justification" class="form-control" id="justification" required="required" rows="4"></textarea>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="contractTypeTerm">
                <spring:message code="label.internalBilling.billableService.contractType" text="Contract Type" />
            </label>
            <div class="col-sm-10">
                <select id="contractType" name="contractType" class="form-control">
                    <% for (final ContractType contractType : ExpenditureTrackingSystem.getInstance().getContractTypeSet()) { %>
                        <option value="<%= contractType.getExternalId() %>"><%= contractType.getName().getContent() %></option>
                    <% } %>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.create" text="Create" />
                </button>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';

    $(document).ready(function() {
    });

    $(function() {
        $('#materialTern').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response) {
                $.post(contextPath + "/consultation/materials", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#materialTern" ).val( ui.item.label );
                $( "#material" ).val( ui.item.value );               
                return false;
            }
        });
    });
</script>
