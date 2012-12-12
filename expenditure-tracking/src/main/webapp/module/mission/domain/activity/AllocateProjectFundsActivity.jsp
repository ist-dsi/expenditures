<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>

<jsp:include page="../displayPriorFundAllocations.jsp"/>

<fr:form action="/missionProcess.do?method=allocateProjectFunds">
	<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
	<html:hidden property="processId" value="<%= processId %>"/>

	<fr:edit name="information" property="missionItemFinancerFundAllocationBeans" schema="module.mission.domain.activity.FundAllocationActivityInformation.MissionItemFinancerFundAllocationBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle2 mtop05 mbottom15"/>
		</fr:layout>
	</fr:edit>

	<fr:edit id="information" name="information" schema="module.mission.domain.activity.AllocateFundsActivityInformation.void">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle2"/>
		</fr:layout>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form>
