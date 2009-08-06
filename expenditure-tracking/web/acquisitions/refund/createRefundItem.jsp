<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processClass" name="refundProcess" property="class.simpleName"/> 
<bean:define id="processOID" name="refundProcess" property="externalId" type="java.lang.String"/>
<bean:define id="actionMapping" value='<%= "/acquisition" + processClass %>'/>

<h2><bean:message key="refundProcess.title.createRefundItem" bundle="ACQUISITION_RESOURCES"/></h2>

<jsp:include page="../../commons/defaultErrorDisplay.jsp"/>
<fr:edit id="refundItemBean" name="bean" action='<%= actionMapping + ".do?method=actualCreationRefundItem&refundProcessOid=" +  processOID%>' schema="createRefundItem">
	<fr:layout name="tabular">
			<fr:property name="classes" value="form" />
			<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
	<fr:destination name="cancel" path='<%= actionMapping + ".do?method=viewRefundProcess&refundProcessOid=" +  processOID %>'/>
</fr:edit>