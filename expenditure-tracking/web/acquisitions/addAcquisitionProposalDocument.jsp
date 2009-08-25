<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.AddAcquisitionProposalDocument" bundle="ACQUISITION_RESOURCES"/></h2>

<div class="infobox">
	<bean:message key="message.help.addAcquisitionProposalDocument" bundle="ACQUISITION_RESOURCES"/>
</div>

<bean:define id="acquisitionProcess"
		name="acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		/>
<bean:define id="urlView">/acquisition<%= acquisitionProcess.getClass().getSimpleName() %>.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlPrepareAdd">/acquisition<%= acquisitionProcess.getClass().getSimpleName() %>.do?method=executeAddAcquisitionProposalDocument&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlAdd">/acquisition<%= acquisitionProcess.getClass().getSimpleName() %>.do?method=addAcquisitionProposalDocument&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="acquisitionProposalDocumentForm"
		name="acquisitionProposalDocumentForm"
		schema="addAcquisitionProposalDocument"
		action="<%= urlAdd %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
		<fr:destination name="invalid" path="<%= urlPrepareAdd %>" />
		<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
