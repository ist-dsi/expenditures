<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="module.mission.domain.MissionSystem"%>

<h2><bean:message key="acquisitionProcess.title.createAcquisitionRequest" bundle="ACQUISITION_RESOURCES" /></h2>

<div class="infobox">
	<bean:message key="acquisitionProcess.refund.message.note" bundle="ACQUISITION_RESOURCES"/>
</div>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<p class="mtop15 mbottom05"><strong><bean:message key="label.refund" bundle="EXPENDITURE_RESOURCES"/></strong></p>

<bean:define id="selection" value="internalPerson"/>

<logic:equal name="bean" property="externalPerson" value="true"> 
	<bean:define id="selection" value="externalPerson"/>
</logic:equal>

<bean:define id="underCCP" type="java.lang.Boolean" name="bean" property="underCCP"/>
<% String actionUrl = underCCP ? "/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP" : "/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderRCIST"; %>

<% if (MissionSystem.getInstance().hasAnyMissionProcesses()) { %>
	<fr:form id="createForm" action="<%= actionUrl %>">
		<fr:edit id="selectMissionBean" name="bean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean" bundle="ACQUISITION_RESOURCES">
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

<fr:form action="/acquisitionRefundProcess.do?method=createRefundProcess">
	<logic:equal name="bean" property="isForMission" value="true">
		<fr:edit id="acquisitionProcessBeanMissionProcess" name="bean">
			<fr:schema type="pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean" bundle="ACQUISITION_RESOURCES">
    			<fr:slot name="missionProcess" layout="autoComplete" key="label.mission.process" bundle="ACQUISITION_RESOURCES"
    					required="true">
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
	<fr:edit id="createRefundProcess" name="bean" schema='<%= "createRefundProcess." + selection %>'>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form mtop05" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
		<fr:destination name="postBack" path="/acquisitionRefundProcess.do?method=createRefundProcessPostBack"/>
		<fr:destination name="invalid" path="<%= actionUrl %>"/>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form>
