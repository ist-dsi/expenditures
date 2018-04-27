<%@page import="pt.ist.expenditureTrackingSystem.domain.organization.Supplier"%>
<%@page import="module.finance.util.Money"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>
<% final MultipleSupplierConsultation consultation = process.getConsultation(); %>

<ul>
    <% for (final Supplier supplier : consultation.getSupplierSet()) {
        final Money permanent = supplier.getTotalPermanentAllocatedForMultipleSupplierConsultation();
        final Money verified = supplier.getTotalAllocatedForMultipleSupplierConsultation();
        final Money pending = supplier.getTotalAllocatedAndPendingForMultipleSupplierConsultation();
    %>
        <li>
            <span style="font-weight: bolder;"><%= supplier.getPresentationName() %> -</span>
            <bean:message key="supplier.message.info.totalAllocatedMultipleSupplierConsultation.permanent" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
            <%= permanent.toFormatString() %>;
            <bean:message key="supplier.message.info.totalAllocatedMultipleSupplierConsultation.verified" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
            <%= verified.subtract(permanent).toFormatString() %>;
            <bean:message key="supplier.message.info.totalAllocatedMultipleSupplierConsultation.pending" bundle="EXPENDITURE_ORGANIZATION_RESOURCES"/>:
            <%= pending.subtract(verified).toFormatString() %>
        </li>
    <% } %>
</ul>

