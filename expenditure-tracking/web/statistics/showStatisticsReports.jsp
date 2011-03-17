<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.statistics.reports" bundle="STATISTICS_RESOURCES"/></h2>

<fr:edit id="yearBean"
		name="yearBean"
		schema="pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics.YearBean"
		action="statistics.do?method=showStatisticsReports">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
</fr:edit>

<br/>

<div class="infobox_dotted">
	<ul>
		<li>
			<html:link action="/statistics.do?method=downloadStatisticsByCPV" paramId="year" paramName="yearBean" paramProperty="year">
				<bean:message key="label.statistics.report.by.cpv" bundle="STATISTICS_RESOURCES"/>
			</html:link>
		</li>
        <li>
            <html:link action="/statistics.do?method=downloadTotalValuesStatistics" paramId="year" paramName="yearBean" paramProperty="year">
                <bean:message key="label.statistics.report.totalValuesByType" bundle="STATISTICS_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link action="/statistics.do?method=downloadRefundTotalValuesStatistics" paramId="year" paramName="yearBean" paramProperty="year">
                <bean:message key="label.statistics.report.refundTotalValuesByType" bundle="STATISTICS_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link action="/statistics.do?method=downloadAfterTheFactTotalValuesStatistics" paramId="year" paramName="yearBean" paramProperty="year">
                <bean:message key="label.statistics.report.afterTheFactTotalValuesByType" bundle="STATISTICS_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link action="/statistics.do?method=downloadInformationForExpenseReports" paramId="year" paramName="yearBean" paramProperty="year">
                <bean:message key="label.statistics.report.informationForExpenseReports" bundle="STATISTICS_RESOURCES"/>
            </html:link>
        </li>
	</ul>
</div>
