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

<spring:url var="noSupplierURL" value="/expenditure/acquisitons/create/isRefund" />

<style>
.btn-xlarge {
    padding: 18px 28px;
    font-size: 20px;
    line-height: normal;
    -webkit-border-radius: 8px;
    -moz-border-radius: 8px;
    border-radius: 8px;
    border-color: #07a;
    border-width: 2px;
    border-style: dotted;
}

.btable {
    margin-top: 25px;
    margin-bottom: 25px;
    width: 100%;
    text-align: center;
}

.btable td {
    width: 25%;
}
</style>

<div class="page-header">
	<h2><spring:message code="acquisitionCreationWizard.title.newAcquisitionOrRefund"></spring:message></h2>
</div>

<div class="page-body">
    <div class="infobox_dotted" id="selectSupplierQuestionDiv" style="display: none;">
        <spring:message code="acquisitionCreationWizard.text.intro.know.supplier.questuin"/>
        <br/>
        <br/>

        <a href='#' class="btn btn-default" onclick='displaySelectSupplier();'>
            <spring:message code="label.yes"/>
        </a>

        <a href="#" class="btn btn-default" onclick='displayIsMission();'>
            <spring:message code="label.no"/>
        </a>
    </div>

    <div class="infobox_dotted" id="selectSupplierDiv">
        <spring:message code="acquisitionCreationWizard.text.intro"></spring:message>
        <spring:message code="acquisitionCreationWizard.supplier.create" arguments="${createSupplierUrl},${createSupplierLabel}"></spring:message>
        <br/>
        <br/>
	   <form class="form-horizontal" action='<%= contextPath + "/expenditure/acquisitons/create/selectType" %>' method="GET">
            <div class="form-group">
                <label class="control-label col-sm-2" for="supplierTerm">
                    <spring:message code="acquisitionCreationWizard.label.supplier"/>
                </label>
                <div class="col-sm-10">
                    <input id="supplierTern" type="text" class="form-control" required="required"/>
                    <input type="hidden" id="supplier" name="supplier" value="">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-10 col-sm-offset-2">
	               <button type="submit" class="btn btn-primary">
	            	  <spring:message code="link.next" text="Next"></spring:message>
	               </button>
                    <a href="#" class="btn btn-default" onclick='displayIsMission();'>
                        <spring:message code="acquisitionCreationWizard.link.unknown"></spring:message>
                    </a>
                </div>
            </div>
        </form>
    </div>

    <div class="infobox_dotted" id="isMissionDiv" style="display: none;">
        <spring:message code="acquisitionCreationWizard.label.isRefund"/>
        <br/>
        <br/>

        <a href="<%= request.getContextPath() %>/expenditure/acquisitons/create/refund" class="btn btn-primary">
            <spring:message code="label.yes"/>
        </a>

        <a href="#" class="btn btn-default" onclick='displayAllOptions();'>
            <spring:message code="label.no"/>
        </a>
    </div>

    <div class="infobox_dotted" id="allOptionsDiv" style="display: none;">
        <spring:message code="acquisitionCreationWizard.text.information.intro"></spring:message>
        <ul>
            <li><spring:message code="acquisitionCreationWizard.text.information.simplified"></spring:message></li>
            <li><spring:message code="acquisitionCreationWizard.text.information.standard"></spring:message></li>
            <li><spring:message code="acquisitionCreationWizard.text.information.refund"></spring:message></li>
            <li><spring:message code="acquisitionCreationWizard.text.information.consultation"></spring:message></li>
        </ul>

        <table class="btable">
            <tr>
                <td>
                    <a href='<%= request.getContextPath() %>/expenditure/acquisitons/create/acquisition' class="btn btn-default btn-xlarge">
                        <bean:message key="link.create.simplifiedAcquisitionProcedure" bundle="EXPENDITURE_RESOURCES"/>
                    </a>
                </td>
                <td>
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        <bean:message key="link.create.standardAcquisitionProcess" bundle="EXPENDITURE_RESOURCES"/>
                    </button>
                </td>
                <td>
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        <bean:message key="link.create.multipleSupplierConsultationProcess" bundle="EXPENDITURE_RESOURCES"/>
                    </button>
        <!--
                    <a href="<%= request.getContextPath() %>/consultation/prepareCreateNewMultipleSupplierConsultationProcess" class="btn btn-default btn-xlarge">
                        <bean:message key="link.create.multipleSupplierConsultationProcess" bundle="EXPENDITURE_RESOURCES"/>
                        <br/>&nbsp;
                    </a>
        -->
                </td>
                <td>
                    <a href="<%= request.getContextPath() %>/expenditure/acquisitons/create/refund" class="btn btn-default btn-xlarge">
                        <bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
                        <br/>&nbsp;
                    </a>
                </td>
            </tr>
        </table>
    </div>
</div>

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

    function displaySelectSupplier() {
    	document.getElementById("selectSupplierQuestionDiv").style.display = "none";
    	document.getElementById("isMissionDiv").style.display = "none";
    	document.getElementById("allOptionsDiv").style.display = "none";
    	document.getElementById("selectSupplierDiv").style.display = "block";
    }

    function displayIsMission() {
        document.getElementById("selectSupplierQuestionDiv").style.display = "none";
        document.getElementById("selectSupplierDiv").style.display = "none";
        document.getElementById("allOptionsDiv").style.display = "none";
        document.getElementById("isMissionDiv").style.display = "block";
    }

    function displayAllOptions() {
        document.getElementById("selectSupplierQuestionDiv").style.display = "none";
        document.getElementById("isMissionDiv").style.display = "none";
        document.getElementById("selectSupplierDiv").style.display = "none";
        document.getElementById("allOptionsDiv").style.display = "block";
    }

</script>