<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.acquisition.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="acquisitionProcess"
		name="acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		/>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlPrepareAdd">/acquisitionProcess.do?method=prepareAddAcquisitionProposalDocument&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlAdd">/acquisitionProcess.do?method=addAcquisitionProposalDocument&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="acquisitionProposalDocumentForm"
		name="acquisitionProposalDocumentForm"
		type="pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions.AcquisitionProcessAction$AcquisitionProposalDocumentForm"
		schema="addAcquisitionProposalDocument"
		action="<%= urlAdd %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
		<fr:destination name="invalid" path="<%= urlPrepareAdd %>" />
		<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
