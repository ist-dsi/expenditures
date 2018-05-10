<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<spring:url var="helpURL"
    value="/expenditure/acquisitons/create/info" />

<div class="page-header">
	<h2><spring:message code="acquisitionCreationWizard.title.newAcquisitionOrRefund"></spring:message></h2>
</div>

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

<div class="page-body">
    <div class="infobox_dotted" id="selectSupplierQuestionDiv" style="overflow:auto;">
        <c:if test="${!suggestSimplified}">
            <div class="alert alert-danger">
                <spring:message code="acquisitionCreationWizard.supplier.allocationInfo.simplified.notPossible" argumentSeparator="@" arguments="${supplier.presentationName}@${supplier.softTotalAllocated.toFormatString()}@${supplier.supplierLimit.toFormatString()}"></spring:message>
            </div>
            <div class="alert alert-danger">
                <spring:message code="acquisitionCreationWizard.supplier.allocationInfo.refund.notPossible" argumentSeparator="@" arguments="${supplier.presentationName}@${supplier.softTotalAllocated.toFormatString()}@${supplier.supplierLimit.toFormatString()}"></spring:message>
            </div>
        </c:if>
        <c:if test="${!suggestConsultation}">
            <div class="alert alert-danger">
                <spring:message code="acquisitionCreationWizard.supplier.allocationInfo.consultation.notPossible" argumentSeparator="@" arguments="${supplier.presentationName}@${supplier.softTotalAllocated.toFormatString()}@${supplier.supplierLimit.toFormatString()}"></spring:message>
            </div>
        </c:if>

        <div class="col-sm-12">
            <p>
                <spring:message code="acquisitionCreationWizard.text.selectProcessType"></spring:message>
            </p>
        </div>

<spring:url var="backUrl" value="/expenditure/acquisitons/create" />
<spring:url var="acquisitionUrl" value="/expenditure/acquisitons/create/acquisition?supplier=${supplier.fiscalIdentificationCode}" />
<spring:url var="consultationUrl" value="/expenditure/acquisitons/create/consultation?supplier=${supplier.fiscalIdentificationCode}" />
<spring:url var="refundUrl" value="/expenditure/acquisitons/create/refund?supplier=${supplier.fiscalIdentificationCode}" />

<table class="btable">
    <tr>
        <td>
                    <a href='${acquisitionUrl}' class="btn btn-default btn-xlarge">
                        <spring:message code="acquisitionCreationWizard.suggestion.simplified"></spring:message>
                    </a>
        </td>
        <td>
            <button class="btn btn-default btn-xlarge" disabled="disabled">
                <spring:message code="acquisitionCreationWizard.suggestion.standard"></spring:message>
            </button>
        </td>
        <td>
<!--
                    <a href="${consultationUrl}" class="btn btn-default btn-xlarge">
                        <spring:message code="acquisitionCreationWizard.suggestion.consultation"></spring:message>
                    </a>
-->
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        <spring:message code="acquisitionCreationWizard.suggestion.consultation"></spring:message>
                    </button>

        </td>
        <td>
                    <a href="${refundUrl}" class="btn btn-default btn-xlarge">
                        <spring:message code="acquisitionCreationWizard.suggestion.refund"></spring:message>
                    </a>
        </td>
    </tr>
</table>

        <div class="col-sm-12">
            <a href="#" class="btn btn-default" onclick='showHelp();'>
                <spring:message code="acquisitionCreationWizard.link.selectProcessType.help"></spring:message>
            </a>
            <a class="btn btn-default" href="${backUrl}">
                <spring:message code="link.select.other.supplier" text="Back" />
            </a>
        </div>

        <br/>
        <br/>
        <br/>

        <div class="col-sm-12" id="helpDiv" style="display: none;">
            <spring:message code="acquisitionCreationWizard.text.information.intro"></spring:message>
            <ul>
                <li><spring:message code="acquisitionCreationWizard.text.information.simplified"></spring:message></li>
                <li><spring:message code="acquisitionCreationWizard.text.information.standard"></spring:message></li>
                <li><spring:message code="acquisitionCreationWizard.text.information.consultation"></spring:message></li>
                <li><spring:message code="acquisitionCreationWizard.text.information.refund"></spring:message></li>
            </ul>
        </div>

    </div>
</div>

<script type="text/javascript">

    function showHelp() {
        document.getElementById("selectSupplierQuestionDiv").style.display = "none";
    	document.getElementById("helpDiv").style.display = "block";
        document.getElementById("selectSupplierQuestionDiv").style.display = "block";
    }

</script>