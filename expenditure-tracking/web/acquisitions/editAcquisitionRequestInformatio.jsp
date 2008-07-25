<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.edit.acquisition.request" bundle="EXPENDITURE_RESOURCES"/></h2>
<br />
<br />

<bean:define id="acquisitionProcess"
		name="acquisitionRequestInformation"
		property="acquisitionRequest.acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		/>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<bean:define id="urlEdit">/acquisitionProcess.do?method=editAcquisitionRequestInformation&amp;acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="acquisitionRequestInformation"
		name="acquisitionRequestInformation"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestInformation"
		schema="editAcquisitionRequestInformation"
		action="<%= urlView %>">
	<fr:layout name="tabular">
		<fr:destination name="invalid" path="<%= urlEdit %>" />
		<fr:destination name="cancel" path="<%= urlView %>" />
	</fr:layout>
</fr:edit>
