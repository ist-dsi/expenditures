<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation"%>
<%@page import="java.util.Set"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Unit"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.Material"%>
<%@page import="java.util.Calendar"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ContractType"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% final String contextPath = request.getContextPath(); %>
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/bootstrap-datetimepicker/2.4.2/js/bootstrap-datetimepicker.js" %>'></script>
<link rel="stylesheet" href='<%= contextPath + "/webjars/bootstrap-datetimepicker/2.4.2/css/bootstrap-datetimepicker.min.css" %>'>

<div class="page-header">
    <h2><spring:message code="title.expense.contests" text="Contests"/></h2>
</div>

<%
final Material material = (Material) request.getAttribute("material");
final ContractType contractType = (ContractType) request.getAttribute("contractType");
final User selectedUser = (User) request.getAttribute("selectedUser");
final Unit selectedUnit = (Unit) request.getAttribute("selectedUnit");
final Supplier selectedSupplier = (Supplier) request.getAttribute("selectedSupplier");

final String isCollapsedParam = request.getParameter("isCollapsed");
final boolean collapse = isCollapsedParam == null || isCollapsedParam.isEmpty() || Boolean.parseBoolean(isCollapsedParam);

final Set<MultipleSupplierConsultationProcess> searchResult = (Set<MultipleSupplierConsultationProcess>) request.getAttribute("searchResult");
%>

