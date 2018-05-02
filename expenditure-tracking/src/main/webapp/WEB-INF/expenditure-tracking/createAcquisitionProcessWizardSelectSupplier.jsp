<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="org.fenixedu.bennu.core.i18n.BundleUtil"%>
<%@page import="module.mission.domain.MissionSystem"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src='<%= contextPath + "/webjars/jquery-ui/1.11.1/jquery-ui.js" %>'></script>

<spring:url var="noSupplierURL"
	value="/expenditure/acquisitons/create/isRefund" />

<div class="page-header">
	<h2><spring:message code="title.create.multipleSupplierConsultationProcess" text="New Acquisition/Refund"/></h2>
</div>

<p class="mvert05">
	Introdução
	<form class="form-horizontal" action='<%= contextPath + "/expenditure/acquisitons/create/selectType" %>' method="GET">
        <p>
			No caso do fornecedor pretendido não estar registado no sistema, deverá contactar o <a href="mailto:novos-fornecedores@tecnico.ulisboa.pt">novos-fornecedores@tecnico.ulisboa.pt</a>
		</p>
        <div class="form-group">
            <label class="control-label col-sm-2" for="supplierTerm">
                <spring:message code="label.internalBilling.billableService.supplier" text="Supplier" />
            </label>
            <div class="col-sm-10">
                <input id="supplierTern" type="text" class="form-control" required="required"/>
                <input type="hidden" id="supplier" name="supplier" value="">
				<a href="${noSupplierURL}">Não sei.</a>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-10 col-sm-offset-2">
	            <button type="submit" class="btn btn-primary">
	            	<spring:message code="label.next" text="Next" />
	            </button>
            </div>
        </div>
   </form>
</p>

<script type="text/javascript">
    var contextPath = '<%= contextPath %>';

    $(document).ready(function() {
    });

    $(function() {
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
                            value: item.nif
                        }
                    }));
                });
            },
            
            select: function( event, ui ) {
                $( "#supplierTern" ).val( ui.item.label );
                $( "#supplier" ).val( ui.item.value );               
                return false;
            }
        });
    });
</script>