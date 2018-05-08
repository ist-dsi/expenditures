<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationPart"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document.SupplierCriteriaSelectionDocument"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultation"%>
<%@page import="pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess"%>
<% final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) request.getAttribute("process"); %>
<% final MultipleSupplierConsultation consultation = process.getConsultation(); %>


<% if (!process.doesNotExceedSupplierLimits()) { %>
    <div class="infobox_warning" style="border-color: maroon; background-color: #ffb2b2;">
        <p class="mvert025">
            <bean:message key="message.multiple.consultation.supplier.limit.exceeded" bundle="ACQUISITION_RESOURCES"/>
            <jsp:include page="displaySupplierLimits.jsp"/>
        </p>
     </div>
<% } else if (!process.doesNotExceedSupplierLimits()) { %>
    <div class="infobox_warning" style="border-color: maroon; background-color: #ffb2b2;">
        <p class="mvert025">
            <bean:message key="message.multiple.consultation.supplier.limit.possibly.exceeded" bundle="ACQUISITION_RESOURCES"/>
            <jsp:include page="displaySupplierLimits.jsp"/>
        </p>
     </div>
<% } %>

<% if (consultation.getContractType() == null) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.contract.type" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getMaterial() == null) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.material" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!MultipleSupplierConsultation.isPresent(consultation.getDescription())) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.description" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!consultation.getValue().isPositive()) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.value" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getProposalDeadline() == null) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.proposal.deadline" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getProposalValidity() == null || consultation.getProposalValidity().intValue() < 66) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.invalid-proposal.validity" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!MultipleSupplierConsultation.isPresent(consultation.getJustification())) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.justification" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!MultipleSupplierConsultation.isPresent(consultation.getPriceLimitJustification())) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.price.limit.justification" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!MultipleSupplierConsultation.isPresent(consultation.getSupplierCountJustification())) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.supplier.count.justification" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getContractManager() == null) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.contract.manager" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!consultation.isJuryValid()) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.invalid.jury" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getPartSet().size() < 1) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.parts" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<%
    boolean hasInvalidPart = false;
    for (final MultipleSupplierConsultationPart part : consultation.getPartSet()) {
        if (!part.isValid()) {
            hasInvalidPart = true;
        }
    }
%>
<% if (hasInvalidPart) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.invalidParts" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getTieBreakCriteriaSet().size() < 2) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.not.enough.tie.break.criteria" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (!consultation.areFinancersValid()) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.invalid.financers" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (consultation.getSupplierSet().size() < 3) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.warning.no.supplier" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if ((consultation.getSpecificEvaluationMethod() == null || !consultation.getSpecificEvaluationMethod().booleanValue()) && process.getState().ordinal() < MultipleSupplierConsultationProcessState.SUBMITTED_FOR_EXPENSE_PROCESS_IDENTIFICATION.ordinal()) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.supplierSelectionCriteria.price.warning" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>

<% if (process.getFiles(SupplierCriteriaSelectionDocument.class).isEmpty()) { %>
     <div class="infobox_warning">
        <p class="mvert025">
            <bean:message key="label.consultation.process.supplierSelectionCriteria.document.required" bundle="EXPENDITURE_RESOURCES"/>
        </p>
     </div>
<% } %>
