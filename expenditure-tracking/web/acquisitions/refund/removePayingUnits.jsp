<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RemovePayingUnit" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="processClass" name="refundProcess" property="class.simpleName"/>
<bean:define id="actionMapping" value="<%= "/acquisition" + processClass %>"/>

<p class="mbottom05">
	<html:link page="<%= actionMapping + ".do?method=viewRefundProcess"%>" paramId="refundProcessOid" paramName="refundProcess" paramProperty="OID">
		« <bean:message key="link.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:link>
</p>

<jsp:include page="../commons/defaultErrorDisplay.jsp"/>

<bean:define id="processOID" name="refundProcess" property="OID"/>
<fr:view name="payingUnits" schema="unitName">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
		<fr:property name="columnClasses" value="aleft,"/>
		<fr:property name="link(delete)" value='<%= actionMapping + ".do?method=removePayingUnit&refundProcessOid=" + processOID %>'/>
		<fr:property name="bundle(delete)" value="EXPENDITURE_RESOURCES"/>
		<fr:property name="key(delete)" value="link.remove"/>
		<fr:property name="param(delete)" value="OID/unitOID"/>
		<fr:property name="order(delete)" value="1"/>
	</fr:layout>
</fr:view>
