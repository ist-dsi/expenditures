<%@page import="pt.ist.expenditureTrackingSystem.domain.ContractType"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<style>
.removeSupplier {
    cursor: pointer;
}
</style>

<div class="page-header">
    <h2><spring:message code="title.create.multipleSupplierConsultationProcess" text="New Prior Consultation Procedure"/></h2>
</div>

<br/>

<div class="page-body">
    <form id="createForm" class="form-horizontal" action='<%= contextPath + "/consultation/createNewMultipleSupplierConsultationProcess" %>' method="POST">
        ${csrf.field()}
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
                <input type="hidden" id="material" name="material" value="" required="required">
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
            <label class="control-label col-sm-2" for="supplierTerm">
                <spring:message code="label.suppliers"/>
            </label>
            <div class="col-sm-10">
                <c:if test="${supplier == null}">
                    <div class="alert alert-warning" id="noSuppliersWarn">
                        <spring:message code="text.addSupplier.none"></spring:message>
                    </div>
                </c:if>
                <ul id="supplierList">
                    <c:if test="${supplier != null}">
                        <li>${supplier.presentationName}</li>
                    </c:if>
                </ul>
                <input type="hidden" name="suppliers" id="suppliers" />
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="supplierTerm">
                <spring:message code="label.addSupplier"/>
            </label>
            <div class="col-sm-10">
                <input id="supplierTern" type="text" class="form-control" />
            </div>
            <div class="col-sm-10 col-sm-offset-2">
                <div class="alert alert-danger" id="supplierErr" style="display: none;">
                </div>
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

<c:choose>
    <c:when test="${supplier != null}">
        <script type="text/javascript">
            var selectedSuppliers = [
                    {
                        label: "${supplier.presentationName}",
                        value: "${supplier.fiscalIdentificationCode}",
                        available: true
                    }
            ];
        </script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript">
            var selectedSuppliers = [ ];
        </script>
    </c:otherwise>
</c:choose>

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';

    $(document).ready(function() {
    });

    function hasSupplierBeenSelected(supplier) {
        for (var i = 0; i < selectedSuppliers.length; i++) {
            var s = selectedSuppliers[i];
            if (s.value == supplier.value) {
                return true;
            }
        }
        return false;
    }

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

        $('#supplierTern').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response) {
                $.get(contextPath + "/expenditure/acquisitons/create/supplier/json", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.nif,
                            available: (item.multiTotalAllocated <= item.multiSupplierLimit),
                            formatted: item.formatted
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#supplierTern" ).val( "" );

                var supplier = ui.item;

                if(!supplier.available) {
                    var supplierExceededErrMsg = `<spring:message code="text.addSupplier.exceededLimits"/>`;
                    $("#supplierErr").text(supplierExceededErrMsg);
                    $("#supplierErr").show();
                    return false;
                }
                if(hasSupplierBeenSelected(supplier)) {
                    var supplierDuplicateErrMsg = `<spring:message code="text.addSupplier.duplicate"/>`;
                    $("#supplierErr").text(supplierDuplicateErrMsg);
                    $("#supplierErr").show();
                    return false;
                }

                selectedSuppliers.push(supplier);
                
                $("#supplierList").append("<li>" + supplier.label + " (<a class='removeSupplier' data-nif='" + supplier.value + "'>Remover</a>)</li>");
                $("#noSuppliersWarn").hide();
                $("#supplierErr").hide();
                
                return false;
            }
        });

        $('#supplierList').on("click", ".removeSupplier", function() {
            var nif = $(this).data("nif");

            for (var i = 0; i < selectedSuppliers.length; i++) {
                var s = selectedSuppliers[i];
                if (s.value == nif) {
                    selectedSuppliers.splice(i, 1);
                    break;
                }
            }

            $(this).parent().remove();
            if(selectedSuppliers.length == 0) {
                $("#noSuppliersWarn").show();
            }
            $("#supplierErr").hide();
        });

        $("#createForm").submit(function() {
            var material = $("#material").val();
            if (material.length === 0 || !material.trim()) {
                $( "#materialTern" ).val( "" );
                return false;
            }

            var nifs = selectedSuppliers.map(function(s) { return s.value; });
            $("#suppliers").val(nifs.join());

            return true;
        });
    });
</script>
