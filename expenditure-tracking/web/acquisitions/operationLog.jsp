<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.view.logs.process" bundle="EXPENDITURE_RESOURCES"/></h2>

<ul>
	<li>
		<html:link action="/acquisitionProcess.do?method=viewAcquisitionProcess" paramId="acquisitionProcessOid" paramName="process" paramProperty="OID">
			<bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<bean:message key="label.logs.for.state" bundle="ACQUISITION_RESOURCES"/> <strong><bean:message key="<%= "AcquisitionProcessStateType." + request.getParameter("state") %>" bundle="ENUMERATION_RESOURCES"/></strong>

<p>
<logic:empty name="operationLogs">
	<em><bean:message key="label.no.logs.available" bundle="ACQUISITION_RESOURCES"/>.</em>
</logic:empty>

<fr:view name="operationLogs" schema="viewOperationLog">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="sortBy" value="whenOperationWasRan"/>
	</fr:layout>
</fr:view>
</p>

