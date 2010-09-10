<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<%--
	<bean:define id="currentState" name="process" property="processState.refundProcessStateType"/>
	<fr:view name="process"> 
		<fr:layout name="process-state">
			<fr:property name="stateParameterName" value="state"/>
			<fr:property name="url" value="/viewLogs.do?method=viewOperationLog&processOid=${externalId}"/>
			<fr:property name="contextRelative" value="true"/>
			<fr:property name="currentStateClass" value=""/>
			<fr:property name="linkable" value="false"/>
		</fr:layout>
	</fr:view>
--%>


<h2>
	<bean:message key="refundProcess.title.viewRefundRequest" bundle="ACQUISITION_RESOURCES"/> 
	<span class="processNumber">(<fr:view name="process" property="acquisitionProcessId"/>)</span>
</h2>