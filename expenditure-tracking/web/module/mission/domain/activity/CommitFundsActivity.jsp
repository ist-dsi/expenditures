<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>

<jsp:include page="../displayPriorFundAllocations.jsp"/>

<fr:form action="/missionProcess.do?method=commitFunds">
	<bean:define id="processId" type="java.lang.String" name="process" property="externalId"/>
	<html:hidden property="processId" value="<%= processId %>"/>

	<fr:edit name="information" property="missionFinancerCommitFundAllocationBeans" schema="module.mission.domain.activity.CommitFundsActivityInformation.MissionFinancerCommitFundAllocationBeans">
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
