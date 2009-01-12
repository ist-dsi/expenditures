<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="process.logs.title.viewLogs" bundle="EXPENDITURE_RESOURCES"/></h2>

<bean:define id="processClass" name="process" property="class.simpleName"/>

<p class="mtop05">
	<html:link action="<%= "/acquisition" + processClass + ".do?method=viewAcquisitionProcess"%>" paramId="acquisitionProcessOid" paramName="process" paramProperty="OID">
		«  <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>


<logic:present name="state">
	<h4 class="mbottom05"><bean:message key="process.logs.label.forState" bundle="EXPENDITURE_RESOURCES"/> <bean:message key='<%= "AcquisitionProcessStateType." + request.getParameter("state") %>' bundle="ENUMERATION_RESOURCES"/></h4>
</logic:present>


<logic:empty name="operationLogs">
	<p>
		<em><bean:message key="process.logs.info.noLogsAvailable" bundle="EXPENDITURE_RESOURCES"/>.</em>
	</p>
</logic:empty>


<fr:view name="operationLogs" schema="<%= "viewLogFor" +  processClass %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value=",aleft,"/>
		<fr:property name="sortBy" value="whenOperationWasRan"/>
	</fr:layout>
</fr:view>


<logic:equal name="process" property="class.name" value="pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess">
	<html:img action="statistics.do?method=simplifiedProcessStatisticsActivityTimeChartForProcess" paramId="processId" paramName="process" paramProperty="OID"/>
</logic:equal>