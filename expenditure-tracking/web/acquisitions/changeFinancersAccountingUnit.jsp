<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.projectFundAllocation" bundle="ACQUISITION_RESOURCES"/></h2>

<bean:define id="acquisitionProcess" name="acquisitionProcess" toScope="request"/>
<jsp:include page="viewAcquisitionRequest.jsp" flush="true"/>

<bean:define id="urlActivity">/acquisition<%= acquisitionProcess.getClass().getSimpleName() %>.do?method=changeFinancersAccountingUnit&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<bean:define id="urlView">/acquisition<%= acquisitionProcess.getClass().getSimpleName() %>.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<bean:write name="acquisitionProcess" property="OID"/></bean:define>
<fr:edit action="<%= urlActivity %>" 
		id="financersAccountingUnits" 
		schema="changeFinancersAccountingUnit" 
		name="financersBean" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>
