<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<h2><bean:message key="title.newAcquisitionOrRefund" bundle="EXPENDITURE_RESOURCES"/></h2>

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

<p class="mvert05">
    <bean:message key="label.selectProcessType" bundle="EXPENDITURE_RESOURCES"/>:
</p>

<table class="btable">
    <tr>
        <td>
            <a href='<%= request.getContextPath() %>/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess' class="btn btn-default btn-xlarge">
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
            <a href="<%= request.getContextPath() %>/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP" class="btn btn-default btn-xlarge">
                <bean:message key="link.create.refundProcess" bundle="EXPENDITURE_RESOURCES"/>
                <br/>&nbsp;
            </a>
        </td>
    </tr>
</table>
