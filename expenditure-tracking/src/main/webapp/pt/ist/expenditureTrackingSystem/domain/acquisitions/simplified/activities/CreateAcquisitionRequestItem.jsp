<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<div class="infobox">
	<bean:message key="acquisitionRequestItem.message.info.help.prefix" bundle="ACQUISITION_RESOURCES"/>
	<html:link action="/expenditureTrackingOrganization.do?method=listCPVReferences" styleClass="bluelink" target="_blank">
		<bean:message key="acquisitionRequestItem.message.info.help.infix" bundle="ACQUISITION_RESOURCES"/>
	</html:link>
	<bean:message key="acquisitionRequestItem.message.info.help.suffix" bundle="ACQUISITION_RESOURCES"/>
</div>

<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<bean:define id="schemaType"
		name="information"
		property="createItemSchemaType"/>
		
<div class="dinline forminline">

<fr:form action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
		
<fr:edit id="activityBean" name="information" visible="false"/>
			
<h3><bean:message key="acquisitionProcess.title.description" bundle="ACQUISITION_RESOURCES"/></h3>

<div  class="form1">
	<fr:edit id="acquisitionRequestItem.block1" name="information" schema="createAcquisitionRequestItem.block1">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="invalid" path="/expenditureProcesses.do?method=itemInvalidInfo"/>
	</fr:edit>
</div>


<h3><bean:message key="acquisitionProcess.title.quantityAndCosts" bundle="ACQUISITION_RESOURCES"/></h3>

<div  class="form1">
	<fr:edit id="acquisitionRequestItem.block2" name="information" schema="createAcquisitionRequestItem.block2">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="invalid" path="/expenditureProcesses.do?method=itemInvalidInfo"/>
	</fr:edit>
</div>


<h3><bean:message key="acquisitionProcess.title.deliveryInformation" bundle="ACQUISITION_RESOURCES"/></h3>

<div  class="form1 mbottom1">
	<fr:edit id="acquisitionRequestItem.block3" name="information" schema='<%= "createAcquisitionRequestItem.block3_" + schemaType.toString() %>'>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form thwidth150px"/>
			<fr:property name="columnClasses" value=",,tderror"/>
		</fr:layout>
		<fr:destination name="postBack" path="/expenditureProcesses.do?method=itemPostBack"/>
		<fr:destination name="invalid" path="/expenditureProcesses.do?method=itemInvalidInfo"/>
	</fr:edit>
</div>
	
	<html:submit styleClass="inputbutton"><bean:message key="button.createItem" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>
<fr:form action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form>
</div>
