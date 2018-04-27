<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<% final String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>

<h2>
    <bean:message key="label.consultation.process" bundle="EXPENDITURE_RESOURCES"/>
    <span class="processNumber">(<%= process.getProcessNumber() %>)</span>
</h2>