<div class="page-body">
    <form class="form-horizontal" action="<%= contextPath + "/expenseContests/search" %>" method="GET">
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="procedureDiv">
            <label class="control-label col-sm-2" for="procedure">
                <spring:message code="title.expense.contest.procedure" text="Procedure" />
            </label>
            <div class="col-sm-10">
                <spring:message var="procedureValue" code="title.consultation.process" text="Prior Consultation Process" scope="request"/>
                <input id="procedure" name="procedure" value="${procedureValue}" disabled="disabled" class="form-control">
            </div>
        </div>
        <div class="form-group" id="processNumberDiv">
            <label class="control-label col-sm-2" for="processNumber">
                <spring:message code="label.process.number" text="Process Number" />
            </label>
            <div class="col-sm-10">
                <input name="processNumber" type="text" class="form-control" id="processNumber" value="${processNumber}">
            </div>
        </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="yearDiv">
            <label class="control-label col-sm-2" for="year">
                <spring:message code="label.year" text="Year" />
            </label>
            <div class="col-sm-10 input-append date" id="datetimepicker" data-date="12-02-2012" data-date-format="yyyy" >
                <input name="year" type="text" class="form-control" id="year" required="required" value="${year}"/>
                <span class="add-on"><i class="icon-th"></i></span>
            </div>
       </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="materialTermDiv">
            <label class="control-label col-sm-2" for="materialTerm">
                <spring:message code="label.internalBilling.billableService.material" text="Material" />
            </label>
            <div class="col-sm-10">
                <input name="materialTerm" type="text" class="form-control" id="materialTerm" <% if (material != null) { %>value="<%= material.getFullDescription() %>"<% } %>/>
                <input type="hidden" id="material" name="material" <% if (material != null) { %>value="<%= material.getExternalId() %>"<% } %>>
            </div>
        </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="contractTypeTermDiv">
            <label class="control-label col-sm-2" for="contractTypeTerm">
                <spring:message code="label.internalBilling.billableService.contractType" text="Contract Type" />
            </label>
            <div class="col-sm-10">
                <select id="contractType" name="contractType" class="form-control">
                    <option value=""></option>
                    <% for (final ContractType ct : ExpenditureTrackingSystem.getInstance().getContractTypeSet()) { %>
                        <option value="<%= ct.getExternalId() %>" <% if (contractType == ct) { %>selected="selected"<% } %>><%= ct.getName().getContent() %></option>
                    <% } %>
                </select>
            </div>
        </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="selectedUserDiv">
            <label class="control-label col-sm-2" for="selectedUserTerm">
                <spring:message code="label.user" text="User"/>
            </label>
            <div class="col-sm-10">
                <input name="selectedUserTerm" type="text" class="form-control" id="selectedUserTerm" <% if (selectedUser != null) { %>value='<%= selectedUser.getDisplayName() + " (" + selectedUser.getUsername() + ")" %>'<% } %>>
                <input name="selectedUser" type="hidden" id="selectedUser" <% if (selectedUser != null) { %>value="<%= selectedUser.getExternalId() %>"<% } %>>
                <br>
                <input type="checkbox" name="includeContractManager" id="includeContractManager" checked="checked">
                <label for="includeContractManager">
                    <spring:message code="label.multipleSupplierConsultationProcess.contract.manager" text="Contract Manager"/>
                </label>
                <input type="checkbox" name="includeJuryMembers" id="includeJuryMembers" checked="checked" style="margin-left: 20px;">
                <label for="includeJuryMembers">
                    <spring:message code="label.multipleSupplierConsultationProcess.jury.member" text="Jury Member"/>
                </label>
                <input type="checkbox" name="includeRequester" id="includeRequester" checked="checked" style="margin-left: 20px;">
                <label for="includeRequester">
                    <spring:message code="label.multipleSupplierConsultationProcess.requester" text="Requester"/>
                </label>
            </div>
        </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="selectedUnitDiv">
            <label class="control-label col-sm-2" for="selectedUnitTerm">
                <spring:message code="label.unit" text="Unit"/>
            </label>
            <div class="col-sm-10">
                <input name="selectedUnitTerm" type="text" class="form-control" id="selectedUnitTerm" <% if (selectedUnit != null) { %>value="<%= selectedUnit.getPresentationName() %>"<% } %>>
                <input name="selectedUnit" type="hidden" id="selectedUnit" <% if (selectedUnit != null) { %>value="<%= selectedUnit.getExternalId() %>"<% } %>>
            </div>
        </div>
        <div class="form-group <% if (collapse) { %>collapse<% } %>" id="selectedSupplierDiv">
            <label class="control-label col-sm-2" for="selectedSupplierTerm">
                <spring:message code="label.supplier" text="Supplier"/>
            </label>
            <div class="col-sm-10">
                <input name="selectedSupplierTerm" type="text" class="form-control" id="selectedSupplierTerm" <% if (selectedSupplier != null) { %>value="<%= selectedSupplier.getPresentationName() %>"<% } %>>
                <input name="selectedSupplier" type="hidden" id="selectedSupplier" <% if (selectedSupplier != null) { %>value="<%= selectedSupplier.getExternalId() %>"<% } %>>
                <br>
                <input type="checkbox" name="includeCandidates" id="includeCandidates" checked="checked">
                <label for="includeCandidates">
                    <spring:message code="label.multipleSupplierConsultationProcess.include.candidates" text="Include Candidates"/>
                </label>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
                <button id="submitRequest" class="btn btn-primary">
                    <spring:message code="label.search" text="Search" />
                </button>
                <a class="btn btn-default" data-toggle="collapse" data-target="#procedureDiv, #yearDiv, #materialTermDiv, #contractTypeTermDiv, #selectedUserDiv, #selectedUnitDiv, #selectedSupplierDiv" role="button" href="#"
                        onclick="changeIsCollapsedValue();">
                    <spring:message code="label.search.advanced" text="Advanced Search" />
                </a>
                <input id="isCollapsed" name="isCollapsed" type="hidden" value="<%= collapse %>">
            </div>
        </div>
    </form>

    <% if (searchResult != null) { %>
        <% if (searchResult.isEmpty()) { %>
            <spring:message code="label.search.no.results" text="No results." />
         <% } else { %>
            <br/>
            <div>
            <table class="table" style="width: 100%;">
                <tr>
                    <th>
                        <spring:message code="label.process" text="Process" />
                    </th>
                    <th>
                        <spring:message code="label.internalBilling.billableService.contractType" text="Contract Type" />
                    </th>
                    <th>
                        <spring:message code="label.value" text="Valor" />
                    </th>
                    <th>
                        <spring:message code="label.state" text="State" />
                    </th>
                </tr>
                <% for (final MultipleSupplierConsultationProcess process : searchResult) { %>
                    <% final MultipleSupplierConsultation consultation = process.getConsultation(); %>
                    <tr>
                        <td>
                            <a href='<%= contextPath + "/ForwardToProcess/" + process.getExternalId() %>'>
                                <%= process.getProcessNumber() %>
                            </a>
                        </td>
                        <td>
                            <% if (consultation.getContractType() != null) { %>
                                <%= consultation.getContractType().getName().getContent() %>
                            <% } %>
                        </td>
                        <td>
                            <%= consultation.getValue().toFormatString() %>
                        </td>
                        <td>
                            <%= process.getState().getCompletedTitle() %>
                        </td>
                    </tr>
                <% } %>
            </table>
            </div>
         <% } %>
    <% } %>
