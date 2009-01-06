<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="process.label.searchRefundProcesses" bundle="EXPENDITURE_RESOURCES"/></h2>

<logic:notEmpty name="searchRefundProcess">
	<fr:view name="searchRefundProcess"
			schema="viewRefundProcessInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop2"/>
			<fr:property name="columnClasses" value=",,,width30px,,,,,"/>
			
			<fr:property name="linkFormat(view)" value="/acquisition${class.simpleName}.do?method=viewRefundProcess&refundProcessOid=${OID}"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>