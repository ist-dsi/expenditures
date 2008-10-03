<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionRequestItem.title.edit" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<bean:define id="processOID" name="itemBean" property="acquisitionRequest.acquisitionProcess.OID"/>
<bean:define id="itemOID" name="itemBean" property="item.OID"/>

<bean:define id="urlView">/acquisitionProcess.do?method=viewAcquisitionProcess&acquisitionProcessOid=<%= processOID %></bean:define>
<fr:form action='<%= "/acquisitionProcess.do?method=executeAcquisitionRequestItemEdition&acquisitionProcessOid="  + processOID + "&acquisitionRequestItemOid=" + itemOID%>'> 

<fr:edit id="acquisitionRequestItem" name="itemBean"  visible="false"/>

<h3><bean:message key="acquisitionProcess.title.description" bundle="ACQUISITION_RESOURCES"/></h3>

<div  class="form1">
	<fr:edit id="acquisitionRequestItem.block1" name="itemBean" schema="createAcquisitionRequestItem.block1">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlView %>" />
		<fr:destination name="invalid" path='<%= "/acquisitionProcess.do?method=executeCreateAcquisitionRequestItem&acquisitionProcessOid=" + processOID %>'/>
		<fr:destination name="postBack" path="/acquisitionProcess.do?method=createItemPostBack" />
	</fr:edit>
</div>

<h3><bean:message key="acquisitionProcess.title.quantityAndCosts" bundle="ACQUISITION_RESOURCES"/></h3>

<div  class="form1">
	<fr:edit id="acquisitionRequestItem.block2" name="itemBean" schema="createAcquisitionRequestItem.block2">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlView %>" />
		<fr:destination name="invalid" path='<%= "/acquisitionProcess.do?method=executeCreateAcquisitionRequestItem&acquisitionProcessOid=" + processOID %>'/>
		<fr:destination name="postBack" path="/acquisitionProcess.do?method=createItemPostBack" />
	</fr:edit>
</div>

<h3><bean:message key="acquisitionProcess.title.deliveryInformation" bundle="ACQUISITION_RESOURCES"/></h3>

<bean:define id="schemaType"
		name="itemBean"
		property="createItemSchemaType"/>
		
<div  class="form1">
	<fr:edit id="acquisitionRequestItem.block3" name="itemBean" schema="<%= "createAcquisitionRequestItem.block3_" + schemaType.toString() %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="postBack" path="/acquisitionProcess.do?method=createItemPostBack" />
	</fr:edit>
</div>
	
	<html:submit styleClass="inputbutton"><bean:message key="button.create" bundle="EXPENDITURE_RESOURCES"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="button.cancel" bundle="EXPENDITURE_RESOURCES"/></html:cancel>
</fr:form>