<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="module.mission.domain.MissionSystem"%>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/></h2>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>

<div class="infobox">
	<bean:message key="acquisitionProcess.message.note" bundle="ACQUISITION_RESOURCES" />
</div>

<p class="mtop15 mbottom05"><strong><fr:view name="acquisitionProcessBean" property="classification"/></strong></p>

<logic:notEmpty name="acquisitionProcessBean" property="suppliers">

	<table>
		<logic:iterate id="supplier" name="acquisitionProcessBean" property="suppliers" indexId="index">
		<tr>
			<td>
				<fr:view name="supplier" property="name"/>
			</td>
			<td>
				<fr:form action="<%= "/acquisitionSimplifiedProcedureProcess.do?method=removeSupplierInCreationPostBack&index=" + index.toString() %>">
					<fr:edit id="<%= "bean-" + index.toString() %>" name="acquisitionProcessBean" visible="false"/>
					<html:submit styleClass="inputbutton"><bean:message key="link.remove" bundle="MYORG_RESOURCES"/></html:submit>
				</fr:form>
			</td>
		</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<% if (MissionSystem.getInstance().hasAnyMissions()) { %>
	<fr:form id="createForm" action="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT75000">
		<fr:edit id="selectMissionBean" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
   				<fr:slot name="isForMission" key="label.aquisition.process.create.is.for.mission" layout="radio-postback">
    				<fr:property name="classes" value="liinline"/>
   				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
<% } %>

<fr:form
	action="/acquisitionSimplifiedProcedureProcess.do?method=addSupplierInCreationPostBack">
	<fr:edit id="bean" name="acquisitionProcessBean"
		schema="createStandardAcquistion.selectSuppliers" />
	<html:submit styleClass="inputbutton"><bean:message key="label.addSupplier" bundle="ACQUISITION_RESOURCES"/></html:submit>
</fr:form>

<fr:form action="/acquisitionSimplifiedProcedureProcess.do?method=createNewAcquisitionProcess">
	<logic:equal name="acquisitionProcessBean" property="isForMission" value="true">
		<fr:edit id="acquisitionProcessBeanMissionProcess" name="acquisitionProcessBean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean" bundle="ACQUISITION_RESOURCES">
    			<fr:slot name="missionProcess" layout="autoComplete" key="label.mission.process" bundle="ACQUISITION_RESOURCES"
    					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
        			<fr:property name="args" value="provider=module.mission.presentationTier.provider.MissionProcessProvider" />
        			<fr:property name="labelField" value="processIdentification"/>
        			<fr:property name="format" value="${processIdentification}"/>
        			<fr:property name="classes" value="inputsize100px"/>
        			<fr:property name="minChars" value="1"/>
        			<fr:property name="sortBy" value="processIdentification"/>
					<fr:property name="saveOptions" value="true"/>
    			</fr:slot>
    		</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
				<fr:property name="columnClasses" value=",,tderror"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	<fr:edit id="acquisitionProcessBean" name="acquisitionProcessBean"
		type="pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean"
		schema="createStandardAcquistion.selectRequester">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
		<fr:destination name="invalid" path="/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcessCT75000"/>
	</fr:edit>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.create" bundle="EXPENDITURE_RESOURCES" />
	</html:submit>
</fr:form>