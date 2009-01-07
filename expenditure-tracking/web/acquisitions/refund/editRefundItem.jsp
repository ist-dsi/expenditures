<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message
	key="acquisitionProcess.title.editRefundRequest"
	bundle="ACQUISITION_RESOURCES" /></h2>
	
<bean:define id="refundProcessOid" name="refundProcess" property="OID"/>
<bean:define id="refundItemOid" name="refundItem" property="OID"/>

<fr:edit id="refundItemBean" name="bean" schema="createRefundItem" action="<%= "/acquisitionRefundProcess.do?method=actualEditRefundItem&refundItemOid=" + refundItemOid + "&refundProcessOid=" + refundProcessOid%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/acquisitionRefundProcess.do?method=viewRefundProcess&refundProcessOid=" + refundProcessOid %>"/>
</fr:edit>
