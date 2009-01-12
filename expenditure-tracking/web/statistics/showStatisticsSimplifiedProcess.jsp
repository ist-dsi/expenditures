<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="label.statistics.process.simplified" bundle="STATISTICS_RESOURCES"/></h2>

<fr:edit id="yearBean"
		name="yearBean"
		schema="pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics.YearBean"
		action="statistics.do?method=showSimplifiedProcessStatistics">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
</fr:edit>

<br/>
<br/>

<logic:present name="simplifiedProcessStatistics">
	<bean:message key="label.statistics.process.total.number" bundle="STATISTICS_RESOURCES"/> <bean:write name="simplifiedProcessStatistics" property="numberOfProcesses"/>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsChart" paramId="year" paramName="yearBean" paramProperty="year"/>
	<br/>
	<br/>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsTimeChart" paramId="year" paramName="yearBean" paramProperty="year"/>
</logic:present>
<logic:notPresent name="simplifiedProcessStatistics">
	<bean:message key="label.statistics.not.available" bundle="STATISTICS_RESOURCES"/>
</logic:notPresent>
