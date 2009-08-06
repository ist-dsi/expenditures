<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/messages.tld" prefix="messages" %>

<bean:define id="item" name="bean" property="item"/>
<bean:define id="process" name="item" property="request.process"/>
<bean:define id="itemOID" name="item" property="externalId" type="java.lang.String"/>
<bean:define id="processOID" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="processClass" name="process" property="class.simpleName"/>

<h2>
	<bean:message key="title.addInvoice" bundle="EXPENDITURE_RESOURCES"/>
</h2>

<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>

<fr:edit id="bean" name="bean" schema="create.refund.invoice" action='<%= "/acquisition" + processClass + ".do?method=createRefundInvoice&refundProcessOid=" + processOID + "&refundItemOid=" + itemOID%>'>
		<fr:layout name="tabular">
		<fr:property name="classes" value="form" />
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
	<fr:destination name="cancel" path='<%= "/acquisition" + processClass + ".do?method=viewRefundProcess&refundProcessOid=" + processOID %>'/>
</fr:edit>

