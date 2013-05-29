<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.statistics.process.simplified" bundle="STATISTICS_RESOURCES"/></h2>

<fr:edit id="yearBean"
		name="yearBean"
		action="statistics.do?method=showSimplifiedProcessStatistics">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
</fr:edit>

<br/>

<div class="infobox_dotted">
	<ul>
		<li>
			<html:link action="/statistics.do?method=simplifiedProcessStatistics" paramId="year" paramName="yearBean" paramProperty="year">
				<bean:message key="label.statistics.simplifiedProcess" bundle="STATISTICS_RESOURCES"/>
			</html:link>
		</li>
		<li>	
			<html:link action="/statistics.do?method=generateProcessesStatsCSV" paramId="year" paramName="yearBean" paramProperty="year">
				<bean:message key="label.processedDump.processes" bundle="STATISTICS_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/statistics.do?method=generateLogStatsCSV" paramId="year" paramName="yearBean" paramProperty="year">
				<bean:message key="label.processedDump.logs" bundle="STATISTICS_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="/statistics.do?method=downloadStatisticsForConfirmedProcesses" paramId="year" paramName="yearBean" paramProperty="year">
				<bean:message key="label.statistics.report.confirmed.processes" bundle="STATISTICS_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</div>

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
