<%@page import="module.mission.domain.util.MissionProcessCreationBean"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.bennu.core.domain.User"%>

<h2><bean:message key="title.mission.process.new" bundle="MISSION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<%
	final String currentUserName = ((MissionProcessCreationBean) request.getAttribute("missionProcessCreationBean")).getCurrentUserName();
%>
<fr:form id="createForm" action="/missionProcess.do?method=newMissionCreation">
	<fr:edit name="missionProcessCreationBean">
		<fr:schema type="module.mission.domain.util.MissionProcessCreationBean" bundle="MISSION_RESOURCES">
			<fr:slot name="country" key="label.mission.country" layout="autoComplete" required="true">
        		<fr:property name="labelField" value="name.content"/>
				<fr:property name="format" value="${name.content}"/>
				<fr:property name="minChars" value="3"/>		
				<fr:property name="args" value="provider=module.geography.presentationTier.provider.CountryAutoCompleteProvider"/>
				<fr:property name="classes" value="inputsize300px"/>
			</fr:slot>
			<fr:slot name="location" key="label.mission.location" required="true"/>
			<fr:slot name="daparture" key="label.mission.departure" layout="picker" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator"/>
			<fr:slot name="arrival" key="label.mission.arrival" layout="picker" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator"/>
			<fr:slot name="objective" key="label.mission.objective" required="true">
				<fr:property name="size" value="60"/>
			</fr:slot>
			<fr:slot name="isCurrentUserAParticipant" key="label.mission.isCurrentUserAParticipant" arg0="<%= currentUserName %>" help="label.mission.isCurrentUserAParticipant.help"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
			<fr:property name="requiredMarkShown" value="true"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.submit" bundle="EXPENDITURE_RESOURCES"/></html:submit>
</fr:form>
