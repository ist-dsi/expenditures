<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="title.mission.process.new" bundle="MISSION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:edit name="missionProcessCreationBean" schema="module.mission.domain.util.MissionProcessCreationBean"
		action="/missionProcess.do?method=newMissionCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
		<fr:property name="requiredMarkShown" value="true"/>
	</fr:layout>
</fr:edit>