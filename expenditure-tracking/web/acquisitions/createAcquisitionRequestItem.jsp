<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="acquisitionRequestItem.title.create" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<bean:define id="schemaType"
		name="bean"
		property="createItemSchemaType"/>

<bean:define id="acquisitionProcess"
		name="acquisitionProcess"
		property="acquisitionRequest.acquisitionProcess"
		type="pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess"
		/>
<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=<%= acquisitionProcess.getOID() %></bean:define>
<fr:edit id="acquisitionRequestItem"
		name="bean"
		schema='<%= "createAcquisitionRequestItem_" + schemaType.toString()%>'
		action="/acquisitionProcess.do?method=createNewAcquisitionRequestItem">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
	<fr:destination name="invalid" path='<%= "/acquisitionProcess.do?method=executeCreateAcquisitionRequestItem&acquisitionProcessOid=" + acquisitionProcess.getOID() %>'/>
	<fr:destination name="postBack" path="/acquisitionProcess.do?method=createItemPostBack" />
</fr:edit>
