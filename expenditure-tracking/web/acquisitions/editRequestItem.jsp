<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.edit.acquisition.request.item" bundle="ACQUISITION_RESOURCES"/></h2>


<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<bean:define id="schemaType"
		name="itemBean"
		property="createItemSchemaType"/>

<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.OID"/>
<bean:define id="itemOID" name="itemBean" property="item.OID"/>
<fr:edit id="acquisitionRequestItem" name="itemBean" schema="<%= "createAcquisitionRequestItem_" + schemaType.toString()%>" 
	action="<%= "/acquisitionProcess.do?method=executeAcquisitionRequestItemEdition&acquisitionProcessOid="  + processOID + "&acquisitionRequestItemOid=" + itemOID%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= "/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid="  + processOID %>"/>
		<fr:destination name="invalid" path="<%= "/acquisitionProcess.do?method=executeEditAcquisitionRequestItem&acquisitionRequestItemOid=" + itemOID + "&acquisitionProcessOid=" + processOID %>"/>
		<fr:destination name="postBack" path="/acquisitionProcess.do?method=createItemPostBack" />
</fr:edit>