<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>
<% final MultipleSupplierConsultation consultation = process.getConsultation(); %>

<div class="infobox">
    <table class="process-info mbottom0">
        <tr>
            <th>
                <bean:message key="label.consultation.process.contractType" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <td>
                <%= consultation.getContractType().getName().getContent() %>
            </td>
        </tr>
        <tr>
            <th>
                <bean:message key="label.consultation.process.material" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <td>
                <%= consultation.getMaterial().getFullDescription() %>
            </td>
        </tr>
        <tr>
            <th>
                <bean:message key="label.consultation.process.description" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <td>
                <%= consultation.getDescription() %>
            </td>
        </tr>
        <tr>
            <th>
                <bean:message key="label.consultation.process.value" bundle="EXPENDITURE_RESOURCES"/>
            </th>
            <td>
                <%= consultation.getValue().toFormatString() %>
            </td>
        </tr>
    </table>
</div>
