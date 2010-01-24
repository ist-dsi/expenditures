<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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

<logic:present name="simplifiedProcessStatistics">
	<p><bean:message key="label.statistics.process.total.number" bundle="STATISTICS_RESOURCES"/>: <bean:write name="simplifiedProcessStatistics" property="numberOfProcesses"/></p>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsStateChart" paramId="year" paramName="yearBean" paramProperty="year"/>
	<br/>
	<br/>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsStateTimeChart" paramId="year" paramName="yearBean" paramProperty="year"/>
	<br/>
	<br/>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsStateTimeAverageChart" paramId="year" paramName="yearBean" paramProperty="year"/>
<%-- 
	<br/>
	<br/>
	<html:img action="statistics.do?method=simplifiedProcessStatisticsActivityTimeChart" paramId="year" paramName="yearBean" paramProperty="year"/>
 --%>
</logic:present>
<logic:notPresent name="simplifiedProcessStatistics">
	<bean:message key="label.statistics.not.available" bundle="STATISTICS_RESOURCES"/>
</logic:notPresent>
