<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<h2><bean:message key="label.statistics.process.refund" bundle="STATISTICS_RESOURCES"/></h2>

<fr:edit id="yearBean"
		name="yearBean"
		schema="pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics.YearBean"
		action="statistics.do?method=showRefundProcessStatistics">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
</fr:edit>

<br/>

<logic:present name="refundProcessStatistics">
	<p><bean:message key="label.statistics.process.total.number" bundle="STATISTICS_RESOURCES"/>: <bean:write name="refundProcessStatistics" property="numberOfProcesses"/></p>
	<html:img action="statistics.do?method=refundProcessStatisticsChart" paramId="year" paramName="yearBean" paramProperty="year"/>
	<br/>
	<br/>
	<html:img action="statistics.do?method=refundProcessStatisticsTimeChart" paramId="year" paramName="yearBean" paramProperty="year"/>
</logic:present>
<logic:notPresent name="refundProcessStatistics">
	<bean:message key="label.statistics.not.available" bundle="STATISTICS_RESOURCES"/>
</logic:notPresent>
