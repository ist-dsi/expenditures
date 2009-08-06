<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="process.label.searchRefundProcesses" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:form action="/acquisitionRefundProcess.do?method=searchRefundProcess">
	<fr:edit id="searchRefundProcess"
			name="searchRefundProcess"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.SearchRefundProcesses"
			schema="searchRefundProcesses">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
	<html:submit styleClass="inputbutton"><bean:message key="button.search" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
</fr:form>

<bean:define id="result" name="searchRefundProcess" property="result"/>

<logic:notEmpty name="result">
	<fr:view name="result"
			schema="viewRefundProcessInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop2"/>
			
			<fr:property name="linkFormat(view)" value="/acquisition${class.simpleName}.do?method=viewRefundProcess&refundProcessOid=${externalId}"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>