</div>

<style>
    .ui-autocomplete-loading{background: url(<%= contextPath %>/images/autocomplete/spinner.gif) no-repeat right center}
</style>
<script type="text/javascript">
    var contextPath = '<%= contextPath %>';

    $(document).ready(function() {
    });

    $(function() {
        $('#materialTerm').autocomplete({
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
                $( "#materialTerm" ).val( ui.item.label );
                $( "#material" ).val( ui.item.value );               
                return false;
            }
        });

        $('#datetimepicker').datetimepicker({
            format: "yyyy",
            autoclose: true,
            startView: 'decade',
            minView: 'decade',
            maxView: 'decade'
        });
    });

    $(function() {
        $('#selectedUserTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response){
                $.post(contextPath + "/internalBilling/billableService/availablePeople", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#selectedUserTerm" ).val( ui.item.label );
                $( "#selectedUser" ).val( ui.item.value );               
                return false;
            }
        });
    });

    $(function() {
        $('#selectedUnitTerm').autocomplete({
            focus: function(event, ui) {
                //  $( "#searchString" ).val( ui.item.label);
                return false;
            },
            minLength: 2,   
            contentType: "application/json; charset=UTF-8",
            search  : function(){$(this).addClass('ui-autocomplete-loading');},
            open    : function(){$(this).removeClass('ui-autocomplete-loading');},
            source : function(request,response) {
                $.post(contextPath + "/consultation/units", request,function(result) {
                    response($.map(result,function(item) {
                        return{
                            label: item.name,
                            value: item.id
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#selectedUnitTerm" ).val( ui.item.label );
                $( "#selectedUnit" ).val( ui.item.value );               
                return false;
            }
        });
    });


        $(function() {
            $('#selectedSupplierTerm').autocomplete({
                focus: function(event, ui) {
                    //  $( "#searchString" ).val( ui.item.label);
                    return false;
                },
                minLength: 2,   
                contentType: "application/json; charset=UTF-8",
                search  : function(){$(this).addClass('ui-autocomplete-loading');},
                open    : function(){$(this).removeClass('ui-autocomplete-loading');},
                source : function(request,response) {
                    $.post(contextPath + "/consultation/suppliers", request,function(result) {
                        response($.map(result,function(item) {
                            return{
                                label: item.name,
                                value: item.id
                            }
                        }));
                    });
                },
                
                select: function( event, ui ) {
                    $( "#selectedSupplierTerm" ).val( ui.item.label );
                    $( "#selectedSupplier" ).val( ui.item.value );               
                    return false;
                }
            });
        });

        function changeIsCollapsedValue() {
        	var currentValue = document.getElementById('isCollapsed').value == 'true';
        	var newValue = !currentValue;
        	document.getElementById('isCollapsed').value = newValue;
        };

</script>
