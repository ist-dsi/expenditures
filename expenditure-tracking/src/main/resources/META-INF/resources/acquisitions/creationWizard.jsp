<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem"%>

<%
	final ExpenditureTrackingSystem system = ExpenditureTrackingSystem.getInstance();
	final String acquisitionCreationWizardJsp = system.getAcquisitionCreationWizardJsp();
	if (acquisitionCreationWizardJsp != null && !acquisitionCreationWizardJsp.isEmpty()) {
%>
		<jsp:include page="<%= acquisitionCreationWizardJsp %>"/>
<% 	} %>
			
