<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.create.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></h2>


<bean:define id="acquisitionProcess"
		name="acquisitionProcess"
		property="acquisitionRequest.acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		/>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="acquisitionRequestItem"
		name="bean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionRequestItemBean"
		schema="createAcquisitionRequestItem"
		action="/acquisitionProcess.do?method=createNewAcquisitionRequestItem">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
